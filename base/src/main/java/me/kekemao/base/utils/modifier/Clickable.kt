package me.kekemao.base.utils.modifier

import androidx.compose.foundation.clickable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun Modifier.debouncedClickable(
    debounceTime: Long = 300L, // 默认防抖时间为300毫秒
    onClick: () -> Unit
): Modifier = composed {
    var lastClickTime by remember { mutableStateOf(0L) }
    val coroutineScope = rememberCoroutineScope()

    this.then(
        Modifier.clickable {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastClickTime >= debounceTime) {
                lastClickTime = currentTime
                coroutineScope.launch {
                    delay(debounceTime)
                    onClick()
                }
            }
        }
    )
}

// 使用示例
@Composable
fun MyButton() {
    Button(
        onClick = { /* 点击事件处理 */ },
        modifier = Modifier.debouncedClickable {
            println("Button clicked")
        }
    ) {
        Text("Click Me")
    }
}
