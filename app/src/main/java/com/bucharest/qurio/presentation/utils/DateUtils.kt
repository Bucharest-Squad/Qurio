package com.bucharest.qurio.presentation.utils

import kotlinx.datetime.LocalDateTime
import com.bucharest.qurio.presentation.constants.PresentationConstants

object DateUtils {
    
    fun formatDate(dateTime: LocalDateTime): String {
        val day = dateTime.dayOfMonth.toString().padStart(
            PresentationConstants.DATE_PADDING_LENGTH, 
            PresentationConstants.DATE_PADDING_CHAR
        )
        val month = dateTime.monthNumber.toString().padStart(
            PresentationConstants.DATE_PADDING_LENGTH, 
            PresentationConstants.DATE_PADDING_CHAR
        )
        val year = dateTime.year
        return "$day-$month-$year"
    }
}
