package com.eedurda77.composerealm.presentation.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eedurda77.composerealm.R
import com.eedurda77.composerealm.R.font
import com.eedurda77.composerealm.presentation.ui.MainEvent
import com.eedurda77.composerealm.presentation.ui.MainViewModel
import com.eedurda77.composerealm.presentation.ui.Status


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState()
    val refreshing =  state.value.isLoading
    val swipeRefreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = { viewModel.onEvent(MainEvent.Refresh) }
    )
    val status =  state.value.status
    Box(
        modifier = modifier
    ) {
        Column(modifier = modifier
            .fillMaxSize()
        ) {
            Text(
                modifier = modifier
                    .fillMaxWidth(1f)
                    .padding(top = 10.dp),
                textAlign = TextAlign.Center,
                color = colorResource(id = R.color.text_dark),
                fontFamily = FontFamily(Font(font.circe)),
                fontSize = 21.sp,
                text = stringResource(id = R.string.my_home)
            )
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 34.dp)
            ) {
                Column(
                    modifier = modifier
                        .height(50.dp)
                        .weight(1f)
                        .clickable {
                            viewModel.onEvent(MainEvent.ToggleStatus(status = Status.CAMERA))
                        }
                ) {
                    Text(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(top = 13.dp)
                            .weight(1f),
                        textAlign = TextAlign.Center,
                        color = colorResource(id = R.color.text_dark),
                        fontFamily = FontFamily(Font(font.circe)),
                        fontSize = 17.sp,
                        text = stringResource(id = R.string.cameras)
                    )
                    Divider(modifier = modifier
                        .padding(top = 13.dp)
                        .height(2.dp),
                    color = when (status) {
                        Status.CAMERA -> colorResource(id = R.color.line_blue)
                        Status.DOOR -> colorResource(id = R.color.white)
                    })
                }
                Column(
                    modifier = modifier
                        .height(50.dp)
                        .weight(1f)
                        .clickable {
                            viewModel.onEvent(MainEvent.ToggleStatus(status = Status.DOOR))
                        }
                ) {
                    Text(
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(top = 13.dp)
                            .weight(1f),
                        textAlign = TextAlign.Center,
                        color = colorResource(id = R.color.text_dark),
                        fontFamily = FontFamily(Font(font.circe)),
                        fontSize = 17.sp,
                        text = stringResource(id = R.string.doors)
                    )
                    Divider(modifier = modifier
                        .padding(top = 13.dp)
                        .height(2.dp),
                        color = when (status) {
                            Status.CAMERA -> colorResource(id = R.color.white)
                            Status.DOOR -> colorResource(id = R.color.line_blue)
                        })
                }
            }
            when (status) {
                Status.CAMERA -> {
                    CamerasSection(
                        rooms = state.value.roomswithCamers,
                        onEvent = viewModel::onEvent,
                        swipeRefreshState = swipeRefreshState
                    )
                }
                Status.DOOR -> {

                }
            }
        }
        PullRefreshIndicator(
            modifier = modifier.align(Alignment.TopCenter),
            refreshing = refreshing,
            state = swipeRefreshState
        )
    }
}


