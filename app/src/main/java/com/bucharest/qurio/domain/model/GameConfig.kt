package com.bucharest.qurio.domain.model

data class GameConfig(
    val coinsPerCorrect: Int,
    val coinsPerSkipped: Int
) {
    companion object {
        private const val DEFAULT_COINS_PER_CORRECT = 10
        private const val DEFAULT_COINS_PER_SKIPPED = 2
        
        fun default() = GameConfig(
            coinsPerCorrect = DEFAULT_COINS_PER_CORRECT,
            coinsPerSkipped = DEFAULT_COINS_PER_SKIPPED
        )
    }
}

