package com.example.minami1389.fridgekun.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.example.minami1389.fridgekun.Activity.LoginActivity
import com.example.minami1389.fridgekun.R
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton


class WelcomeActivity : AppCompatActivity() {

    var callbackManager = CallbackManager.Factory.create()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        var loginButton = findViewById<View>(R.id.loginButton) as LoginButton
        loginButton.setReadPermissions("email", "public_profile")
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d("WelcomeActivity", "facebook:onSuccess:" + loginResult)
                startActivity(LoginActivity().createIntent(loginResult.getAccessToken()))
            }
            override fun onCancel() {
                Log.d("WelcomeActivity", "facebook:onCancel")
            }
            override fun onError(exception: FacebookException) {
                Log.d("WelcomeActivity", "facebook:onError", exception)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}
