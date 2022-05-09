package com.thrifthunter

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StoriesData(
    val name: String,
    val photoUrl: String,
    val description: String
) : Parcelable