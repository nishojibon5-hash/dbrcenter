package com.example.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SiteSettingsEntity
import com.example.ui.theme.*

@Composable
fun DbrFooter(
    settings: SiteSettingsEntity,
    onCallNumber: (String) -> Unit,
    onSocialClick: (String) -> Unit,
    isDesktop: Boolean = false
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF04241F))
            .padding(
                horizontal = if (isDesktop) 28.dp else 16.dp,
                vertical = if (isDesktop) 24.dp else 18.dp
            )
            .testTag("dbr_footer")
    ) {
        if (isDesktop) {
            // Desktop 3-column footer layout
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Col 1: Logo & copyright
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    val footerLogo = settings.footerLogoUrl.ifBlank { settings.headerLogoUrl }
                    DbrLogo(
                        logoUrl = footerLogo,
                        isCompact = false
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "বগুড়ার সর্বাধুনিক ও বিশ্বাসযোগ্য মাল্টিব্র্যান্ড অটোমোবাইল সার্ভিস সেন্টার।",
                        color = Color.White.copy(alpha = 0.75f),
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }

                Spacer(modifier = Modifier.width(24.dp))

                // Col 2: Address
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = DbrOrange,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "আমাদের ঠিকানা",
                            color = DbrOrange,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "${settings.addressRoad}\n${settings.addressArea}",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }

                Spacer(modifier = Modifier.width(24.dp))

                // Col 3: Contact Numbers
                Column(
                    modifier = Modifier.weight(1.2f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = null,
                            tint = DbrOrange,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "যোগাযোগ করুন",
                            color = DbrOrange,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "${settings.phoneManager} (ম্যানেজার)",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 12.sp,
                        modifier = Modifier.clickable { onCallNumber(settings.phoneManager) }
                    )
                    Text(
                        text = "${settings.phoneChiefTech} (হেড টেকনিশিয়ান)",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 12.sp,
                        modifier = Modifier.clickable { onCallNumber(settings.phoneChiefTech) }
                    )
                    Text(
                        text = "${settings.phoneAccounts} (অ্যাকাউন্ট্যান্ট)",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 12.sp,
                        modifier = Modifier.clickable { onCallNumber(settings.phoneAccounts) }
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Col 4: Social & Links
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "আমাদের সাথে থাকুন",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SocialIconButton(Icons.Default.ThumbUp, "Facebook", Color(0xFF1877F2)) { onSocialClick("Facebook") }
                        SocialIconButton(Icons.Default.LocationOn, "Google Maps", DbrOrange) { onSocialClick("GoogleMap") }
                        SocialIconButton(Icons.Default.Chat, "WhatsApp", DbrWhatsAppGreen) { onSocialClick("WhatsApp") }
                    }
                }
            }
        } else {
            // Mobile layout
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Logo
                val footerLogo = settings.footerLogoUrl.ifBlank { settings.headerLogoUrl }
                DbrLogo(
                    logoUrl = footerLogo,
                    isCompact = true,
                    modifier = Modifier.padding(top = 4.dp)
                )

                // Address
                Column(
                    modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = DbrOrange,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "আমাদের ঠিকানা",
                            color = DbrOrange,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${settings.addressRoad}\n${settings.addressArea}",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 9.5.sp,
                        lineHeight = 12.5.sp
                    )
                }

                // Phone
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = null,
                            tint = DbrOrange,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "যোগাযোগ করুন",
                            color = DbrOrange,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${settings.phoneManager} (ম্যানেজার)",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 9.sp,
                        modifier = Modifier.clickable { onCallNumber(settings.phoneManager) }
                    )
                    Text(
                        text = "${settings.phoneChiefTech} (হেড টেকনিশিয়ান)",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 9.sp,
                        modifier = Modifier.clickable { onCallNumber(settings.phoneChiefTech) }
                    )
                    Text(
                        text = "${settings.phoneAccounts} (অ্যাকাউন্ট্যান্ট)",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 9.sp,
                        modifier = Modifier.clickable { onCallNumber(settings.phoneAccounts) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Mobile Social Action Row (Facebook, Google Maps, WhatsApp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = { onSocialClick("Facebook") },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1877F2)),
                        shape = RoundedCornerShape(16.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Icon(Icons.Default.ThumbUp, contentDescription = "Facebook", tint = Color(0xFF1877F2), modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("ফেসবুক পেজ", fontSize = 10.5.sp, color = Color.White)
                    }

                    OutlinedButton(
                        onClick = { onSocialClick("GoogleMap") },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DbrOrange),
                        shape = RoundedCornerShape(16.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Icon(Icons.Default.LocationOn, contentDescription = "Google Maps", tint = DbrOrange, modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("গুগল লোকেশন", fontSize = 10.5.sp, color = Color.White)
                    }

                    OutlinedButton(
                        onClick = { onSocialClick("WhatsApp") },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DbrWhatsAppGreen),
                        shape = RoundedCornerShape(16.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Icon(Icons.Default.Chat, contentDescription = "WhatsApp", tint = DbrWhatsAppGreen, modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("WhatsApp", fontSize = 10.5.sp, color = Color.White)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Divider(color = Color.White.copy(alpha = 0.12f), thickness = 0.8.dp)
        Spacer(modifier = Modifier.height(10.dp))

        // Bottom Bar: Copyright & "Drive Safe • Stay Happy"
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = settings.copyrightText,
                color = Color.White.copy(alpha = 0.65f),
                fontSize = if (isDesktop) 11.5.sp else 9.5.sp
            )

            Text(
                text = settings.footerSlogan,
                color = DbrGold,
                fontSize = if (isDesktop) 13.sp else 11.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun SocialIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    desc: String,
    tint: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.15f))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = desc,
            tint = tint,
            modifier = Modifier.size(16.dp)
        )
    }
}
