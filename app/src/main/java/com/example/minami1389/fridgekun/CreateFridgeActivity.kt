package com.example.minami1389.fridgekun

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser



class CreateFridgeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_fridge)

        findViewById(R.id.existFridgeTextView).visibility = View.INVISIBLE

        val createFridgeButton = findViewById(R.id.createFridgeButton)
        createFridgeButton.setOnClickListener {
            val name = (findViewById(R.id.fridgeNameEditText) as TextView).text.toString()
            Fridge().fetchFridge(name, { fridge ->
                if (fridge != null) {
                    findViewById(R.id.existFridgeTextView).visibility = View.VISIBLE
                } else {
                    val password = (findViewById(R.id.fridgePasswordEditText) as TextView).text.toString()
                    val user = FirebaseAuth.getInstance().currentUser
                    Fridge(name, user?.uid.toString(), password).writeNewFridge()
                    val intent = Intent(this, FridgeActivity::class.java)
                    intent.putExtra(FridgeActivity.FRIDGE_NAME_EXTRA, name)
                    startActivity(intent)
                }
            })


        }
    }
}
