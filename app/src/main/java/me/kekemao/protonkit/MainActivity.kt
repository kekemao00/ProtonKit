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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import me.kekemao.base_compose.modifier.debouncedClickable
import me.kekemao.protonkit.ui.theme.ProtonKitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProtonKitTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
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
    Column {

        Text(
            text = "Hello $name!",
            modifier = modifier
        )

        Button(
            onClick = { ++num.intValue },
            Modifier.debouncedClickable {
                ++num1.intValue
                Toast.makeText(
                    context,
                    "Button clicked",
                    Toast.LENGTH_SHORT
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