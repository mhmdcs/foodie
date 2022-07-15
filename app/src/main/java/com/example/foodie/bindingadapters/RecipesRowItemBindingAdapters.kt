package com.example.foodie.bindingadapters

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import coil.load
import com.example.foodie.R

class RecipesRowItemBindingAdapters {
    companion object {

        @BindingAdapter("load_image")
        @JvmStatic
        fun loadImage(imageView: ImageView, link: String){
            imageView.load(link) {
                crossfade(600)
                error(R.drawable.ic_error_placeholder)
            }
        }

        @BindingAdapter("set_likes")
        @JvmStatic
        fun setLikes(textView: TextView, likes: Int){
            textView.text = likes.toString()
        }

        @BindingAdapter("set_minutes")
        @JvmStatic
        fun setMinutes(textView: TextView, minutes: Int){
            textView.text = minutes.toString()
        }

        @BindingAdapter("set_vegan")
        @JvmStatic
        fun setVegan(view: View, vegan: Boolean){
            if(vegan){
                when(view){
                    is TextView -> view.setTextColor(ContextCompat.getColor(view.context, R.color.green)) // or just directly call Color.GREEN without having to go through ContextCompat, although the colors in ContextCompat are easier on the eyes.
                    is ImageView -> view.setColorFilter(ContextCompat.getColor(view.context, R.color.green))
                }
            }
        }

    }
}