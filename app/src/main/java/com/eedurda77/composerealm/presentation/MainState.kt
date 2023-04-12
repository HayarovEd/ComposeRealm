package com.eedurda77.composerealm.presentation

import com.eedurda77.composerealm.domain.models.CameraMain
import com.eedurda77.composerealm.domain.models.DoorMain
import com.eedurda77.composerealm.presentation.ui.Status

data class MainState(
    val cameras: List<CameraMain> = emptyList(),
    val doors: List<DoorMain> = emptyList(),
    val status: Status = Status.CAMERA,
    val isLoading: Boolean = true,
    val error: String? = null
)
