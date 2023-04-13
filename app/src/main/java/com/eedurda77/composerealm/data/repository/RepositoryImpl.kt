package com.eedurda77.composerealm.data.repository

import com.eedurda77.composerealm.data.local.CameraEntity
import com.eedurda77.composerealm.data.local.DoorEntity
import com.eedurda77.composerealm.data.local.RoomEntity
import com.eedurda77.composerealm.data.mapper.*
import com.eedurda77.composerealm.data.remote.CarsApi
import com.eedurda77.composerealm.domain.models.CameraMain
import com.eedurda77.composerealm.domain.models.DoorMain
import com.eedurda77.composerealm.domain.models.RoomMain
import com.eedurda77.composerealm.domain.repository.Repo
import com.eedurda77.composerealm.utils.Resource
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.executeTransactionAwait
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val api: CarsApi,
    private val config: RealmConfiguration
) : Repo {
    //private

    override suspend fun getCameras(isRefresh: Boolean): Flow<Resource<List<CameraMain>>> {
        val realm = Realm.getInstance(config)
        return flow {
            val localListings = mutableListOf<CameraMain>()
            realm.executeTransactionAwait(Dispatchers.IO) { realmTransaction ->
                localListings.addAll(
                    realmTransaction
                        .where(CameraEntity::class.java)
                        .findAll()
                        .convertToCameraMain()
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
                    realmTransaction.insert(listings.convertToCamerasEntity())
                }
                localListings.clear()
                realm.executeTransactionAwait(Dispatchers.IO) { realmTransaction ->
                    localListings.addAll(
                        realmTransaction
                            .where(CameraEntity::class.java)
                            .findAll()
                            .convertToCameraMain()
                    )
                }
                emit(Resource.Success(data = localListings))
            }
        }
    }

    override suspend fun getRooms(isRefresh: Boolean): Flow<Resource<List<RoomMain>>> {
        val realm = Realm.getInstance(config)
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
    }

    override suspend fun getDoors(isRefresh: Boolean): Flow<Resource<List<DoorMain>>> {
        val realm = Realm.getInstance(config)
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

    override suspend fun changeCameraName(name: String, id: Int) {
        val realm = Realm.getInstance(config)
        realm.executeTransactionAwait(Dispatchers.IO) { realmTransaction ->
            val camera = realmTransaction
                .where(CameraEntity::class.java)
                .equalTo("id", id)
                .findFirst()
            camera?.name = name
            if (camera != null) {
                realmTransaction.copyToRealmOrUpdate(camera)
            }
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
}

