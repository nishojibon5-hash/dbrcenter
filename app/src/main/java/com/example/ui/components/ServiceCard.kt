package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ServiceItemEntity
import com.example.ui.theme.*

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun getIconForService(key: String): ImageVector {
    return when (key.lowercase()) {
        "dent" -> Icons.Default.DirectionsCar
        "wash" -> Icons.Default.LocalCarWash
        "engine" -> Icons.Default.Handyman
        "gearbox" -> Icons.Default.Settings
        "brake" -> Icons.Default.Adjust
        "suspension" -> Icons.Default.Tune
        "fuel" -> Icons.Default.LocalGasStation
        "scan" -> Icons.Default.Computer
        "wheel" -> Icons.Default.Brightness5
        "oil" -> Icons.Default.WaterDrop
        else -> Icons.Default.Build
    }
}

@Composable
fun ServiceCard(
    service: ServiceItemEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isDesktop: Boolean = false,
    index: Int = 0
) {
    // Graceful scroll/mount entry animation: fade + slide up + scale
    val alphaAnim = remember { Animatable(0f) }
    val offsetYAnim = remember { Animatable(32f) }
    val scaleAnim = remember { Animatable(0.96f) }

    LaunchedEffect(service.id) {
        // Staggered cascade delay based on grid index
        delay(((index % 8) * 60L))
        kotlinx.coroutines.coroutineScope {
            launch {
                alphaAnim.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
                )
            }
            launch {
                offsetYAnim.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(durationMillis = 550, easing = FastOutSlowInEasing)
                )
            }
            launch {
                scaleAnim.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
                )
            }
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("service_card_${service.id}")
            .graphicsLayer {
                alpha = alphaAnim.value
                translationY = offsetYAnim.value
                scaleX = scaleAnim.value
                scaleY = scaleAnim.value
            }
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(DbrCardBorder)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Orange Round Icon
            Box(
                modifier = Modifier
                    .size(if (isDesktop) 46.dp else 42.dp)
                    .clip(CircleShape)
                    .background(DbrOrange),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getIconForService(service.iconKey),
                    contentDescription = service.title,
                    tint = Color.White,
                    modifier = Modifier.size(if (isDesktop) 24.dp else 22.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Title & Description
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = service.title,
                    color = DbrTextDark,
                    fontSize = if (isDesktop) 14.sp else 13.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = service.description,
                    color = DbrTextMuted,
                    fontSize = if (isDesktop) 11.sp else 10.5.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 14.sp
                )
            }

            // Arrow Chevron
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Details",
                tint = Color(0xFF0F766E),
                modifier = Modifier
                    .size(18.dp)
                    .padding(start = 2.dp)
            )
        }
    }
}
