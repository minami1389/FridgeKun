package com.example.minami1389.fridgekun

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import org.w3c.dom.Text
import android.content.Intent
import com.facebook.login.LoginManager


class SelectTeamActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_team)

        var userNameTextView = findViewById(R.id.userNameTextView) as TextView
        val userName = intent.getStringExtra(WelcomeActivity.USER_NAME_EXTRA)
        userNameTextView.text = userName + "さんこんにちは"

        //var userPhotoUrl = intent.getStringArrayExtra(WelcomeActivity.USER_PHOTO_URL_EXTRA)

        val logoutButton = findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            LoginManager.getInstance().logOut()
            val intent = Intent(this, WelcomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }
    }
}
