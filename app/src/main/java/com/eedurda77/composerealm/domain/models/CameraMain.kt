package com.eedurda77.composerealm.domain.models

data class CameraMain(
    val isFavorite: Boolean?,
    val id: Int?,
    val name: String?,
    val isRec: Boolean?,
    val room: String? = null,
    val urlPath: String?
)
