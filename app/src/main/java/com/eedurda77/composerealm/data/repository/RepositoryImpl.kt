package com.eedurda77.composerealm.data.repository

import com.eedurda77.composerealm.data.local.CameraEntity
import com.eedurda77.composerealm.data.local.DoorEntity
import com.eedurda77.composerealm.data.local.RoomEntity
import com.eedurda77.composerealm.data.mapper.*
import com.eedurda77.composerealm.data.remote.CarsApi
import com.eedurda77.composerealm.domain.models.CameraMain
import com.eedurda77.composerealm.domain.models.DoorMain
import com.eedurda77.composerealm.domain.models.RoomWithCameras
import com.eedurda77.composerealm.domain.repository.Repo
import com.eedurda77.composerealm.utils.Resource
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.executeTransactionAwait
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val api: CarsApi
) : Repo {

    override suspend fun getCameras(isRefresh: Boolean): Flow<Resource<List<RoomWithCameras>>> {
        return flow {
            val realm = Realm.getDefaultInstance()
            val localCameraListings = mutableListOf<CameraMain>()
            val localRoomListings = mutableListOf<String>()
            realm.executeTransactionAwait(Dispatchers.IO) { realmTransaction ->
                localCameraListings.addAll(
                    realmTransaction
                        .where(CameraEntity::class.java)
                        .findAll()
                        .convertToCameraMain()
                )
            }
            realm.executeTransactionAwait(Dispatchers.IO) { realmTransaction ->
                localRoomListings.addAll(
                    realmTransaction
                        .where(RoomEntity::class.java)
                        .findAll()
                        .convertToString()
                )
            }

            val isDbEmpty = localCameraListings.isEmpty()||localRoomListings.isEmpty()
            if (isDbEmpty||isRefresh) {
                val remoteListings = try {
                    api.getCameras()
                } catch (e: IOException) {
                    e.printStackTrace()
                    emit(Resource.Error(message = e.message.toString()))
                    null
                } catch (e: HttpException) {
                    e.printStackTrace()
                    emit(Resource.Error(message = e.message.toString()))
                    null
                }
                remoteListings?.let { listings ->

                    realm.executeTransactionAwait(Dispatchers.IO) { realmTransaction ->
                        val cameras = realmTransaction
                            .where(CameraEntity::class.java)
                            .findAll()
                        cameras.deleteAllFromRealm()
                    }
                    realm.executeTransactionAwait(Dispatchers.IO) { realmTransaction ->
                        val rooms = realmTransaction
                            .where(RoomEntity::class.java)
                            .findAll()
                        rooms.deleteAllFromRealm()
                    }

                    realm.executeTransactionAwait(Dispatchers.IO) { realmTransaction ->
                        realmTransaction.insert(listings.convertToCamerasEntity())
                    }
                    realm.executeTransactionAwait(Dispatchers.IO) { realmTransaction ->
                        realmTransaction.insert(listings.convertToRoomEntity())
                    }
                    localCameraListings.clear()
                    localRoomListings.clear()
                    realm.executeTransactionAwait(Dispatchers.IO) { realmTransaction ->
                        localCameraListings.addAll(
                            realmTransaction
                                .where(CameraEntity::class.java)
                                .findAll()
                                .convertToCameraMain()
                        )
                    }
                    realm.executeTransactionAwait(Dispatchers.IO) { realmTransaction ->
                        localRoomListings.addAll(
                            realmTransaction
                                .where(RoomEntity::class.java)
                                .findAll()
                                .convertToString()
                        )
                    }
                    realm.close()
                    emit(
                        Resource.Success(
                            data = convertToCameraMainByRoom(
                                cameras = localCameraListings,
                                rooms = localRoomListings
                            )
                        )
                    )
                }
            } else {
                emit(
                    Resource.Success(
                        data = convertToCameraMainByRoom(
                            cameras = localCameraListings,
                            rooms = localRoomListings
                        )
                    )
                )
            }
        }
    }


    override suspend fun getDoors(isRefresh: Boolean): Flow<Resource<List<DoorMain>>> {
        return flow {
            val realm = Realm.getDefaultInstance()
            val localListings = mutableListOf<DoorMain>()
            realm.executeTransactionAwait(Dispatchers.IO) { realmTransaction ->
                localListings.addAll(
                    realmTransaction
                        .where(DoorEntity::class.java)
                        .findAll()
                        .convertToDoorsMain()
                )
            }
            val isDbEmpty = localListings.isEmpty()
            if (isDbEmpty||isRefresh) {
                val remoteListings = try {
                    api.getDoors()
                } catch (e: IOException) {
                    e.printStackTrace()
                    emit(Resource.Error(message = e.message.toString()))
                    null
                } catch (e: HttpException) {
                    e.printStackTrace()
                    emit(Resource.Error(message = e.message.toString()))
                    null
                }
                remoteListings?.let { listings ->
                    realm.executeTransactionAwait(Dispatchers.IO) { realmTransaction ->
                        realmTransaction.deleteAll()
                    }
                    realm.executeTransactionAwait(Dispatchers.IO) { realmTransaction ->
                        realmTransaction.insert(listings.convertToDoorsEntity())
                    }
                    localListings.clear()
                    realm.executeTransactionAwait(Dispatchers.IO) { realmTransaction ->
                        localListings.addAll(
                            realmTransaction
                                .where(DoorEntity::class.java)
                                .findAll()
                                .convertToDoorsMain()
                        )
                    }
                    realm.close()
                    emit(Resource.Success(data = localListings))
                }
            } else {
                emit(
                    Resource.Success(
                        data = localListings
                    )
                )
            }
        }
    }

    override suspend fun changeCameraFavorite(isUpdateFavorite: Boolean, id: Int) {
        withContext(Dispatchers.IO) {
            val realm = Realm.getDefaultInstance()
            realm.executeTransaction { realmD ->
                val realmItem = realmD.where(CameraEntity::class.java).equalTo("id", id).findFirst()
                realmItem?.apply {
                    this.isFavorite = isUpdateFavorite
                }
            }
            realm.close()
        }
    }

    override suspend fun changeDoorName(name: String, id: Int) {
        withContext(Dispatchers.IO) {
            val realm = Realm.getDefaultInstance()
            realm.executeTransaction { realmD ->
                val realmItem = realmD.where(DoorEntity::class.java).equalTo("id", id).findFirst()
                realmItem?.apply {
                    this.name = name
                }
            }
            realm.close()
        }
    }

    private fun convertToCameraMainByRoom(
        cameras: List<CameraMain>,
        rooms: List<String>
    ): List<RoomWithCameras> {
        val roomsByCameras = mutableListOf<RoomWithCameras>()
        rooms.forEach { room ->
            val camerasByRoom = cameras.filter { it.room == room }
            roomsByCameras.add(
                RoomWithCameras(
                    nameRoom = room,
                    cameras = camerasByRoom
                )
            )
        }
        return roomsByCameras
    }

}

