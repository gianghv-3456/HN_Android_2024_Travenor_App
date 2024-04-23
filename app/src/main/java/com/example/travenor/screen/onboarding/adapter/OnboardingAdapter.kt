package com.example.travenor.screen.onboarding.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.cardview.widget.CardView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.travenor.R.color
import com.example.travenor.R.drawable
import com.example.travenor.R.string
import com.example.travenor.databinding.OnboardingFavoriteSurveyLayoutBinding
import com.example.travenor.databinding.OnboardingSliderLayoutBinding

class OnboardingAdapter : RecyclerView.Adapter<OnboardingAdapter.ViewHolder>() {
    private lateinit var mUserSurveyChangeListener: OnUserInterestSurveyChangeListener

    override fun getItemCount(): Int {
        return 4
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            FIRST_PAGE_INDEX, SECOND_PAGE_INDEX, THIRD_PAGE_INDEX -> ONBOARDING_VIEW_TYPE
            SURVEY_PAGE_INDEX -> USER_SURVEY_VIEW_TYPE
            else -> ONBOARDING_VIEW_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            USER_SURVEY_VIEW_TYPE -> {
                val binding = OnboardingFavoriteSurveyLayoutBinding.inflate(inflater, parent, false)
                UserSurveyViewHolder(binding)
            }

            ONBOARDING_VIEW_TYPE -> {
                val binding = OnboardingSliderLayoutBinding.inflate(inflater, parent, false)
                OnboardingViewHolder(binding)
            }

            else -> {
                val binding = OnboardingSliderLayoutBinding.inflate(inflater, parent, false)
                OnboardingViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (position) {
            FIRST_PAGE_INDEX -> {
                holder.initLayout(
                    titleResId = firstPageHtmlTitle,
                    descResId = firstPageDescription,
                    backgroundDrawableId = drawable.onboarding_bg_1
                )
            }

            SECOND_PAGE_INDEX -> {
                holder.initLayout(
                    titleResId = secondPageHtmlTitle,
                    descResId = secondPageDescription,
                    backgroundDrawableId = drawable.onboarding_bg_2
                )
            }

            THIRD_PAGE_INDEX -> {
                holder.initLayout(
                    titleResId = thirdPageHtmlTitle,
                    descResId = thirdPageDescription,
                    backgroundDrawableId = drawable.onboarding_bg_3
                )
            }

            SURVEY_PAGE_INDEX -> {
                holder.initLayout(
                    titleResId = survey_page_html_title
                )
            }
        }
    }

    abstract class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun initLayout(titleResId: Int, descResId: Int? = null, backgroundDrawableId: Int? = null) {
            setPageTitle(titleResId)
            if (descResId != null) {
                setPageDesc(descResId)
            }
            if (backgroundDrawableId != null) {
                setHeaderBackground(backgroundDrawableId)
            }
        }

        abstract fun setPageTitle(titleResId: Int)
        open fun setPageDesc(descResId: Int) {}
        open fun setHeaderBackground(backgroundDrawableId: Int) {}
    }

    class OnboardingViewHolder(
        private val mBinding: OnboardingSliderLayoutBinding
    ) : ViewHolder(mBinding.root) {

        override fun setPageDesc(descResId: Int) {
            val content = itemView.context.getString(descResId)
            mBinding.textPageDescription.text = content
        }

        override fun setPageTitle(titleResId: Int) {
            val htmlText = itemView.context.getString(titleResId)
            mBinding.textPageTitle.text =
                HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }

        override fun setHeaderBackground(backgroundDrawableId: Int) {
            mBinding.imageHeaderBackground.setImageDrawable(
                AppCompatResources.getDrawable(
                    itemView.context,
                    backgroundDrawableId
                )
            )
        }
    }

    inner class UserSurveyViewHolder(
        private val mBinding: OnboardingFavoriteSurveyLayoutBinding
    ) : ViewHolder(mBinding.root) {
        private val mSelectionCards = mutableMapOf<String, CardView>()
        private val mCardSelectionStatus = mutableMapOf<String, Boolean>()

        init {
            putSelectionCardIntoMap()
            initSelectionState()
            registerSelectionCardOnclick()
        }

        private fun registerSelectionCardOnclick() {
            mSelectionCards.forEach {
                val key = it.key
                val card = it.value
                card.setOnClickListener { view ->
                    // change click state
                    var state = mCardSelectionStatus[key]
                    state = !state!!
                    mCardSelectionStatus[key] = state

                    // set card background when state change
                    val background = view.background
                    if (state) {
                        background.setTint(itemView.context.getColor(color.onclick_selection_card_bg))
                    } else {
                        background.setTint(itemView.context.getColor(color.selection_card_bg))
                    }
                    // callback to activity
                    mUserSurveyChangeListener.userSurveyChangeListener(Pair(key, state))
                }
            }
        }

        private fun putSelectionCardIntoMap() {
            // put cardView into selectionCard map
            mSelectionCards[MOUNTAIN] = mBinding.cardMoutain
            mSelectionCards[BEACH] = mBinding.cardBeach
            mSelectionCards[CAVE] = mBinding.cardCave
            mSelectionCards[ASIAN_FOOD] = mBinding.cardAsianFood
            mSelectionCards[FAST_FOOD] = mBinding.cardFastFood
            mSelectionCards[EUROPEAN_FOOD] = mBinding.cardEuropeanFood
            mSelectionCards[SEA_FOOD] = mBinding.cardSeaFood
        }

        private fun initSelectionState() {
            mCardSelectionStatus[MOUNTAIN] = false
            mCardSelectionStatus[BEACH] = false
            mCardSelectionStatus[CAVE] = false
            mCardSelectionStatus[ASIAN_FOOD] = false
            mCardSelectionStatus[FAST_FOOD] = false
            mCardSelectionStatus[EUROPEAN_FOOD] = false
            mCardSelectionStatus[SEA_FOOD] = false
        }

        override fun setPageTitle(titleResId: Int) {
            val htmlText = itemView.context.getString(titleResId)
            mBinding.textPageTitle.text =
                HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    }

    fun setOnUserSurveyChangeListener(listener: OnUserInterestSurveyChangeListener) {
        mUserSurveyChangeListener = listener
    }

    interface OnUserInterestSurveyChangeListener {
        fun userSurveyChangeListener(pair: Pair<String, Boolean>)
    }

    companion object {
        // view type
        private const val ONBOARDING_VIEW_TYPE = 1
        private const val USER_SURVEY_VIEW_TYPE = 2

        // page title resourceId
        private var firstPageHtmlTitle = string.first_screen_title_html
        private var secondPageHtmlTitle = string.second_page_html_title
        private var thirdPageHtmlTitle = string.third_page_html_title
        private var survey_page_html_title = string.survey_page_html_title

        // page description resourceId
        private var firstPageDescription = string.first_page_description
        private var secondPageDescription = string.second_page_description
        private var thirdPageDescription = string.third_page_description

        const val FIRST_PAGE_INDEX = 0
        const val SECOND_PAGE_INDEX = 1
        const val THIRD_PAGE_INDEX = 2
        const val SURVEY_PAGE_INDEX = 3

        // Map key to store items
        const val MOUNTAIN = "MOUNTAIN"
        const val BEACH = "BEACH"
        const val CAVE = "CAVE"
        const val ASIAN_FOOD = "ASIAN_FOOD"
        const val FAST_FOOD = "FAST_FOOD"
        const val SEA_FOOD = "SEA_FOOD"
        const val EUROPEAN_FOOD = "EUROPEAN_FOOD"
    }
}
