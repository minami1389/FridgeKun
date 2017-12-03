package com.example.minami1389.fridgekun.model

import android.util.Log
import com.google.firebase.database.*

/**
 * Created by minami.baba on 2017/11/18.
 */

@IgnoreExtraProperties
class Item() {
    val mDatabase = FirebaseDatabase.getInstance().getReference("/fridge")

    var userId: String? = null
    var userName: String? = null
    var name: String? = null
    var expired: String? = null
    var key: String? = null

    constructor(name: String, userId:String, userName: String, expired: String, key: String? = null) : this() {
        this.userId = userId
        this.userName = userName
        this.name = name
        this.expired = expired
        this.key = key
    }

    fun writeNewItem(fridgeName: String) {
        val value = hashMapOf<String?, Any?>("name" to this.name, "userId" to this.userId, "userName" to this.userName, "expired" to this.expired)
        val key = mDatabase.child(fridgeName + "/items").push().key
        mDatabase.child(fridgeName + "/items" + "/" + key).updateChildren(value)
    }

    fun fetchItems(fridgeName: String, onSuccess: (result: Fridge?)->Unit) {
        mDatabase.child(fridgeName + "/items").addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot?, previousChildName: String?) {
                Log.d("Item onChildAdded", dataSnapshot.toString())
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

//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                if (dataSnapshot.hasChild("founderUid") && dataSnapshot.hasChild("password")) {
//                    val founderUid = dataSnapshot.child("founderUid").value.toString()
//                    val password = dataSnapshot.child("password").value.toString()
//                    onSuccess.invoke(Fridge(name, founderUid, password))
//                } else {
//                    onSuccess.invoke(null)
//                }
//            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("onCancelled", "error:", error.toException())
            }
        })
    }
}


