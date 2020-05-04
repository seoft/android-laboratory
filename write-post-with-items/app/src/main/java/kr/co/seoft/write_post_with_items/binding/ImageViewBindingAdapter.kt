package kr.co.seoft.write_post_with_items.binding

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import java.io.File

@BindingAdapter("bind:image")
fun setImageResource(
    imageView: ImageView,
    file: File
) {
    Glide.with(imageView.context)
        .load(file)
        .transition(DrawableTransitionOptions.withCrossFade())
        .placeholder(ColorDrawable(Color.parseColor("#cccccc")))
        .into(imageView)
}