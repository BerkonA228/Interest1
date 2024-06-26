package com.example.interest1.ui.screens.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.interest1.R
import com.example.interest1.database.CURRENT_UID
import com.example.interest1.database.NODE_PHONES_CONTACTS
import com.example.interest1.database.NODE_REQUESTS
import com.example.interest1.database.NODE_USERS
import com.example.interest1.database.REF_DATABASE_ROOT
import com.example.interest1.database.getCommonModel
import com.example.interest1.models.CommonModel
import com.example.interest1.ui.screens.base.BaseFragment
import com.example.interest1.ui.screens.request.RequestFragment
import com.example.interest1.ui.screens.single_chat.SingleChatFragment
import com.example.interest1.utilits.APP_ACTIVITY
import com.example.interest1.utilits.AppValueEventListener
import com.example.interest1.utilits.downloadAndSetImage
import com.example.interest1.utilits.replaceFragment
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.contact_item.view.*
import kotlinx.android.synthetic.main.fragment_contacts.*


class ContactsFragment : BaseFragment(R.layout.fragment_contacts) {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: FirebaseRecyclerAdapter<CommonModel, ContactsHolder>
    private lateinit var mRefContacts:DatabaseReference
    private lateinit var mRefUsers: DatabaseReference
    private lateinit var mRefUsersListener: AppValueEventListener
    private  var mapListeners = hashMapOf<DatabaseReference,AppValueEventListener>()

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = "Контакты"
        checkRequests()
        initRecycleView()
        item_request.setOnClickListener {
            replaceFragment(RequestFragment())
        }
    }

    private fun checkRequests() {
        REF_DATABASE_ROOT.child(NODE_REQUESTS).child(CURRENT_UID).addListenerForSingleValueEvent(AppValueEventListener { requests ->
            val count = requests.children.count()
            if (count > 0) {
                item_counter.text = count.toString()
                item_request.visibility = View.VISIBLE
            }
        })
    }

    private fun initRecycleView() {
        mRecyclerView = contacts_recycle_view
        mRefContacts = REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS).child(CURRENT_UID)

        val options = FirebaseRecyclerOptions.Builder<CommonModel>()
            .setQuery(mRefContacts,CommonModel::class.java)
            .build()
        mAdapter = object: FirebaseRecyclerAdapter<CommonModel, ContactsHolder>(options){

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.contact_item,parent,false)
                return ContactsHolder(view)
            }

            override fun onBindViewHolder(
                holder: ContactsHolder,
                position: Int,
                model: CommonModel
            ) {
                mRefUsers = REF_DATABASE_ROOT.child(NODE_USERS).child(model.id)

                mRefUsersListener = AppValueEventListener {
                    val contact = it.getCommonModel()
                    if (contact.fullname.isEmpty()){
                        holder.name.text = model.fullname
                    } else {
                        holder.name.text = contact.fullname
                    }
                    holder.status.text = contact.state
                    holder.photo.downloadAndSetImage(contact.photoUrl)
                    holder.itemView.setOnClickListener { replaceFragment(SingleChatFragment(model)) }
                }
                mRefUsers.addValueEventListener(mRefUsersListener)
                mapListeners[mRefUsers] = mRefUsersListener

            }
        }
        mRecyclerView.adapter = mAdapter
        mAdapter.startListening()
    }

    class ContactsHolder(view: View):RecyclerView.ViewHolder(view){
        val name:TextView = view.contact_fullname
        val status:TextView = view.contact_status
        val photo:CircleImageView = view.contact_photo
    }

    override fun onPause() {
        super.onPause()
        mAdapter.stopListening()
        mapListeners.forEach{
            it.key.removeEventListener(it.value)
        }
        APP_ACTIVITY.title = "Interest"
    }
}



