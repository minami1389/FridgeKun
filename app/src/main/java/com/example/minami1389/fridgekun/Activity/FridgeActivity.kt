package com.example.minami1389.fridgekun.activity

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.TextView
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

    val itemEventListener = object: ChildEventListener {
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

class ItemAdapter(private val context: Context) : BaseAdapter() {
    private val layoutInflater: LayoutInflater
    private var items: ArrayList<Item> = ArrayList()

    private class ViewHolder {
        var itemNameTextView: TextView? = null
        var itemExpireTextView: TextView? = null
    }

    init {
        this.layoutInflater = LayoutInflater.from(context)
    }

    fun addItem(item: Item) {
        items.add(item)
    }

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        val holder: ViewHolder
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.grid_item_fridge, null)
            holder = ViewHolder()
            holder.itemNameTextView = convertView.findViewById<TextView>(R.id.itemNameTextView)
            holder.itemExpireTextView = convertView.findViewById<TextView>(R.id.itemExpireTextView)
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        holder.itemNameTextView?.setText(items[position].name)
        holder.itemExpireTextView?.setText(items[position].expired)

        return convertView!!
    }
}