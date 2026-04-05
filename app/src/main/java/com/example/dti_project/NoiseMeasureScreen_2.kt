package com.example.dti_project



import android.Manifest
import android.graphics.Paint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.math.cos
import kotlin.math.sin

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NoiseDashboardScreen() {

    val micPermission = rememberPermissionState(Manifest.permission.RECORD_AUDIO)

    var dbLevel by remember { mutableStateOf(0f) }
    var isRecording by remember { mutableStateOf(false) }

    val weeklyData = listOf(40, 55, 30, 25, 60, 35, 50)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom){
            Text("Sound Levels", color = Color.White, fontSize= 24.sp
            )
                Spacer(Modifier.weight(1f))
                Text("Decibels", color = colorResource(R.color.primaryButton),fontSize= 20.sp)
            }
            Spacer(Modifier.height(20.dp))

            // 🔥 CIRCULAR DB METER
            CircularDbMeter(dbLevel)

            Spacer(Modifier.height(32.dp))

            // 📊 BAR CHART
            NoiseBarChart(Modifier.fillMaxWidth())

            Spacer(Modifier.weight(1f))

            // 🔻 BOTTOM NAV
            BottomBar()
        }

        // 🎤 START RECORDING BUTTON (floating)
        FloatingActionButton(
            onClick = {
                if (!micPermission.status.isGranted) {
                    micPermission.launchPermissionRequest()
                } else {
                    isRecording = !isRecording
                    if (isRecording) startRecording() else stopRecording()
                }
            },
            containerColor = Color(0xFF7B61FF),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-80).dp) // 👈 moves it ABOVE bottom bar
        ) {
            Text(if (isRecording) "Stop" else "Start")
        }
    }

    // 🎧 REAL-TIME AUDIO LOOP
    LaunchedEffect(isRecording) {
        while (isRecording) {
            val value = withContext(Dispatchers.IO) {
                getDecibel().toFloat()
            }
            dbLevel = value.coerceIn(0f, 120f)
            delay(100)
        }
    }
}
@Composable
fun CircularDbMeter(db: Float) {

    Box(contentAlignment = Alignment.Center) {

        Canvas(modifier = Modifier.size(250.dp)) {

            val sweep = (db / 120f) * 360f

            // background circle
            drawCircle(
                color = Color.DarkGray,
                style = Stroke(width = 20f)
            )

            // progress arc
            drawArc(
                brush = Brush.sweepGradient(
                    listOf(Color.Magenta, Color.Cyan)
                ),
                startAngle = -90f,
                sweepAngle = sweep,
                useCenter = false,
                style = Stroke(width = 20f, cap = StrokeCap.Round)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${db.toInt()} dB",
                color = Color.White,
                fontSize = 28.sp
            )
            Text(
                text = getNoiseLabel(db),
                color = Color.Gray
            )
        }
    }
}

@Composable
fun NoiseBarChart(modifier: Modifier= Modifier){
    val barcolor=colorResource(R.color.primaryButton)
    Box(
        modifier.size(height = 300.dp, width = 300.dp), contentAlignment = Alignment.Center
    ){
        Canvas(
            modifier.fillMaxSize().padding(12.dp)
        ) {

            val textPaint= Paint().apply {
                textSize=34f
                color=Color.White.toArgb()
            }


            val ySpacing= size.height.div(90)
            val xSpacing=size.width.div(7)
            drawContext.canvas.nativeCanvas.drawText(
                "Your Surrounding",
                0f,
                size.height-ySpacing.times(70),
                textPaint

            )
            drawContext.canvas.nativeCanvas.drawText(
                "Levels",
                xSpacing.times(7),
                size.height-ySpacing.times(70),
                textPaint

            )
            for(i in 10..50 step 10) {
                drawContext.canvas.nativeCanvas.drawText(
                   i.toString()+" dB",
                    0f,
                    size.height-ySpacing.times(i),
                    textPaint



                )
            }
            val days=listOf<String>("","Mon","Tue","Wed","Thu","Fri","Sat","Sun")
            for(i in 0 until days.size){
                drawContext.canvas.nativeCanvas.drawText(
                    days.get(i),
                    xSpacing.times(i),
                    size.height,
                    textPaint
                )
            }
            val data=listOf<Int>(0,20,50,10,30,25,40,15)
            
            for(i in 1 until days.size){
                val barheight=(data[i]*(ySpacing))-45
                drawRoundRect(
                    color=barcolor,
                    cornerRadius = CornerRadius(10f,10f),
                    size = Size(55f,barheight),
                    topLeft = Offset(xSpacing.times(i),size.height-ySpacing.times(data[i]))
                )
            }
        }

    }

}

@Preview(showBackground = true)
@Composable
fun BarChartPreview() {
    NoiseBarChart()
}


@Composable
fun BottomBar() {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(
                Brush.horizontalGradient(
                    listOf(Color(0xFF7B61FF), Color(0xFF5E35B1))
                ),
                shape = RoundedCornerShape(20.dp)
            ),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(Icons.Default.LocationOn, contentDescription = "", tint = Color.White)
        Icon(Icons.Default.Home, contentDescription = "", tint = Color.White)
        Icon(Icons.Default.Settings, contentDescription = "", tint = Color.White)
    }
}


fun getNoiseLabel(db: Float): String {
    return when {
        db < 30 -> "Quiet"
        db < 60 -> "Moderate"
        db < 85 -> "Loud"
        else -> "Dangerous"
    }
}

