package com.example.dti_project
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import kotlinx.coroutines.delay
import kotlin.math.log10



private var audioRecord: AudioRecord? = null
private var isRecordingAudio = false
@RequiresPermission(Manifest.permission.RECORD_AUDIO)
fun startRecording() {

    val sampleRate = 44100

    val bufferSize = AudioRecord.getMinBufferSize(
        sampleRate,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT
    )

    audioRecord = AudioRecord(
        MediaRecorder.AudioSource.MIC,
        sampleRate,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT,
        bufferSize
    )

    audioRecord?.startRecording()
    isRecordingAudio = true
}
fun getDecibel(): Double {

    val buffer = ShortArray(1024)

    val read = audioRecord?.read(buffer, 0, buffer.size) ?: 0

    if (read <= 0) return 0.0

    var sum = 0.0

    for (i in 0 until read) {
        sum += buffer[i] * buffer[i]
    }

    val rms = Math.sqrt(sum / read)

    return if (rms > 0) {
        20 * log10(rms / 32767.0) + 90
    } else {
        0.0
    }


}
fun stopRecording() {

    isRecordingAudio = false

    audioRecord?.apply {
        stop()
        release()
    }

    audioRecord = null
}
@SuppressLint("MissingPermission")
@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NoiseMeasureScreen(){
    val samples = mutableListOf<Double>()
    val microphonePermission=rememberPermissionState(Manifest.permission.RECORD_AUDIO)
    var dbLevel by remember { mutableStateOf(0.0) }
    var isRecording by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text("Noise Level: ${dbLevel.toInt()} dB")
        Button(
            onClick = {
                if(!microphonePermission.status.isGranted){
                    microphonePermission.launchPermissionRequest()
                }
                else{
                    startRecording()
                    isRecording = true

                }

            })
        {
            Text("Start Measuring")
        }
        Button(
            onClick = {
                stopRecording()
                isRecording = false
            }
        ) {
            Text("Stop")
        }



    }

    LaunchedEffect(isRecording) {

        if (isRecording) {

            samples.clear()

            repeat(50) {     // 50 samples ≈ 5 seconds if delay = 100ms
                val db = getDecibel()
                samples.add(db)
                delay(100)
            }

            stopRecording()
            isRecording = false

            val highSamples = samples.filter { it > 50 }   // consider loud sounds
            dbLevel = if (highSamples.isNotEmpty()) {
                highSamples.average()
            } else {
                samples.average()
            }
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            stopRecording()
        }
    }
}

@Composable
fun loadingScreen(){

}

@RequiresApi(Build.VERSION_CODES.S)
@Preview(showBackground = true)
@Composable
fun NoiseMeasureScreenPreview(){
    NoiseMeasureScreen()
}