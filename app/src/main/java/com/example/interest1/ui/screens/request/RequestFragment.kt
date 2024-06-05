package com.example.interest1.ui.screens.request

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.interest1.R
import com.example.interest1.database.CHILD_FULLNAME
import com.example.interest1.database.CHILD_ID
import com.example.interest1.database.CHILD_PHOTO_URL
import com.example.interest1.database.CURRENT_UID
import com.example.interest1.database.NODE_REQUESTS
import com.example.interest1.database.NODE_USERS
import com.example.interest1.database.REF_DATABASE_ROOT
import com.example.interest1.models.RequestModel
import com.example.interest1.ui.screens.base.BaseFragment
import com.example.interest1.utilits.APP_ACTIVITY
import com.example.interest1.utilits.AppValueEventListener
import kotlinx.android.synthetic.main.fragment_request.*

class RequestFragment: BaseFragment(R.layout.fragment_request) {
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: RequestItemAdapter

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = "Просмотр заявок"
        initRecyclerView()
        getData()
    }

    private fun getData() {
        REF_DATABASE_ROOT.child(NODE_REQUESTS).child(CURRENT_UID).addListenerForSingleValueEvent(AppValueEventListener { requests ->
            requests.children.forEach { request ->
                REF_DATABASE_ROOT.child(NODE_USERS).child(request.key.toString()).addListenerForSingleValueEvent(AppValueEventListener {
                    val id = it.child(CHILD_ID).value.toString()
                    val name = it.child(CHILD_FULLNAME).value.toString()
                    val photoUrl = it.child(CHILD_PHOTO_URL).value.toString()
                    mAdapter.updateListItems(
                        RequestModel(
                            id = id,
                            name = name,
                            photoUrl = photoUrl
                        )
                    )
                })
            }
        })
    }

    private fun initRecyclerView() {
        mRecyclerView = requestList
        mAdapter = RequestItemAdapter()
        mRecyclerView.adapter = mAdapter
        mAdapter.checkRange()
        mAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                if (itemCount == 0) {
                    listIsEmpty.visibility = View.VISIBLE
                }
                else {
                    listIsEmpty.visibility = View.GONE
                }
            }
        })
    }
}