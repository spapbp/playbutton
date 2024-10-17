package com.example.playbutton

import ApiResponse
import RetrofitClient
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.playbutton.ui.theme.PlaybuttonTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PlaybuttonTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    val context = LocalContext.current
    var mediaPlayer: MediaPlayer? = remember { MediaPlayer.create(context, R.raw.treehouse) }
    var playing by remember { mutableStateOf(false) }  // State for button text
    var message by remember { mutableStateOf("Click the button to fetch data") }

    // Ensure MediaPlayer is released when the composable is disposed
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
        }
    }

    // Use a Column to arrange elements vertically
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),  // Add some padding
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))  // Space between text and button
        Button(
            onClick = {
                if (playing) {
                    mediaPlayer?.pause()
                }
                else {
                    mediaPlayer?.start()
                }
                playing = !playing
            }
        )
        {
            Text(text=if(playing) "Pause" else "Play")
        }
        Button(
            onClick = {
                fetchDataFromApi { responseMessage ->
                    message=responseMessage
                    Toast.makeText(context, responseMessage, Toast.LENGTH_LONG).show()
                }
            }
        )
        {
            Text(text="Make backend request")
        }
    }

}

// Function to call Flask API using Retrofit
private fun fetchDataFromApi(onResult: (String) -> Unit) {
    val call = RetrofitClient.apiService.getData()

    call.enqueue(object : Callback<ApiResponse> {
        override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
            if (response.isSuccessful) {
                val apiResponse = response.body()
                onResult(apiResponse?.message ?: "No message received")
            } else {
                onResult("Failed to retrieve data: ${response.message()}")
            }
        }

        override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
            onResult("Error: ${t.message}")
        }
    })
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PlaybuttonTheme {
        MainScreen()  // Preview the MainScreen composable
    }
}
