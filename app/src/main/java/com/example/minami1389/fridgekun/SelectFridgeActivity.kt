package com.example.minami1389.fridgekun

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import android.content.Intent
import android.util.Log
import android.view.View
import com.facebook.login.LoginManager
import com.google.android.gms.common.ErrorDialogFragment
import com.google.firebase.database.FirebaseDatabase
import android.content.DialogInterface




class SelectFridgeActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_team)

        var userNameTextView = findViewById(R.id.userNameTextView) as TextView
        val userName = intent.getStringExtra(WelcomeActivity.USER_NAME_EXTRA)
        userNameTextView.text = userName + "さんこんにちは"

        //var userPhotoUrl = intent.getStringArrayExtra(WelcomeActivity.USER_PHOTO_URL_EXTRA)

        setOnClickListener()
    }

    fun setOnClickListener() {
        val logoutButton = findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            LoginManager.getInstance().logOut()
            val intent = Intent(this, WelcomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }

        val createTeamButton = findViewById(R.id.createTeamButton)
        createTeamButton.setOnClickListener {
            val fridgeName = (findViewById(R.id.selectFridgeEditText) as TextView).text.toString()
            var uid = intent.getStringExtra(WelcomeActivity.USER_UID_EXTRA)
            Fridge(fridgeName, uid, "").writeNewFridge()
        }

        val joinTeamButton = findViewById(R.id.joinTeamButton)
        joinTeamButton.setOnClickListener {
            val fridgeName = (findViewById(R.id.selectFridgeEditText) as TextView).text.toString()
            Fridge().fetchFridge(fridgeName, { fridge ->
                if (fridge != null) {

                } else {
                    CustomDialogFragment().show(fragmentManager, "fridgeNotExist")
                 }
            })
        }
    }
}

class CustomDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle): Dialog {
        val builder = AlertDialog.Builder(activity)
        builder.setMessage("この名前のFridgeは存在しません")
                .setPositiveButton("はい", DialogInterface.OnClickListener { dialog, id ->
                    // FIRE ZE MISSILES!
                })
        return builder.create()
    }
}