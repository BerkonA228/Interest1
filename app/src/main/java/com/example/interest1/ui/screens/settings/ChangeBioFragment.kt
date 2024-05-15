package com.example.interest1.ui.screens.settings

import com.example.interest1.R
import com.example.interest1.database.*
import com.example.interest1.ui.screens.base.BaseChangeFragment
import kotlinx.android.synthetic.main.fragment_change_bio.*


class ChangeBioFragment : BaseChangeFragment(R.layout.fragment_change_bio) {

    override fun onResume() {
        super.onResume()
        settings_input_bio.setText(USER.bio)
    }

    override fun change() {
        super.change()
        val newBio = settings_input_bio.text.toString()

        setBioToDatabase(newBio)
    }


}
