package com.example.tanify.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.example.tanify.R

class CustomButtonStrokeYellow: AppCompatButton {
    private lateinit var enabledBackground: Drawable
    private lateinit var disabledBackground: Drawable
    private var txtColor: Int = 0
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        txtColor = ContextCompat.getColor(context, R.color.primary_green)
        background = if(isEnabled) enabledBackground else disabledBackground
        setTextColor(txtColor)
        gravity = Gravity.CENTER
        textSize = 14f

        textAlignment = TEXT_ALIGNMENT_CENTER

    }
    private fun init() {
        txtColor = ContextCompat.getColor(context, android.R.color.background_light)
        enabledBackground = ContextCompat.getDrawable(context, R.drawable.bg_button_yellow_stroke) as Drawable
        disabledBackground = ContextCompat.getDrawable(context, R.drawable.bg_button_yellow_stroke) as Drawable
    }
}