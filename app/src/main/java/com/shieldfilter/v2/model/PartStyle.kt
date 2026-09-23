package com.shieldfilter.v2.model

import com.shieldfilter.v2.model.BlockMode

data class PartStyle(
    val part: FemaleBodyPart,
    val enabled: Boolean,
    val mode: BlockMode,
    val mosaicIntensity: Int = 75,
    val alpha: Float = 0.9f,
    val customText: String = "已屏蔽",
    val textSize: Float = 32f
)
