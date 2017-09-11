package com.example.minami1389.fridgekun

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.IgnoreExtraProperties

/**
 * Created by minami1389 on 2017/09/07.
 */
@IgnoreExtraProperties
class Team() {
    val mDatabase = FirebaseDatabase.getInstance().getReference()

    var name: String? = ""

    constructor(name: String) : this() {
        this.name = name
    }

    fun writeNewTeam(founderUid: String) {
        mDatabase.child("team").child(name).child("users").child(founderUid).setValue(true)
    }

    fun addUser(teamName: String, uid: String) {
        mDatabase.child("team").child(teamName).child("users").child(uid).setValue(true)
        mDatabase.child("user").child(uid).child("groups").child(teamName).setValue(true)
    }
}


