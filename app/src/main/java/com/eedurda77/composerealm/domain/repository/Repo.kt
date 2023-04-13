package com.eedurda77.composerealm.domain.repository

import com.eedurda77.composerealm.domain.models.CameraMain
import com.eedurda77.composerealm.domain.models.DoorMain
import com.eedurda77.composerealm.domain.models.RoomMain
import com.eedurda77.composerealm.domain.models.RoomWithCameras
import com.eedurda77.composerealm.utils.Resource
import kotlinx.coroutines.flow.Flow

interface Repo {
    suspend fun getCameras(isRefresh: Boolean): Flow<Resource<List<RoomWithCameras>>>

    //suspend fun getRooms(isRefresh: Boolean): Flow<Resource<List<RoomMain>>>
    suspend fun getDoors(isRefresh: Boolean): Flow<Resource<List<DoorMain>>>
    suspend fun changeCameraFavorite(isFavorite:Boolean, id: Int)
    suspend fun changeDoorName(name: String, id: Int)
}