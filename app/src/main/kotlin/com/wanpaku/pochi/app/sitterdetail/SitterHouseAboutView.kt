package com.wanpaku.pochi.app.sitterdetail

import android.content.Context
import android.support.annotation.IntegerRes
import android.support.annotation.StringRes
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.flexbox.*
import com.wanpaku.pochi.R
import com.wanpaku.pochi.domain.dog.Dog
import com.wanpaku.pochi.domain.sitter.Sitter
import com.wanpaku.pochi.infra.ui.adapter.BaseRecyclerViewAdapter

class SitterHouseAboutView @JvmOverloads constructor(context: Context,
                                                     attrs: AttributeSet? = null,
                                                     defStyleAttr: Int = 0,
                                                     defStyleRes: Int = 0)
    : SitterDetailWithRecyclerViewItem<SitterHouseAboutView.Adapter>(context, attrs, defStyleAttr, defStyleRes) {

    override fun layoutManager() = FlexboxLayoutManager(context, FlexDirection.ROW, FlexWrap.WRAP).apply {
        justifyContent = JustifyContent.SPACE_AROUND
        alignItems = AlignItems.STRETCH
    }

    override fun itemDecorations(): List<RecyclerView.ItemDecoration> = emptyList()

    override fun titleResId(): Int = R.string.about_sitter_house

    override fun createAdapter(): Adapter = Adapter()

    fun apply(viewModel: ViewModel) {
        getAdapter().clear()
        getAdapter().addAll(viewModel.sitterHouseAboutTypes)
    }

    class Adapter : BaseRecyclerViewAdapter<SitterHouseAboutType, Adapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_house_about, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val type = get(position)
            holder.title.text = holder.itemView.context.getString(type.titleResId)
            holder.icon.setImageResource(type.iconResId)
            holder.itemView.layoutParams = holder.itemView.layoutParams.apply {
                if (this is FlexboxLayoutManager.LayoutParams) flexGrow = 1f
            }
        }

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            @BindView(R.id.icon) lateinit var icon: ImageView
            @BindView(R.id.title) lateinit var title: TextView

            init {
                ButterKnife.bind(this, view)
            }
        }

    }

    class ViewModel(sitter: Sitter) {

        val sitterHouseAboutTypes: List<SitterHouseAboutType>

        init {
            sitterHouseAboutTypes = mutableListOf<SitterHouseAboutType>().apply {
                sitter.acceptableDogSizes.apply { addAll(toSizeAboutTypes()) }
                sitter.houseType.apply { add(toHouseAboutType()) }
                sitter.smokingPolicy.apply { add(toSmokingAboutType()) }
                sitter.kidsTypes.apply { addAll(toKidsAboutTypes()) }
                sitter.options.apply { addAll(toOptionAboutTypes()) }
            }.toList()
        }

        private fun List<Dog.SizeType>.toSizeAboutTypes() = listOf(
                find { it == Dog.SizeType.Small }?.let { SitterHouseAboutType.SmallDog } ?: SitterHouseAboutType.SmallDogNg,
                find { it == Dog.SizeType.Medium }?.let { SitterHouseAboutType.MediumDogNg } ?: SitterHouseAboutType.MediumDogNg,
                find { it == Dog.SizeType.Large }?.let { SitterHouseAboutType.LargeDog } ?: SitterHouseAboutType.LargeDogNg
        )

        private fun Sitter.HouseType.toHouseAboutType() =
                if (this == Sitter.HouseType.Isolated) SitterHouseAboutType.Isolated else SitterHouseAboutType.Apartment

        private fun Sitter.SmokingPolicy.toSmokingAboutType() =
                if (this == Sitter.SmokingPolicy.Smoking) SitterHouseAboutType.Smoking else SitterHouseAboutType.NonSmoking

        private fun List<Sitter.KidsType>.toKidsAboutTypes() = map {
            when (it) {
                Sitter.KidsType.Infant -> SitterHouseAboutType.Infant
                Sitter.KidsType.Schoolchild -> SitterHouseAboutType.Schoolchild
                else -> throw RuntimeException()
            }
        }

        private fun List<Sitter.Option>.toOptionAboutTypes() = map {
            when (it) {
                Sitter.Option.EarlyMorning -> SitterHouseAboutType.EarlyMorning
                Sitter.Option.LateNight -> SitterHouseAboutType.LateNight
                else -> throw RuntimeException()
            }
        }

    }

    enum class SitterHouseAboutType(@IntegerRes val iconResId: Int,
                                    @StringRes val titleResId: Int) {
        SmallDog(R.drawable.ic_small_dog, R.string.small_dog_ok),
        SmallDogNg(R.drawable.ic_small_dog_ng, R.string.small_dog_ng),
        MediumDog(R.drawable.ic_medium_dog, R.string.medium_dog_ok),
        MediumDogNg(R.drawable.ic_medium_dog_ng, R.string.medium_dog_ng),
        LargeDog(R.drawable.ic_large_dog, R.string.large_dog_ok),
        LargeDogNg(R.drawable.ic_large_dog_ng, R.string.large_dog_ng),
        Isolated(R.drawable.ic_isolated, R.string.isolated),
        Apartment(R.drawable.ic_apartment, R.string.apartment),
        Smoking(R.drawable.ic_smoking, R.string.smoking_house),
        NonSmoking(R.drawable.ic_non_smoking, R.string.non_smoking_house),
        Infant(R.drawable.ic_infant, R.string.infant_exist),
        Schoolchild(R.drawable.ic_schoolchild, R.string.schoolchild_exist),
        Adult(R.drawable.ic_adult, R.string.adult_exist),
        EarlyMorning(R.drawable.ic_early_morning, R.string.early_morning),
        LateNight(R.drawable.ic_late_night, R.string.late_night);
    }

}
