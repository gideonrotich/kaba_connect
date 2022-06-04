package com.swayy.kaba_connect.model

class Verified {
    private var fullname: String = ""


    constructor()

    constructor(fullname: String)

    {
        this.fullname = fullname

    }


    fun getFullname(): String
    {
        return fullname
    }

    fun setFullname(fullname: String)
    {
        this.fullname = fullname
    }


}