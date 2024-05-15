package com.example.interest1

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.interest1.database.AUTH
import com.example.interest1.database.initFirebase
import com.example.interest1.database.initUser
import com.example.interest1.databinding.ActivityMainBinding
import com.example.interest1.ui.screens.main_list.MainListFragment
import com.example.interest1.ui.screens.register.EnterPhoneNumberFragment
import com.example.interest1.ui.objects.AppDrawer
import com.example.interest1.utilits.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding : ActivityMainBinding
    lateinit var mToolbar:Toolbar
    lateinit var mAppDrawer: AppDrawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        APP_ACTIVITY = this
        initFirebase()
        initUser{
            CoroutineScope(Dispatchers.IO).launch {
                initContacts()
            }
            initFields()
            initFunc()
        }
    }



    private fun initFunc() {
        setSupportActionBar(mToolbar)
        if (AUTH.currentUser!=null){
            mAppDrawer.create()
            replaceFragment(MainListFragment(),false)
        } else {
            replaceFragment(EnterPhoneNumberFragment(),false)
        }
    }

    private fun initFields() {
        mToolbar = mBinding.mainToolbar
        mAppDrawer = AppDrawer()

    }

    override fun onStart() {
        super.onStart()
        AppStates.updateState(AppStates.ONLINE)
    }

    override fun onStop() {
        super.onStop()
        AppStates.updateState(AppStates.OFFLINE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (ContextCompat.checkSelfPermission(APP_ACTIVITY, READ_CONTACTS)==PackageManager.PERMISSION_GRANTED){
            initContacts()
        }
    }
}
