package com.example.ui.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkBg
import com.example.ui.theme.DarkCard
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.RizzViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(
    viewModel: RizzViewModel,
    modifier: Modifier = Modifier
) {
    val history by viewModel.history.collectAsState()
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val dateFormat = SimpleDateFormat("MMM d, h:mm a", Locale.getDefault())

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBg)
            .padding(16.dp)
            .testTag("history_screen")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Generation History 📜",
                    color = TextPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "${history.size} recent lines generated",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            }

            if (history.isNotEmpty()) {
                FilledTonalButton(
                    onClick = { viewModel.clearHistory() },
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = DarkCardBorder.copy(alpha = 0.5f),
                        contentColor = TextSecondary
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("clear_history_button")
                ) {
                    Icon(
                        Icons.Default.DeleteSweep,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Clear", fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (history.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(text = "📜", fontSize = 48.sp)
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "No pickup lines yet.",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Your generation timeline will appear here automatically.",
                        color = TextSecondary,
                        fontSize = 13.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = { viewModel.navigateTo("generator") },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonViolet),
                        modifier = Modifier.testTag("start_generating_button")
                    ) {
                        Icon(
                            Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Start Generating",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(history, key = { it.id }) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkCard),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = androidx.compose.ui.graphics.SolidColor(DarkCardBorder)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(NeonViolet.copy(alpha = 0.15f))
                                            .padding(horizontal = 8.dp, vertical = 3.dp)
                                    ) {
                                        Text(
                                            text = item.style,
                                            color = NeonViolet,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = item.language,
                                        color = TextMuted,
                                        fontSize = 11.sp
                                    )
                                }

                                Text(
                                    text = dateFormat.format(Date(item.timestamp)),
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )
                            }

                            if (!item.targetName.isNullOrBlank() || !item.situation.isNullOrBlank()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Target: ${item.targetName ?: "Unknown"} • Situation: ${item.situation ?: "Chat"}",
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "“${item.text}”",
                                color = TextPrimary,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium,
                                lineHeight = 22.sp
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                FilledTonalIconButton(
                                    onClick = { viewModel.deleteHistoryItem(item.id) },
                                    modifier = Modifier.size(36.dp),
                                    shape = CircleShape,
                                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                                        containerColor = DarkCardBorder.copy(alpha = 0.4f),
                                        contentColor = TextSecondary
                                    )
                                ) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Delete item",
                                        modifier = Modifier.size(15.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                FilledTonalIconButton(
                                    onClick = {
                                        clipboardManager.setText(AnnotatedString(item.text))
                                        viewModel.setToast("Copied! ✓")
                                    },
                                    modifier = Modifier.size(36.dp),
                                    shape = CircleShape,
                                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                                        containerColor = DarkCardBorder.copy(alpha = 0.4f),
                                        contentColor = TextPrimary
                                    )
                                ) {
                                    Icon(
                                        Icons.Default.ContentCopy,
                                        contentDescription = "Copy to clipboard",
                                        modifier = Modifier.size(15.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                FilledTonalIconButton(
                                    onClick = {
                                        shareHistoryItem(context, item.text)
                                    },
                                    modifier = Modifier.size(36.dp),
                                    shape = CircleShape,
                                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                                        containerColor = DarkCardBorder.copy(alpha = 0.4f),
                                        contentColor = TextPrimary
                                    )
                                ) {
                                    Icon(
                                        Icons.Default.Share,
                                        contentDescription = "Share line",
                                        modifier = Modifier.size(15.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun shareHistoryItem(context: Context, text: String) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(
            Intent.EXTRA_TEXT,
            "Check out this pickup line generated by RIZZAI 😎\n\n\"$text\""
        )
    }
    context.startActivity(Intent.createChooser(shareIntent, "Share Pickup Line via"))
}
