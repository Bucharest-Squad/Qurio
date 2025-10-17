package com.bucharest.qurio.presentation.constants

object PresentationConstants {
    
    const val DEFAULT_QUESTION_TIME_MILLIS = 20_000L
    const val TIMER_UPDATE_INTERVAL = 1L
    const val DEFAULT_SCORE_POINTS = 100
    const val DEFAULT_LIVES_COUNT = 4
    const val DEFAULT_TOTAL_QUESTIONS = 10
    const val DEFAULT_CATEGORY_ID = 22
    const val QUESTION_AMOUNT_FETCH = 12
    
    const val CAROUSEL_OFFSCREEN_PAGE_LIMIT = 5
    const val FIRST_CHILD_INDEX = 0
    const val NO_PADDING = 0
    const val ITEM_CACHE_SIZE = 10
    const val MIN_ACTIVE_STREAK = 0
    const val RECENT_GAMES_LIMIT = 10
    const val DEFAULT_DURATION_SECONDS = 0
    const val DATE_PADDING_LENGTH = 2
    const val DATE_PADDING_CHAR = '0'
    const val NO_STREAK = 0
    const val DAYS_IN_WEEK = 7
    
    const val TIMER_TEXT_SIZE_MULTIPLIER = 0.5f
    const val CAP_DX_PER_DY = 14.549f / 16f
    
    const val ERROR_LOADING_DATA = "Failed to load home data"
    const val ERROR_NO_LIVES = "No lives left! Please buy more lives to continue playing."
    const val ERROR_CATEGORY_NOT_FOUND = "Category not found"
    const val ERROR_NO_QUESTIONS = "No questions available for this category"
    const val ERROR_FINISH_GAME = "Failed to finish game"
    const val ERROR_LOAD_CATEGORY = "Failed to load category"
    
    const val MESSAGE_SELECT_ANSWER = "Please select an answer first"
    const val MESSAGE_TIME_UP = "Time's up!"
    const val MESSAGE_NO_LIVES_LEFT = "No lives left! Game over. You can buy more lives or return to home."
    
    const val APP_TITLE = "Qurio"
    const val TOAST_DURATION_SHORT = android.widget.Toast.LENGTH_SHORT
}
