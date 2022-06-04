package com.swayy.kaba_connect.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Userr(val fullname: String, val email:String,val mobile:String,val uid:String,val image:String,val profession:String): Parcelable
{
    constructor(): this("","","","","","")
}