package com.example.travenor.screen.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.travenor.databinding.ItemRecentSearchTextBinding

class RecentSearchAdapter : RecyclerView.Adapter<RecentSearchAdapter.ViewHolder>(), Filterable {
    private val mRecentSearchList = mutableListOf<String>()
    private val mDisplayList = mutableListOf<String>()
    private var mListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    fun updateList(list: List<String>) {
        mRecentSearchList.clear()
        mRecentSearchList.addAll(list)

        mDisplayList.clear()
        mRecentSearchList.forEachIndexed { index, s ->
            if (index < MAX_RECENT_SEARCH_DISPLAY_COUNT) {
                mDisplayList.add(s)
            }
        }
        notifyDataSetChanged()
    }

    fun addRecentSearchText(text: String) {
        if (mRecentSearchList.contains(text)) {
            mRecentSearchList.remove(text)
        }
        mRecentSearchList.add(text)
    }

    fun getRecentSearchList(): List<String> {
        return mRecentSearchList
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val results = FilterResults()
                if (p0.isNullOrEmpty()) {
                    results.values = mDisplayList
                } else {
                    val filteredList = mutableListOf<String>()
                    mRecentSearchList.forEach {
                        if (it.contains(p0, true)) {
                            filteredList.add(it)
                        }
                    }

                    results.values = filteredList
                }

                return results
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                mDisplayList.clear()
                p1?.values.toString()
                mDisplayList.addAll(p1?.values as List<String>)
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecentSearchTextBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mDisplayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mDisplayList[position]
        holder.binding.textRecentSearch.text = item
        holder.binding.root.setOnClickListener {
            mListener?.onRecentSearchTextClick(item)
        }
    }

    class ViewHolder(val binding: ItemRecentSearchTextBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface OnItemClickListener {
        fun onRecentSearchTextClick(text: String)
    }

    companion object {
        private const val MAX_RECENT_SEARCH_DISPLAY_COUNT = 5
    }
}
