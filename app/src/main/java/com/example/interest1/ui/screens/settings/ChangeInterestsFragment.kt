package com.example.interest1.ui.screens.settings

import android.view.View
import com.example.interest1.R
import com.example.interest1.database.*
import com.example.interest1.ui.screens.base.BaseChangeFragment
import com.example.interest1.utilits.APP_ACTIVITY
import com.example.interest1.utilits.showToast
import kotlinx.android.synthetic.main.fragment_change_interests.*


class ChangeInterestsFragment : BaseChangeFragment(R.layout.fragment_change_interests) {
    private val pathFishing = REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_FISHING)
    private val pathTraveling = REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_TRAVELING)
    private val pathSwimming = REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_SWIMMING)

    override fun onResume() {
         super.onResume()
        initFields()
    }

    private fun initFields() {
        pathFishing.setValue(false)
        pathTraveling.setValue(false)
        pathSwimming.setValue(false)
        interest_fishing.setOnClickListener {
            if (interest_fishing_choice.visibility == View.INVISIBLE) {
                interest_fishing_choice.visibility = View.VISIBLE
                pathFishing.setValue(true)
            } else {
                interest_fishing_choice.visibility = View.INVISIBLE
                pathFishing.setValue(false)
            }
        }
        interest_traveling.setOnClickListener {
            if (interest_traveling_choice.visibility == View.INVISIBLE) {
                interest_traveling_choice.visibility = View.VISIBLE
                pathTraveling.setValue(true)
            } else {
                interest_traveling_choice.visibility = View.INVISIBLE
                pathTraveling.setValue(false)
            }
        }
        interest_swimming.setOnClickListener {
            if (interest_swimming_choice.visibility == View.INVISIBLE) {
                interest_swimming_choice.visibility = View.VISIBLE
                pathSwimming.setValue(true)
            } else {
                interest_swimming_choice.visibility = View.INVISIBLE
                pathSwimming.setValue(false)
            }
        }
    }

    override fun change() {
        super.change()
        showToast("Интересы обновлены")
        APP_ACTIVITY.supportFragmentManager.popBackStack()
    }
}
