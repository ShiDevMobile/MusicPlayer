package com.player.mp3.ui.activity

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.player.mp3.R
import com.player.mp3.ui.component.LoadingDialog
import com.player.mp3.ui.component.NavigationBar
import com.player.mp3.ui.component.SheetContent
import com.player.mp3.ui.screen.HomeScreen
import com.player.mp3.ui.theme.MP3PlayerTheme
import com.player.mp3.util.screenHeight
import com.player.mp3.util.showPermissionsRationalDialog
import dagger.hilt.android.AndroidEntryPoint


@OptIn(ExperimentalMaterialApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("SourceLockedOrientationActivity")
    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val mainViewModel by viewModels<MainViewModel>()

        installSplashScreen().apply{
            setKeepOnScreenCondition{
                mainViewModel.state.isLoading
            }
        }
        setContent {
            val sheetState = rememberModalBottomSheetState(
                initialValue =  ModalBottomSheetValue.Hidden,
                skipHalfExpanded = true,
            )


            val scope = rememberCoroutineScope()

            val context = LocalContext.current

            val state = mainViewModel.state

            val density = LocalDensity.current

            WindowCompat.setDecorFitsSystemWindows(window, false)
            val windowInsets = WindowInsetsCompat.toWindowInsetsCompat(window.decorView.rootWindowInsets)
            val statusBarHeight = with(density) { windowInsets.systemWindowInsetTop.toDp() }
            val navigationBarHeight = with(density) { windowInsets.systemWindowInsetBottom.toDp() }


            val dialogText = stringResource(R.string.txt_permissions)

            val errorText= stringResource(R.string.txt_error_app_settings)

            val screenHeight = screenHeight()

            val launcher =  rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions(),
                onResult = {
                    permissions ->
                    val permissionsGranted =  permissions.values.reduce { acc, next -> acc && next }
                    if(!permissionsGranted){
                        showPermissionsRationalDialog(
                            context = context,
                            okButtonTextResId = R.string.lbl_ok,
                            cancelButtonTextResId = R.string.lbl_cancel,
                            dialogText = dialogText,
                            errorText = errorText,
                            packageName = packageName
                        )
                    }
                }
            )

            MP3PlayerTheme {
                ModalBottomSheetLayout(
                    sheetContent =
                    {
                        SheetContent(
                            state = state,
                            scope = scope,
                            mainViewModel = mainViewModel,
                            sheetState = sheetState,
                            context = context
                        )
                    },
                    sheetState = sheetState,
                    content = {
                        LoadingDialog(
                            isLoading = state.isLoading,
                            modifier = Modifier
                                .clip(shape = MaterialTheme.shapes.large)
                                .background(MaterialTheme.colors.surface)
                                .requiredSize(size = 80.dp),
                            onDone = {
                                mainViewModel.onEvent(event = AudioPlayerEvent.HideLoadingDialog)
                            }
                        )
                       HomeScreen(
                            statusBarHeight = statusBarHeight,
                            navigationBarHeight = navigationBarHeight,
                            state = state,
                            mainViewModel = mainViewModel,
                            context = context,
                            launcher = launcher,
                            scope = scope,
                            sheetState = sheetState,
                            screenHeight = screenHeight
                        )

                    }
                )
            }
        }
    }




}

