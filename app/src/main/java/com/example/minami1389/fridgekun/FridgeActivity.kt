package com.example.minami1389.fridgekun

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class FridgeActivity : AppCompatActivity() {

    companion object Factory {
        val FRIDGE_NAME_EXTRA = "com.example.minami1389.fridgekun.FRIDGE_NAME_EXTRA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fridge)

        val fridgeNameTextView = findViewById(R.id.fridgeNameTextView) as TextView
        val fridgeName = intent.getStringExtra(FRIDGE_NAME_EXTRA)
        fridgeNameTextView.setText(fridgeName)
    }
}
