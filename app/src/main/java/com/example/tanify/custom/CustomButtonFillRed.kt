package com.example.tanify.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.example.tanify.R

class CustomButtonFillRed: AppCompatButton {
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
        background = if(isEnabled) enabledBackground else disabledBackground
        setTextColor(txtColor)
        gravity = Gravity.CENTER
        textSize = 14f
        textAlignment = TEXT_ALIGNMENT_CENTER

    }
    private fun init() {
        txtColor = ContextCompat.getColor(context, android.R.color.white)
        enabledBackground = ContextCompat.getDrawable(context, R.drawable.bg_button_red_ennabled) as Drawable
        disabledBackground = ContextCompat.getDrawable(context, R.drawable.bg_button_dissable) as Drawable
    }
}