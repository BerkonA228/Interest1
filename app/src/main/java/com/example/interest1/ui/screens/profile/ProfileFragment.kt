package com.example.interest1.ui.screens.profile


import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
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
import com.example.interest1.models.ProfileModel
import com.example.interest1.ui.screens.base.BaseFragment
import com.example.interest1.utilits.APP_ACTIVITY
import com.example.interest1.utilits.AppValueEventListener
import com.example.interest1.utilits.downloadAndSetImage
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : BaseFragment(R.layout.fragment_profile) {
    private lateinit var spinner: Spinner
    private var pullPosition = 0
    private val userInterestMutableList = mutableListOf<String>()
    private val pullOfUsersMutableList = mutableListOf<ProfileModel>()
    private val pullOfSeenUsersMutableList = mutableListOf<String>()
    private val usersContactsMutableList = mutableListOf<String>()
    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = "Просмотр анкет"
        checkContacts()
        setData()
        confirmUser.setOnClickListener {
            sendRequest()
            addInSeenList()
            updateUI()
        }
        cancelUser.setOnClickListener {
            addInSeenList()
            updateUI()
        }
    }

    private fun checkContacts() {
        REF_DATABASE_ROOT.child(NODE_PHONES_CONTACTS).child(CURRENT_UID).addListenerForSingleValueEvent(AppValueEventListener { contacts ->
            contacts.children.forEach {contact ->
                usersContactsMutableList.add(contact.key.toString())
            }
        })
    }

    private fun addInSeenList() {
        pullOfSeenUsersMutableList.add(pullOfUsersMutableList[pullPosition].id)
        pullPosition += 1
    }

    private fun sendRequest() {
        val user = pullOfUsersMutableList[pullPosition]
        val res = mapOf("request" to "true")
        REF_DATABASE_ROOT.child(NODE_REQUESTS).child(user.id).child(CURRENT_UID).setValue(res)
    }

    private fun setData() {
        // 1
        REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(NODE_INTERESTS)
            .addListenerForSingleValueEvent(
                AppValueEventListener { userDataSnapshot ->
                    userDataSnapshot.children.forEach { userData ->
                        userInterestMutableList.add(userData.value.toString())
                    }
                    if (userInterestMutableList.isEmpty()) {
                        usersInterests.visibility = View.GONE
                        userImage.visibility = View.INVISIBLE
                        userName.text = "Сначала выберите интересы в настройках"
                        userInterests.visibility = View.GONE
                        aboutUser.visibility = View.GONE
                        cancelUser.visibility = View.GONE
                        confirmUser.visibility = View.GONE
                    }
                    else {
                        initSpinner(userInterestMutableList)
                    }
                })
    }

    private fun initSpinner(interestsList: List<String>) {
        spinner = usersInterests

        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item, interestsList
        )
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
//              searchUserByInterests
                searchUserByInterests(interestsList[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }

    private fun searchUserByInterests(interest: String) {
        pullOfUsersMutableList.clear()
        pullPosition = 0
        // 1
        REF_DATABASE_ROOT.child(NODE_USERS)
            .addListenerForSingleValueEvent(
                AppValueEventListener { users ->
                    users.children.forEach { user ->
                        // 2
                        val userId = user.key.toString()
                        val fullName = user.child(CHILD_FULLNAME).value.toString()
                        val bio = user.child(CHILD_BIO).value.toString()
                        val photoUrl = user.child(CHILD_PHOTO_URL).value.toString()
                        if (userId != CURRENT_UID && userId !in pullOfSeenUsersMutableList && userId !in usersContactsMutableList) {
                            val usersInterestsMutableList = mutableListOf<String>()
                            var interestString = ""
                            user.child(NODE_INTERESTS).children.forEach {userInterests ->
                                interestString = interestString + userInterests.value.toString().toLowerCase() + ", "
                                usersInterestsMutableList.add(userInterests.value.toString())
                            }
                            interestString = interestString.substringBeforeLast(", ")
                            if (interest in usersInterestsMutableList) {
                                pullOfUsersMutableList.add(
                                    ProfileModel(
                                        id = userId,
                                        name = fullName,
                                        bio = bio,
                                        photoUrl = photoUrl,
                                        interests = interestString
                                    )
                                )
                            }
                        }
                    }
                    updateUI()
                })
    }

    private fun updateUI() {
        if (pullPosition < pullOfUsersMutableList.size) {
            val profile = pullOfUsersMutableList[pullPosition]
            userName.text = profile.name
            userInterests.text = "Интересы: ${profile.interests}"
            aboutUser.text = "О себе: ${profile.bio}"
            userImage.downloadAndSetImage(profile.photoUrl)
        }
        else {
            val spinnerPsition = spinner.selectedItemPosition
            if (spinnerPsition < spinner.count - 1) {
                spinner.setSelection(spinner.selectedItemPosition + 1)
            }
            else {
                userImage.visibility = View.INVISIBLE
                userName.text = "Просмотрены все анкеты"
                userInterests.visibility = View.GONE
                aboutUser.visibility = View.GONE
                cancelUser.visibility = View.GONE
                confirmUser.visibility = View.GONE
            }
        }
    }
}
