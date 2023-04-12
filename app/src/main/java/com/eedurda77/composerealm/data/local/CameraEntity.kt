package com.eedurda77.composerealm.data.local

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class CameraEntity(
    @PrimaryKey
    var id: Int,
    @Required
    var isFavorite: Boolean,
    @Required
    var name: String,
    @Required
    var isRecord: Boolean,
    @Required
    var room: String? = null,
    @Required
    var urlPath: String
) : RealmObject()
