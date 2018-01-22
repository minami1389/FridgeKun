package com.example.minami1389.fridgekun.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.example.minami1389.fridgekun.R
import com.example.minami1389.fridgekun.model.User
import com.facebook.AccessToken
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth

/**
 * Created by minami.baba on 2018/01/23.
 */

class LoginActivity : AppCompatActivity() {
    var auth = FirebaseAuth.getInstance()
    var authListener: FirebaseAuth.AuthStateListener? = null
    var token: AccessToken? = null

    val keyToken = "KEY_TOKEN"
    fun createIntent(token: AccessToken): Intent {
        var intent = Intent(this, LoginActivity::class.java)
        intent.putExtra(keyToken, token)
        return intent
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        token = intent.getParcelableExtra(keyToken)

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

    override fun onResume() {
        super.onResume()
        val credential = FacebookAuthProvider.getCredential(token?.token!!)
        auth.signInWithCredential(credential).addOnCompleteListener(this, object : OnCompleteListener<AuthResult> {
            override fun onComplete(task: Task<AuthResult>) {
                Log.d("WelcomeActivity", "signInWithCredential:onComplete:" + task.isSuccessful)
                if (!task.isSuccessful) {
                    Log.w("WelcomeActivity", "signInWithCredential", task.getException())
                    Toast.makeText(this@LoginActivity, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
