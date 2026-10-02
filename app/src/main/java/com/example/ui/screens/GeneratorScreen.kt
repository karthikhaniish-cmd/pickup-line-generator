package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CookingLoader
import com.example.ui.components.RizzCard
import com.example.ui.theme.DarkBg
import com.example.ui.theme.DarkCard
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.RizzViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GeneratorScreen(
    viewModel: RizzViewModel,
    modifier: Modifier = Modifier
) {
    val name by viewModel.name.collectAsState()
    val situation by viewModel.situation.collectAsState()
    val language by viewModel.language.collectAsState()
    val style by viewModel.style.collectAsState()
    val confidence by viewModel.confidence.collectAsState()
    val count by viewModel.count.collectAsState()

    val lines by viewModel.currentLines.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isDemoMode by viewModel.isDemoMode.collectAsState()
    val favorites by viewModel.favorites.collectAsState()

    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    var situationMenuExpanded by remember { mutableStateOf(false) }

    val favTexts = remember(favorites) { favorites.map { it.text }.toSet() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBg)
            .verticalScroll(scrollState)
            .padding(16.dp)
            .testTag("generator_screen")
    ) {
        // Heading
        Text(
            text = "Generate Pickup Lines 🔥",
            color = TextPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.Black
        )
        Text(
            text = "Customize the vibe and let AI write the rizz",
            color = TextSecondary,
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Demo Mode Badge
        if (isDemoMode) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(NeonAmber.copy(alpha = 0.12f))
                    .border(1.dp, NeonAmber.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = null,
                        tint = NeonAmber,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Demo Mode: Powered by curated native rizz engine",
                        color = NeonAmber,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Form Container
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(DarkCard)
                .border(1.dp, DarkCardBorder, RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            // Field 1: Name (Optional)
            Text(
                text = "Their Name (Optional)",
                color = TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { viewModel.updateName(it) },
                placeholder = { Text("Enter their name... (e.g., Rithanya)", color = TextMuted) },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = null, tint = TextMuted)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("name_input"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonViolet,
                    unfocusedBorderColor = DarkCardBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedContainerColor = DarkSurface,
                    unfocusedContainerColor = DarkSurface
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Field 2: Situation
            Text(
                text = "Situation / Context",
                color = TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Box {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(DarkSurface)
                        .border(1.dp, DarkCardBorder, RoundedCornerShape(12.dp))
                        .clickable { situationMenuExpanded = true }
                        .padding(horizontal = 14.dp, vertical = 14.dp)
                        .testTag("situation_dropdown"),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = situation,
                        color = TextPrimary,
                        fontSize = 14.sp
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Select situation",
                        tint = NeonViolet
                    )
                }

                DropdownMenu(
                    expanded = situationMenuExpanded,
                    onDismissRequest = { situationMenuExpanded = false },
                    modifier = Modifier
                        .background(DarkSurface)
                        .border(1.dp, DarkCardBorder, RoundedCornerShape(8.dp))
                ) {
                    viewModel.situations.forEach { sitOption ->
                        DropdownMenuItem(
                            text = { Text(sitOption, color = TextPrimary) },
                            onClick = {
                                viewModel.updateSituation(sitOption)
                                situationMenuExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Field 3: Language
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Language",
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Tanglish recommended 🔥",
                    color = NeonPink,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                viewModel.languages.forEach { lang ->
                    val isSelected = language == lang
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) NeonViolet else DarkSurface)
                            .border(
                                1.dp,
                                if (isSelected) NeonViolet else DarkCardBorder,
                                RoundedCornerShape(10.dp)
                            )
                            .clickable { viewModel.updateLanguage(lang) }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                            .testTag("lang_$lang")
                    ) {
                        Text(
                            text = lang,
                            color = if (isSelected) Color.White else TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Field 4: Style
            Text(
                text = "Style / Mood",
                color = TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                viewModel.styles.forEach { st ->
                    val isSelected = style == st
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) NeonPink else DarkSurface)
                            .border(
                                1.dp,
                                if (isSelected) NeonPink else DarkCardBorder,
                                RoundedCornerShape(10.dp)
                            )
                            .clickable { viewModel.updateStyle(st) }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                            .testTag("style_$st")
                    ) {
                        Text(
                            text = st,
                            color = if (isSelected) Color.White else TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Field 5: Confidence Level
            Text(
                text = "Confidence Level",
                color = TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                viewModel.confidenceLevels.forEach { conf ->
                    val isSelected = confidence == conf
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) NeonViolet else DarkSurface)
                            .border(
                                1.dp,
                                if (isSelected) NeonViolet else DarkCardBorder,
                                RoundedCornerShape(10.dp)
                            )
                            .clickable { viewModel.updateConfidence(conf) }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = conf,
                            color = if (isSelected) Color.White else TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Field 6: Number of Lines
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Number of Lines",
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(3, 5, 10).forEach { cnt ->
                        val isSelected = count == cnt
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) NeonViolet else DarkSurface)
                                .border(
                                    1.dp,
                                    if (isSelected) NeonViolet else DarkCardBorder,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { viewModel.updateCount(cnt) }
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "$cnt",
                                color = if (isSelected) Color.White else TextSecondary,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Generate Button + Surprise Me
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { viewModel.generateLines() },
                    enabled = !isLoading,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .testTag("generate_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NeonViolet,
                        disabledContainerColor = NeonViolet.copy(alpha = 0.5f)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isLoading) "Generating..." else "Generate Rizz 🔥",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                OutlinedButton(
                    onClick = { viewModel.surpriseMe() },
                    enabled = !isLoading,
                    modifier = Modifier
                        .height(50.dp)
                        .testTag("generator_surprise_me"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = NeonPink),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = androidx.compose.ui.graphics.SolidColor(NeonPink)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Casino,
                        contentDescription = null,
                        tint = NeonPink,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "🎲",
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Results Section
        Text(
            text = "Generated Results",
            color = TextPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (isLoading) {
            CookingLoader()
        } else if (lines.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(DarkCard)
                    .border(1.dp, DarkCardBorder, RoundedCornerShape(16.dp))
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "✨", fontSize = 36.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Ready to start?",
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Configure details above and hit 'Generate Rizz'!",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                }
            }
        } else {
            lines.forEach { line ->
                val isFav = favTexts.contains(line.text)
                RizzCard(
                    line = line,
                    isFavorite = isFav,
                    onToggleFavorite = { viewModel.toggleFavorite(line, isFav) },
                    onCopy = { viewModel.setToast("Copied! ✓") },
                    onRegenerate = { viewModel.generateLines() }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
