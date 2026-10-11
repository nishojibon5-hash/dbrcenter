package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import com.example.data.model.CustomerReviewEntity
import com.example.ui.theme.*
import java.util.Locale

@Composable
fun TestimonialsSection(
    reviews: List<CustomerReviewEntity>,
    currentIndex: Int,
    onPrevClick: () -> Unit,
    onNextClick: () -> Unit,
    onSelectIndex: (Int) -> Unit,
    onWriteReviewClick: () -> Unit,
    onSeeAllReviewsClick: () -> Unit = onWriteReviewClick,
    isDesktop: Boolean = false,
    sectionTitle: String = "গ্রাহকদের মতামত ও গুগল রেটিং"
) {
    if (reviews.isEmpty()) return

    // Calculate rating score and distribution
    val totalCount = reviews.size
    val averageScore = if (totalCount > 0) {
        reviews.map { it.rating }.average()
    } else {
        5.0
    }
    val formattedScore = String.format(Locale.US, "%.1f", averageScore)

    val count5 = reviews.count { it.rating == 5 }
    val count4 = reviews.count { it.rating == 4 }
    val count3 = reviews.count { it.rating == 3 }
    val count2 = reviews.count { it.rating == 2 }
    val count1 = reviews.count { it.rating == 1 }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = if (isDesktop) 24.dp else 16.dp, vertical = 14.dp)
            .testTag("testimonials_section")
    ) {
        // Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Verified,
                    contentDescription = null,
                    tint = DbrOrange,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = sectionTitle,
                    color = DbrTextDark,
                    fontSize = if (isDesktop) 20.sp else 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Write Review Pill Button on header
            Button(
                onClick = onWriteReviewClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = DbrOrange,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(20.dp),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                modifier = Modifier.testTag("give_rating_button_top")
            ) {
                Icon(imageVector = Icons.Default.RateReview, contentDescription = null, modifier = Modifier.size(15.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = "রেটিং দিন", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // TRUST & RATING SCOREBOARD CARD
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("public_rating_scoreboard"),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = androidx.compose.ui.graphics.SolidColor(DbrCardBorder)
            )
        ) {
            if (isDesktop) {
                // Desktop 3-Column Scoreboard
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Left Column: Big Score & Stars
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = formattedScore,
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Black,
                            color = DbrTextDark
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            repeat(5) { index ->
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = if (index < averageScore.toInt()) DbrGold else Color.LightGray,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "মোট $totalCount টি ভেরিফাইড গ্রাহক রেটিং",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = DbrTextMuted
                        )
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = DbrMintTeal,
                            modifier = Modifier.padding(top = 6.dp)
                        ) {
                            Text(
                                text = "✓ ১০০% বিশ্বস্ত অটোমোবাইল সার্ভিস",
                                color = DbrTeal,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Middle Column: Rating Progress Bars
                    Column(
                        modifier = Modifier.weight(1.4f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        StarRatingBar(star = 5, count = count5, total = totalCount)
                        StarRatingBar(star = 4, count = count4, total = totalCount)
                        StarRatingBar(star = 3, count = count3, total = totalCount)
                        StarRatingBar(star = 2, count = count2, total = totalCount)
                        StarRatingBar(star = 1, count = count1, total = totalCount)
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    // Right Column: Call to Action for Public Rating
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "আপনি কি আমাদের সার্ভিস নিয়েছেন?",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = DbrTextDark
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "আপনার সত্য মতামত অন্য গাড়ি মালিকদের সঠিক সিদ্ধান্ত নিতে সাহায্য করবে।",
                            fontSize = 11.sp,
                            color = DbrTextMuted,
                            lineHeight = 15.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = onWriteReviewClick,
                            colors = ButtonDefaults.buttonColors(containerColor = DbrOrange),
                            shape = RoundedCornerShape(20.dp),
                            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 10.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Star, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("রিভিউ ও রেটিং দিন", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            } else {
                // Mobile Scoreboard Layout
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Big Rating Display
                        Column {
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = formattedScore,
                                    fontSize = 36.sp,
                                    fontWeight = FontWeight.Black,
                                    color = DbrTextDark
                                )
                                Text(
                                    text = " / ৫.০",
                                    fontSize = 14.sp,
                                    color = DbrTextMuted,
                                    modifier = Modifier.padding(bottom = 6.dp)
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                repeat(5) { index ->
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = if (index < averageScore.toInt()) DbrGold else Color.LightGray,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                            Text(
                                text = "মোট $totalCount জন রেটিং দিয়েছেন",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = DbrTextMuted
                            )
                        }

                        // Rating Bars (Compact)
                        Column(
                            modifier = Modifier.width(160.dp),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            StarRatingBar(star = 5, count = count5, total = totalCount)
                            StarRatingBar(star = 4, count = count4, total = totalCount)
                            StarRatingBar(star = 3, count = count3, total = totalCount)
                            StarRatingBar(star = 2, count = count2, total = totalCount)
                            StarRatingBar(star = 1, count = count1, total = totalCount)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Divider(color = DbrCardBorder.copy(alpha = 0.6f))
                    Spacer(modifier = Modifier.height(10.dp))

                    // Public CTA Button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(imageVector = Icons.Default.Security, contentDescription = null, tint = DbrTeal, modifier = Modifier.size(16.dp))
                            Text(
                                text = "১০০% ভেরিফাইড গ্রাহক রিভিউ",
                                fontSize = 11.sp,
                                color = DbrTeal,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        OutlinedButton(
                            onClick = onWriteReviewClick,
                            border = androidx.compose.foundation.BorderStroke(1.2.dp, DbrOrange),
                            shape = RoundedCornerShape(16.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            modifier = Modifier.testTag("give_rating_button_mobile")
                        ) {
                            Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = DbrOrange, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("রেটিং দিন", color = DbrOrange, fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // INDIVIDUAL REVIEWS DISPLAY (4-5 latest reviews on home)
        val homeReviews = reviews.take(5)
        if (isDesktop) {
            // Desktop: Multiple Review Cards in a Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                homeReviews.forEach { review ->
                    ReviewCard(
                        review = review,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        } else {
            // Mobile: Carousel with Navigation Arrows
            val safeIndex = currentIndex.coerceIn(0, homeReviews.size - 1)
            val currentReview = homeReviews[safeIndex]

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Prev button
                IconButton(
                    onClick = onPrevClick,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(1.dp, DbrCardBorder, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.ChevronLeft,
                        contentDescription = "Previous",
                        tint = DbrTextDark
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                ReviewCard(
                    review = currentReview,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Next button
                IconButton(
                    onClick = onNextClick,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(1.dp, DbrCardBorder, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Next",
                        tint = DbrTextDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Carousel Dots
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                reviews.take(5).indices.forEach { index ->
                    val isSelected = index == safeIndex
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 3.dp)
                            .size(if (isSelected) 8.dp else 6.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) DbrOrange else Color(0xFFD0DCD6))
                            .clickable { onSelectIndex(index) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Bottom 'All Reviews' (সব রিভিউ) Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = onSeeAllReviewsClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = DbrTeal,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
                modifier = Modifier.testTag("all_reviews_button_bottom")
            ) {
                Icon(
                    imageVector = Icons.Default.RateReview,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "সকল রিভিউ দেখুন (অল রিভিউ) →",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun StarRatingBar(
    star: Int,
    count: Int,
    total: Int
) {
    val progress = if (total > 0) count.toFloat() / total else 0f
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "$star ★",
            fontSize = 10.5.sp,
            fontWeight = FontWeight.Bold,
            color = DbrTextDark,
            modifier = Modifier.width(24.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .weight(1f)
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = DbrOrange,
            trackColor = Color(0xFFE5EEE9)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = "$count",
            fontSize = 10.sp,
            color = DbrTextMuted,
            modifier = Modifier.width(18.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.End
        )
    }
}

@Composable
fun ReviewCard(
    review: CustomerReviewEntity,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.testTag("review_card_${review.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(DbrCardBorder)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Customer Avatar Circle
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            try {
                                Color(android.graphics.Color.parseColor(review.avatarColorHex))
                            } catch (e: Exception) {
                                DbrTeal
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = review.customerName.take(1),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = review.customerName,
                            color = DbrTextDark,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.5.sp
                        )
                        if (review.isVerified) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Verified",
                                tint = DbrSuccessGreen,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            repeat(review.rating) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = DbrGold,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                        Text(
                            text = review.carModelOrRole,
                            color = DbrTextMuted,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            // Service Taken Badge
            if (review.serviceTaken.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = DbrMintTeal.copy(alpha = 0.6f)
                ) {
                    Text(
                        text = "সার্ভিস: ${review.serviceTaken}",
                        fontSize = 10.sp,
                        color = DbrTeal,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = review.comment,
                color = DbrTextDark,
                fontSize = 12.5.sp,
                lineHeight = 17.sp,
                fontStyle = FontStyle.Normal
            )

            // Google Maps User Profile & Contributor Card
            Spacer(modifier = Modifier.height(10.dp))
            val context = LocalContext.current
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFF1F5F9),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFFEA4335)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Place,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(11.dp)
                                    )
                                    Text(
                                        text = "Google Maps",
                                        color = Color.White,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Text(
                                text = "লোকাল গাইড কন্ট্রিবিউটর",
                                color = Color(0xFF1E293B),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        // Star badge
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = DbrGold,
                                modifier = Modifier.size(11.dp)
                            )
                            Text(
                                text = "${review.rating}.0",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = DbrTextDark
                            )
                        }
                    }

                    if (review.googleMapsProfile.isNotBlank()) {
                        Spacer(modifier = Modifier.height(5.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "ম্যাপ আইডি: ${review.googleMapsProfile.take(28)}...",
                                fontSize = 9.5.sp,
                                color = Color(0xFF64748B),
                                maxLines = 1
                            )

                            Text(
                                text = "কন্ট্রিবিউট করুন ↗",
                                color = Color(0xFF1D4ED8),
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .clickable {
                                        try {
                                            val url = if (review.googleMapsProfile.startsWith("http")) {
                                                review.googleMapsProfile
                                            } else {
                                                "https://www.google.com/maps/search/?api=1&query=${Uri.encode(review.googleMapsProfile)}"
                                            }
                                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                            context.startActivity(intent)
                                        } catch (e: Exception) {
                                            // Fallback
                                        }
                                    }
                                    .padding(2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ----------------- PUBLIC REVIEW SUBMISSION DIALOG -----------------

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PublicReviewDialog(
    servicesList: List<String>,
    onDismiss: () -> Unit,
    onSubmit: (name: String, carModel: String, rating: Int, serviceTaken: String, comment: String, googleMapsProfile: String, avatarUrl: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var carModel by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(5) }
    var selectedService by remember { mutableStateOf(servicesList.firstOrNull() ?: "ডেন্ট পেইন্ট") }
    var comment by remember { mutableStateOf("") }
    var googleMapsProfile by remember { mutableStateOf("https://maps.google.com/contrib/dbr_bogra") }
    var avatarUrl by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val ratingLabels = mapOf(
        5 to "অসাধারণ! সম্পূর্ণ সন্তুষ্ট ⭐⭐⭐⭐⭐",
        4 to "খুবই ভালো সার্ভিস ⭐⭐⭐⭐",
        3 to "মোটামুটি ভালো ⭐⭐⭐",
        2 to "আরও উন্নতির সুযোগ আছে ⭐⭐",
        1 to "ভালো লাগেনি ⭐"
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("public_review_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(DbrOrangeLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.RateReview,
                                contentDescription = null,
                                tint = DbrOrange,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "গুগল ম্যাপ রেটিং ও রিভিউ দিন",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = DbrTextDark
                            )
                            Text(
                                text = "অরজিনাল গুগল ম্যাপ আইডি সহ কন্ট্রিবিউশন",
                                fontSize = 10.5.sp,
                                color = DbrTeal,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = DbrTextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Star Rating Picker
                Text(
                    text = "সার্ভিসের মান কেমন লেগেছে? *",
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = DbrTextDark
                )
                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    (1..5).forEach { starIndex ->
                        IconButton(
                            onClick = { rating = starIndex },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "$starIndex Star",
                                tint = if (starIndex <= rating) DbrGold else Color(0xFFD0DCD6),
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }

                Text(
                    text = ratingLabels[rating] ?: "",
                    fontSize = 11.5.sp,
                    color = DbrOrangeDark,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Customer Name (Optional / Default to সম্মানিত গ্রাহক)
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it; errorMessage = null },
                    label = { Text("আপনার নাম (ঐচ্ছিক)") },
                    placeholder = { Text("যেমন: ইঞ্জিনিয়ার হাসিবুল / কাস্টমার") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Review Comment (Primary Input Required by Buyer)
                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it; errorMessage = null },
                    label = { Text("আপনার মন্তব্য বা মতামত লিখুন *") },
                    placeholder = { Text("DBR কার ওয়ার্কশপের কাজের মান ও আপনার বাস্তব অভিজ্ঞতা সম্পর্কে লিখুন...") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 4,
                    maxLines = 6,
                    shape = RoundedCornerShape(12.dp)
                )

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = errorMessage!!,
                        color = DbrDangerRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Submit Button
                Button(
                    onClick = {
                        if (comment.isBlank()) {
                            errorMessage = "অনুগ্রহ করে আপনার মূল্যবান মন্তব্য লিখুন।"
                        } else {
                            val finalName = if (name.isBlank()) "সম্মানিত গ্রাহক" else name.trim()
                            onSubmit(finalName, "সম্মানিত গ্রাহক", rating, "কার কেয়ার সার্ভিস", comment.trim(), "https://maps.google.com/?cid=dbr_bogra", "")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DbrOrange,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("submit_public_review_button")
                ) {
                    Icon(imageVector = Icons.Default.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Submit Review (রিভিউ জমা দিন)",
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
