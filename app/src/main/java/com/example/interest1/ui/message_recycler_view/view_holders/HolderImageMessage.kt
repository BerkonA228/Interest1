package com.example.interest1.ui.message_recycler_view.view_holders

import android.view.View
import android.widget.ImageView
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
import kotlinx.android.synthetic.main.message_item_image.view.*

class HolderImageMessage(view: View) : RecyclerView.ViewHolder(view), MessageHolder {
    private val blocReceivedImageMessage: ConstraintLayout = view.bloc_received_image_message
    private val blocUserImageMessage: ConstraintLayout = view.bloc_user_image_message
    private val chatUserImage: ImageView = view.chat_user_image
    private val chatReceivedImage: ImageView = view.chat_received_image
    private val chatUserImageMessageTime: TextView = view.chat_user_image_message_time
    private val chatReceivedImageMessageTime: TextView = view.chat_received_image_message_time

    private val chatReceivedPhoto: CircleImageView = view.chat_received_photo
    private val chatReceivedName: TextView = view.chat_received_name


    override fun drawMessage(view: MessageView, isGroup: Boolean) {
        if (view.from == CURRENT_UID) {
            blocReceivedImageMessage.visibility = View.GONE
            blocUserImageMessage.visibility = View.VISIBLE
            chatUserImage.downloadAndSetImage(view.fileUrl)
            chatUserImageMessageTime.text = view.timeStamp.asTime()
        } else {
            blocReceivedImageMessage.visibility = View.VISIBLE
            blocUserImageMessage.visibility = View.GONE
            chatReceivedImage.downloadAndSetImage(view.fileUrl)
            chatReceivedImageMessageTime.text = view.timeStamp.asTime()
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