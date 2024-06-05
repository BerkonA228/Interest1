package com.example.interest1.ui.screens.request

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.interest1.R
import com.example.interest1.database.CURRENT_UID
import com.example.interest1.database.NODE_PHONES_CONTACTS
import com.example.interest1.database.NODE_REQUESTS
import com.example.interest1.database.REF_DATABASE_ROOT
import com.example.interest1.database.USER
import com.example.interest1.models.RequestModel
import com.example.interest1.ui.screens.viewprofile.ViewProfileFragment
import com.example.interest1.utilits.downloadAndSetImage
import com.example.interest1.utilits.replaceFragment
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.request_item.view.*

class RequestItemAdapter:
    RecyclerView.Adapter<RequestItemAdapter.RequestItemViewHolder>() {
    private val listItems = mutableListOf<RequestModel>()

    class RequestItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userImage: CircleImageView = view.userImage
        val userName: TextView = view.userName
        val confirmUser: ImageView = view.confirmUser
        val cancelUser: ImageView = view.cancelUser
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.request_item, parent, false)

        val holder = RequestItemViewHolder(view)

        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            replaceFragment(ViewProfileFragment(listItems[position].id))
        }

        holder.confirmUser.setOnClickListener {
            val position = holder.adapterPosition
            addInContacts(position)
            removeItem(position)
        }
        holder.cancelUser.setOnClickListener {
            removeItem(holder.adapterPosition)
        }

        return holder
    }

    override fun getItemCount(): Int = listItems.size

    override fun onBindViewHolder(holder: RequestItemViewHolder, position: Int) {
        val requestModel = listItems[position]

        holder.userImage.downloadAndSetImage(requestModel.photoUrl)
        holder.userName.text = requestModel.name
    }

    fun updateListItems(item: RequestModel){
        listItems.add(item)
        notifyItemInserted(listItems.size)
    }

    private fun removeItem(position: Int) {
        val current = listItems[position]
        REF_DATABASE_ROOT.child(NODE_REQUESTS).child(CURRENT_UID).child(current.id).removeValue()
        listItems.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, listItems.size)
    }

    fun checkRange() {
        notifyItemRangeChanged(0, listItems.size)
    }

    private fun addInContacts(position: Int) {
        val current = listItems[position]
        val contact1 = hashMapOf("fullname" to current.name, "id" to current.id)
        REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS).child(CURRENT_UID).child(current.id).setValue(contact1)

        val contact2 = hashMapOf("fullname" to USER.fullname, "id" to CURRENT_UID)
        REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS).child(current.id).child(CURRENT_UID).setValue(contact2)
    }
}