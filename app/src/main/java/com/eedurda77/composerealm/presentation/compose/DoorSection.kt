package com.eedurda77.composerealm.presentation.compose

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
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
    idChangedName: Int,
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
                idChangedName = idChangedName,
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
    onEvent: (MainEvent) -> Unit,
    idChangedName: Int
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
            if (idChangedName == door.id) {
                TextFieldChangeName(
                    nameDoor = door.name,
                    idDoor = door.id,
                    onEvent = onEvent
                )
            } else {
                ButtonShowTextField(
                    idDoor = door.id,
                    onEvent = onEvent
                )
            }
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

@Composable
fun ButtonShowTextField(
    modifier: Modifier = Modifier,
    idDoor: Int?,
    onEvent: (MainEvent) -> Unit
) {
    Image(
        modifier = modifier
            .padding(start = 33.dp)
            .clickable {
                onEvent(
                    MainEvent.ToggleVisibleFieldName(
                        id = idDoor ?: -1
                    )
                )
            },
        imageVector = ImageVector.vectorResource(id = R.drawable.edit),
        contentDescription = ""
    )
}

@Composable
fun TextFieldChangeName(
    modifier: Modifier = Modifier,
    nameDoor: String?,
    idDoor: Int?,
    onEvent: (MainEvent) -> Unit
) {
    val message = remember { mutableStateOf(nameDoor ?: "") }
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 33.dp),
        trailingIcon = {
            Icon(
                modifier = modifier.clickable {
                    onEvent(
                        MainEvent.ToggleVisibleFieldName(
                            id = -1
                        )
                    )
                    onEvent(
                        MainEvent.ChangeDoorName(
                            name = message.value,
                            id = idDoor ?: -1000
                        )
                    )
                },
                imageVector = ImageVector.vectorResource(id = R.drawable.edit),
                contentDescription = ""
            )
        },
        textStyle = TextStyle(
            color = colorResource(id = R.color.text_light_dark),
            fontFamily = FontFamily(Font(R.font.circe)),
            fontSize = 16.sp,
        ),
        value = message.value,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor= colorResource(id = R.color.line_blue),
            unfocusedBorderColor =colorResource(id = R.color.line_blue),
            trailingIconColor = colorResource(id = R.color.line_blue)),
        onValueChange = { newText -> message.value = newText })
}