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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.*
import com.example.ui.viewmodel.ViewMode

@Composable
fun DbrLogo(
    modifier: Modifier = Modifier,
    logoUrl: String = "",
    isCompact: Boolean = false,
    isDarkText: Boolean = false
) {
    if (logoUrl.isNotBlank()) {
        AsyncImage(
            model = logoUrl,
            contentDescription = "DBR Complete Auto Centre Logo",
            contentScale = ContentScale.Fit,
            modifier = modifier
                .height(if (isCompact) 38.dp else 44.dp)
                .widthIn(max = if (isCompact) 180.dp else 220.dp)
                .padding(horizontal = 6.dp, vertical = 2.dp)
                .clickable { /* Brand click */ }
        )
    } else {
        Column(
            modifier = modifier
                .padding(horizontal = 6.dp)
                .clickable { /* Brand click */ },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "DBR",
                color = DbrOrange,
                fontSize = if (isCompact) 22.sp else 28.sp,
                fontWeight = FontWeight.Black,
                fontStyle = FontStyle.Italic,
                letterSpacing = 1.sp
            )
            Text(
                text = "CAR WORKSHOP",
                color = if (isDarkText) DbrTextDark else Color.White,
                fontSize = if (isCompact) 8.sp else 9.5.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            )
        }
    }
}

@Composable
fun MobileTopBar(
    onMenuClick: () -> Unit,
    onCallClick: () -> Unit,
    viewMode: ViewMode,
    onToggleViewMode: () -> Unit,
    onAdminClick: () -> Unit,
    currentLang: String = "bn",
    logoUrl: String = "",
    isDarkMode: Boolean = false,
    onToggleDarkMode: () -> Unit = {},
    onLanguageSelected: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isDarkMode) Color(0xFF091411) else DbrDarkTeal)
    ) {
        // Mode switch and Language/Theme switch helper bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(if (isDarkMode) Color(0xFF040A09) else Color(0xFF041E1A))
                .padding(horizontal = 12.dp, vertical = 3.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (viewMode == ViewMode.MOBILE) DbrOrange else Color.Transparent,
                    modifier = Modifier.clickable { if (viewMode != ViewMode.MOBILE) onToggleViewMode() }
                ) {
                    Text(
                        text = "📱 মোবাইল",
                        color = Color.White,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.5.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (viewMode == ViewMode.DESKTOP) DbrOrange else Color.Transparent,
                    modifier = Modifier.clickable { if (viewMode != ViewMode.DESKTOP) onToggleViewMode() }
                ) {
                    Text(
                        text = "🖥️ ডেক্সটপ",
                        color = Color.White,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.5.dp)
                    )
                }
            }

            // Dark/Light Mode and Language Switcher in Mobile Top Bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isDarkMode) DbrOrange else Color(0x33FFFFFF),
                    modifier = Modifier.clickable { onToggleDarkMode() }
                ) {
                    Text(
                        text = if (isDarkMode) "🌙 ডার্ক" else "☀️ লাইট",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.5.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (currentLang == "bn") DbrTeal else Color(0x33FFFFFF),
                    modifier = Modifier.clickable { onLanguageSelected("bn") }
                ) {
                    Text(
                        text = "🇧🇩 বাং",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = if (currentLang == "bn") FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.5.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (currentLang == "en") DbrTeal else Color(0x33FFFFFF),
                    modifier = Modifier.clickable { onLanguageSelected("en") }
                ) {
                    Text(
                        text = "🇬🇧 EN",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = if (currentLang == "en") FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.5.dp)
                    )
                }
            }
        }

        // Exact Mobile Header with Safe Area Symmetrical Fitting
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onMenuClick,
                modifier = Modifier
                    .size(40.dp)
                    .testTag("mobile_menu_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }

            DbrLogo(
                logoUrl = logoUrl,
                isCompact = false,
                modifier = Modifier.testTag("app_logo")
            )

            IconButton(
                onClick = onCallClick,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF0F766E))
                    .testTag("call_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Call",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun DesktopTopBar(
    currentTab: String,
    onTabSelected: (String) -> Unit,
    onCallClick: () -> Unit,
    viewMode: ViewMode,
    onToggleViewMode: () -> Unit,
    onAdminClick: () -> Unit,
    currentLang: String = "bn",
    logoUrl: String = "",
    isDarkMode: Boolean = false,
    onToggleDarkMode: () -> Unit = {},
    onLanguageSelected: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(if (isDarkMode) Color(0xFF101E1B) else Color.White)
            .border(width = 1.dp, color = if (isDarkMode) Color(0xFF1C362F) else DbrCardBorder)
    ) {
        // Mode Switch Header Strip
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(if (isDarkMode) Color(0xFF061512) else DbrDarkTeal)
                .padding(horizontal = 24.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "🔧 DBR COMPLETE AUTO CENTRE — বগুড়ার সর্বাধুনিক ও বিশ্বাসযোগ্য অটোমোবাইল সার্ভিসিং সেন্টার",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 12.sp
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (viewMode == ViewMode.MOBILE) DbrOrange else Color(0x33FFFFFF),
                    modifier = Modifier.clickable { if (viewMode != ViewMode.MOBILE) onToggleViewMode() }
                ) {
                    Text(
                        text = "📱 মোবাইল মোড",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (viewMode == ViewMode.DESKTOP) DbrOrange else Color(0x33FFFFFF),
                    modifier = Modifier.clickable { if (viewMode != ViewMode.DESKTOP) onToggleViewMode() }
                ) {
                    Text(
                        text = "🖥️ ডেক্সটপ মোড",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }
        }

        // Exact Desktop Navigation Bar with Symmetrical Screen Padding
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = DbrDarkTeal,
                modifier = Modifier.padding(2.dp)
            ) {
                DbrLogo(
                    logoUrl = logoUrl,
                    isCompact = true,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }

            // Desktop Nav Links
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val navItems = listOf(
                    "home" to "হোম",
                    "services" to "সেবা সমূহ",
                    "gallery" to "গ্যালারি",
                    "blog" to "ব্লগ ও টিপস",
                    "mechanics" to "আমাদের মেকানিক",
                    "reviews" to "গুগল রেটিং",
                    "about" to "আমাদের সম্পর্কে",
                    "contact" to "যোগাযোগ"
                )

                navItems.forEach { (key, label) ->
                    val isSelected = currentTab == key
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) DbrTeal else Color.Transparent,
                        modifier = Modifier.clickable { onTabSelected(key) }
                    ) {
                        Text(
                            text = label,
                            color = if (isSelected) Color.White else (if (isDarkMode) Color(0xFFCBD5E1) else DbrTextDark),
                            fontSize = 12.5.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Dark / Light Mode Switcher Pill
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (isDarkMode) Color(0xFF142421) else Color(0xFFF1F5F9),
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isDarkMode) Color(0xFF233F37) else Color(0xFFCBD5E1)),
                    modifier = Modifier.padding(end = 2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (!isDarkMode) DbrOrange else Color.Transparent,
                            modifier = Modifier.clickable { if (isDarkMode) onToggleDarkMode() }
                        ) {
                            Text(
                                text = "☀️ লাইট",
                                color = if (!isDarkMode) Color.White else Color(0xFF94A3B8),
                                fontSize = 11.5.sp,
                                fontWeight = if (!isDarkMode) FontWeight.Bold else FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (isDarkMode) DbrOrange else Color.Transparent,
                            modifier = Modifier.clickable { if (!isDarkMode) onToggleDarkMode() }
                        ) {
                            Text(
                                text = "🌙 ডার্ক",
                                color = if (isDarkMode) Color.White else DbrTextDark,
                                fontSize = 11.5.sp,
                                fontWeight = if (isDarkMode) FontWeight.Bold else FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                // Language Switcher Pills
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (isDarkMode) Color(0xFF142421) else Color(0xFFF1F5F9),
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isDarkMode) Color(0xFF233F37) else Color(0xFFCBD5E1)),
                    modifier = Modifier.padding(end = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (currentLang == "bn") DbrTeal else Color.Transparent,
                            modifier = Modifier.clickable { onLanguageSelected("bn") }
                        ) {
                            Text(
                                text = "🇧🇩 বাংলা",
                                color = if (currentLang == "bn") Color.White else DbrTextDark,
                                fontSize = 12.sp,
                                fontWeight = if (currentLang == "bn") FontWeight.Bold else FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (currentLang == "en") DbrTeal else Color.Transparent,
                            modifier = Modifier.clickable { onLanguageSelected("en") }
                        ) {
                            Text(
                                text = "🇬🇧 Eng",
                                color = if (currentLang == "en") Color.White else DbrTextDark,
                                fontSize = 12.sp,
                                fontWeight = if (currentLang == "en") FontWeight.Bold else FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }
                }

                // "কল করুন" Pill Button
                Button(
                    onClick = onCallClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0F766E),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(24.dp),
                    contentPadding = PaddingValues(horizontal = 18.dp, vertical = 10.dp),
                    modifier = Modifier.testTag("desktop_call_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (currentLang == "en") "Call Us" else "কল করুন",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}
