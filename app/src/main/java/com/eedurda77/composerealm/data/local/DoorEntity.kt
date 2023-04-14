package com.eedurda77.composerealm.data.local

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class DoorEntity (
    @PrimaryKey
    var id: Int? = null,

    var isFavorite: Boolean? = null,

    var name: String? = null,

    var room: String? = null,

    var urlPath: String? = null
) : RealmObject()