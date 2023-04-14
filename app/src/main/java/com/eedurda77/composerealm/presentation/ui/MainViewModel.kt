package com.eedurda77.composerealm.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eedurda77.composerealm.domain.repository.Repo
import com.eedurda77.composerealm.presentation.MainState
import com.eedurda77.composerealm.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(private val repo: Repo) : ViewModel() {
    private var _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    init {
        getData()
    }

    fun onEvent(event: MainEvent) {
        when (event) {
            is MainEvent.ChangeCameraStatusFavorite -> {
                viewModelScope.launch{
                    repo.changeCameraFavorite(event.isFavorite, event.id)
                }
                getData()
            }
            is MainEvent.ChangeDoorName -> {
                viewModelScope.launch{
                    repo.changeDoorName(event.name, event.id)
                }
                getData()
            }
            is MainEvent.Refresh -> {
                _state.update { currentState ->
                    currentState.copy(
                        isLoading = true
                    )
                }
                getData()
            }
            is MainEvent.ToggleStatus -> {
                _state.update { currentState ->
                    currentState.copy(
                        status = event.status
                    )
                }
                getData()
            }
        }
    }

    private fun getData() {
        val isRefresh = _state.value.isLoading
        when (_state.value.status) {
            Status.CAMERA -> {
                viewModelScope.launch {
                    repo.getCameras(isRefresh = isRefresh).collect { resultCamera ->
                        when (resultCamera) {
                            is Resource.Error -> {
                                _state.update { currentState ->
                                    currentState.copy(
                                        isLoading = false,
                                        error = resultCamera.message,
                                    )
                                }
                            }
                            is Resource.Success -> {
                                _state.update { currentState ->
                                    currentState.copy(
                                        isLoading = false,
                                        roomswithCamers = resultCamera.data?: emptyList()
                                    )
                                }

                            }
                        }
                    }
                }
            }
            Status.DOOR -> {
                viewModelScope.launch {
                    repo.getDoors(isRefresh = isRefresh).collect { result ->
                        when (result) {
                            is Resource.Error -> {
                                _state.update { currentState ->
                                    currentState.copy(
                                        isLoading = false,
                                        error = result.message
                                    )
                                }
                            }
                            is Resource.Success -> {
                                _state.update { currentState ->
                                    currentState.copy(
                                        isLoading = false,
                                        doors = result.data ?: emptyList()
                                    )
                                }
                            }
                        }
                    }

                }
            }
        }
    }

    /*private fun convertToCameraMainByRoom(
        cameras: List<CameraMain>,
        rooms: List<RoomMain>
    ): List<RoomWithCameras> {
        val roomsByCameras = mutableListOf<RoomWithCameras>()
        rooms.forEach { room ->
            val camerasByRoom = cameras.filter { it.room == room.nameRoom }
            roomsByCameras.add(
                RoomWithCameras(
                    nameRoom = room.nameRoom,
                    cameras = camerasByRoom
                )
            )
        }
        return roomsByCameras
    }*/
}