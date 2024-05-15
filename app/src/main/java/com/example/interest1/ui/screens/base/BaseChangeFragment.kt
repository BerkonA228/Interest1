package com.example.interest1.ui.screens.base

import android.view.*
import androidx.fragment.app.Fragment
import com.example.interest1.MainActivity

import com.example.interest1.R
import com.example.interest1.utilits.hideKeyboard


open class BaseChangeFragment (layout:Int) : Fragment(layout) {

    override fun onStart() {
        super.onStart()
        setHasOptionsMenu(true)
        (activity as MainActivity).mAppDrawer.disableDrawer()

    }

    override fun onStop() {
        super.onStop()
        hideKeyboard()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        (activity as MainActivity).menuInflater.inflate(R.menu.settings_menu_confirm,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.settings_confirm_change -> change()
        }
        return true
    }

    open fun change() {

    }
}
