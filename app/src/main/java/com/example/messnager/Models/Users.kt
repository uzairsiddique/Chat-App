package com.example.messnager.Models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize

data class Users(
    var uid: String = ""
    ,var username: String = "",
    var email: String = "",
    var profileImageurl: String = ""
) : Parcelable {


}