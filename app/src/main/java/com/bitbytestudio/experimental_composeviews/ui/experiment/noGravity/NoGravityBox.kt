package com.bitbytestudio.experimental_composeviews.ui.experiment.noGravity

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.isActive
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

data class PhysicsObject(
    var x: Float,
    var y: Float,
    var vx: Float = 0f,
    var vy: Float = 0f,
    var width: Float,
    var height: Float,
    val id: Int,
    var isDragging: Boolean = false,
    var lastDragTime: Long = 0L,
    var lastDragX: Float = 0f,
    var lastDragY: Float = 0f
)

@Composable
fun NoGravityBox(
    modifier: Modifier = Modifier,
    friction: Float = 0.98f,
    restitution: Float = 0.7f,
    enableGyroscope: Boolean = true,
    gyroscopeScale: Float = 50f,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val density = LocalDensity.current

    var objects by remember { mutableStateOf<List<PhysicsObject>>(emptyList()) }
    var containerSize by remember { mutableStateOf(Offset.Zero) }
    var gyroForce by remember { mutableStateOf(Offset.Zero) }

    // Gyroscope sensor
    DisposableEffect(enableGyroscope) {
        if (!enableGyroscope) {
            onDispose {}
        }else {

            val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
            val gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

            val listener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    event?.let {
                        // Gyroscope measures rotation rate, integrate to get acceleration-like effect
                        gyroForce = Offset(
                            x = it.values[1] * gyroscopeScale,
                            y = -it.values[0] * gyroscopeScale
                        )
                    }
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
            }

            gyroscope?.let {
                sensorManager.registerListener(listener, it, SensorManager.SENSOR_DELAY_GAME)
            }

            onDispose {
                sensorManager.unregisterListener(listener)
            }
        }
    }

    // Physics simulation loop
    LaunchedEffect(Unit) {
        while (isActive) {
            awaitFrame()

            if (objects.isEmpty() || containerSize == Offset.Zero) continue

            objects = objects.map { obj ->
                if (obj.isDragging) return@map obj

                // Apply gyroscope force
                var newVx = obj.vx + gyroForce.x * 0.016f
                var newVy = obj.vy + gyroForce.y * 0.016f

                // Apply friction
                newVx *= friction
                newVy *= friction

                // Update position
                var newX = obj.x + newVx
                var newY = obj.y + newVy

                // Wall collision
                if (newX <= 0f) {
                    newX = 0f
                    newVx = abs(newVx) * restitution
                } else if (newX + obj.width >= containerSize.x) {
                    newX = containerSize.x - obj.width
                    newVx = -abs(newVx) * restitution
                }

                if (newY <= 0f) {
                    newY = 0f
                    newVy = abs(newVy) * restitution
                } else if (newY + obj.height >= containerSize.y) {
                    newY = containerSize.y - obj.height
                    newVy = -abs(newVy) * restitution
                }

                obj.copy(x = newX, y = newY, vx = newVx, vy = newVy)
            }

            // Object-to-object collision
            objects = resolveCollisions(objects, restitution)
        }
    }

    Layout(
        modifier = modifier.pointerInput(Unit) {
            detectDragGestures(
                onDragStart = { offset ->
                    objects = objects.map { obj ->
                        val hit = offset.x >= obj.x && offset.x <= obj.x + obj.width &&
                                offset.y >= obj.y && offset.y <= obj.y + obj.height
                        if (hit) {
                            obj.copy(
                                isDragging = true,
                                lastDragTime = System.currentTimeMillis(),
                                lastDragX = offset.x,
                                lastDragY = offset.y,
                                vx = 0f,
                                vy = 0f
                            )
                        } else obj
                    }
                },
                onDrag = { change, dragAmount ->
                    change.consume()
                    val currentTime = System.currentTimeMillis()

                    objects = objects.map { obj ->
                        if (!obj.isDragging) return@map obj

                        val newX = (obj.x + dragAmount.x).coerceIn(0f, containerSize.x - obj.width)
                        val newY = (obj.y + dragAmount.y).coerceIn(0f, containerSize.y - obj.height)

                        obj.copy(
                            x = newX,
                            y = newY,
                            lastDragTime = currentTime,
                            lastDragX = change.position.x,
                            lastDragY = change.position.y
                        )
                    }
                },
                onDragEnd = {
                    val endTime = System.currentTimeMillis()

                    objects = objects.map { obj ->
                        if (!obj.isDragging) return@map obj

                        // Calculate velocity from drag
                        val dt = (endTime - obj.lastDragTime) / 1000f
                        val velocity = if (dt > 0f && dt < 0.1f) {
                            Offset(
                                x = (obj.x - obj.lastDragX) / dt,
                                y = (obj.y - obj.lastDragY) / dt
                            )
                        } else Offset.Zero

                        obj.copy(
                            isDragging = false,
                            vx = velocity.x.coerceIn(-2000f, 2000f),
                            vy = velocity.y.coerceIn(-2000f, 2000f)
                        )
                    }
                }
            )
        },
        content = content
    ) { measurables, constraints ->
        val placeables = measurables.mapIndexed { index, measurable ->
            measurable.measure(Constraints())
        }

        // Initialize objects if needed
        if (objects.isEmpty() && placeables.isNotEmpty()) {
            objects = placeables.mapIndexed { index, placeable ->
                PhysicsObject(
                    x = (index * 100f) % (constraints.maxWidth - placeable.width).toFloat(),
                    y = (index * 100f / constraints.maxWidth) * 100f,
                    width = placeable.width.toFloat(),
                    height = placeable.height.toFloat(),
                    id = index
                )
            }
        }

        containerSize = Offset(constraints.maxWidth.toFloat(), constraints.maxHeight.toFloat())

        layout(constraints.maxWidth, constraints.maxHeight) {
            placeables.forEachIndexed { index, placeable ->
                val obj = objects.getOrNull(index) ?: return@forEachIndexed
                placeable.place(obj.x.toInt(), obj.y.toInt())
            }
        }
    }
}

private fun resolveCollisions(objects: List<PhysicsObject>, restitution: Float): List<PhysicsObject> {
    val result = objects.toMutableList()

    for (i in result.indices) {
        for (j in i + 1 until result.size) {
            val obj1 = result[i]
            val obj2 = result[j]

            if (obj1.isDragging || obj2.isDragging) continue

            // AABB collision detection
            val dx = (obj1.x + obj1.width / 2) - (obj2.x + obj2.width / 2)
            val dy = (obj1.y + obj1.height / 2) - (obj2.y + obj2.height / 2)
            val dist = sqrt(dx * dx + dy * dy)
            val minDist = (obj1.width + obj2.width) / 2

            if (dist < minDist && dist > 0) {
                // Collision detected - separate and bounce
                val overlap = minDist - dist
                val nx = dx / dist
                val ny = dy / dist

                // Separate objects
                result[i] = obj1.copy(
                    x = obj1.x + nx * overlap / 2,
                    y = obj1.y + ny * overlap / 2
                )
                result[j] = obj2.copy(
                    x = obj2.x - nx * overlap / 2,
                    y = obj2.y - ny * overlap / 2
                )

                // Calculate relative velocity
                val dvx = obj1.vx - obj2.vx
                val dvy = obj1.vy - obj2.vy
                val dvn = dvx * nx + dvy * ny

                if (dvn < 0) continue // Objects moving apart

                // Apply impulse
                val impulse = 2 * dvn / 2 * restitution

                result[i] = result[i].copy(
                    vx = obj1.vx - impulse * nx,
                    vy = obj1.vy - impulse * ny
                )
                result[j] = result[j].copy(
                    vx = obj2.vx + impulse * nx,
                    vy = obj2.vy + impulse * ny
                )
            }
        }
    }

    return result
}


@Composable
fun FloatingChip(label: String, color: Color) {
    Box (
        modifier = Modifier
            .background(Color(0xFF2A2D35), RoundedCornerShape(20))
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(label, color = Color.Red)
    }
}



@Preview(showSystemUi = true, showBackground = true)
@Composable
fun DemoNoGravityScreen() {

    Box(modifier = Modifier.fillMaxSize()) {
        NoGravityBox(
            modifier = Modifier.fillMaxSize(),
            friction = 0.98f,
            restitution = 0.7f,
            enableGyroscope = true,
            gyroscopeScale = 50f
        ) {
            // Add your composables here
            FloatingChip(label = "Item 1", color = Color.Red)
            FloatingChip(label = "Item 2", color = Color.Green)
            FloatingChip(label = "Item 3", color = Color.Blue)
            FloatingChip(label = "Item 4", color = Color.LightGray)
        }
    }
}

