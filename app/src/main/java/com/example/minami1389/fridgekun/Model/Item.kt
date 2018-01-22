package com.example.minami1389.fridgekun.model

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.IgnoreExtraProperties

/**
 * Created by minami.baba on 2017/11/18.
 */

@IgnoreExtraProperties
class Item() {
    companion object {
        val mDatabase = FirebaseDatabase.getInstance().getReference("/fridge")

        fun addEventListener(fridgeName: String, listener: ChildEventListener) {
            mDatabase.child(fridgeName + "/items").addChildEventListener(listener)
        }

        fun removeEventListener(fridgeName: String, listener: ChildEventListener) {
            mDatabase.child(fridgeName + "/items").removeEventListener(listener)
        }
    }

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
}


