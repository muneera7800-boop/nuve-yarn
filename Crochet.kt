package com.example.bcashoppingapplication.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Crochet(
    var documentId: String? = null,
    var crochetName: String = "",
    var crochetPrice: Long = 0,
    var crochetDrawableName: String = "",
    var crochetDescription: String = "",
    var crochetQuantity: Int = 0,
    var crochetRating: Double = 0.0,
    var categoryType: String = ""
) : Parcelable