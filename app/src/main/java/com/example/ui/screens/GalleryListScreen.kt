package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.WorkShowcaseEntity
import com.example.ui.components.RepairWorkCard
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryListScreen(
    items: List<WorkShowcaseEntity>,
    onItemClick: (WorkShowcaseEntity) -> Unit,
    onBack: () -> Unit,
    isDesktop: Boolean = false
) {
    BackHandler { onBack() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "হোম",
                            color = DbrOrange,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { onBack() }
                        )
                        Text(
                            text = " / কাজের ধরন ও গ্যালারি",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "হোমে ফিরে যান", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DbrDarkTeal)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(DbrBackground)
                .padding(horizontal = if (isDesktop) 24.dp else 16.dp, vertical = 14.dp)
        ) {
            item {
                Text(
                    text = "আমাদের সফলভাবে সম্পন্ন কাজের নিদর্শন (ক্লিক করে বিস্তারিত দেখুন)",
                    color = Color(0xFF64748B),
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            if (isDesktop) {
                val chunks = items.chunked(3)
                items(chunks.size) { index ->
                    val rowItems = chunks[index]
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        rowItems.forEach { item ->
                            Box(modifier = Modifier.weight(1f)) {
                                RepairWorkCard(
                                    item = item,
                                    onClick = { onItemClick(item) },
                                    onWhatsAppClick = { onItemClick(item) },
                                    isDesktop = true
                                )
                            }
                        }
                        for (i in 0 until (3 - rowItems.size)) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            } else {
                val chunks = items.chunked(2)
                items(chunks.size) { index ->
                    val rowItems = chunks[index]
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        rowItems.forEach { item ->
                            Box(modifier = Modifier.weight(1f)) {
                                RepairWorkCard(
                                    item = item,
                                    onClick = { onItemClick(item) },
                                    onWhatsAppClick = { onItemClick(item) },
                                    isDesktop = false
                                )
                            }
                        }
                        if (rowItems.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}
