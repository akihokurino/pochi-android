package com.wanpaku.pochi.app.sitterdetail

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.request.RequestOptions
import com.wanpaku.pochi.R
import com.wanpaku.pochi.domain.dog.Dog
import com.wanpaku.pochi.domain.sitter.Sitter
import com.wanpaku.pochi.infra.ui.widget.load

class SitterProfileMessageView @JvmOverloads constructor(context: Context,
                                                         attrs: AttributeSet? = null,
                                                         defStyleAttr: Int = 0,
                                                         defStyleRes: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val thumbnail: ImageView
    private val fullName: TextView
    private val nickName: TextView
    private val introductionMessage: TextView
    private val petDogLabel: TextView
    private val petDogs: TextView
    private val petDogMessage: TextView

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.view_sitter_profile_message, this)
        thumbnail = view.findViewById(R.id.thumbnail)
        fullName = view.findViewById(R.id.full_name)
        nickName = view.findViewById(R.id.nickname)
        introductionMessage = view.findViewById(R.id.introduction_message)
        petDogLabel = view.findViewById(R.id.pet_dog_label)
        petDogs = view.findViewById(R.id.pet_dogs)
        petDogMessage = view.findViewById(R.id.about_pet_dog_message)
    }

    fun apply(viewModel: ViewModel) {
        thumbnail.load(viewModel.thumbnailUrl, RequestOptions.circleCropTransform())
        fullName.text = viewModel.fullName
        nickName.text = viewModel.nickName
        introductionMessage.text = viewModel.introductionMessage
        petDogLabel.text = viewModel.petDogLabel
        petDogs.text = viewModel.petDogs
        petDogMessage.text = viewModel.petDogMessage
    }

    class ViewModel(sitter: Sitter, dogs: List<Dog>, context: Context) {
        val thumbnailUrl = sitter.user.iconUri
        val fullName = "${sitter.user.lastName} ${sitter.user.firstName}"
        val nickName = sitter.user.nickName
        val introductionMessage = sitter.introductionMessage
        val petDogLabel = context.getString(R.string.pet_dog_label, sitter.user.nickName)
        val petDogs: String = if (dogs.isEmpty()) context.getString(R.string.pet_dog_is_not_exists) else dogs.map { it.breed.label }.reduce { s1, s2 -> "$s1\n$s2" }
        val petDogMessage = sitter.serviceDescription
    }

}