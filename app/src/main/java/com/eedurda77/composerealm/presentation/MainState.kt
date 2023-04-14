package com.eedurda77.composerealm.presentation

import com.eedurda77.composerealm.domain.models.DoorMain
import com.eedurda77.composerealm.domain.models.RoomWithCameras
import com.eedurda77.composerealm.presentation.ui.Status

data class MainState(
    val roomsWithCameras: List<RoomWithCameras> = emptyList(),
    val doors: List<DoorMain> = emptyList(),
    val status: Status = Status.CAMERA,
    val isLoading: Boolean = false,
    val error: String? = null,
    val idIsChangedName: Int = -1
)
