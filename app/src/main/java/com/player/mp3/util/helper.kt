package com.player.mp3.util

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.player.mp3.domain.model.AudioMetadata

fun setupPermissions(
    context: Context,
    permissions: Array<String>,
    launcher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>,
    onPermissionGranted: (() -> Unit)? = null
){
    if(permissions.all{ permission ->
            ContextCompat.checkSelfPermission(
                context,
                permission
        ) == PackageManager.PERMISSION_GRANTED
        })
    {
        onPermissionGranted?.invoke()
    }
    else
    {
        launcher.launch(permissions)
    }
}

fun showPermissionsRationalDialog(
    context: Context,
    dialogText: String,
    errorText: String,
    @StringRes okButtonTextResId: Int,
    @StringRes cancelButtonTextResId: Int,
    packageName: String
){
    AlertDialog.Builder(context).setMessage(dialogText).setPositiveButton(okButtonTextResId){
        _,_ ->
        try {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            context.startActivity(intent)
        }
        catch (exp: Exception) {
            AlertDialog.Builder(context).setMessage(errorText)
        }
    }.setNegativeButton(cancelButtonTextResId){
        dialog,_ -> dialog.dismiss()
    }.show()
}

fun AudioMetadata.isNotEmpty(): Boolean{
    return this != AudioMetadata.emptyMetadata()
}

@Composable
fun screenHeight(): Dp {
    return LocalContext.current.resources.displayMetrics.heightPixels.dp / LocalDensity.current.density
}

fun millisecondsToTimeString(milliseconds: Int): String{
    val timeString: String
    val secondsString: String
    var hoursString = ""
    val hours = (milliseconds / (1000 * 60 * 60))
    val minutes = (milliseconds % (1000 * 60 * 60)) / (1000 * 60)
    val seconds = (milliseconds % (1000 * 60 * 60) % (1000 * 60) / 1000)

    if(hours >0){
        hoursString = "$hours:"
    }
    secondsString = if(seconds < 10){
        "0$seconds"
    } else{
        "$seconds"
    }
    timeString = "$hoursString$minutes:$secondsString"
    return timeString

}


