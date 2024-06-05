package com.example.interest1.ui.screens.settings.interests

import androidx.recyclerview.widget.RecyclerView
import com.example.interest1.R
import com.example.interest1.database.*
import com.example.interest1.models.InterestModel
import com.example.interest1.ui.screens.base.BaseChangeFragment
import com.example.interest1.utilits.APP_ACTIVITY
import com.example.interest1.utilits.AppValueEventListener
import com.example.interest1.utilits.showToast
import kotlinx.android.synthetic.main.fragment_change_interests.*


class ChangeInterestsFragment : BaseChangeFragment(R.layout.fragment_change_interests) {
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: InterestsAdapter
    private val mainInterestMutableList = mutableListOf<String>()
    private val userInterestMutableList = mutableListOf<String>()
    private val keysInterestMutableList = mutableListOf<String>()




    override fun onResume() {
         super.onResume()
        setData()
    }
    private fun setData() {
        // 1
        REF_DATABASE_ROOT.child(NODE_INTERESTS).addListenerForSingleValueEvent(AppValueEventListener { mainDataSnapshot ->
            mainDataSnapshot.children.forEach { mainData ->
                mainInterestMutableList.add(mainData.value.toString())
                keysInterestMutableList.add(mainData.key.toString())
            }
            // 2
            REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(NODE_INTERESTS).addListenerForSingleValueEvent(AppValueEventListener { userDataSnapshot ->
                userDataSnapshot.children.forEach { userData ->
                    userInterestMutableList.add(userData.value.toString())
                }
                val finalList = mutableListOf<InterestModel>()

                for (i in mainInterestMutableList.indices) {
                    val interest = mainInterestMutableList[i]
                    val key = keysInterestMutableList[i]
                    finalList.add(InterestModel(key, interest, userInterestMutableList.contains(interest)))
                }
                initRecyclerView(finalList.toList())
            })
        })
    }

    private fun initRecyclerView(interestModelList: List<InterestModel>) {
        mRecyclerView = interest_list
        mAdapter = InterestsAdapter(interestModelList)
        mRecyclerView.adapter = mAdapter
    }

    private fun clearAndUpdateData() {
        val data = mAdapter.getData()
        REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(NODE_INTERESTS).setValue(data)
    }

    override fun change() {
        super.change()
        if (mAdapter != null) {
            clearAndUpdateData()
        }
        showToast("Интересы обновлены")
        APP_ACTIVITY.supportFragmentManager.popBackStack()
    }
}
