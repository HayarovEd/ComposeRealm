package com.eedurda77.composerealm.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eedurda77.composerealm.domain.repository.Repo
import com.eedurda77.composerealm.presentation.MainState
import com.eedurda77.composerealm.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repo: Repo) : ViewModel() {
    private var _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    init {
        getCameras()
    }

    fun onEvent(event: MainEvent) {
        when (event) {
            is MainEvent.ChangeCameraName -> {
                viewModelScope.launch{
                    repo.changeCameraName(event.name, event.id)
                }
            }
            is MainEvent.ChangeDoorName -> {
                viewModelScope.launch{
                    repo.changeDoorName(event.name, event.id)
                }
            }
            is MainEvent.Refresh -> {
                _state.update { currentState ->
                    currentState.copy(
                        isLoading = true
                    )
                }
            }
            is MainEvent.ToggleStatus -> {
                _state.update { currentState ->
                    currentState.copy(
                        status = event.status
                    )
                }
                getCameras()
            }
        }
    }

    private fun getCameras() {
        val isRefresh = _state.value.isLoading
        when (_state.value.status) {
            Status.CAMERA -> {
                viewModelScope.launch {
                    repo.getCameras(isRefresh = isRefresh).collect { result ->
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
                                        cameras = result.data ?: emptyList()
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
}