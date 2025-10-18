package com.bucharest.qurio.presentation.settings

import com.bucharest.qurio.presentation.base.BaseView

interface SettingsView : BaseView {
    fun updateSoundLevel(level: Int)
    fun updateMusicLevel(level: Int)
    fun showSavedMessage()
    fun showDiscardedMessage()
    fun dismissDialog()
}