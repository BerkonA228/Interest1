package com.example.interest1.ui.message_recycler_view.view_holders

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.interest1.database.CURRENT_UID
import com.example.interest1.database.getFullNameById
import com.example.interest1.database.getPhotoUrlById
import com.example.interest1.ui.message_recycler_view.views.MessageView
import com.example.interest1.utilits.asTime
import com.example.interest1.utilits.downloadAndSetImage
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.message_item_text.view.*

class HolderTextMessage(view: View) : RecyclerView.ViewHolder(view), MessageHolder {
    private val blocUserMessage: ConstraintLayout = view.bloc_user_message
    private val chatUserMessage: TextView = view.chat_user_message
    private val chatUserMessageTime: TextView = view.chat_user_message_time
    private val blocReceivedMessage: ConstraintLayout = view.bloc_received_message
    private val chatReceivedMessage: TextView = view.chat_received_message
    private val chatReceivedMessageTime: TextView = view.chat_received_message_time

    private val chatReceivedPhoto: CircleImageView = view.chat_received_photo
    private val chatReceivedName: TextView = view.chat_received_name


    override fun drawMessage(view: MessageView, isGroup: Boolean) {
        if (view.from == CURRENT_UID) {
            blocUserMessage.visibility = View.VISIBLE
            blocReceivedMessage.visibility = View.GONE
            chatUserMessage.text = view.text
            chatUserMessageTime.text =
                view.timeStamp.asTime()
        } else {
            blocUserMessage.visibility = View.GONE
            blocReceivedMessage.visibility = View.VISIBLE
            chatReceivedMessage.text = view.text
            chatReceivedMessageTime.text = view.timeStamp.asTime()
            if (isGroup) {
                getFullNameById(view.from) { fullName ->
                    chatReceivedName.text = fullName
                }
                getPhotoUrlById(view.from) {
                    chatReceivedPhoto.downloadAndSetImage(it)
                }
                chatReceivedPhoto.visibility = View.VISIBLE
                chatReceivedName.visibility = View.VISIBLE
            }
            else {
                chatReceivedPhoto.visibility = View.GONE
                chatReceivedName.visibility = View.GONE
            }
        }
    }

    override fun onAttach(view: MessageView) {

    }

    override fun onDetach() {

    }
}