package com.eedurda77.composerealm.presentation.ui

sealed class MainEvent {
    object Refresh : MainEvent()
    data class ToggleStatus(val status: Status) : MainEvent()
    //data class ToggleVisibleFieldName(val isShow: Boolean) : MainEvent()
    data class ChangeCameraName(val name: String, val id:Int) : MainEvent()
    data class ChangeDoorName(val name: String, val id:Int) : MainEvent()
}