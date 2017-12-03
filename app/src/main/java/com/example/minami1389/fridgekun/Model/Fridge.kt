package com.example.minami1389.fridgekun.model

import android.util.Log
import com.google.firebase.database.*

/**
 * Created by minami1389 on 2017/09/07.
 */
@IgnoreExtraProperties
class Fridge() {
    val mDatabase = FirebaseDatabase.getInstance().getReference("/fridge")

    var name: String? = null
    var founderUid: String? = null
    var password: String? = null

    constructor(name: String, founderUid: String, password: String) : this() {
        this.name = name
        this.founderUid = founderUid
        this.password = password
    }

    fun writeNewFridge() {
        val value = hashMapOf<String?, String?>("founderUid" to this.founderUid, "password" to this.password)
        mDatabase.child(name).setValue(value)
    }

    fun fetchFridge(name: String, onSuccess: (result: Fridge?)->Unit) {
        mDatabase.child(name).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.hasChild("founderUid") && dataSnapshot.hasChild("password")) {
                    val founderUid = dataSnapshot.child("founderUid").value.toString()
                    val password = dataSnapshot.child("password").value.toString()
                    onSuccess.invoke(Fridge(name, founderUid, password))
                } else {
                    onSuccess.invoke(null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("onCancelled", "error:", error.toException())
            }
        })
    }
}


