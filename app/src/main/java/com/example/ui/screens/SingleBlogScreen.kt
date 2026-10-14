package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.model.BlogPostEntity
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleBlogScreen(
    post: BlogPostEntity,
    onBack: () -> Unit,
    onBookService: () -> Unit,
    isDesktop: Boolean = false
) {
    BackHandler { onBack() }
    val context = LocalContext.current
    val scrollState = rememberScrollState()

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
                            text = " / ব্লগ ও টিপস / ",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 13.sp
                        )
                        Text(
                            text = post.category,
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(DbrBackground),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = if (isDesktop) 850.dp else 600.dp)
                    .verticalScroll(scrollState)
                    .padding(if (isDesktop) 24.dp else 16.dp)
            ) {
                // Post Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        // Hero Image
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(if (isDesktop) 320.dp else 220.dp)
                                .background(DbrDeepTeal)
                        ) {
                            if (post.imageUrl.isNotBlank()) {
                                AsyncImage(
                                    model = ImageRequest.Builder(context)
                                        .data(post.imageUrl)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = post.title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Article,
                                        contentDescription = null,
                                        tint = Color.White.copy(alpha = 0.6f),
                                        modifier = Modifier.size(64.dp)
                                    )
                                }
                            }
                        }

                        Column(modifier = Modifier.padding(20.dp)) {
                            // Category Tag & Meta
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.padding(bottom = 8.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = DbrLightMint
                                ) {
                                    Text(
                                        text = post.category,
                                        color = DbrPrimaryTeal,
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }

                                Text(
                                    text = "লেখক: ${post.author}",
                                    color = Color(0xFF64748B),
                                    fontSize = 12.sp
                                )

                                Text(
                                    text = "• ${post.readTimeMinutes} মিনিট পড়ার সময়",
                                    color = Color(0xFF64748B),
                                    fontSize = 12.sp
                                )
                            }

                            // Title
                            Text(
                                text = post.title,
                                color = DbrTextDark,
                                fontSize = if (isDesktop) 24.sp else 20.sp,
                                fontWeight = FontWeight.Bold,
                                lineHeight = 28.sp
                            )

                            Spacer(modifier = Modifier.height(14.dp))
                            Divider(color = Color(0xFFF1F5F9))
                            Spacer(modifier = Modifier.height(14.dp))

                            // Summary / Intro
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFFF8FAFC),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp)
                            ) {
                                Text(
                                    text = post.summary,
                                    color = Color(0xFF475569),
                                    fontSize = 14.5.sp,
                                    fontWeight = FontWeight.Medium,
                                    lineHeight = 22.sp,
                                    modifier = Modifier.padding(14.dp)
                                )
                            }

                            // Full Content Text
                            Text(
                                text = if (post.content.isNotBlank()) post.content else post.summary,
                                color = Color(0xFF334155),
                                fontSize = 15.sp,
                                lineHeight = 24.sp
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "গাড়ির সুস্থতায় নিয়মিত রক্ষণাবেক্ষণের কোনো বিকল্প নেই। আপনার গাড়ির যেকোনো অদ্ভুত শব্দ, ইঞ্জিন ভাইব্রেশন অথবা গিয়ারবক্সের সমস্যাকে কখনই অবহেলা করবেন না। সময়মতো অল্প খরচে সমাধান না করলে পরবর্তীতে বড় ধরণের আর্থিক ক্ষতির মুখে পড়তে হতে পারে। DBR কমপ্লিট অটো সেন্টারে আমরা গ্রাহকদের শতভাগ নির্ভুল সেবা নিশ্চিত করি।",
                                color = Color(0xFF334155),
                                fontSize = 14.5.sp,
                                lineHeight = 24.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Checklist / Advice Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFF86EFAC), RoundedCornerShape(16.dp))
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Shield, contentDescription = null, tint = Color(0xFF15803D))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "DBR এক্সপার্ট চেক-লিস্ট ও পরামর্শ:",
                                color = Color(0xFF15803D),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        val tips = listOf(
                            "প্রতি ৩,০০০ - ৪,০০০ কিলোমিটারে ইঞ্জিন অয়েল ও অয়েল ফিল্টার পরিবর্তন করুন।",
                            "ড্যাশবোর্ডে ইঞ্জিন চেক লাইট জ্বললে দেরি না করে দ্রুত কম্পিউটার স্ক্যান করান।",
                            "বর্ষাকালে গাড়ির চেসিস ও বডিতে কাদা জমতে না দিয়ে নিয়মিত ওয়াশ করান।",
                            "টায়ারের ব্যালান্সিং ও লেজার অ্যালাইনমেন্ট প্রতি ১০,০০০ কিমিতে পরীক্ষা করুন।"
                        )

                        tips.forEach { tip ->
                            Row(modifier = Modifier.padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text("✓", color = Color(0xFF16A34A), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(tip, color = Color(0xFF166534), fontSize = 13.5.sp)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Bottom Call & Booking Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DbrDarkTeal),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "আপনার গাড়িতে কোনো সমস্যা হচ্ছে?",
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "DBR কার ওয়ার্কশপে অভিজ্ঞ মেকানিক দ্বারা আজই ফ্রি ডায়াগনস্টিক পরামর্শ নিন।",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 13.5.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = {
                                try {
                                    val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=DBR+Complete+Auto+Centre+Bogura"))
                                    context.startActivity(mapIntent)
                                } catch (_: Exception) {}
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                        ) {
                            Icon(imageVector = Icons.Default.LocationOn, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("গুগোল মেপে লোকেশন দেখুন", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = onBookService,
                                colors = ButtonDefaults.buttonColors(containerColor = DbrOrange),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("বুকিং দিন")
                            }

                            OutlinedButton(
                                onClick = {
                                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:01711287258"))
                                    context.startActivity(intent)
                                },
                                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(imageVector = Icons.Default.Call, contentDescription = null, tint = DbrPrimaryTeal, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("কল করুন", color = DbrPrimaryTeal)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}
