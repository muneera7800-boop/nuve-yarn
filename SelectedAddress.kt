package com.example.bcashoppingapplication.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SelectedAddress(
    var address: String? = null,
    var isSelected : Boolean = false
) : Parcelable
