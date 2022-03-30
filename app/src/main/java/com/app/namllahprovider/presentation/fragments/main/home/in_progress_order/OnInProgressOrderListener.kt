package com.app.namllahprovider.presentation.fragments.main.home.in_progress_order

interface OnInProgressOrderListener {
    fun onClickOrder(position:Int)
    fun onClickCancel(position:Int)
    fun onClickAction(position:Int)
    fun onClickShowIntMap(position:Int)
}