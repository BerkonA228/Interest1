package com.example.interest1.ui.message_recycler_view.view_holders

import com.example.interest1.ui.message_recycler_view.views.MessageView

interface MessageHolder {
    fun drawMessage(view: MessageView, isGroup: Boolean = false)
    fun onAttach(view: MessageView)
    fun onDetach()
}