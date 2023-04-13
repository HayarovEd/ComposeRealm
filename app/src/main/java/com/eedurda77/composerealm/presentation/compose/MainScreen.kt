package com.eedurda77.composerealm.presentation.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
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
    Box(
        modifier = modifier
            .pullRefresh(state = swipeRefreshState)
    ) {
        Column(modifier = modifier
            .fillMaxSize()
        ) {
            Text(
                modifier = modifier
                    .fillMaxWidth(1f)
                    .padding(top = 10.dp),
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(font.circe)),
                fontSize = 21.sp,
                text = stringResource(id = R.string.my_home)
            )
            Row(modifier = modifier) {
                
            }
        }
        PullRefreshIndicator(refreshing = refreshing, state = swipeRefreshState)
    }
}

