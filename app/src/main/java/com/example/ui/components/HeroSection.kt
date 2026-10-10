package com.example.ui.components

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.SiteSettingsEntity
import com.example.ui.theme.*

@Composable
fun HeroSection(
    settings: SiteSettingsEntity,
    onBookClick: () -> Unit,
    onWhatsAppClick: () -> Unit,
    onReviewClick: () -> Unit = {},
    isDesktop: Boolean = false
) {
    if (isDesktop) {
        // Desktop Wide Hero Layout with Workshop Building Background (Uploaded user screenshot)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp)
                .clip(RoundedCornerShape(20.dp))
        ) {
            val hasCustomBg = settings.heroDesktopBg.isNotBlank() && settings.heroDesktopBg != "workshop_building.jpg"
            if (hasCustomBg) {
                AsyncImage(
                    model = settings.heroDesktopBg,
                    contentDescription = "DBR Workshop Building",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.workshop_building),
                    contentDescription = "DBR Workshop Building",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )
            }

            // Neutral soft vignette on left ensures text and buttons are crisp while the car shop photo remains fresh, vibrant and crystal-clear
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.horizontalGradient(
                            0f to Color(0xCC000000),
                            0.38f to Color(0x99000000),
                            0.65f to Color(0x28000000),
                            1f to Color(0x00000000)
                        )
                    )
            )

            // Desktop Hero Content: Clean Spacious Column over background image without duplicate card
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.72f)
                    .padding(horizontal = 32.dp, vertical = 32.dp)
            ) {
                    // Badge & Rating Trust Pill
                    val displayBadge = if (settings.heroBadge.contains("কার ওয়ার্কশপ") || settings.heroBadge.isBlank()) "DBR COMPLETE AUTO CENTRE" else settings.heroBadge

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .width(32.dp)
                                    .height(5.dp)
                                    .clip(RoundedCornerShape(2.5.dp))
                                    .background(DbrOrange)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = displayBadge,
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.8.sp
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = DbrMintTeal,
                            modifier = Modifier.clickable { onReviewClick() }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = DbrGold, modifier = Modifier.size(15.dp))
                                Text(text = "৪.৯/৫ (পাবলিক রেটিং)", fontSize = 12.sp, color = DbrTeal, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // Main Bangla Headline
                    Text(
                        text = settings.heroTitleLine1,
                        color = Color.White,
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Black,
                        lineHeight = 44.sp
                    )
                    Text(
                        text = settings.heroTitleLine2,
                        color = Color.White,
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Black,
                        lineHeight = 44.sp
                    )
                    Text(
                        text = settings.heroTitleHighlight,
                        color = DbrOrange,
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Black,
                        lineHeight = 48.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = settings.heroDescription,
                        color = Color(0xFFE2E8F0),
                        fontSize = 14.sp,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Action Buttons
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = onBookClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DbrOrange,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(24.dp),
                            contentPadding = PaddingValues(horizontal = 22.dp, vertical = 14.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                            modifier = Modifier.testTag("hero_book_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "সেবা বুক করুন",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        OutlinedButton(
                            onClick = onWhatsAppClick,
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Color.White,
                                contentColor = DbrTextDark
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF0F766E)),
                            shape = RoundedCornerShape(24.dp),
                            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 14.dp),
                            modifier = Modifier.testTag("hero_whatsapp_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Chat,
                                contentDescription = null,
                                tint = Color(0xFF25D366),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "WhatsApp এ যোগাযোগ",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = DbrTextDark
                            )
                        }
                    }
            }
        }
    } else {
        // Mobile Stacked Hero Layout (Matches mobile screenshot exactly)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            val mobileBadge = if (settings.heroBadge.contains("কার ওয়ার্কশপ") || settings.heroBadge.isBlank()) {
                "DBR COMPLETE AUTO CENTRE"
            } else {
                settings.heroBadge
            }

            // Top Status & Rating Bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = DbrMintTeal,
                    modifier = Modifier.clickable { onReviewClick() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = DbrGold, modifier = Modifier.size(14.dp))
                        Text(text = "৪.৯/৫ (পাবলিক রেটিং)", fontSize = 11.5.sp, color = DbrTeal, fontWeight = FontWeight.Bold)
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = DbrOrangeLight
                ) {
                    Text(
                        text = "✓ প্রিমিয়াম অটো কেয়ার",
                        fontSize = 11.sp,
                        color = DbrOrangeDark,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            // Prominent, Large and Beautiful "DBR COMPLETE AUTO CENTRE" Heading
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(26.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(DbrOrange)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = mobileBadge,
                    color = DbrDarkTeal,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp
                )
            }

            // Main Bangla Headline
            Text(
                text = settings.heroTitleLine1,
                color = DbrTextDark,
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                lineHeight = 32.sp
            )
            Text(
                text = settings.heroTitleLine2,
                color = DbrTextDark,
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                lineHeight = 32.sp
            )
            Text(
                text = settings.heroTitleHighlight,
                color = DbrOrange,
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                lineHeight = 36.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = settings.heroDescription,
                color = DbrTextMuted,
                fontSize = 12.5.sp,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // CTA Buttons in Mobile (Full Width Rounded Pills)
            Button(
                onClick = onBookClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = DbrOrange,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(24.dp),
                contentPadding = PaddingValues(vertical = 12.dp, horizontal = 16.dp),
                modifier = Modifier
                    .fillMaxWidth(0.78f)
                    .testTag("hero_book_button_mobile")
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "সেবা বুক করুন",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = onWhatsAppClick,
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White,
                    contentColor = DbrTextDark
                ),
                border = androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFF0F766E)),
                shape = RoundedCornerShape(24.dp),
                contentPadding = PaddingValues(vertical = 12.dp, horizontal = 16.dp),
                modifier = Modifier
                    .fillMaxWidth(0.78f)
                    .testTag("hero_whatsapp_button_mobile")
            ) {
                Icon(
                    imageVector = Icons.Default.Chat,
                    contentDescription = null,
                    tint = Color(0xFF25D366),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "WhatsApp এ যোগাযোগ",
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = DbrTextDark
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Workshop Profile Image in 4:3 Aspect Ratio with Premium Border & Badges
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(4f / 3f)
                    .clip(RoundedCornerShape(18.dp))
                    .border(3.dp, DbrOrange, RoundedCornerShape(18.dp))
                    .shadow(12.dp, RoundedCornerShape(18.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.workshop_building),
                    contentDescription = "DBR Workshop Profile Building (4:3)",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // High-End Bengali Tag Overlay in bottom of 4:3 frame
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color(0xDD04211C))
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
                                text = "DBR কমপ্লিট অটো সেন্টার • বগুড়া",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "শহীদ জিয়া মেডিকেল রোড, শাকপালা",
                                color = Color(0xFFCBD5E1),
                                fontSize = 10.5.sp
                            )
                        }
                        Box(
                            modifier = Modifier
                                .background(DbrOrange, RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "৪:৩ ফ্রেম",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }
            }
        }
    }
}
