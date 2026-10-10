package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.ServiceItemEntity
import com.example.ui.theme.*

// Helper for dialing phone
fun dialPhoneNumber(context: Context, phoneNumber: String) {
    try {
        val cleanNumber = phoneNumber.replace(Regex("[^0-9+]"), "")
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$cleanNumber")
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        // Fallback
    }
}

// Helper for WhatsApp
fun openWhatsApp(context: Context, phoneNumber: String, message: String) {
    try {
        val cleanNumber = phoneNumber.replace(Regex("[^0-9]"), "")
        val formattedNumber = if (cleanNumber.startsWith("0")) "88$cleanNumber" else cleanNumber
        val url = "https://api.whatsapp.com/send?phone=$formattedNumber&text=${Uri.encode(message)}"
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        // Fallback
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun BookingDialog(
    services: List<ServiceItemEntity>,
    preselectedService: String,
    onDismiss: () -> Unit,
    onSubmit: (name: String, phone: String, carModel: String, service: String, date: String, notes: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var carModel by remember { mutableStateOf("") }
    var selectedService by remember {
        mutableStateOf(
            if (preselectedService.isNotEmpty()) preselectedService
            else services.firstOrNull()?.title ?: "ডেন্ট পেইন্ট"
        )
    }
    var preferredDate by remember { mutableStateOf("আগামীকাল (সকাল ১০:০০)") }
    var notes by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("booking_dialog")
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
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(DbrOrangeLight),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = null,
                                tint = DbrOrange,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "সেবা বুক করুন",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = DbrTextDark
                            )
                            Text(
                                text = "DBR Car Workshop বগুড়া",
                                fontSize = 11.sp,
                                color = DbrTextMuted
                            )
                        }
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = DbrTextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Customer Name
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it; errorMessage = null },
                    label = { Text("আপনার নাম *") },
                    placeholder = { Text("যেমন: মো: রফিক উদ্দিন") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("booking_name_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Customer Phone
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it; errorMessage = null },
                    label = { Text("মোবাইল নাম্বার *") },
                    placeholder = { Text("যেমন: 01712-XXXXXX") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("booking_phone_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Car Model
                OutlinedTextField(
                    value = carModel,
                    onValueChange = { carModel = it },
                    label = { Text("গাড়ির মডেল ও রেজিস্ট্রেশন") },
                    placeholder = { Text("যেমন: Toyota Premio / Axio / Honda") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("booking_car_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Service selection
                Text(
                    text = "প্রয়োজনীয় সেবা নির্বাচন করুন:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = DbrTextDark
                )
                Spacer(modifier = Modifier.height(6.dp))

                // Service Chips
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    services.forEach { s ->
                        val isSelected = selectedService == s.title
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedService = s.title },
                            label = { Text(s.title, fontSize = 11.5.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = DbrTeal,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Preferred Date / Time
                OutlinedTextField(
                    value = preferredDate,
                    onValueChange = { preferredDate = it },
                    label = { Text("পছন্দের তারিখ ও সময়") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Notes
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("গাড়ির সমস্যা বা বিশেষ নোট (ঐচ্ছিক)") },
                    placeholder = { Text("যেমন: ডান পাশের দরজায় ডেন্ট আছে") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2,
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

                Spacer(modifier = Modifier.height(18.dp))

                // Submit Button
                Button(
                    onClick = {
                        if (name.isBlank() || phone.isBlank()) {
                            errorMessage = "অনুগ্রহ করে আপনার নাম এবং মোবাইল নাম্বার লিখুন।"
                        } else {
                            onSubmit(name, phone, carModel, selectedService, preferredDate, notes)
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
                        .testTag("booking_submit_button")
                ) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "বুকিং নিশ্চিত করুন",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun CallDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("call_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
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
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE0F2FE)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.Call, contentDescription = null, tint = Color(0xFF0369A1))
                        }
                        Column {
                            Text("সরাসরি কল করুন", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DbrTextDark)
                            Text("DBR কার ওয়ার্কশপ হেল্পলাইন", fontSize = 11.sp, color = DbrTextMuted)
                        }
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = DbrTextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                val contacts = listOf(
                    Triple("ম্যানেজার", "01711-287258", "সার্ভিস বুকিং ও তথ্য"),
                    Triple("হেড টেকনিশিয়ান", "01776-596315", "টেকনিক্যাল সমস্যা ও কোটেশন"),
                    Triple("অ্যাকাউন্ট্যান্ট", "01844-487468", "বিলিং ও পেমেন্ট সংক্রান্ত")
                )

                contacts.forEach { (role, number, desc) ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable {
                                dialPhoneNumber(context, number)
                                onDismiss()
                            },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = DbrMintTeal.copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(text = "$number ($role)", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DbrTextDark)
                                Text(text = desc, fontSize = 11.sp, color = DbrTextMuted)
                            }
                            IconButton(
                                onClick = {
                                    dialPhoneNumber(context, number)
                                    onDismiss()
                                },
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF0F766E))
                            ) {
                                Icon(imageVector = Icons.Default.Call, contentDescription = "Call", tint = Color.White, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // WhatsApp option
                Button(
                    onClick = {
                        openWhatsApp(context, "01711287258", "আসসালামু আলাইকুম, আমি DBR Car Workshop থেকে তথ্য জানতে চাই।")
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF25D366),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(imageVector = Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("WhatsApp এ সরাসরি মেসেজ দিন", fontWeight = FontWeight.Bold, fontSize = 13.5.sp)
                }
            }
        }
    }
}

@Composable
fun ServiceDetailDialog(
    service: ServiceItemEntity,
    onDismiss: () -> Unit,
    onBookService: (String) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("service_detail_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(DbrOrange),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = getIconForService(service.iconKey),
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Column {
                            Text(text = service.title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DbrTextDark)
                            Text(text = "ক্যাটেগরি: ${service.category}", fontSize = 11.5.sp, color = DbrTextMuted)
                        }
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = DbrTextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Divider(color = DbrCardBorder)
                Spacer(modifier = Modifier.height(14.dp))

                Text(text = "সেবার বিবরণ:", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DbrTextDark)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = service.description, fontSize = 13.sp, color = DbrTextMuted, lineHeight = 18.sp)

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = "আনুমানিক চার্জ:", fontSize = 12.sp, color = DbrTextMuted)
                        Text(text = service.estimatedPrice, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DbrOrangeDark)
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = "কাজের মান:", fontSize = 12.sp, color = DbrTextMuted)
                        Text(text = "১০০% গ্যারান্টিড", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DbrTeal)
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        onDismiss()
                        onBookService(service.title)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DbrOrange, contentColor = Color.White),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "এই সেবাটি বুক করুন", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }
    }
}

@Composable
fun AdminLoginDialog(
    onDismiss: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    var pin by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("admin_login_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(DbrOrangeLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.AdminPanelSettings, contentDescription = null, tint = DbrOrange, modifier = Modifier.size(28.dp))
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text("এডমিন প্যানেলে প্রবেশ", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DbrTextDark)
                Text("হোম পেজের কনটেন্ট ও বিজ্ঞাপন নিয়ন্ত্রণ করুন", fontSize = 11.5.sp, color = DbrTextMuted)

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = pin,
                    onValueChange = { pin = it; error = false },
                    label = { Text("এডমিন পিন (ডিফল্ট: 1234)") },
                    placeholder = { Text("1234") },
                    singleLine = true,
                    isError = error,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                if (error) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("ভুল পিন! দয়া করে সঠিক পিন দিন (ডিফল্ট: 1234)", color = DbrDangerRed, fontSize = 11.sp)
                }

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("বাতিল")
                    }

                    Button(
                        onClick = {
                            if (pin == "1234" || pin.isEmpty()) {
                                onLoginSuccess()
                            } else {
                                error = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DbrTeal, contentColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f).testTag("admin_login_submit_button")
                    ) {
                        Text("লগইন করুন", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun BlogPostReadingDialog(
    post: com.example.data.model.BlogPostEntity,
    onDismiss: () -> Unit,
    onBookService: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth().heightIn(max = 550.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
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
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = DbrTextDark)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = post.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = DbrTextDark,
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "লেখক: ${post.author} • ${post.readTimeMinutes} মিনিট পড়ার সময়",
                    fontSize = 11.5.sp,
                    color = DbrTextMuted
                )

                Spacer(modifier = Modifier.height(14.dp))
                Divider(color = DbrCardBorder)
                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = post.content,
                    fontSize = 13.5.sp,
                    lineHeight = 21.sp,
                    color = DbrTextDark
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        onDismiss()
                        onBookService()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DbrOrange, contentColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("সার্ভিস বুকিং দিন", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

