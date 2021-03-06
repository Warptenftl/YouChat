package com.idn.adniwhatsappclone.util

import android.os.Parcel
import android.os.Parcelable

data class User(
    val email: String? = "", // Model merupakan layer yang menunjuk pada objek dan
    val phone: String? = "", // data yang ada pada aplikasi
    val name: String? = "", // sehingga User disini akan memiliki data-data disamping
    val imageUrl: String? = "",
    val status: String? = "",
    val statusUrl: String? = "",
    val statusTime: String? = ""

)

data class Contact(
    val name: String?,
    val phone: String?
)

data class Chat(
    val chatParticipants: ArrayList<String>
)

data class Message(
    val sentBy: String? = "",
    val message: String? = "",
    val messageTime: Long? = 0
)

data class StatusListElement( // supaya intent dapat mengirim data, perlu untuk
    val userName: String?, // melakukan perubahan pada Model dari data class
    val userUrl: String?, // StatusListElement, maka data class perlu mewarisi
    val status: String?, // class Parcelable
    val statusUrl: String?,
    val statusTime: String?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userName)
        parcel.writeString(userUrl)
        parcel.writeString(status)
        parcel.writeString(statusUrl)
        parcel.writeString(statusTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StatusListElement> {
        override fun createFromParcel(parcel: Parcel): StatusListElement {
            return StatusListElement(parcel)
        }

        override fun newArray(size: Int): Array<StatusListElement?> {
            return arrayOfNulls(size)
        }
    }
}