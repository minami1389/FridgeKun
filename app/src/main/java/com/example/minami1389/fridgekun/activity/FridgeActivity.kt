package com.example.minami1389.fridgekun.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.GridView
import com.example.minami1389.fridgekun.ItemAdapter
import com.example.minami1389.fridgekun.R
import com.example.minami1389.fridgekun.fragment.AddItemFragment
import com.example.minami1389.fridgekun.model.Item
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError


class FridgeActivity : AppCompatActivity() {

    companion object Factory {
        val FRIDGE_NAME_EXTRA = "com.example.minami1389.fridgekun.FRIDGE_NAME_EXTRA"
    }

    var fridgeName = ""
    private var itemAdapter: ItemAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fridge)

        fridgeName = intent.getStringExtra(FRIDGE_NAME_EXTRA)
        title = fridgeName

        val gridView = findViewById<GridView>(R.id.gridView)
        itemAdapter = ItemAdapter(this)
        gridView.adapter = itemAdapter

        val addItemButton = findViewById<View>(R.id.addItemButton)
        addItemButton.setOnClickListener {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.add(R.id.addItemFragmentContainer, AddItemFragment())
            transaction.commit()
        }

        Item.addEventListener(fridgeName, itemEventListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        Item.removeEventListener(fridgeName, itemEventListener)
    }

    val itemEventListener = object : ChildEventListener {
        override fun onChildAdded(dataSnapshot: DataSnapshot?, previousChildName: String?) {
            val data = dataSnapshot!!
            val name = data.child("name").value.toString()
            val userId = data.child("userId").value.toString()
            val userName = data.child("userName").value.toString()
            val expired = data.child("expired").value.toString()
            val key = data.key

            itemAdapter?.addItem(Item(name, userId, userName, expired, key))
            itemAdapter?.notifyDataSetChanged()
        }

        override fun onChildChanged(dataSnapshot: DataSnapshot?, previousChildName: String?) {
            Log.d("Item onChildChanged", dataSnapshot.toString())
        }

        override fun onChildRemoved(dataSnapshot: DataSnapshot?) {
            Log.d("Item onChildRemoved", dataSnapshot.toString())
        }


        override fun onChildMoved(dataSnapshot: DataSnapshot?, previousChildName: String?) {
            Log.d("Item onChildMoved", dataSnapshot.toString())
        }

        override fun onCancelled(error: DatabaseError) {
            Log.w("onCancelled", "error:", error.toException())
        }
    }

}