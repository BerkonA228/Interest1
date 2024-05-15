package com.example.interest1.ui.screens.groups

import androidx.recyclerview.widget.RecyclerView
import com.example.interest1.R
import com.example.interest1.database.*
import com.example.interest1.models.CommonModel
import com.example.interest1.ui.screens.base.BaseFragment
import com.example.interest1.utilits.*
import kotlinx.android.synthetic.main.fragment_add_contacts.*


class AddContactsFragment : BaseFragment(R.layout.fragment_add_contacts) {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: AddContactsAdapter
    private val mRefContactsList = REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS).child(CURRENT_UID)
    private val mRefUsers = REF_DATABASE_ROOT.child(NODE_USERS)
    private val mRefMessages = REF_DATABASE_ROOT.child(NODE_MESSAGES).child(CURRENT_UID)
    private var mListItems = listOf<CommonModel>()



    override fun onResume() {
        listContacts.clear()
        super.onResume()
        APP_ACTIVITY.title = "Добавить участников"
        hideKeyboard()
        initRecycleView()
        add_contacts_btn_next.setOnClickListener {
            if (listContacts.isEmpty()) showToast("Добавьте участников")
            else replaceFragment(CreateGroupFragment(listContacts))
        }
    }

    private fun initRecycleView() {
        mRecyclerView = add_contacts_recycle_view
        mAdapter = AddContactsAdapter()

        mRefContactsList.addListenerForSingleValueEvent(AppValueEventListener{ dataSnapshot ->
            mListItems = dataSnapshot.children.map { it.getCommonModel() }
            mListItems.forEach { model ->


                mRefUsers.child(model.id)
                    .addListenerForSingleValueEvent(AppValueEventListener{ dataSnapshot1 ->
                    val newModel = dataSnapshot1.getCommonModel()

                    mRefMessages.child(model.id).limitToLast(1).addListenerForSingleValueEvent(AppValueEventListener{ dataSnapshot2 ->
                        val tempList = dataSnapshot2.children.map { it.getCommonModel() }
                        if (tempList.isEmpty()){
                            newModel.lastmessage  = "Чат очищен"
                        } else{
                            newModel.lastmessage = tempList[0].text
                        }
                        if (newModel.fullname.isEmpty()){
                            newModel.fullname = newModel.phone
                        }
                        mAdapter.updateListItems(newModel)
                    })
                })
            }
        })

        mRecyclerView.adapter = mAdapter
    }
    companion object{
        val listContacts = mutableListOf<CommonModel>()
    }
}
