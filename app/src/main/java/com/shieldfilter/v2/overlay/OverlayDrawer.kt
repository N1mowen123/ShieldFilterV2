package com.shieldfilter.v2.overlay

import android.graphics.*
import com.shieldfilter.v2.model.PartStyle

class OverlayDrawer {
    private fun drawMosaic(canvas: Canvas, rect: Rect, block: Int=12) {
        val p = Paint()
        p.color = Color.GRAY
        var x = rect.left
        while(x < rect.right){
            var y = rect.top
            while(y < rect.bottom){
                canvas.drawRect(x.toFloat(), y.toFloat(), (x+block).toFloat(), (y+block).toFloat(), p)
                y += block
            }
            x += block
        }
    }

    fun draw(canvas: Canvas, rect: Rect, style: PartStyle) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.alpha = (style.alpha * 255).toInt()
        when(style.mode){
            com.shieldfilter.v2.model.BlockMode.MOSAIC -> drawMosaic(canvas, rect)
            com.shieldfilter.v2.model.BlockMode.BLACK_SOLID -> {
                paint.color = Color.BLACK
                canvas.drawRect(rect, paint)
            }
            com.shieldfilter.v2.model.BlockMode.BLACK_WITH_TEXT -> {
                paint.color = Color.BLACK
                canvas.drawRect(rect, paint)
                paint.color = Color.WHITE
                paint.textSize = style.textSize
                paint.textAlign = Paint.Align.CENTER
                canvas.drawText(style.customText, rect.centerX().toFloat(), rect.centerY().toFloat(), paint)
            }
        }
    }
}
