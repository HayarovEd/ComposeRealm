package com.eedurda77.composerealm.data.repository

import com.eedurda77.composerealm.data.local.CameraEntity
import com.eedurda77.composerealm.data.local.DoorEntity
import com.eedurda77.composerealm.data.local.RoomEntity
import com.eedurda77.composerealm.data.mapper.convertToCameraMain
import com.eedurda77.composerealm.data.mapper.convertToCamerasEntity
import com.eedurda77.composerealm.data.mapper.convertToDoorsEntity
import com.eedurda77.composerealm.data.mapper.convertToDoorsMain
import com.eedurda77.composerealm.data.mapper.convertToRoomEntity
import com.eedurda77.composerealm.data.mapper.convertToString
import com.eedurda77.composerealm.data.remote.CarsApi
import com.eedurda77.composerealm.domain.models.CameraMain
import com.eedurda77.composerealm.domain.models.DoorMain
import com.eedurda77.composerealm.domain.models.RoomWithCameras
import com.eedurda77.composerealm.domain.repository.Repo
import com.eedurda77.composerealm.utils.Resource
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.executeTransactionAwait
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class RepositoryImpl @Inject constructor(
    private val api: CarsApi,
    private val config: RealmConfiguration
) : Repo {
    private val realm = Realm.getInstance(config)

    override suspend fun getCameras(isRefresh: Boolean): Flow<Resource<List<RoomWithCameras>>> {
        return flow {
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
            emit(
                Resource.Success(
                    data = convertToCameraMainByRoom(
                        cameras = localCameraListings,
                        rooms = localRoomListings
                    )
                )
            )
            val isDbEmpty = localCameraListings.isEmpty()||localRoomListings.isEmpty()
            val shouldJustLoadFromCache = !isDbEmpty && !isRefresh
            if (shouldJustLoadFromCache) {
                return@flow
            }
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

    /* override suspend fun getRooms(isRefresh: Boolean): Flow<Resource<List<RoomMain>>> {
         return flow {
             val localListings = mutableListOf<RoomMain>()
             realm.executeTransactionAwait(Dispatchers.IO) { realmTransaction ->
                 localListings.addAll(
                     realmTransaction
                         .where(RoomEntity::class.java)
                         .findAll()
                         .convertToRoomMain()
                 )
             }
             emit(
                 Resource.Success(
                     data = localListings
                 )
             )
             val isDbEmpty = localListings.isEmpty()
             val shouldJustLoadFromCache = !isDbEmpty && !isRefresh
             if (shouldJustLoadFromCache) {
                 return@flow
             }
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
                     realmTransaction.deleteAll()
                 }
                 realm.executeTransactionAwait(Dispatchers.IO) { realmTransaction ->
                     realmTransaction.insert(listings.convertToRoomEntity())
                 }
                 localListings.clear()
                 realm.executeTransactionAwait(Dispatchers.IO) { realmTransaction ->
                     localListings.addAll(
                         realmTransaction
                             .where(RoomEntity::class.java)
                             .findAll()
                             .convertToRoomMain()
                     )
                 }
                 emit(Resource.Success(data = localListings))
             }
         }
     }*/

    override suspend fun getDoors(isRefresh: Boolean): Flow<Resource<List<DoorMain>>> {
        return flow {
            val localListings = mutableListOf<DoorMain>()
            realm.executeTransactionAwait(Dispatchers.IO) { realmTransaction ->
                localListings.addAll(
                    realmTransaction
                        .where(DoorEntity::class.java)
                        .findAll()
                        .convertToDoorsMain()
                )
            }
            emit(
                Resource.Success(
                    data = localListings
                )
            )
            val isDbEmpty = localListings.isEmpty()
            val shouldJustLoadFromCache = !isDbEmpty && !isRefresh
            if (shouldJustLoadFromCache) {
                return@flow
            }
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
                emit(Resource.Success(data = localListings))
            }
        }
    }

    override suspend fun changeCameraFavorite(isFavorite: Boolean, id: Int) {

        realm.executeTransactionAwait(Dispatchers.IO) { realmTransaction ->
            val cameraOne = realmTransaction
                .where(CameraEntity::class.java)
                .equalTo("id", id)
                .findFirst()

            val newCamera = CameraEntity(
                id = cameraOne?.id,
                isFavorite = isFavorite,
                name = cameraOne?.name,
                isRecord = cameraOne?.isRecord,
                room = cameraOne?.room,
                urlPath = cameraOne?.urlPath
            )


            realmTransaction.copyToRealmOrUpdate(
                newCamera
            )
            //camera?.let { realmTransaction.copyToRealmOrUpdate(it) }
        }
    }

    override suspend fun changeDoorName(name: String, id: Int) {
        val realm = Realm.getInstance(config)
        realm.executeTransactionAwait(Dispatchers.IO) { realmTransaction ->
            val door = realmTransaction
                .where(DoorEntity::class.java)
                .equalTo("id", id)
                .findFirst()
            door?.name = name
            if (door != null) {
                realmTransaction.copyToRealmOrUpdate(door)
            }
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

