package com.shieldfilter.v2.processor

import com.shieldfilter.v2.model.FemaleBodyPart
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark
import android.graphics.RectF

class FemaleRegionCalculator {
    private fun bounding(landmarks: List<NormalizedLandmark>): RectF {
        val xs = landmarks.map { it.x() }
        val ys = landmarks.map { it.y() }
        return RectF(xs.minOrNull() ?:0f, ys.minOrNull()?:0f, xs.maxOrNull()?:1f, ys.maxOrNull()?:1f)
    }

    private fun RectF.scale(factor: Float): RectF {
        val cx = centerX()
        val cy = centerY()
        val w = width() * factor / 2f
        val h = height() * factor / 2f
        return RectF(cx - w, cy - h, cx + w, cy + h)
    }

    fun calculate(landmarks: List<NormalizedLandmark>): Map<FemaleBodyPart, RectF?> {
        val map = mutableMapOf<FemaleBodyPart, RectF?>()
        if (landmarks.size < 33) return map

        map[FemaleBodyPart.EYES] = bounding(listOf(landmarks[2],landmarks[5])).scale(1.4f)
        map[FemaleBodyPart.CHEST] = bounding(listOf(landmarks[11],landmarks[12],landmarks[23],landmarks[24])).scale(1.15f)
        map[FemaleBodyPart.ARMPITS] = bounding(listOf(landmarks[11],landmarks[12],landmarks[13],landmarks[14])).scale(1.2f)
        map[FemaleBodyPart.PELVIS] = bounding(listOf(landmarks[23],landmarks[24],landmarks[25],landmarks[26])).scale(1.1f)
        map[FemaleBodyPart.FEET] = bounding(listOf(landmarks[27],landmarks[28],landmarks[31],landmarks[32])).scale(1.25f)

        return map
    }
}
