package com.eedurda77.composerealm.data.remote

import com.eedurda77.composerealm.data.remote.dto.CamerasDto
import com.eedurda77.composerealm.data.remote.dto.DoorsDto
import retrofit2.http.GET

interface CarsApi {
    @GET("cameras/")
    suspend fun getCameras (
    ) : CamerasDto

    @GET("doors/")
    suspend fun getDoors (
    ) : DoorsDto
}