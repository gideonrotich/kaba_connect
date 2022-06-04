package com.swayy.kaba_connect.model

import kotlinx.android.parcel.Parcelize

class User {
    private var fullname: String = ""
    private var email: String = ""
    private var mobile: String = ""
    private var uid: String = ""
    private var image: String = ""
    private var time: String = ""

    constructor()
    constructor(fullname: String, email: String, mobile: String, uid: String,image: String,time: String) {
        this.fullname = fullname
        this.email = email
        this.mobile = mobile
        this.uid = uid
        this.image = image
        this.time = time
    }

    fun getFullname(): String
    {
        return fullname
    }

    fun setFullname(fullname: String)
    {
        this.fullname = fullname
    }

    fun getEmail(): String
    {
        return email
    }

    fun setEmail(email: String)
    {
        this.email = email
    }


    fun getMobile(): String
    {
        return mobile
    }

    fun setMobile(mobile: String)
    {
        this.mobile = mobile
    }

    fun getUid(): String
    {
        return uid
    }

    fun setUid(uid: String)
    {
        this.uid = uid
    }

    fun getImage(): String
    {
        return image
    }

    fun setImage(image: String)
    {
        this.image = image
    }
    fun getTime(): String
    {
        return time
    }

    fun setTime(time: String)
    {
        this.time = time
    }
}