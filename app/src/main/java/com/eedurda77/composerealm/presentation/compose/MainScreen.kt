package com.eedurda77.composerealm.presentation.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import com.eedurda77.composerealm.presentation.ui.MainViewModel


@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel()
) {

    Column(modifier = modifier.fillMaxSize()) {
        Text(
            modifier = modifier
                .fillMaxWidth(1f)
                .padding(top = 10.dp),
            textAlign = TextAlign.Center,
            fontFamily = FontFamily(Font(font.circe)),
            fontSize = 21.sp,
            text = stringResource(id = R.string.my_home)
        )
    }
}

