package com.eedurda77.composerealm.data.mapper

import com.eedurda77.composerealm.data.local.CameraEntity
import com.eedurda77.composerealm.data.local.DoorEntity
import com.eedurda77.composerealm.data.local.RoomEntity
import com.eedurda77.composerealm.data.remote.dto.CamerasDto
import com.eedurda77.composerealm.data.remote.dto.DoorsDto
import com.eedurda77.composerealm.domain.models.CameraMain
import com.eedurda77.composerealm.domain.models.DoorMain
import com.eedurda77.composerealm.domain.models.RoomMain

fun CamerasDto.convertToCamerasEntity(): List<CameraEntity> {
    return this.dataCamera.cameras.map { camera ->
        CameraEntity(
            id = camera.id,
            isFavorite = camera.favorites,
            name = camera.name,
            isRecord = camera.rec,
            room = camera.room,
            urlPath = camera.snapshot
        )
    }
}

fun DoorsDto.convertToDoorsEntity(): List<DoorEntity> {
    return this.data.map { door ->
        DoorEntity(
            id = door.id,
            isFavorite = door.favorites,
            name = door.name,
            room = door.room,
            urlPath = door.snapshot
        )
    }
}

fun CamerasDto.convertToRoomEntity(): List<RoomEntity> {
    return this.dataCamera.room.map {
        RoomEntity(
            name = it
        )
    }
}

fun List<CameraEntity>.convertToCameraMain(): List<CameraMain> {
    return this.map { camera ->
        CameraMain(
            id = camera.id,
            isFavorite = camera.isFavorite,
            name = camera.name,
            isRec = camera.isRecord,
            room = camera.room,
            urlPath = camera.urlPath
        )
    }
}


fun List<RoomEntity>.convertToString(): List<String> {
    return this.map { room ->
        room.name?: ""
    }
}
fun List<DoorEntity>.convertToDoorsMain(): List<DoorMain> {
    return this.map { door ->
        DoorMain(
            id = door.id,
            isFavorite = door.isFavorite,
            name = door.name,
            room = door.room,
            urlPath = door.urlPath
        )
    }
}

fun List<RoomEntity>.convertToRoomMain(): List<RoomMain> {
    return this.map { room ->
        RoomMain(
            nameRoom = room.name?: ""
        )
    }
}
