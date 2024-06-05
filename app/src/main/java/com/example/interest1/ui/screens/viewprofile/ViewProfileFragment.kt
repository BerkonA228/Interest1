package com.example.interest1.ui.screens.viewprofile

import com.example.interest1.R
import com.example.interest1.database.CHILD_BIO
import com.example.interest1.database.CHILD_FULLNAME
import com.example.interest1.database.CHILD_PHOTO_URL
import com.example.interest1.database.CURRENT_UID
import com.example.interest1.database.NODE_INTERESTS
import com.example.interest1.database.NODE_PHONES_CONTACTS
import com.example.interest1.database.NODE_REQUESTS
import com.example.interest1.database.NODE_USERS
import com.example.interest1.database.REF_DATABASE_ROOT
import com.example.interest1.database.USER
import com.example.interest1.ui.screens.base.BaseFragment
import com.example.interest1.utilits.APP_ACTIVITY
import com.example.interest1.utilits.AppValueEventListener
import com.example.interest1.utilits.downloadAndSetImage
import kotlinx.android.synthetic.main.fragment_profile.*

class ViewProfileFragment(private val userId: String): BaseFragment(R.layout.fragment_view_full_profile) {
    private lateinit var fullnameMain: String

    override fun onResume() {
        super.onResume()
        getAndSetData()
        cancelUser.setOnClickListener {
            cancelUser()
        }
        confirmUser.setOnClickListener {
            confirmUser()
            cancelUser()
        }
    }

    private fun getAndSetData() {
        REF_DATABASE_ROOT.child(NODE_USERS).child(userId).addListenerForSingleValueEvent(
                AppValueEventListener { user ->
                    val fullName = user.child(CHILD_FULLNAME).value.toString()
                    val bio = user.child(CHILD_BIO).value.toString()
                    val photoUrl = user.child(CHILD_PHOTO_URL).value.toString()
                    var interestString = ""
                    user.child(NODE_INTERESTS).children.forEach { userInterests ->
                        interestString = interestString + userInterests.value.toString().toLowerCase() + ", "
                    }
                    interestString = interestString.substringBeforeLast(", ")
                    fullnameMain = fullName
                    APP_ACTIVITY.title = fullnameMain
                    userName.text = fullnameMain
                    userInterests.text = "Интересы: $interestString"
                    aboutUser.text = "О себе: $bio"
                    userImage.downloadAndSetImage(photoUrl)
                })
    }

    private fun cancelUser() {
        REF_DATABASE_ROOT.child(NODE_REQUESTS).child(CURRENT_UID).child(userId).removeValue()
        APP_ACTIVITY.supportFragmentManager.popBackStack()
    }

    private fun confirmUser() {
        val contact1 = hashMapOf("fullname" to fullnameMain, "id" to userId)
        REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS).child(CURRENT_UID).child(userId).setValue(contact1)

        val contact2 = hashMapOf("fullname" to USER.fullname, "id" to CURRENT_UID)
        REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS).child(userId).child(CURRENT_UID).setValue(contact2)
    }

}