package com.example.travenor.core.network

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.travenor.core.network.annotation.GET
import com.example.travenor.core.network.annotation.POST
import com.example.travenor.core.network.annotation.PUT
import com.example.travenor.core.network.annotation.Path
import com.example.travenor.core.network.annotation.Query
import com.example.travenor.core.network.exception.NetworkException
import com.google.gson.Gson
import org.json.JSONException
import java.io.BufferedReader
import java.io.IOException
import java.lang.reflect.Method
import java.lang.reflect.Parameter
import java.lang.reflect.Proxy
import java.lang.reflect.Type
import java.net.HttpRetryException
import java.net.HttpURLConnection
import java.net.URL

@Suppress("UNCHECKED_CAST")
class NetWorker(
    private val baseUrl: String,
    private val mConnectTimeout: Int,
    private val mReadTimeout: Int
) {

    /**
     * We create an ApiService instance with Java Reflection!
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun <T> create(service: Class<T>): T {
        return Proxy.newProxyInstance(
            service.classLoader,
            arrayOf(service)
        ) { _, method, args ->
            var needArgs = false
            if (method.parameterAnnotations.isNotEmpty()) needArgs = true

            // Check if needed args are passed into api
            if (needArgs && args == null) require(true)

            // Handle HTTP Method
            val putAnnotation = method.getAnnotation(PUT::class.java)
            val postAnnotation = method.getAnnotation(POST::class.java)
            val getAnnotation = method.getAnnotation(GET::class.java)

            when {
                getAnnotation != null -> {
                    // Handle get method
                    return@newProxyInstance performGetRequest<T>(
                        getAnnotation,
                        baseUrl,
                        method,
                        args
                    )
                }

                putAnnotation != null -> {
                    // handle put method
                }

                postAnnotation != null -> {
                    // handle post method
                }

                else -> null
            }
        } as T
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun <T> performGetRequest(
        annotation: GET,
        baseUrl: String,
        method: Method,
        args: Array<Any?>?
    ): Call<T> {
        // initial Url
        var url = "$baseUrl${annotation.value}"

        // get Path params
        val params = method.parameters

        // Handle @Path param annotation
        url = handlePathAnnotation(params, url, args)

        // Handle @Query param annotation
        url = handleQueryAnnotation(params, url, args)

        Log.d(LOG_TAG, "finish endpoint Url METHOD: $url")
        val apiUrl = URL(url)

        // Return a Call
        return object : Call<T> {
            override fun enqueue(parseType: Type, callback: Callback<T>) {
                Thread {
                    try {
                        Log.d(LOG_TAG, "Network request")

                        val connection = apiUrl.openConnection() as HttpURLConnection
                        connection.requestMethod = GET_METHOD
                        connection.connectTimeout = mConnectTimeout
                        connection.readTimeout = mReadTimeout

                        val responseCode = connection.responseCode
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            val inputStream = connection.inputStream
                            val responseBody =
                                inputStream.bufferedReader().use(BufferedReader::readText)

                            Log.d(LOG_TAG, "Raw: $responseBody")

                            val data = Gson().fromJson(responseBody, parseType) as T
                            callback.onResponse(responseBody, Response(data))
                        } else {
                            // Throw ApiException for non-OK HTTP response codes
                            throw NetworkException("HTTP error: $responseCode")
                        }
                    } catch (e: IOException) {
                        // Catch any other exceptions and invoke onFailure callback
                        callback.onFailure(e)
                    } catch (e: JSONException) {
                        callback.onFailure(e)
                    } catch (e: HttpRetryException) {
                        callback.onFailure(e)
                    }
                }.start()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handlePathAnnotation(
        params: Array<Parameter>,
        url: String,
        args: Array<Any?>?
    ): String {
        // Handle @Path param annotation
        var url1 = url
        params.forEachIndexed { i, v ->
            if (args != null) {
                if (v.isAnnotationPresent(Path::class.java)) {
                    val name = v.getAnnotation(Path::class.java)?.value
                    url1 = url1.replace("{$name}", args[i].toString())
                }
            }
        }

        return url1
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleQueryAnnotation(
        params: Array<Parameter>,
        urlIn: String,
        args: Array<Any?>?
    ): String {
        var url = urlIn
        val queryParams = mutableListOf<String>()
        params.forEachIndexed { i, v ->
            if (args != null) {
                if (v.isAnnotationPresent(Query::class.java)) {
                    url = url.replace("{${v.getAnnotation(Path::class.java)}}", args[i].toString())
                    val s = "${v.getAnnotation(Query::class.java)?.value}=${args[i]}"
                    queryParams.add(s)
                }
            }
        }
        if (queryParams.isNotEmpty()) {
            val queryString = queryParams.joinToString("&", "?")
            url = "$url$queryString"
        }
        return url
    }

    companion object {
        const val GET_METHOD = "GET"
        const val LOG_TAG = "NET"
    }
}
