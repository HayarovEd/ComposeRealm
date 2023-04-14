package com.eedurda77.composerealm.presentation.compose

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.eedurda77.composerealm.R
import com.eedurda77.composerealm.domain.models.CameraMain
import com.eedurda77.composerealm.domain.models.RoomWithCameras
import com.eedurda77.composerealm.presentation.ui.MainEvent

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CamerasSection(
    modifier: Modifier = Modifier,
    rooms: List<RoomWithCameras>,
    onEvent: (MainEvent) -> Unit,
    swipeRefreshState: PullRefreshState
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .pullRefresh(state = swipeRefreshState)
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
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .height(560.dp)
                .padding(top = 11.dp)
        ) {
            items(room.cameras) { camera ->
                ItemCamera(
                    camera = camera,
                    onEvent = onEvent
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ItemCamera(
    modifier: Modifier = Modifier,
    camera: CameraMain,
    onEvent: (MainEvent) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            Box(
                modifier = modifier
                    .width(360.dp)
                    .height(207.dp)
                    .clip(shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
            ) {
                GlideImage(
                    modifier = modifier.fillMaxSize(),
                    contentScale = ContentScale.FillWidth,
                    model = camera.urlPath,
                    contentDescription = ""
                )
                if (camera.isRec == true) {
                    Image(
                        modifier = modifier
                            .align(Alignment.TopStart)
                            .padding(top = 8.dp, start = 8.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_rec),
                        contentDescription = ""
                    )
                }
                if (camera.isFavorite == true) {
                    Image(
                        modifier = modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 8.dp, end = 8.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_star),
                        contentDescription = ""
                    )
                }
                Image(
                    modifier = modifier.align(Alignment.Center),
                    imageVector = ImageVector.vectorResource(id = R.drawable.play_button),
                    contentDescription = ""
                )
            }
            Image(
                modifier = modifier
                    .clickable {
                        onEvent(
                            MainEvent.ChangeCameraStatusFavorite(
                                changeStatusFavorite(
                                    currentStatus = camera.isFavorite ?: true
                                ),
                                id = camera.id?: 100
                            )
                        )
                    }
                    .padding(start = 9.dp)
                    .clip(shape = CircleShape)
                    .border(width = 1.dp, color = colorResource(id = R.color.grey))
                    .align(alignment = CenterVertically)
                    .padding(8.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_unchecked_star),
                contentDescription = ""
            )
        }
        Text(
            modifier = modifier.padding(top = 22.dp, bottom = 20.dp, start = 16.dp),
            color = colorResource(id = R.color.text_light_dark),
            fontFamily = FontFamily(Font(R.font.circe)),
            fontSize = 17.sp,
            text = camera.name ?: ""
        )
    }
}

private fun changeStatusFavorite(currentStatus: Boolean) = !currentStatus
