package com.eedurda77.composerealm.data.remote.dto


import com.google.gson.annotations.SerializedName

data class CamerasDto(
    @SerializedName("data")
    val dataCamera: DataCamera,
    @SerializedName("success")
    val success: Boolean
)