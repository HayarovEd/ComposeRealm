package com.eedurda77.composerealm.presentation.compose

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.runtime.Composable
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
import com.eedurda77.composerealm.domain.models.DoorMain
import com.eedurda77.composerealm.presentation.ui.MainEvent

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DoorSection(
    modifier: Modifier = Modifier,
    doors: List<DoorMain>,
    onEvent: (MainEvent) -> Unit,
    swipeRefreshState: PullRefreshState
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.base_background))
            .padding(horizontal = 21.dp, vertical = 15.dp)
            .pullRefresh(state = swipeRefreshState)
    ) {
        items(doors) { door ->
            ItemDoor(
                door = door,
                onEvent = onEvent
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ItemDoor(
    modifier: Modifier = Modifier,
    door: DoorMain,
    onEvent: (MainEvent) -> Unit
) {
    Column(modifier = modifier
        .padding(top = 11.dp)
        .fillMaxWidth()
        .background(colorResource(id = R.color.white))
        .padding(11.dp)) {
        Row(
            modifier = modifier
                .padding(vertical = 21.dp)
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
        ) {
            Row(
                modifier = modifier.width(333.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = modifier.padding(start = 16.dp),
                    color = colorResource(id = R.color.text_light_dark),
                    fontFamily = FontFamily(Font(R.font.circe)),
                    fontSize = 17.sp,
                    text = door.name ?: ""
                )
                Image(
                    modifier = modifier
                        .padding(end = 21.dp),
                    imageVector = ImageVector.vectorResource(id = R.drawable.lockon),
                    contentDescription = ""
                )
            }
            Image(
                modifier = modifier
                    .padding(start = 33.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable.edit),
                contentDescription = ""
            )
            Image(
                modifier = modifier
                    .padding(start = 9.dp, end = 21.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable.door_star),
                contentDescription = ""
            )
        }
        if (door.urlPath != null) {
            Box(
                modifier = modifier
                    .padding(top = 8.dp)
                    .width(360.dp)
                    .height(207.dp)
                    .background(colorResource(id = R.color.white))
                    .clip(shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
            ) {
                GlideImage(
                    modifier = modifier
                        .fillMaxWidth(),
                    contentScale = ContentScale.FillWidth,
                    model = door.urlPath,
                    contentDescription = ""
                )
            }
        }
    }
}
