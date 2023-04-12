package com.eedurda77.composerealm.data.remote.dto


import com.google.gson.annotations.SerializedName

data class DataCamera(
    @SerializedName("cameras")
    val cameras: List<Camera>,
    @SerializedName("room")
    val room: List<String>
)