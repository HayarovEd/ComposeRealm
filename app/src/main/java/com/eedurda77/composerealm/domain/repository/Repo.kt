package com.eedurda77.composerealm.domain.repository

import com.eedurda77.composerealm.domain.models.DoorMain
import com.eedurda77.composerealm.domain.models.RoomWithCameras
import com.eedurda77.composerealm.utils.Resource
import kotlinx.coroutines.flow.Flow

interface Repo {
    suspend fun getCameras(isRefresh: Boolean): Flow<Resource<List<RoomWithCameras>>>
    suspend fun getDoors(isRefresh: Boolean): Flow<Resource<List<DoorMain>>>
    suspend fun changeCameraFavorite(isUpdateFavorite:Boolean, id: Int)
    suspend fun changeDoorName(name: String, id: Int)
}