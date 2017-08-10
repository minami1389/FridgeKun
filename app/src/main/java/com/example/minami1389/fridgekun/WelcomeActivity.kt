package com.example.minami1389.fridgekun

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.facebook.login.LoginManager
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import android.support.annotation.NonNull
import android.support.v4.app.FragmentActivity
import android.util.AttributeSet
import android.widget.TextView
import android.widget.Toast
import com.facebook.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider


class WelcomeActivity : AppCompatActivity() {
    var callbackManager = CallbackManager.Factory.create()
    var auth = FirebaseAuth.getInstance()
    var authListener: FirebaseAuth.AuthStateListener? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        var loginButton = findViewById(R.id.login_button) as LoginButton
        loginButton.setReadPermissions("email", "public_profile")
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d("WelcomeActivity", "facebook:onSuccess:" + loginResult)
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            override fun onCancel() {
                Log.d("WelcomeActivity", "facebook:onCancel")
            }

            override fun onError(exception: FacebookException) {
                Log.d("WelcomeActivity", "facebook:onError", exception)
            }
        })

        authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                // User is signed in
                var welcomeTextView = findViewById(R.id.welcomeTextView) as TextView
                welcomeTextView.text = user.displayName
                Log.d("WelcomeActivity", "onAuthStateChanged:signed_in:" + user.photoUrl)
            } else {
                // User is signed out
                Log.d("WelcomeActivity", "onAuthStateChanged:signed_out")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    public override fun onStart() {
        super.onStart()
        auth.addAuthStateListener(authListener as FirebaseAuth.AuthStateListener)
    }

    public override fun onStop() {
        super.onStop()
        if (authListener != null) {
            auth.removeAuthStateListener(authListener as FirebaseAuth.AuthStateListener)
        }
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d("WelcomeActivity", "handleFacebookAccessToken:" + token);

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential).addOnCompleteListener(this, object: OnCompleteListener<AuthResult> {
            override public fun onComplete(task: Task<AuthResult>) {
                Log.d("WelcomeActivity", "signInWithCredential:onComplete:" + task.isSuccessful)

                if (!task.isSuccessful) {
                    Log.w("WelcomeActivity", "signInWithCredential", task.getException())
                    Toast.makeText(this@WelcomeActivity, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

}
