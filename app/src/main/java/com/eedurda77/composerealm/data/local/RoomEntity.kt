package com.eedurda77.composerealm.data.local

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class RoomEntity(
    @PrimaryKey
    var name: String? = null,
) : RealmObject()