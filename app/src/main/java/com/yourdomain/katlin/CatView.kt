package com.yourdomain.katlin

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.imageView
import org.jetbrains.anko.textView
import java.util.*

private val random = Random()
class CatView(context: Context) : FrameLayout(context) {

    val imageView: ImageView
    val textView: TextView
    init {
        layoutParams = ViewGroup.LayoutParams(-1, -1)
        imageView = imageView {
            layoutParams = LayoutParams(-1, -1)
            scaleType = ImageView.ScaleType.CENTER_CROP
        }
        textView = textView {
            layoutParams = LayoutParams(-1, -2)
            gravity = Gravity.CENTER
        }

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }

    fun setDisplayedCat(cat: Cat) {
        backgroundColor = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256))
        textView.text = cat.id
        Picasso.with(context).load(cat.url).into(imageView)
    }
}