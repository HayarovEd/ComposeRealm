package com.eedurda77.composerealm.domain.models

data class DoorMain(
    val isFavorite: Boolean?,
    val id: Int?,
    val name: String?,
    val room: String? = null,
    val urlPath: String? = null
)
