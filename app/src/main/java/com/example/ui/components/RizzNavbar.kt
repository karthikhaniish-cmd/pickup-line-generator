package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.NeonPink
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.PrimaryGradient
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun RizzTopBar(
    onGenerateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(DarkSurface)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(PrimaryGradient),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "⚡", fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "RIZZAI",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(NeonPink.copy(alpha = 0.2f))
                            .padding(horizontal = 5.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = "AI WINGMAN",
                            color = NeonPink,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Text(
                    text = "Smart Pickup Line Engine",
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(PrimaryGradient)
                .clickable { onGenerateClick() }
                .padding(horizontal = 14.dp, vertical = 8.dp)
                .testTag("topbar_generate_button"),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Generate ✨",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun RizzBottomNav(
    currentScreen: String,
    favoritesCount: Int,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .fillMaxWidth()
            .border(width = 0.5.dp, color = DarkCardBorder, shape = RoundedCornerShape(0.dp)),
        containerColor = DarkSurface,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = currentScreen == "home",
            onClick = { onNavigate("home") },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = NeonViolet,
                indicatorColor = NeonViolet,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            ),
            modifier = Modifier.testTag("nav_home")
        )

        NavigationBarItem(
            selected = currentScreen == "generator",
            onClick = { onNavigate("generator") },
            icon = { Icon(Icons.Default.AutoAwesome, contentDescription = "Generator") },
            label = { Text("Generator", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = NeonViolet,
                indicatorColor = NeonViolet,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            ),
            modifier = Modifier.testTag("nav_generator")
        )

        NavigationBarItem(
            selected = currentScreen == "favorites",
            onClick = { onNavigate("favorites") },
            icon = {
                if (favoritesCount > 0) {
                    BadgedBox(badge = {
                        Badge(
                            containerColor = NeonPink,
                            contentColor = Color.White
                        ) {
                            Text("$favoritesCount")
                        }
                    }) {
                        Icon(Icons.Default.Favorite, contentDescription = "Favorites")
                    }
                } else {
                    Icon(Icons.Default.Favorite, contentDescription = "Favorites")
                }
            },
            label = { Text("Favorites", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = NeonPink,
                indicatorColor = NeonPink,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            ),
            modifier = Modifier.testTag("nav_favorites")
        )

        NavigationBarItem(
            selected = currentScreen == "history",
            onClick = { onNavigate("history") },
            icon = { Icon(Icons.Default.History, contentDescription = "History") },
            label = { Text("History", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = NeonViolet,
                indicatorColor = NeonViolet,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            ),
            modifier = Modifier.testTag("nav_history")
        )

        NavigationBarItem(
            selected = currentScreen == "about",
            onClick = { onNavigate("about") },
            icon = { Icon(Icons.Default.Info, contentDescription = "About") },
            label = { Text("About", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = NeonViolet,
                indicatorColor = NeonViolet,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            ),
            modifier = Modifier.testTag("nav_about")
        )
    }
}
