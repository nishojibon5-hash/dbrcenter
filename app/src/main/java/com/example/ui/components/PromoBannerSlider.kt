package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.*
import kotlinx.coroutines.delay

data class PromoSlide(
    val id: Int,
    val tag: String,
    val title: String,
    val description: String,
    val imageRes: Int,
    val features: List<String>,
    val badgeTop: String,
    val badgeBottom: String
)

@Composable
fun PromoBannerSlider(
    modifier: Modifier = Modifier,
    isDesktop: Boolean = false,
    onBookClick: () -> Unit = {},
    onWhatsAppClick: () -> Unit = {}
) {
    val slides = remember {
        listOf(
            PromoSlide(
                id = 0,
                tag = "প্রিমিয়াম ওয়ার্কশপ প্রোফাইল",
                title = "আপনার গাড়ি আমাদের যত্নে — বগুড়ার বিশ্বস্ত অটো সেন্টার",
                description = "অত্যাধুনিক ডেন্ট-পেইন্ট ওভেন বুথ, জাপানিজ ডিজিটাল স্ক্যানার এবং অভিজ্ঞ মেকানিক দ্বারা সকল মডেলের গাড়ির ১০০% আস্থার ও নির্ভুল সেবা।",
                imageRes = R.drawable.workshop_building,
                features = listOf("১০০% জেনুইন পার্টস ও নির্ভুল কাজ", "স্বচ্ছ বিলিং ও অন-টাইম ডেলিভারি", "অভিজ্ঞ ইঞ্জিনিয়ার ও হেড মেকানিক টিম"),
                badgeTop = "৪:৩ ভেরিফাইড",
                badgeBottom = "শহীদ জিয়া মেডিকেল রোড, শাকপালা"
            ),
            PromoSlide(
                id = 1,
                tag = "বডি রিস্টোরেশন",
                title = "অরিজিনাল কালার ও নিখুঁত ফিনিশিং গ্যারান্টি",
                description = "যেকোনো স্ক্র্যাচ বা অ্যাক্সিডেন্ট ডেন্ট ঠিক করতে আধুনিক পুলার এবং ফ্যাক্টরি গ্রেড হিট ওভেন কালার স্প্রে দ্বারা গাড়ির নতুনের মতো উজ্জ্বলতা ফিরিয়ে দেওয়া হয়।",
                imageRes = R.drawable.hero_orange_car,
                features = listOf("কম্পিউটারাইজড কালার ম্যাচিং প্রযুক্তি", "ডাস্ট-ফ্রি হিট চেম্বার ওভেন পেইন্ট", "দীর্ঘস্থায়ী গ্লস ও প্রটেকশন কোটিং"),
                badgeTop = "১০০% অরিজিনাল",
                badgeBottom = "হিট ওভেন পেইন্ট বুথ"
            ),
            PromoSlide(
                id = 2,
                tag = "হাইব্রিড প্রযুক্তি",
                title = "হাইব্রিড ব্যাটারি হেলথ ও কুলিং ফ্যান সার্ভিস",
                description = "ব্যাটারি অতিরিক্ত গরম হওয়া, মাইলেজ কমে যাওয়া বা এরর কোডের সঠিক সমাধানে প্রতিটি সেলের ভোল্টেজ ব্যালান্সিং ও ডিপ ক্লিনিং সেবা।",
                imageRes = R.drawable.workshop_building,
                features = listOf("কুলিং ফ্যান ক্লিন ও ফিল্টার চেকআপ", "স্বতন্ত্র সেল ভোল্টেজ ব্যালান্সিং", "ইনভার্টার ও কুল্যান্ট সিস্টেম চেক"),
                badgeTop = "হাইব্রিড এক্সপার্ট",
                badgeBottom = "কম্পিউটারাইজড সেল ডায়াগনসিস"
            ),
            PromoSlide(
                id = 3,
                tag = "এক্সক্লুসিভ বেনিফিট",
                title = "ফ্রি কম্পিউটার স্ক্যানিং ও ফোম কার ওয়াশ অফার!",
                description = "যেকোনো মেজর ইঞ্জিন সার্ভিস বা ফুল বডি পেইন্ট সার্ভিসের সাথে সম্পূর্ণ বিনামূল্যে পান ডিজিটাল স্ক্যানিং রিপোর্ট ও প্রফেশনাল ফোম ওয়াশ।",
                imageRes = R.drawable.hero_orange_car,
                features = listOf("সম্পূর্ণ ফ্রি OBD-II ডিজিটাল স্ক্যান", "ফ্রি প্রিমিয়াম ফোম কার ওয়াশ", "সাসপেনশন ও ব্রেক সেফটি চেকআপ"),
                badgeTop = "সীমিত অফার",
                badgeBottom = "ফ্রি ডায়াগনস্টিক রিপোর্ট"
            )
        )
    }

    var currentSlideIndex by remember { mutableIntStateOf(0) }

    // Auto rotate every 5 seconds
    LaunchedEffect(Unit) {
        while (true) {
            delay(5000L)
            currentSlideIndex = (currentSlideIndex + 1) % slides.size
        }
    }

    val currentSlide = slides[currentSlideIndex]

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = if (isDesktop) 24.dp else 16.dp, vertical = 10.dp)
            .shadow(4.dp, RoundedCornerShape(20.dp))
            .testTag("promo_banner_slider"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(modifier = Modifier.fillMaxWidth().padding(if (isDesktop) 24.dp else 16.dp)) {
            AnimatedContent(
                targetState = currentSlide,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "PromoSlideAnimation"
            ) { slide ->
                if (isDesktop) {
                    // Desktop Layout: 2 Columns
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(28.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Image (4:3 aspect ratio with orange border)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(4f / 3f)
                                .clip(RoundedCornerShape(16.dp))
                                .border(3.dp, DbrOrange, RoundedCornerShape(16.dp))
                        ) {
                            Image(
                                painter = painterResource(id = slide.imageRes),
                                contentDescription = slide.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            // Tag overlay
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .fillMaxWidth()
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(Color.Transparent, Color(0xEE04211C))
                                        )
                                    )
                                    .padding(horizontal = 14.dp, vertical = 10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = slide.title.take(24) + "...",
                                            color = Color.White,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = slide.badgeBottom,
                                            color = Color(0xFFCBD5E1),
                                            fontSize = 10.sp
                                        )
                                    }
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = DbrOrange
                                    ) {
                                        Text(
                                            text = slide.badgeTop,
                                            color = Color.White,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }
                                }
                            }
                        }

                        // Content Details
                        Column(modifier = Modifier.weight(1.2f)) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFFFF7ED),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFEDD5))
                            ) {
                                Text(
                                    text = "★ " + slide.tag,
                                    color = DbrOrange,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = slide.title,
                                color = DbrTextDark,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                lineHeight = 28.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = slide.description,
                                color = Color(0xFF475569),
                                fontSize = 13.5.sp,
                                lineHeight = 20.sp
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            slide.features.forEach { feat ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(vertical = 2.dp)
                                ) {
                                    Icon(
                                        Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = DbrOrange,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = feat,
                                        color = DbrTextDark,
                                        fontSize = 12.5.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(18.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Button(
                                    onClick = onBookClick,
                                    colors = ButtonDefaults.buttonColors(containerColor = DbrOrange),
                                    shape = RoundedCornerShape(20.dp),
                                    contentPadding = PaddingValues(horizontal = 18.dp, vertical = 10.dp)
                                ) {
                                    Icon(Icons.Default.CalendarMonth, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("সেবা বুক করুন", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                }
                                OutlinedButton(
                                    onClick = onWhatsAppClick,
                                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF0F766E)),
                                    shape = RoundedCornerShape(20.dp),
                                    contentPadding = PaddingValues(horizontal = 18.dp, vertical = 10.dp)
                                ) {
                                    Icon(Icons.Default.Chat, contentDescription = null, tint = Color(0xFF25D366), modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("হোয়াটসঅ্যাপে জানুন", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F766E))
                                }
                            }
                        }
                    }
                } else {
                    // Mobile Layout: Stacked
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // 4:3 Image
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(4f / 3f)
                                .clip(RoundedCornerShape(14.dp))
                                .border(2.5.dp, DbrOrange, RoundedCornerShape(14.dp))
                        ) {
                            Image(
                                painter = painterResource(id = slide.imageRes),
                                contentDescription = slide.title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            // Overlay tag
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .fillMaxWidth()
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(Color.Transparent, Color(0xEE04211C))
                                        )
                                    )
                                    .padding(horizontal = 10.dp, vertical = 8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = slide.title.take(20) + "...",
                                        color = Color.White,
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = DbrOrange
                                    ) {
                                        Text(
                                            text = slide.badgeTop,
                                            color = Color.White,
                                            fontSize = 9.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFFFF7ED)
                        ) {
                            Text(
                                text = "★ " + slide.tag,
                                color = DbrOrange,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = slide.title,
                            color = DbrTextDark,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = slide.description,
                            color = Color(0xFF475569),
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        slide.features.forEach { feat ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 1.5.dp)
                            ) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = DbrOrange,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = feat,
                                    color = DbrTextDark,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = onBookClick,
                                colors = ButtonDefaults.buttonColors(containerColor = DbrOrange),
                                shape = RoundedCornerShape(18.dp),
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(vertical = 8.dp)
                            ) {
                                Text("বুক করুন", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                            OutlinedButton(
                                onClick = onWhatsAppClick,
                                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF0F766E)),
                                shape = RoundedCornerShape(18.dp),
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(vertical = 8.dp)
                            ) {
                                Text("WhatsApp", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F766E))
                            }
                        }
                    }
                }
            }

            // Dots indicator at bottom right
            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 4.dp, bottom = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                slides.indices.forEach { index ->
                    Box(
                        modifier = Modifier
                            .size(if (index == currentSlideIndex) 20.dp else 8.dp, 8.dp)
                            .clip(CircleShape)
                            .background(if (index == currentSlideIndex) DbrOrange else Color(0xFFCBD5E1))
                            .clickable { currentSlideIndex = index }
                    )
                }
            }
        }
    }
}
