package com.example.minami1389.fridgekun.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.example.minami1389.fridgekun.R
import com.example.minami1389.fridgekun.model.Fridge
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth

class SelectFridgeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_team)

        var userNameTextView = findViewById<View>(R.id.userNameTextView) as TextView
        val userName = FirebaseAuth.getInstance().currentUser?.displayName
        userNameTextView.text = userName + "さんこんにちは"

        findViewById<View>(R.id.notFindfridgeTextView).visibility = View.INVISIBLE

        setOnClickListener()

        title = "FridgeKunを呼ぼう"
    }

    fun setOnClickListener() {
        val logoutButton = findViewById<View>(R.id.logoutButton)
        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            LoginManager.getInstance().logOut()
            val intent = Intent(this, WelcomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }

        val createTeamButton = findViewById<View>(R.id.createTeamButton)
        createTeamButton.setOnClickListener {
            val intent = Intent(this, CreateFridgeActivity::class.java)
            startActivity(intent)
        }

        val joinTeamButton = findViewById<View>(R.id.joinTeamButton)
        joinTeamButton.setOnClickListener {
            val fridgeName = (findViewById<View>(R.id.selectFridgeEditText) as TextView).text.toString()
            Fridge().fetchFridge(fridgeName, { fridge ->
                if (fridge != null) {
                    findViewById<View>(R.id.notFindfridgeTextView).visibility = View.INVISIBLE
                    val intent = Intent(this, FridgeActivity::class.java)
                    intent.putExtra(FridgeActivity.FRIDGE_NAME_EXTRA, fridgeName)
                    startActivity(intent)
                } else {
                    findViewById<View>(R.id.notFindfridgeTextView).visibility = View.VISIBLE
                }
            })
        }
    }
}
