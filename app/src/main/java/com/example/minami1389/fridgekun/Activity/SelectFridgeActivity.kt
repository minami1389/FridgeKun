package com.example.minami1389.fridgekun.Activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import android.content.Intent
import android.view.View
import com.example.minami1389.fridgekun.Model.Fridge
import com.example.minami1389.fridgekun.R
import com.facebook.login.LoginManager

class SelectFridgeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_team)

        var userNameTextView = findViewById(R.id.userNameTextView) as TextView
        val userName = FirebaseAuth.getInstance().currentUser?.displayName
        userNameTextView.text = userName + "さんこんにちは"

        findViewById(R.id.notFindfridgeTextView).visibility = View.INVISIBLE

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
            val intent = Intent(this, CreateFridgeActivity::class.java)
            startActivity(intent)
        }

        val joinTeamButton = findViewById(R.id.joinTeamButton)
        joinTeamButton.setOnClickListener {
            val fridgeName = (findViewById(R.id.selectFridgeEditText) as TextView).text.toString()
            Fridge().fetchFridge(fridgeName, { fridge ->
                if (fridge != null) {
                    findViewById(R.id.notFindfridgeTextView).visibility = View.INVISIBLE
                    val intent = Intent(this, FridgeActivity::class.java)
                    intent.putExtra(FridgeActivity.FRIDGE_NAME_EXTRA, fridgeName)
                    startActivity(intent)
                } else {
                    findViewById(R.id.notFindfridgeTextView).visibility = View.VISIBLE
                }
            })
        }
    }
}
