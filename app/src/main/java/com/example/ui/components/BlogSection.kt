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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BlogPostEntity
import com.example.ui.theme.*

@Composable
fun BlogSection(
    posts: List<BlogPostEntity>,
    onPostClick: (BlogPostEntity) -> Unit,
    isDesktop: Boolean = false,
    sectionTitle: String = "ব্লগ ও অটোমোবাইল টিপস"
) {
    if (posts.isEmpty()) return

    var selectedFilter by remember { mutableStateOf("সব") }
    val categories = listOf("সব", "ইঞ্জিন কেয়ার", "ডেন্ট ও পেইন্ট", "গিয়ারবক্স")

    val filteredPosts = if (selectedFilter == "সব") posts else posts.filter { it.category == selectedFilter }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = if (isDesktop) 24.dp else 16.dp, vertical = 20.dp)
    ) {
        // Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(DbrOrangeLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Article,
                        contentDescription = null,
                        tint = DbrOrange,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = sectionTitle,
                        color = DbrTextDark,
                        fontSize = if (isDesktop) 22.sp else 18.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "গাড়ির যত্ন ও আধুনিক মেইনটেনেন্স গাইডলাইন",
                        color = DbrTextMuted,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Category Filter Chips
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 14.dp)
        ) {
            categories.forEach { cat ->
                val isSelected = selectedFilter == cat
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) DbrTeal else Color.White,
                    border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, DbrCardBorder),
                    modifier = Modifier.clickable { selectedFilter = cat }
                ) {
                    Text(
                        text = cat,
                        color = if (isSelected) Color.White else DbrTextDark,
                        fontSize = 11.5.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // Post Grid / List
        if (isDesktop) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                filteredPosts.take(3).forEach { post ->
                    Box(modifier = Modifier.weight(1f)) {
                        BlogPostCard(post = post, onClick = { onPostClick(post) })
                    }
                }
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                filteredPosts.forEach { post ->
                    BlogPostCard(post = post, onClick = { onPostClick(post) })
                }
            }
        }
    }
}

@Composable
fun BlogPostCard(
    post: BlogPostEntity,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(1.dp, DbrCardBorder, RoundedCornerShape(16.dp))
    ) {
        Column {
            if (post.imageUrl.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .background(
                            brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                                colors = listOf(Color(0xFF0F766E), Color(0xFF04211C))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = null,
                            tint = DbrOrange,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "ব্লগ ছবি সংযুক্ত",
                            color = Color.White,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Column(modifier = Modifier.padding(16.dp)) {
            // Category & Read Time
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = DbrMintTeal
                ) {
                    Text(
                        text = post.category,
                        color = DbrTeal,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = DbrTextMuted,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${post.readTimeMinutes} মিনিট পড়ার সময়",
                        color = DbrTextMuted,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Title
            Text(
                text = post.title,
                color = DbrTextDark,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Summary
            Text(
                text = post.summary,
                color = DbrTextMuted,
                fontSize = 12.5.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Bottom Author & Action
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(DbrOrangeLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = DbrOrange,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = post.author,
                        color = DbrTextMuted,
                        fontSize = 11.5.sp
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "পড়ুন",
                        color = DbrOrange,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = DbrOrange,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}
}
