package me.kekemao.base_compose.modifier

import androidx.compose.foundation.clickable
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

/**
 * 防抖点击事件 Modifier
 *
 * @param debounceTime 防抖时间，默认为300毫秒
 * @param onClick 点击事件处理
 */
fun Modifier.debouncedClickable(
    debounceTime: Long = 300L, // 默认防抖时间为300毫秒
    onClick: () -> Unit
): Modifier = composed {
    var lastClickTime by remember { mutableLongStateOf(0L) }

    this.then(Modifier.clickable {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime >= debounceTime) {
            lastClickTime = currentTime
            onClick()
        }
    })
}

// 使用示例
@Composable
fun MyButton() {
    Button(onClick = { /* 点击事件处理 */ }, modifier = Modifier.debouncedClickable {
        println("Button clicked")
    }) {
        Text("Click Me")
    }
}
