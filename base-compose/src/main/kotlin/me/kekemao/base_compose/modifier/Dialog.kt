package me.kekemao.base_compose.modifier

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp


/**
 * 权限申请说明
 *
 * @param permissionTips 权限申请说明
 *
 * ```Kotlin
 *  if (!permissionState.status.isGranted) PermissionTips(R.string.string_permission_tips_record_audio)
 *  ContentScreen()
 * ```
 */
@Composable
fun PermissionTips(@StringRes permissionTips: Int) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black.copy(0.5f))
            .padding(12.dp),

        contentAlignment = Alignment.TopEnd
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface,
        ) {
            Text(
                text = stringResource(id = permissionTips),
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.Center),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
