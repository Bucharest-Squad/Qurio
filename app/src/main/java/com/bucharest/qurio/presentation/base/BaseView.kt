package com.bucharest.qurio.presentation.base

interface BaseView {
    fun showLoading()
    fun hideLoading()
    fun showError(message: String)
    fun showMessage(message: String)
}