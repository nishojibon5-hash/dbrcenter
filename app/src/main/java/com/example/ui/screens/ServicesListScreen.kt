package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ServiceItemEntity
import com.example.ui.components.ServiceCard
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicesListScreen(
    services: List<ServiceItemEntity>,
    onServiceClick: (ServiceItemEntity) -> Unit,
    onBack: () -> Unit,
    isDesktop: Boolean = false
) {
    BackHandler { onBack() }
    var selectedCategory by remember { mutableStateOf("সব") }
    var searchQuery by remember { mutableStateOf("") }

    val categories = listOf("সব", "বডি ও পেইন্ট", "ক্লিনিং", "মেকানিক্যাল", "হুইল", "২ হুইলার", "ফুয়েল", "ইলেকট্রনিক্স", "হাইব্রিড")

    val filteredServices = services.filter { service ->
        val matchesCat = if (selectedCategory == "সব") true else service.category.contains(selectedCategory) || service.title.contains(selectedCategory)
        val matchesSearch = if (searchQuery.isBlank()) true else service.title.contains(searchQuery, ignoreCase = true) || service.description.contains(searchQuery, ignoreCase = true)
        matchesCat && matchesSearch
    }

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
                            text = " / আমাদের সকল সেবা সমূহ",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(DbrLimeGreenLight)
        ) {
            // Category Chips
            Surface(
                color = Color.White,
                shadowElevation = 1.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(vertical = 10.dp)) {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(categories) { cat ->
                            val isSelected = selectedCategory == cat
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = if (isSelected) DbrPrimaryTeal else Color(0xFFF1F5F9),
                                modifier = Modifier.clickable { selectedCategory = cat }
                            ) {
                                Text(
                                    text = cat,
                                    color = if (isSelected) Color.White else DbrTextDark,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                                )
                            }
                        }
                    }
                }
            }

            // List of services
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = if (isDesktop) 24.dp else 16.dp, vertical = 12.dp)
            ) {
                item {
                    Text(
                        text = "মোট ${filteredServices.size}টি সার্ভিস উপলভ্য (ক্লিক করে বিস্তারিত দেখুন)",
                        color = Color(0xFF64748B),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }

                if (isDesktop) {
                    val chunks = filteredServices.chunked(3)
                    items(chunks) { rowItems ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            rowItems.forEach { service ->
                                Box(modifier = Modifier.weight(1f)) {
                                    ServiceCard(
                                        service = service,
                                        onClick = { onServiceClick(service) },
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
                    val chunks = filteredServices.chunked(2)
                    items(chunks) { rowItems ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            rowItems.forEach { service ->
                                Box(modifier = Modifier.weight(1f)) {
                                    ServiceCard(
                                        service = service,
                                        onClick = { onServiceClick(service) },
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
}
