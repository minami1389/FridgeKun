package com.example.minami1389.fridgekun

import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import com.facebook.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider


class WelcomeActivity : AppCompatActivity() {
    companion object Factory {
        val USER_NAME_EXTRA = "com.example.minami1389.fridgekun.USER_NAME_EXTRA"
        val USER_PHOTO_URL_EXTRA = "com.example.minami1389.fridgekun.USER_PHOTO_URL_EXTRA"
        val USER_UID_EXTRA = "com.example.minami1389.fridgekun.USER_UID_EXTRA"
    }

    var callbackManager = CallbackManager.Factory.create()
    var auth = FirebaseAuth.getInstance()
    var authListener: FirebaseAuth.AuthStateListener? = null

    var progressDialog : ProgressDialog? = null

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
            progressDialog?.dismiss()
            val user = firebaseAuth.currentUser
            if (user != null) {
                // User is signed in
                User(user.uid, user.displayName, user.photoUrl?.toString()).writeNewUser()
                val intent = Intent(this, SelectFridgeActivity::class.java)
                intent.putExtra(USER_NAME_EXTRA, user.displayName)
                intent.putExtra(USER_PHOTO_URL_EXTRA, user.photoUrl.toString())
                intent.putExtra(USER_UID_EXTRA, user.uid)
                startActivity(intent)
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

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d("WelcomeActivity", "handleFacebookAccessToken:" + token);

        progressDialog = ProgressDialog(this)
        progressDialog?.setTitle("Loading")
        progressDialog?.show()

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential).addOnCompleteListener(this, object : OnCompleteListener<AuthResult> {
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
