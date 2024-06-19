package me.kekemao.protonkit

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.runBlocking
import me.kekemao.base_compose.modifier.debouncedClickable
import me.kekemao.keyvaluestore.DataStoreManager
import me.kekemao.keyvaluestore.DataStoreManager.Companion.SP_KEY_MESSAGE
import me.kekemao.protonkit.ui.theme.ProtonKitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProtonKitTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android", modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val num1 = remember { mutableIntStateOf(0) }
    val num = remember { mutableIntStateOf(0) }

    var dataStore: DataStoreManager? = null
    val msg = remember { mutableStateOf("NULL") }

    LaunchedEffect(Unit) {
        dataStore = DataStoreManager(context)
        dataStore?.save(SP_KEY_MESSAGE, "Hello, DataStoreManager${System.currentTimeMillis()}")
        msg.value = runBlocking { dataStore?.retrieve(SP_KEY_MESSAGE, "NULL")?:"NULL" }
    }

    Column {

        Text(
            text = "${msg.value}!", modifier = modifier
        )

        Button(onClick = { ++num.intValue }, Modifier.debouncedClickable {
            ++num1.intValue
            Toast.makeText(
                context, "Button clicked", Toast.LENGTH_SHORT
            ).show()
        }) {
            Text("Click Me ${num1.intValue}, ${num.intValue}")
        }

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProtonKitTheme {
        Greeting("Android")
    }
}