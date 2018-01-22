package com.example.minami1389.fridgekun.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.minami1389.fridgekun.R
import com.example.minami1389.fridgekun.model.User
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_welcome.*


class WelcomeActivity : AppCompatActivity() {

    var callbackManager = CallbackManager.Factory.create()
    var auth = FirebaseAuth.getInstance()
    var authListener: FirebaseAuth.AuthStateListener? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        var loginButton = findViewById<View>(R.id.loginButton) as LoginButton
        loginButton.setReadPermissions("email", "public_profile")
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                Log.d("WelcomeActivity", "facebook:onSuccess:" + loginResult)
                handleFacebookLogin(loginResult.accessToken)
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
                User(user.uid, user.displayName, user.photoUrl?.toString()).writeNewUser()
                startActivity(Intent(this, SelectFridgeActivity::class.java))
                finish()
                Log.d("WelcomeActivity", "onAuthStateChanged:signed_in")
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

    fun handleFacebookLogin(token: AccessToken) {
        welcomeTextView.text = "ログイン中..."
        loginButton.visibility = View.INVISIBLE

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential).addOnCompleteListener(this, object : OnCompleteListener<AuthResult> {
            override fun onComplete(task: Task<AuthResult>) {
                Log.d("WelcomeActivity", "signInWithCredential:onComplete:" + task.isSuccessful)
                startActivity(Intent(this@WelcomeActivity, SelectFridgeActivity::class.java))
                if (!task.isSuccessful) {
                    Log.w("WelcomeActivity", "signInWithCredential", task.getException())
                    Toast.makeText(this@WelcomeActivity, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
