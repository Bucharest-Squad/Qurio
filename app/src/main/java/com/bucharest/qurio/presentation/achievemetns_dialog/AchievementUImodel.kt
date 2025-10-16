package com.bucharest.qurio.presentation.achievemetns_dialog

data class AchievementUImodel(
    val id :Int,
    val title:String,
    val description:String,
    val imageResFilledAndOutlined: Pair<Int,Int>,
    val unlocked : Boolean,
    val gradient:Pair<Int,Int>?,
)