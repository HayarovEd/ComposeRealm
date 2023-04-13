package com.eedurda77.composerealm.presentation.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eedurda77.composerealm.R
import com.eedurda77.composerealm.domain.models.RoomWithCameras
import com.eedurda77.composerealm.presentation.ui.MainEvent

@Composable
fun CamerasSection(
    modifier: Modifier = Modifier,
    rooms: List<RoomWithCameras>,
    onEvent: (MainEvent) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
    ) {
        items(rooms) { room ->
            ItemRoom(
                room = room,
                onEvent = onEvent
            )
        }
    }
}


@Composable
fun ItemRoom(
    modifier: Modifier = Modifier,
    room: RoomWithCameras,
    onEvent: (MainEvent) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 21.dp, end = 21.dp, top = 11.dp)
    ) {
        Text(
            modifier = modifier.padding(top = 16.dp),
            color = colorResource(id = R.color.text_dark),
            fontFamily = FontFamily(Font(R.font.circe_light)),
            fontSize = 21.sp,
            text = room.nameRoom
        )
    }
}