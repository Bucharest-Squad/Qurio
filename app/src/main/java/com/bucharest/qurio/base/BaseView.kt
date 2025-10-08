package com.bucharest.qurio.base

interface BaseView {
    fun showLoading()
    fun hideLoading()
    fun showError(message: String)
    fun showMessage(message: String)
}