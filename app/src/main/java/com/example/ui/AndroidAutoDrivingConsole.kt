package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.EvStation
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SettingsApplications
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.data.ANDROID_AUTO_SETTINGS_URI
import com.example.data.BookmarkEntity
import com.example.data.PrivacyAndDisplaySettings
import com.example.ui.theme.AmberRoute
import com.example.ui.theme.EchoCardSurface
import com.example.ui.theme.EchoPitchBlack
import com.example.ui.theme.EchoSubtleLabel
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.EmeraldShield
import com.example.ui.theme.MapsGreen
import com.example.ui.theme.ObsidianBackground
import com.example.ui.theme.ObsidianCardElevated
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.OutlineDark
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondaryDark

@Composable
fun AndroidAutoDrivingConsole(
    bookmarks: List<BookmarkEntity>,
    settings: PrivacyAndDisplaySettings,
    currentPageTitle: String,
    currentPageUrl: String,
    latestPageSummary: String,
    systemPromptBanner: String,
    audioOnlyHideVideo: Boolean = true,
    onToggleAudioOnlyHideVideo: (Boolean) -> Unit = {},
    onPlayYouTubeMusicInBackground: (String) -> Unit = {},
    onMediaPlayPause: () -> Unit = {},
    onMediaRewind10s: () -> Unit = {},
    onMediaForward10s: () -> Unit = {},
    onMediaNextTrack: () -> Unit = {},
    onExitCarMode: () -> Unit,
    onOpenBookmarkInBrowser: (String) -> Unit,
    onOpenPhoneAndroidAutoSettings: () -> Unit,
    onOpenGoogleMapsInBrowser: (String) -> Unit,
    onLaunchExternalGoogleMaps: (String) -> Unit,
    onTriggerVoiceCommand: () -> Unit,
    onSpeakTextInEnglishIndia: (String) -> Unit,
    onToggleDrivingMotionSafety: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onExitCarMode)

    var carMusicQuery by remember { mutableStateOf("") }
    var isCarAudioPlaying by remember { mutableStateOf(true) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(EchoPitchBlack)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            )
            .testTag("android_auto_console"),
        contentPadding = PaddingValues(18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Top Car Head-Unit Bar
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = EchoCardSurface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.5.dp, AmberRoute.copy(alpha = 0.65f), RoundedCornerShape(24.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(AmberRoute.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DirectionsCar,
                                    contentDescription = "Android Auto Console",
                                    tint = AmberRoute,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = "ANDROID AUTO DRIVING CONSOLE",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = AmberRoute,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "AAA Browser v1.1 • Audio-Only YouTube & Maps Ready",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondaryDark
                                )
                            }
                        }

                        Button(
                            onClick = onExitCarMode,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ObsidianSurface,
                                contentColor = TextPrimaryDark
                            ),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .height(50.dp)
                                .testTag("exit_car_mode_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Return to Browser"
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Browser UI")
                        }
                    }

                    // Direct Phone Android Auto Settings Button
                    Button(
                        onClick = onOpenPhoneAndroidAutoSettings,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ElectricCyan.copy(alpha = 0.18f),
                            contentColor = ElectricCyan
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .border(1.dp, ElectricCyan.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                            .testTag("open_phone_android_auto_settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.SettingsApplications,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Open Android Auto Settings on Phone",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // 2. YouTube Audio-Only Car Mode (No Video Shown, Music Plays in Background with 0 Ads!)
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = EchoCardSurface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.5.dp, EmeraldShield.copy(alpha = 0.75f), RoundedCornerShape(24.dp))
                    .testTag("car_youtube_audio_only_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(EmeraldShield.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Headphones,
                                    contentDescription = null,
                                    tint = EmeraldShield,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "YouTube Audio-Only Mode (No Video • No Ads)",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Video is stopped/hidden for safe driving while YouTube music plays in background",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = EmeraldShield
                                )
                            }
                        }

                        Switch(
                            checked = audioOnlyHideVideo,
                            onCheckedChange = onToggleAudioOnlyHideVideo,
                            colors = SwitchDefaults.colors(checkedThumbColor = EmeraldShield)
                        )
                    }

                    // Active Track / Browser Audio Status Box
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = EchoPitchBlack,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.VisibilityOff,
                                contentDescription = null,
                                tint = ElectricCyan,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = currentPageTitle.ifBlank { "YouTube Background Audio Ready" },
                                    style = MaterialTheme.typography.titleSmall,
                                    color = Color.White,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = if (audioOnlyHideVideo) {
                                        "Video Stopped • Listening to Audio Only (0 Ads)"
                                    } else {
                                        "Video Display Enabled • Tap switch to hide video"
                                    },
                                    style = MaterialTheme.typography.bodySmall,
                                    color = EchoSubtleLabel
                                )
                            }
                        }
                    }

                    // Search & Play any YouTube Song in Audio-Only Car Mode
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TextField(
                            value = carMusicQuery,
                            onValueChange = { carMusicQuery = it },
                            placeholder = {
                                Text(
                                    text = "Search song on YouTube (plays audio only)…",
                                    color = EchoSubtleLabel,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(
                                onSearch = {
                                    if (carMusicQuery.isNotBlank()) {
                                        onPlayYouTubeMusicInBackground(carMusicQuery)
                                    }
                                }
                            ),
                            shape = RoundedCornerShape(50),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = EchoPitchBlack,
                                unfocusedContainerColor = EchoPitchBlack,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            modifier = Modifier.weight(1f)
                        )

                        Button(
                            onClick = {
                                onPlayYouTubeMusicInBackground(
                                    carMusicQuery.ifBlank { "latest bollywood & punjabi hits" }
                                )
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = EmeraldShield,
                                contentColor = Color(0xFF002116)
                            ),
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.height(50.dp)
                        ) {
                            Icon(Icons.Default.GraphicEq, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Play Audio", fontWeight = FontWeight.Bold)
                        }
                    }

                    // Large Car Audio Transport Buttons (Controls background YouTube WebView without leaving Car Console!)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onMediaRewind10s,
                            modifier = Modifier
                                .size(54.dp)
                                .clip(CircleShape)
                                .background(EchoPitchBlack)
                        ) {
                            Icon(Icons.Default.FastRewind, contentDescription = "Rewind 10s", tint = Color.White)
                        }

                        IconButton(
                            onClick = {
                                isCarAudioPlaying = !isCarAudioPlaying
                                onMediaPlayPause()
                            },
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(EmeraldShield)
                        ) {
                            Icon(
                                imageVector = if (isCarAudioPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = "Play or Pause Audio",
                                tint = Color(0xFF002116),
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        IconButton(
                            onClick = onMediaForward10s,
                            modifier = Modifier
                                .size(54.dp)
                                .clip(CircleShape)
                                .background(EchoPitchBlack)
                        ) {
                            Icon(Icons.Default.FastForward, contentDescription = "Forward 10s", tint = Color.White)
                        }

                        IconButton(
                            onClick = onMediaNextTrack,
                            modifier = Modifier
                                .size(54.dp)
                                .clip(CircleShape)
                                .background(EchoPitchBlack)
                        ) {
                            Icon(Icons.Default.SkipNext, contentDescription = "Next Song", tint = ElectricCyan)
                        }
                    }
                }
            }
        }

        // 3. Hands-Free Voice Search & Read Page Aloud Bar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF062C24)),
                    modifier = Modifier
                        .weight(1f)
                        .height(104.dp)
                        .border(2.dp, EmeraldShield, RoundedCornerShape(22.dp))
                        .clickable { onTriggerVoiceCommand() }
                        .testTag("car_voice_command_tile")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(EmeraldShield),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Mic,
                                contentDescription = "Voice Search",
                                tint = Color(0xFF002116),
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Voice Search",
                                style = MaterialTheme.typography.titleLarge,
                                color = TextPrimaryDark,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "English (India) Voice",
                                style = MaterialTheme.typography.bodyMedium,
                                color = EmeraldShield
                            )
                        }
                    }
                }

                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF052936)),
                    modifier = Modifier
                        .weight(1f)
                        .height(104.dp)
                        .border(2.dp, ElectricCyan, RoundedCornerShape(22.dp))
                        .clickable {
                            onSpeakTextInEnglishIndia(
                                "Reading page in English India: $currentPageTitle. $latestPageSummary"
                            )
                        }
                        .testTag("car_read_page_aloud_tile")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(ElectricCyan),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                contentDescription = "Read Page Aloud",
                                tint = Color(0xFF001F24),
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Read Page Aloud",
                                style = MaterialTheme.typography.titleLarge,
                                color = TextPrimaryDark,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "English (India) Audio",
                                style = MaterialTheme.typography.bodyMedium,
                                color = ElectricCyan
                            )
                        }
                    }
                }
            }
        }

        // 4. Google Maps Turn-by-Turn Driving Bar
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = EchoCardSurface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.5.dp, MapsGreen.copy(alpha = 0.7f), RoundedCornerShape(22.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Map,
                                contentDescription = null,
                                tint = MapsGreen,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Google Maps Car Navigation",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = TextPrimaryDark
                                )
                                Text(
                                    text = "One-tap destination routing while driving",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondaryDark
                                )
                            }
                        }

                        Button(
                            onClick = { onLaunchExternalGoogleMaps("Google Maps") },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MapsGreen,
                                contentColor = Color(0xFF03220C)
                            ),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.height(50.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Navigation, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Turn-by-Turn App", fontWeight = FontWeight.Bold)
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        CarMapsActionButton(
                            title = "Open Maps Web",
                            icon = Icons.Default.Map,
                            onClick = {
                                onOpenGoogleMapsInBrowser("")
                                onExitCarMode()
                            },
                            modifier = Modifier.weight(1f)
                        )
                        CarMapsActionButton(
                            title = "Fuel Nearby",
                            icon = Icons.Default.LocalGasStation,
                            onClick = {
                                onOpenGoogleMapsInBrowser("Fuel stations near me")
                                onExitCarMode()
                            },
                            modifier = Modifier.weight(1f)
                        )
                        CarMapsActionButton(
                            title = "EV Chargers",
                            icon = Icons.Default.EvStation,
                            onClick = {
                                onOpenGoogleMapsInBrowser("EV charging stations near me")
                                onExitCarMode()
                            },
                            modifier = Modifier.weight(1f)
                        )
                        CarMapsActionButton(
                            title = "Food & Rest",
                            icon = Icons.Default.Restaurant,
                            onClick = {
                                onOpenGoogleMapsInBrowser("Highway restaurants near me")
                                onExitCarMode()
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // 5. Large Car Launcher Tiles for the 4 Bookmarks (YouTube, Google, Netflix, Android Auto settings)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "CAR DASHBOARD BOOKMARKS",
                    style = MaterialTheme.typography.labelLarge,
                    color = ElectricCyan
                )
                val rows = bookmarks.chunked(2)
                rows.forEach { pair ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        pair.forEach { bookmark ->
                            val badgeColor = Color(bookmark.badgeColorHex)
                            val isAutoSettings = bookmark.url == ANDROID_AUTO_SETTINGS_URI ||
                                bookmark.displayDomain.contains("Android Auto", ignoreCase = true)
                            val icon = when {
                                isAutoSettings -> Icons.Default.SettingsApplications
                                bookmark.displayDomain.contains("youtube", true) -> Icons.Default.PlayArrow
                                bookmark.displayDomain.contains("google", true) -> Icons.Default.Search
                                bookmark.displayDomain.contains("netflix", true) -> Icons.Default.Tv
                                else -> Icons.Default.Shield
                            }
                            Card(
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(containerColor = EchoCardSurface),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(92.dp)
                                    .border(1.5.dp, badgeColor.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                                    .clickable {
                                        if (isAutoSettings) {
                                            onOpenPhoneAndroidAutoSettings()
                                        } else {
                                            onOpenBookmarkInBrowser(bookmark.url)
                                            onExitCarMode()
                                        }
                                    }
                                    .testTag("car_bookmark_${bookmark.displayDomain}")
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(52.dp)
                                            .clip(CircleShape)
                                            .background(badgeColor.copy(alpha = 0.22f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = icon,
                                            contentDescription = bookmark.title,
                                            tint = badgeColor,
                                            modifier = Modifier.size(28.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = bookmark.title,
                                            style = MaterialTheme.typography.titleLarge,
                                            color = TextPrimaryDark,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = bookmark.displayDomain,
                                            style = MaterialTheme.typography.labelMedium,
                                            color = ElectricCyan,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }
                        if (pair.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        // 6. Signature Footer at the bottom of the Android Auto Driving Console page
        item {
            SignatureFooterCard()
        }
    }
}

@Composable
private fun CarMapsActionButton(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = ObsidianCardElevated,
        modifier = modifier
            .height(74.dp)
            .border(1.dp, OutlineDark, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = MapsGreen,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = TextPrimaryDark,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
