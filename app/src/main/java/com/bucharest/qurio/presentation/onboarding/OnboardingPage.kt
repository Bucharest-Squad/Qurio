package com.bucharest.qurio.presentation.onboarding

import com.bucharest.qurio.R

data class OnboardingPage(
    val imageRes: Int,
    val title: Int,
    val description: Int
) {
    companion object {
        fun values(): List<OnboardingPage> {
            return listOf(
                OnboardingPage(
                    imageRes = R.drawable.brain_image,
                    title = R.string.onboarding_title_1,
                    description = R.string.onboarding_desc_1
                ),
                OnboardingPage(
                    imageRes = R.drawable.characters,
                    title = R.string.onboarding_title_2,
                    description = R.string.onboarding_desc_2
                ),
                OnboardingPage(
                    imageRes = R.drawable.crown_image,
                    title = R.string.onboarding_title_3,
                    description = R.string.onboarding_desc_3
                ),
                OnboardingPage(
                    imageRes = R.drawable.cup_image,
                    title = R.string.onboarding_title_4,
                    description = R.string.onboarding_desc_4
                ),
            )
        }
    }
}