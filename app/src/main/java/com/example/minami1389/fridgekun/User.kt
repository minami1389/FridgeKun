package com.example.minami1389.fridgekun

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.IgnoreExtraProperties

/**
 * Created by minami1389 on 2017/08/20.
 */

@IgnoreExtraProperties
class User() {
    var uid = ""
    var name: String? = ""
    var photoUrl: String? = ""

    constructor(uid: String, name: String?, photoUrl: String?): this() {
        this.uid = uid
        this.name = name
        this.photoUrl = photoUrl
    }

    fun writeNewUser() {
        val mDatabase = FirebaseDatabase.getInstance().getReference()
        val value = hashMapOf<String?, String?>("name" to this.name, "photoUrl" to this.photoUrl)
        mDatabase.child("user").child(uid).setValue(value)
    }


}