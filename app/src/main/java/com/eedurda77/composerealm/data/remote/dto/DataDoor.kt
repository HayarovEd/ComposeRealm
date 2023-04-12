package com.eedurda77.composerealm.data.remote.dto


import com.google.gson.annotations.SerializedName

data class DataDoor(
    @SerializedName("favorites")
    val favorites: Boolean,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("room")
    val room: String? = null,
    @SerializedName("snapshot")
    val snapshot: String? = null
)