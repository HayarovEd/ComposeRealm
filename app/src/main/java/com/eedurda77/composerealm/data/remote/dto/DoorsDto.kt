package com.eedurda77.composerealm.data.remote.dto


import com.google.gson.annotations.SerializedName

data class DoorsDto(
    @SerializedName("data")
    val `data`: List<DataDoor>,
    @SerializedName("success")
    val success: Boolean
)