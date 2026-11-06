package com.example.ui

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.NorthWest
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.SystemUpdateAlt
import androidx.compose.material.icons.filled.Token
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.BuildConfig
import com.example.data.BookmarkEntity
import com.example.data.BrowserTab
import com.example.data.HistoryEntity
import com.example.data.PrivacyAndDisplaySettings
import com.example.data.SIGNATURE_FOOTER_TEXT
import com.example.data.WebDarkModeStyle
import com.example.engine.AppUpdateManager
import com.example.engine.ReleaseUpdateInfo
import com.example.ui.theme.AmberRoute
import com.example.ui.theme.EchoAvatarTeal
import com.example.ui.theme.EchoCardSurface
import com.example.ui.theme.EchoLavenderAccent
import com.example.ui.theme.EchoPillBorder
import com.example.ui.theme.EchoPitchBlack
import com.example.ui.theme.EchoSheetBackground
import com.example.ui.theme.EchoSubtleLabel
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.EmeraldShield
import com.example.ui.theme.TextPrimaryDark

enum class SegmentPosition {
    SINGLE,
    TOP,
    MIDDLE,
    BOTTOM
}

fun segmentedCardShape(position: SegmentPosition): Shape = when (position) {
    SegmentPosition.SINGLE -> RoundedCornerShape(24.dp)
    SegmentPosition.TOP -> RoundedCornerShape(
        topStart = 24.dp,
        topEnd = 24.dp,
        bottomStart = 6.dp,
        bottomEnd = 6.dp
    )
    SegmentPosition.MIDDLE -> RoundedCornerShape(6.dp)
    SegmentPosition.BOTTOM -> RoundedCornerShape(
        topStart = 6.dp,
        topEnd = 6.dp,
        bottomStart = 24.dp,
        bottomEnd = 24.dp
    )
}

@Composable
fun ComingSoonBadge() {
    Surface(
        shape = RoundedCornerShape(50),
        color = Color(0xFF282B36),
        modifier = Modifier.border(1.dp, EchoPillBorder, RoundedCornerShape(50))
    ) {
        Text(
            text = "Coming Soon",
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
            color = EchoLavenderAccent,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun SegmentedSettingRow(
    title: String,
    subtitle: String? = null,
    icon: ImageVector? = null,
    iconText: String? = null,
    position: SegmentPosition = SegmentPosition.SINGLE,
    isComingSoon: Boolean = false,
    trailingContent: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val shape = segmentedCardShape(position)
    Surface(
        shape = shape,
        color = EchoCardSurface,
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .clickable {
                if (isComingSoon) {
                    Toast.makeText(context, "$title — Coming Soon in AAA Browser!", Toast.LENGTH_SHORT).show()
                } else {
                    onClick?.invoke()
                }
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFFE2E4EC),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(18.dp))
            } else if (iconText != null) {
                Box(
                    modifier = Modifier.width(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = iconText,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFFE2E4EC),
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(18.dp))
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 17.sp),
                    color = TextPrimaryDark,
                    fontWeight = FontWeight.Medium
                )
                if (!subtitle.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = EchoSubtleLabel
                    )
                }
            }

            if (isComingSoon) {
                Spacer(modifier = Modifier.width(10.dp))
                ComingSoonBadge()
            } else if (trailingContent != null) {
                Spacer(modifier = Modifier.width(12.dp))
                trailingContent()
            }
        }
    }
}

@Composable
fun EchoSectionHeader(title: String) {
    Text(
        text = title.uppercase(),
        style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 0.8.sp),
        color = EchoSubtleLabel,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 6.dp, top = 12.dp, bottom = 6.dp)
    )
}

@Composable
fun EchoSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        thumbContent = {
            if (checked) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = EchoLavenderAccent,
                    modifier = Modifier.size(14.dp)
                )
            }
        },
        colors = SwitchDefaults.colors(
            checkedThumbColor = Color(0xFF282C3E),
            checkedTrackColor = EchoLavenderAccent,
            uncheckedThumbColor = Color(0xFF8E92A4),
            uncheckedTrackColor = Color(0xFF2A2C36)
        )
    )
}

/**
 * Top Bar for AAA Browser:
 * Bold "AAA Browser" title on left, History, Privacy Shield, Reader Mode, YouTube Music Mode, Android Auto, and "Y" Avatar on right.
 */
@Composable
fun EchoTopAppBar(
    title: String,
    isReaderModeActive: Boolean,
    isMusicModeActive: Boolean,
    onHistoryClick: () -> Unit,
    onPrivacyStatsClick: () -> Unit,
    onToggleReaderMode: () -> Unit,
    onToggleMusicMode: () -> Unit,
    onAndroidAutoClick: () -> Unit,
    onProfileAvatarClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(EchoPitchBlack)
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 26.sp),
            color = TextPrimaryDark,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            IconButton(
                onClick = onToggleReaderMode,
                modifier = Modifier.testTag("top_reader_mode_button")
            ) {
                Icon(
                    imageVector = Icons.Default.MenuBook,
                    contentDescription = "Reader Mode",
                    tint = if (isReaderModeActive) EmeraldShield else Color(0xFFD5D7E0),
                    modifier = Modifier.size(23.dp)
                )
            }

            IconButton(
                onClick = onToggleMusicMode,
                modifier = Modifier.testTag("top_music_mode_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Headphones,
                    contentDescription = "YouTube Ad-Free Music Mode",
                    tint = if (isMusicModeActive) ElectricCyan else Color(0xFFD5D7E0),
                    modifier = Modifier.size(23.dp)
                )
            }

            IconButton(
                onClick = onHistoryClick,
                modifier = Modifier.testTag("top_history_button")
            ) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = "Bookmarks & History",
                    tint = Color(0xFFD5D7E0),
                    modifier = Modifier.size(23.dp)
                )
            }

            IconButton(
                onClick = onPrivacyStatsClick,
                modifier = Modifier.testTag("top_privacy_stats_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                    contentDescription = "Privacy Shield Stats",
                    tint = Color(0xFFD5D7E0),
                    modifier = Modifier.size(23.dp)
                )
            }

            IconButton(
                onClick = onAndroidAutoClick,
                modifier = Modifier.testTag("top_car_console_button")
            ) {
                Icon(
                    imageVector = Icons.Default.DirectionsCar,
                    contentDescription = "Android Auto Console",
                    tint = Color(0xFFD5D7E0),
                    modifier = Modifier.size(23.dp)
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            // Circular "Y" Avatar Button
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(EchoAvatarTeal)
                    .clickable(onClick = onProfileAvatarClick)
                    .testTag("top_profile_avatar_button"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Y",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

enum class EchoPrimaryTab {
    HOME,
    BOOKMARKS
}

/**
 * Floating Bottom Bar designed per user's exact request:
 * - NO fake music player bar!
 * - Removed the Home and Library toggle buttons!
 * - Far Left: Circular Home Button (`nav_home_button`)
 * - Center: Floating Search & URL Capsule Bar (with Search Suggestions trigger, Reader Mode button, Voice Mic button)
 * - Far Right: Small, cute circular Info Button (`i`) (`page_info_circle_button`) that shows the Page's Info & Quick Bookmarks (replacing Speed Dial)!
 */
@Composable
fun EchoFloatingBottomBar(
    currentTab: BrowserTab,
    isReaderModeActive: Boolean,
    isMusicModeActive: Boolean,
    onHomeLeftClick: () -> Unit,
    onSearchBarClick: () -> Unit,
    onToggleReaderMode: () -> Unit,
    onToggleMusicMode: () -> Unit,
    onVoiceSearchClick: () -> Unit,
    onPageInfoRightClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // 1. Far Left: Circular Home Button
        Surface(
            shape = CircleShape,
            color = Color(0xFF131419).copy(alpha = 0.98f),
            modifier = Modifier
                .size(56.dp)
                .border(1.dp, EchoPillBorder, CircleShape)
                .clip(CircleShape)
                .clickable(onClick = onHomeLeftClick)
                .testTag("nav_home_button")
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // 2. Center: Floating Search & Browser Capsule Bar
        Surface(
            shape = RoundedCornerShape(32.dp),
            color = Color(0xFF131419).copy(alpha = 0.98f),
            modifier = Modifier
                .weight(1f)
                .height(56.dp)
                .border(1.dp, EchoPillBorder, RoundedCornerShape(32.dp))
                .clip(RoundedCornerShape(32.dp))
                .clickable(onClick = onSearchBarClick)
                .testTag("floating_search_circle_button")
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search or Type URL",
                    tint = ElectricCyan,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = currentTab.title.ifBlank { "Search Google or type URL" },
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = currentTab.url,
                        style = MaterialTheme.typography.labelSmall,
                        color = EchoSubtleLabel,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                IconButton(
                    onClick = onToggleReaderMode,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = "Toggle Reader Mode",
                        tint = if (isReaderModeActive) EmeraldShield else Color(0xFFD5D7E0),
                        modifier = Modifier.size(19.dp)
                    )
                }

                IconButton(
                    onClick = onToggleMusicMode,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Headphones,
                        contentDescription = "YouTube Ad-Free Music Mode",
                        tint = if (isMusicModeActive) ElectricCyan else Color(0xFFD5D7E0),
                        modifier = Modifier.size(19.dp)
                    )
                }

                IconButton(
                    onClick = onVoiceSearchClick,
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("omnibox_voice_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Voice Search",
                        tint = Color.White,
                        modifier = Modifier.size(19.dp)
                    )
                }
            }
        }

        // 3. Far Right: Small, cute circular Info Button ("i") replacing Speed Dial!
        Surface(
            shape = CircleShape,
            color = Color(0xFF131419).copy(alpha = 0.98f),
            modifier = Modifier
                .size(56.dp)
                .border(1.dp, EchoLavenderAccent.copy(alpha = 0.65f), CircleShape)
                .clip(CircleShape)
                .clickable(onClick = onPageInfoRightClick)
                .testTag("page_info_circle_button")
        ) {
            Box(contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(EchoLavenderAccent.copy(alpha = 0.18f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Page Info",
                        tint = EchoLavenderAccent,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

/**
 * Page Info Bottom Sheet opened when tapping the cute circular Info ("i") button on the far right!
 * Replaces Speed Dial with live Page Info, Reader Mode, YouTube Ad-Free Music Mode, and Quick Bookmarks.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EchoPageInfoSheet(
    currentTab: BrowserTab,
    settings: PrivacyAndDisplaySettings,
    bookmarks: List<BookmarkEntity>,
    isReaderModeActive: Boolean,
    isMusicModeActive: Boolean,
    onDismiss: () -> Unit,
    onToggleReaderMode: () -> Unit,
    onToggleMusicMode: () -> Unit,
    onReloadPage: () -> Unit,
    onGoBack: () -> Unit,
    onGoForward: () -> Unit,
    onBookmarkCurrentPage: () -> Unit,
    onFindInPage: (String) -> Unit = {},
    onFindNext: () -> Unit = {},
    onChangeTextZoom: (Int) -> Unit = {},
    onOpenBookmarkUrl: (String) -> Unit,
    onOpenFullSettings: () -> Unit
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var findQuery by remember { mutableStateOf("") }
    var currentZoomPercent by remember { mutableIntStateOf(100) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = EchoSheetBackground
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(EchoLavenderAccent.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = EchoLavenderAccent,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Page Info & Browser Tools",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "AAA Browser v${BuildConfig.VERSION_NAME} • Active Page Details",
                                style = MaterialTheme.typography.bodySmall,
                                color = EchoSubtleLabel
                            )
                        }
                    }

                    IconButton(onClick = onReloadPage) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reload Page",
                            tint = Color.White
                        )
                    }
                }
            }

            item { EchoSectionHeader("CURRENT PAGE INFO") }

            item {
                SegmentedSettingRow(
                    title = currentTab.title.ifBlank { "Active Web Page" },
                    subtitle = currentTab.url,
                    icon = if (currentTab.isSecureHttps) Icons.Default.Lock else Icons.Default.Language,
                    position = SegmentPosition.TOP,
                    trailingContent = {
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = if (currentTab.isSecureHttps) {
                                EmeraldShield.copy(alpha = 0.18f)
                            } else {
                                AmberRoute.copy(alpha = 0.18f)
                            }
                        ) {
                            Text(
                                text = if (currentTab.isSecureHttps) "HTTPS Secure" else "HTTP",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (currentTab.isSecureHttps) EmeraldShield else AmberRoute,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    },
                    onClick = {
                        val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                        clipboard.setPrimaryClip(android.content.ClipData.newPlainText("Clean URL", currentTab.url))
                        Toast.makeText(context, "Copied Clean URL (0 trackers)", Toast.LENGTH_SHORT).show()
                    }
                )
            }

            item {
                SegmentedSettingRow(
                    title = "Privacy Shield on This Page",
                    subtitle = "${currentTab.pageAdsBlocked} Ads blocked • ${currentTab.pageTrackersBlocked} Trackers stopped • ${settings.webDarkModeStyle.label}",
                    icon = Icons.Default.Security,
                    position = SegmentPosition.MIDDLE
                )
            }

            item {
                SegmentedSettingRow(
                    title = "Reader Mode",
                    subtitle = if (isReaderModeActive) "Active: Distraction-free article view" else "Tap to read article without page clutter",
                    icon = Icons.Default.MenuBook,
                    position = SegmentPosition.MIDDLE,
                    trailingContent = {
                        EchoSwitch(
                            checked = isReaderModeActive,
                            onCheckedChange = { onToggleReaderMode() }
                        )
                    },
                    onClick = onToggleReaderMode
                )
            }

            item {
                SegmentedSettingRow(
                    title = "YouTube Ad-Free Music Mode",
                    subtitle = "Play YouTube music like Spotify with 0 ads & hidden video",
                    icon = Icons.Default.Headphones,
                    position = SegmentPosition.BOTTOM,
                    trailingContent = {
                        EchoSwitch(
                            checked = isMusicModeActive,
                            onCheckedChange = { onToggleMusicMode() }
                        )
                    },
                    onClick = onToggleMusicMode
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Find in Page & Text Zoom Section
            item {
                EchoSectionHeader("FIND IN PAGE & TEXT ZOOM")
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = EchoCardSurface,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            TextField(
                                value = findQuery,
                                onValueChange = {
                                    findQuery = it
                                    onFindInPage(it)
                                },
                                placeholder = {
                                    Text("Find word on this page…", color = EchoSubtleLabel, style = MaterialTheme.typography.bodySmall)
                                },
                                singleLine = true,
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
                            OutlinedButton(
                                onClick = onFindNext,
                                shape = RoundedCornerShape(50)
                            ) {
                                Text("Next", color = ElectricCyan)
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Page Text Zoom: $currentZoomPercent%",
                                style = MaterialTheme.typography.labelLarge,
                                color = Color.White
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                listOf(85, 100, 125, 150).forEach { zoom ->
                                    Surface(
                                        shape = RoundedCornerShape(50),
                                        color = if (currentZoomPercent == zoom) EmeraldShield.copy(alpha = 0.22f) else EchoPitchBlack,
                                        modifier = Modifier
                                            .border(
                                                1.dp,
                                                if (currentZoomPercent == zoom) EmeraldShield else EchoPillBorder,
                                                RoundedCornerShape(50)
                                            )
                                            .clickable {
                                                currentZoomPercent = zoom
                                                onChangeTextZoom(zoom)
                                            }
                                    ) {
                                        Text(
                                            text = "$zoom%",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (currentZoomPercent == zoom) EmeraldShield else Color.White,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Quick Navigation & Share Clean Link Row
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onGoBack,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Back")
                    }
                    OutlinedButton(
                        onClick = onGoForward,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Forward")
                    }
                    OutlinedButton(
                        onClick = onBookmarkCurrentPage,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.BookmarkAdd, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Save")
                    }
                    OutlinedButton(
                        onClick = {
                            runCatching {
                                val sendIntent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(android.content.Intent.EXTRA_SUBJECT, currentTab.title)
                                    putExtra(android.content.Intent.EXTRA_TEXT, "${currentTab.title} — ${currentTab.url}")
                                }
                                context.startActivity(android.content.Intent.createChooser(sendIntent, "Share Clean Link"))
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Share")
                    }
                }
            }

            item { EchoSectionHeader("PINNED BOOKMARKS") }

            items(bookmarks.size) { index ->
                val bm = bookmarks[index]
                val pos = when {
                    bookmarks.size == 1 -> SegmentPosition.SINGLE
                    index == 0 -> SegmentPosition.TOP
                    index == bookmarks.lastIndex -> SegmentPosition.BOTTOM
                    else -> SegmentPosition.MIDDLE
                }
                SegmentedSettingRow(
                    title = bm.title,
                    subtitle = bm.displayDomain,
                    icon = Icons.Default.Bookmark,
                    position = pos,
                    onClick = {
                        onDismiss()
                        onOpenBookmarkUrl(bm.url)
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))
                SegmentedSettingRow(
                    title = "Settings & v1.1 System Update",
                    subtitle = "Open AAA Browser Settings & OTA Update Center",
                    icon = Icons.Default.Settings,
                    position = SegmentPosition.SINGLE,
                    onClick = {
                        onDismiss()
                        onOpenFullSettings()
                    }
                )
                Spacer(modifier = Modifier.height(14.dp))
                SignatureFooterCard()
            }
        }
    }
}

/**
 * Echo-styled Search & URL Omnibox Sheet with LIVE SEARCH SUGGESTIONS!
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EchoSearchOmniboxSheet(
    initialQuery: String,
    bookmarks: List<BookmarkEntity>,
    history: List<HistoryEntity>,
    onDismiss: () -> Unit,
    onSubmitUrlOrSearch: (String) -> Unit,
    onOpenYouTubeMusicSearch: (String) -> Unit,
    onTriggerVoiceSearch: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var query by remember(initialQuery) { mutableStateOf(initialQuery) }

    val builtInSuggestions = remember {
        listOf(
            "YouTube Ad-Free Music" to "https://m.youtube.com/results?search_query=latest+hindi+songs",
            "Google News India" to "https://news.google.com/?hl=en-IN&gl=IN&ceid=IN:en",
            "Wikipedia Encyclopedia" to "https://en.wikipedia.org",
            "Live Cricket Score India" to "https://www.google.com/search?q=live+cricket+score+india&hl=en-IN",
            "Google Maps Traffic Nearby" to "https://www.google.com/maps?hl=en-IN&gl=IN",
            "Netflix India" to "https://www.netflix.com",
            "Android Auto Settings" to "aegis://android_auto_settings"
        )
    }

    val liveSuggestions = remember(query, bookmarks, history) {
        val trimmed = query.trim()
        if (trimmed.isBlank()) {
            builtInSuggestions
        } else {
            val dynamicList = mutableListOf<Pair<String, String>>()
            dynamicList.add("Search Google for \"$trimmed\"" to trimmed)
            dynamicList.add("Play \"$trimmed\" in YouTube Music Mode (No Ads)" to "YT_MUSIC::$trimmed")
            dynamicList.add("$trimmed latest news India" to "$trimmed latest news India")
            dynamicList.add("$trimmed meaning & overview" to "$trimmed overview")
            if (!trimmed.contains(" ") && !trimmed.contains(".")) {
                dynamicList.add("Open https://www.$trimmed.com" to "https://www.$trimmed.com")
            }
            bookmarks.filter {
                it.title.contains(trimmed, ignoreCase = true) ||
                    it.displayDomain.contains(trimmed, ignoreCase = true)
            }.forEach { bm ->
                dynamicList.add("${bm.title} (${bm.displayDomain})" to bm.url)
            }
            history.filter {
                it.title.contains(trimmed, ignoreCase = true) ||
                    it.url.contains(trimmed, ignoreCase = true)
            }.take(3).forEach { h ->
                dynamicList.add("${h.title} • History" to h.url)
            }
            dynamicList
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = EchoSheetBackground
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                Text(
                    text = "Search or Enter Website",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                TextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = {
                        Text(
                            text = "Search Google, YouTube Music, or type URL…",
                            color = EchoSubtleLabel
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = ElectricCyan
                        )
                    },
                    trailingIcon = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (query.isNotEmpty()) {
                                IconButton(onClick = { query = "" }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Clear",
                                        tint = EchoSubtleLabel
                                    )
                                }
                            }
                            IconButton(onClick = onTriggerVoiceSearch) {
                                Icon(
                                    imageVector = Icons.Default.Mic,
                                    contentDescription = "Voice Search",
                                    tint = EchoLavenderAccent
                                )
                            }
                            IconButton(
                                onClick = {
                                    if (query.isNotBlank()) {
                                        onSubmitUrlOrSearch(query)
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = "Go",
                                    tint = EmeraldShield
                                )
                            }
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                    keyboardActions = KeyboardActions(
                        onGo = {
                            if (query.isNotBlank()) {
                                onSubmitUrlOrSearch(query)
                            }
                        }
                    ),
                    shape = RoundedCornerShape(28.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = EchoCardSurface,
                        unfocusedContainerColor = EchoCardSurface,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                        .testTag("omnibox_url_input")
                )
            }

            item {
                EchoSectionHeader(
                    if (query.isBlank()) "SEARCH SUGGESTIONS" else "LIVE SUGGESTIONS FOR \"${query.uppercase()}\""
                )
            }

            items(liveSuggestions.size) { index ->
                val (label, target) = liveSuggestions[index]
                val pos = when {
                    liveSuggestions.size == 1 -> SegmentPosition.SINGLE
                    index == 0 -> SegmentPosition.TOP
                    index == liveSuggestions.lastIndex -> SegmentPosition.BOTTOM
                    else -> SegmentPosition.MIDDLE
                }
                val isMusicSuggestion = target.startsWith("YT_MUSIC::")
                SegmentedSettingRow(
                    title = label,
                    subtitle = if (isMusicSuggestion) {
                        "Plays in Ad-Free Audio-Only Music Mode"
                    } else {
                        target
                    },
                    icon = if (isMusicSuggestion) Icons.Default.Headphones else Icons.Default.Search,
                    position = pos,
                    trailingContent = {
                        IconButton(
                            onClick = {
                                query = label.removePrefix("Search Google for \"").removeSuffix("\"")
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.NorthWest,
                                contentDescription = "Fill Search Query",
                                tint = EchoSubtleLabel,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    },
                    onClick = {
                        if (isMusicSuggestion) {
                            val songQuery = target.removePrefix("YT_MUSIC::")
                            onOpenYouTubeMusicSearch(songQuery)
                        } else {
                            onSubmitUrlOrSearch(target)
                        }
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
                SignatureFooterCard()
            }
        }
    }
}

/**
 * Distraction-Free Reader Mode Overlay for any webpage!
 */
@Composable
fun EchoReaderModeOverlay(
    pageTitle: String,
    pageUrl: String,
    extractedText: String,
    fallbackSummary: String,
    onSpeakAloud: (String) -> Unit,
    onCloseReaderMode: () -> Unit,
    modifier: Modifier = Modifier
) {
    var fontSizeSp by remember { mutableIntStateOf(18) }
    var readerThemeIndex by remember { mutableIntStateOf(0) } // 0 = AMOLED Black, 1 = Charcoal, 2 = Warm Sepia

    val bgColor = when (readerThemeIndex) {
        1 -> Color(0xFF14161D)
        2 -> Color(0xFF1C1813)
        else -> EchoPitchBlack
    }
    val textColor = when (readerThemeIndex) {
        2 -> Color(0xFFF5E6D3)
        else -> Color(0xFFEAECEF)
    }

    val bodyContent = remember(extractedText, fallbackSummary) {
        extractedText.ifBlank {
            fallbackSummary.ifBlank {
                "Reader Mode has formatted this page for distraction-free reading. Navigate to any article or blog post to view its clean text without ads, popups, or clutter."
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(bgColor)
            .testTag("reader_mode_overlay")
    ) {
        // Reader Mode Toolbar
        Surface(
            color = EchoCardSurface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = EmeraldShield,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Reader Mode",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    OutlinedButton(
                        onClick = { fontSizeSp = (fontSizeSp - 2).coerceAtLeast(14) },
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text("A-", color = Color.White)
                    }
                    OutlinedButton(
                        onClick = { fontSizeSp = (fontSizeSp + 2).coerceAtMost(28) },
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text("A+", color = Color.White)
                    }
                    OutlinedButton(
                        onClick = { readerThemeIndex = (readerThemeIndex + 1) % 3 },
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text(
                            text = when (readerThemeIndex) {
                                1 -> "Dark"
                                2 -> "Warm"
                                else -> "AMOLED"
                            },
                            color = ElectricCyan
                        )
                    }
                    IconButton(
                        onClick = { onSpeakAloud("$pageTitle. $bodyContent") }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = "Read Article Aloud",
                            tint = EmeraldShield
                        )
                    }
                    IconButton(onClick = onCloseReaderMode) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Exit Reader Mode",
                            tint = Color.White
                        )
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 22.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Text(
                    text = pageTitle.ifBlank { "Clean Article View" },
                    style = MaterialTheme.typography.headlineMedium.copy(fontSize = (fontSizeSp + 6).sp),
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$pageUrl • Ad-Free Reader Mode",
                    style = MaterialTheme.typography.labelMedium,
                    color = ElectricCyan
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            val paragraphs = bodyContent.split("\n\n").filter { it.isNotBlank() }
            items(paragraphs) { paragraph ->
                if (paragraph.startsWith("## ")) {
                    Text(
                        text = paragraph.removePrefix("## "),
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = (fontSizeSp + 2).sp),
                        color = ElectricCyan,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Text(
                        text = paragraph,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = fontSizeSp.sp,
                            lineHeight = (fontSizeSp + 9).sp
                        ),
                        color = textColor
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
                SignatureFooterCard()
                Spacer(modifier = Modifier.height(90.dp))
            }
        }
    }
}

/**
 * YouTube Ad-Free Music Mode (Spotify-style Audio-Only Controller):
 * Lets the user select any song from YouTube, strips 100% of ads, and hides the video
 * when listening so it plays like Spotify with zero ads!
 */
@Composable
fun YouTubeAdFreeMusicModeBar(
    currentTitle: String,
    currentUrl: String,
    hideVideoCanvas: Boolean,
    onToggleHideVideoCanvas: (Boolean) -> Unit,
    onSearchYouTubeSong: (String) -> Unit,
    onPlayPause: () -> Unit,
    onRewind10s: () -> Unit,
    onForward10s: () -> Unit,
    onNextTrack: () -> Unit,
    onCloseMusicMode: () -> Unit,
    modifier: Modifier = Modifier
) {
    var songQuery by remember { mutableStateOf("") }
    var isPlaying by remember { mutableStateOf(true) }

    Surface(
        color = Color(0xFF10131C),
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, ElectricCyan.copy(alpha = 0.45f), RoundedCornerShape(bottomStart = 22.dp, bottomEnd = 22.dp))
            .testTag("youtube_music_mode_bar")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf( EmeraldShield, ElectricCyan )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.GraphicEq,
                            contentDescription = null,
                            tint = Color(0xFF001F24),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "YouTube Music Mode • ZERO ADS",
                            style = MaterialTheme.typography.labelMedium,
                            color = EmeraldShield,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = currentTitle.ifBlank { "Select any song on YouTube below" },
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Toggle: Hide Video (Audio-Only like Spotify) vs Show Video to pick song
                FilledTonalButton(
                    onClick = { onToggleHideVideoCanvas(!hideVideoCanvas) },
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = if (hideVideoCanvas) EmeraldShield.copy(alpha = 0.22f) else EchoCardSurface,
                        contentColor = if (hideVideoCanvas) EmeraldShield else Color.White
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Icon(
                        imageVector = if (hideVideoCanvas) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (hideVideoCanvas) "Audio Only (Video Off)" else "Stop Video (Audio Only)",
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                IconButton(
                    onClick = onCloseMusicMode,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Exit Music Mode",
                        tint = EchoSubtleLabel,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Quick YouTube Song Search + Audio Transport Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = songQuery,
                    onValueChange = { songQuery = it },
                    placeholder = {
                        Text(
                            text = "Search song on YouTube (No Ads)…",
                            style = MaterialTheme.typography.bodySmall,
                            color = EchoSubtleLabel
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            if (songQuery.isNotBlank()) {
                                onSearchYouTubeSong(songQuery)
                            }
                        }
                    ),
                    shape = RoundedCornerShape(50),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = EchoCardSurface,
                        unfocusedContainerColor = EchoCardSurface,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    textStyle = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                )

                IconButton(
                    onClick = onRewind10s,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(EchoCardSurface)
                ) {
                    Icon(Icons.Default.FastRewind, contentDescription = "Rewind 10s", tint = Color.White, modifier = Modifier.size(18.dp))
                }

                IconButton(
                    onClick = {
                        isPlaying = !isPlaying
                        onPlayPause()
                    },
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(EmeraldShield)
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Play or Pause",
                        tint = Color(0xFF002116),
                        modifier = Modifier.size(22.dp)
                    )
                }

                IconButton(
                    onClick = onForward10s,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(EchoCardSurface)
                ) {
                    Icon(Icons.Default.FastForward, contentDescription = "Forward 10s", tint = Color.White, modifier = Modifier.size(18.dp))
                }

                IconButton(
                    onClick = onNextTrack,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(EchoCardSurface)
                ) {
                    Icon(Icons.Default.SkipNext, contentDescription = "Next Song", tint = ElectricCyan, modifier = Modifier.size(18.dp))
                }
            }

            // 1-Tap Quick Music Station Pills (Plays Ad-Free Audio on YouTube)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf("Punjabi Hits", "Arijit Singh", "Bollywood Lo-Fi", "Workout Mix").forEach { station ->
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = EchoCardSurface,
                        modifier = Modifier
                            .border(1.dp, EchoPillBorder, RoundedCornerShape(50))
                            .clickable { onSearchYouTubeSong("$station audio jukebox") }
                    ) {
                        Text(
                            text = station,
                            style = MaterialTheme.typography.labelSmall,
                            color = ElectricCyan,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Profile & Quick Menu Bottom Sheet matching Screenshot 5, with "Coming Soon" on unavailable items!
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EchoProfileQuickSheet(
    settings: PrivacyAndDisplaySettings,
    onDismiss: () -> Unit,
    onOpenAccountScreen: () -> Unit,
    onOpenPrivacyHub: () -> Unit,
    onOpenDarkModeSettings: () -> Unit,
    onToggleUniversalDarkMode: (Boolean) -> Unit,
    onToggleAdBlockAndSync: (Boolean) -> Unit,
    onOpenFullSettingsScreen: () -> Unit,
    onOpenSystemUpdate: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = EchoSheetBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            EchoSectionHeader("ACCOUNT")

            SegmentedSettingRow(
                title = "Yug Chhabra",
                subtitle = "chhabrayug37@gmail.com",
                icon = Icons.Default.AccountCircle,
                position = SegmentPosition.TOP,
                trailingContent = {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(EchoAvatarTeal),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Y",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                onClick = onOpenAccountScreen
            )

            SegmentedSettingRow(
                title = "AI Hub",
                subtitle = "AI-powered page summaries",
                iconText = "Ai",
                position = SegmentPosition.MIDDLE,
                isComingSoon = true
            )

            SegmentedSettingRow(
                title = "Extensions",
                subtitle = "Manage third-party browser plugins",
                icon = Icons.Default.Extension,
                position = SegmentPosition.BOTTOM,
                isComingSoon = true
            )

            Spacer(modifier = Modifier.height(6.dp))
            EchoSectionHeader("PREFERENCES")

            SegmentedSettingRow(
                title = "Universal Dark Mode",
                icon = Icons.Default.DarkMode,
                position = SegmentPosition.TOP,
                trailingContent = {
                    EchoSwitch(
                        checked = settings.webDarkModeStyle != WebDarkModeStyle.SITE_DEFAULT,
                        onCheckedChange = onToggleUniversalDarkMode
                    )
                }
            )

            SegmentedSettingRow(
                title = "YouTube Ad-Free Shield",
                icon = Icons.Default.Sync,
                position = SegmentPosition.BOTTOM,
                trailingContent = {
                    EchoSwitch(
                        checked = settings.adBlockEnabled,
                        onCheckedChange = onToggleAdBlockAndSync
                    )
                }
            )

            Spacer(modifier = Modifier.height(6.dp))
            EchoSectionHeader("APP")

            SegmentedSettingRow(
                title = "Settings",
                subtitle = "App preferences and v1.1 System Update",
                icon = Icons.Default.Settings,
                position = SegmentPosition.TOP,
                onClick = onOpenFullSettingsScreen,
                modifier = Modifier.testTag("sheet_open_full_settings")
            )

            SegmentedSettingRow(
                title = "About",
                icon = Icons.Default.Info,
                position = SegmentPosition.BOTTOM,
                trailingContent = {
                    Text(
                        text = "v${BuildConfig.VERSION_NAME}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = EmeraldShield,
                        fontWeight = FontWeight.Bold
                    )
                },
                onClick = onOpenSystemUpdate
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = SIGNATURE_FOOTER_TEXT,
                style = MaterialTheme.typography.bodyMedium,
                color = EchoSubtleLabel,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 18.dp)
            )
        }
    }
}

/**
 * Full-Screen Settings Page matching Screenshots 3 & 6, adapted for AAA Browser v1.1:
 * - All updates come here in Settings (v1.1 Update Card expanded and ready!)
 * - Unavailable items clearly marked with "Coming Soon" badge!
 */
@Composable
fun EchoFullSettingsScreen(
    settings: PrivacyAndDisplaySettings,
    releaseUpdateInfo: ReleaseUpdateInfo,
    onBack: () -> Unit,
    onOpenAccountScreen: () -> Unit,
    onOpenPrivacySheet: () -> Unit,
    onOpenAndroidAutoConsole: () -> Unit,
    onOpenPhoneAndroidAutoSettings: () -> Unit,
    onOpenGoogleMaps: () -> Unit,
    onOpenYouTubeMusicMode: () -> Unit,
    onClearHistoryAndCache: () -> Unit,
    onRestoreDefaultBookmarks: () -> Unit,
    onCheckForUpdates: (String) -> Unit,
    onDownloadUpdateApk: (String, String) -> Unit,
    onOpenReleasesInBrowser: (String) -> Unit
) {
    BackHandler(onBack = onBack)

    var searchQuery by remember { mutableStateOf("") }
    var showSystemUpdateDialog by remember { mutableStateOf(true) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(EchoPitchBlack)
            .windowInsetsPadding(WindowInsets.statusBars)
            .testTag("echo_full_settings_screen"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        item {
            IconButton(
                onClick = onBack,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Text(
                text = "Settings",
                style = MaterialTheme.typography.displayLarge.copy(fontSize = 36.sp),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        text = "Search settings or updates…",
                        color = EchoSubtleLabel,
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = EchoSubtleLabel
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(28.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = EchoCardSurface,
                    unfocusedContainerColor = EchoCardSurface,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 18.dp)
            )
        }

        // Group 1: System Update v1.1 (Prominent in Settings as requested: "Now all the updates should come on settings and this will be 1.1 update of this app")
        item {
            SegmentedSettingRow(
                title = "System update (v1.1)",
                subtitle = "AAA Browser 1.1 Update installed • Tap to manage updates",
                icon = Icons.Default.SystemUpdateAlt,
                position = SegmentPosition.SINGLE,
                trailingContent = {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = EmeraldShield.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = "v1.1 Active",
                            style = MaterialTheme.typography.labelSmall,
                            color = EmeraldShield,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                },
                onClick = { showSystemUpdateDialog = !showSystemUpdateDialog },
                modifier = Modifier.testTag("settings_system_update_row")
            )
        }

        item {
            AnimatedVisibility(visible = showSystemUpdateDialog) {
                SystemUpdateExpandedCard(
                    settings = settings,
                    releaseUpdateInfo = releaseUpdateInfo,
                    onCheckForUpdates = onCheckForUpdates,
                    onDownloadUpdateApk = onDownloadUpdateApk,
                    onOpenReleasesInBrowser = onOpenReleasesInBrowser
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Group 2: Account & AI Hub (with Coming Soon badge)
        item {
            SegmentedSettingRow(
                title = "Account",
                subtitle = "Manage login and integrations",
                icon = Icons.Default.AccountCircle,
                position = SegmentPosition.TOP,
                onClick = onOpenAccountScreen
            )
        }
        item {
            SegmentedSettingRow(
                title = "AI Hub",
                subtitle = "AI-powered page summaries and smart tools",
                iconText = "Ai",
                position = SegmentPosition.BOTTOM,
                isComingSoon = true
            )
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Group 3: Browsing Summary
        item {
            SegmentedSettingRow(
                title = "Browsing Summary",
                subtitle = "${settings.totalAdsBlocked} Ads blocked • ${settings.totalTrackersBlocked} Trackers stopped",
                icon = Icons.Default.History,
                position = SegmentPosition.SINGLE,
                onClick = onOpenPrivacySheet
            )
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Group 4: Appearance, YouTube Music Mode (No Ads), Android Auto, Browse Together (Coming Soon)
        item {
            SegmentedSettingRow(
                title = "Appearance & Reader Mode",
                subtitle = "Themes, colors, and universal dark mode (${settings.webDarkModeStyle.label})",
                icon = Icons.Default.Palette,
                position = SegmentPosition.TOP,
                onClick = onOpenPrivacySheet
            )
        }
        item {
            SegmentedSettingRow(
                title = "YouTube Music Mode (No Ads)",
                subtitle = "Listen to YouTube music like Spotify with video hidden",
                icon = Icons.Default.Headphones,
                position = SegmentPosition.MIDDLE,
                onClick = onOpenYouTubeMusicMode
            )
        }
        item {
            SegmentedSettingRow(
                title = "Android Auto Console",
                subtitle = "Audio-only YouTube in car, voice search & Google Maps",
                icon = Icons.Default.DirectionsCar,
                position = SegmentPosition.MIDDLE,
                onClick = onOpenAndroidAutoConsole
            )
        }
        item {
            SegmentedSettingRow(
                title = "Browse Together",
                subtitle = "Sync tabs and sessions with friends",
                icon = Icons.Default.People,
                position = SegmentPosition.BOTTOM,
                isComingSoon = true
            )
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Group 5: Content, Privacy, Storage, Backup and restore (Matching Screenshot 6)
        item {
            SegmentedSettingRow(
                title = "Content & Language",
                subtitle = "Default Language: English (India) [en-IN]",
                icon = Icons.Default.Language,
                position = SegmentPosition.TOP,
                onClick = onOpenPrivacySheet
            )
        }
        item {
            SegmentedSettingRow(
                title = "Privacy & Ad Shield",
                subtitle = "History, ad-blocking, and tracking protection",
                icon = Icons.Default.Security,
                position = SegmentPosition.MIDDLE,
                onClick = onOpenPrivacySheet
            )
        }
        item {
            SegmentedSettingRow(
                title = "Storage",
                subtitle = "Clear browsing history and cache",
                icon = Icons.Default.Storage,
                position = SegmentPosition.MIDDLE,
                onClick = onClearHistoryAndCache
            )
        }
        item {
            SegmentedSettingRow(
                title = "Backup and restore",
                subtitle = "Export and import browser data",
                icon = Icons.Default.CloudDownload,
                position = SegmentPosition.BOTTOM,
                isComingSoon = true
            )
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Group 6: Supported Links (Android Auto Settings), Google Maps, About
        item {
            SegmentedSettingRow(
                title = "Supported Links (Android Auto)",
                subtitle = "Redirect directly to Android Auto settings in phone",
                icon = Icons.Default.Link,
                position = SegmentPosition.TOP,
                onClick = onOpenPhoneAndroidAutoSettings
            )
        }
        item {
            SegmentedSettingRow(
                title = "Google Maps Navigation",
                subtitle = "Open live route search & turn-by-turn navigation",
                icon = Icons.Default.Map,
                position = SegmentPosition.MIDDLE,
                onClick = onOpenGoogleMaps
            )
        }
        item {
            SegmentedSettingRow(
                title = "About AAA Browser",
                subtitle = "Version ${BuildConfig.VERSION_NAME} (Build ${BuildConfig.VERSION_CODE}) • All updates in Settings",
                icon = Icons.Default.Info,
                position = SegmentPosition.BOTTOM,
                onClick = { showSystemUpdateDialog = true }
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            SignatureFooterCard()
            Spacer(modifier = Modifier.height(36.dp))
        }
    }
}

@Composable
private fun SystemUpdateExpandedCard(
    settings: PrivacyAndDisplaySettings,
    releaseUpdateInfo: ReleaseUpdateInfo,
    onCheckForUpdates: (String) -> Unit,
    onDownloadUpdateApk: (String, String) -> Unit,
    onOpenReleasesInBrowser: (String) -> Unit
) {
    var repoSlugInput by remember(settings.updateRepositorySlug) {
        mutableStateOf(settings.updateRepositorySlug)
    }

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFF151722),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .border(1.dp, EmeraldShield.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "AAA Browser v1.1 Update Center (OTA)",
                style = MaterialTheme.typography.titleMedium,
                color = EmeraldShield,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Installed: ${releaseUpdateInfo.currentVersion} • Latest: ${releaseUpdateInfo.latestVersionTag}",
                style = MaterialTheme.typography.bodySmall,
                color = ElectricCyan
            )
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = EchoCardSurface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "What's New in Update 1.1:",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = releaseUpdateInfo.releaseNotes,
                        style = MaterialTheme.typography.bodySmall,
                        color = EchoSubtleLabel
                    )
                }
            }
            Text(
                text = releaseUpdateInfo.statusMessage,
                style = MaterialTheme.typography.bodySmall,
                color = EmeraldShield
            )

            OutlinedTextField(
                value = repoSlugInput,
                onValueChange = { repoSlugInput = it },
                label = { Text("GitHub Repository (owner/repo)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { onCheckForUpdates(repoSlugInput) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Check New Version", color = ElectricCyan)
                }
                Button(
                    onClick = {
                        onDownloadUpdateApk(
                            releaseUpdateInfo.apkDownloadUrl,
                            releaseUpdateInfo.releasesPageUrl
                        )
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EmeraldShield,
                        contentColor = Color(0xFF002116)
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Update APK", fontWeight = FontWeight.Bold)
                }
            }

            Text(
                text = "Open GitHub Releases Page in Browser →",
                style = MaterialTheme.typography.labelLarge,
                color = ElectricCyan,
                modifier = Modifier
                    .clickable {
                        val slug = AppUpdateManager.normalizeRepoSlug(repoSlugInput)
                        onOpenReleasesInBrowser("https://github.com/$slug/releases")
                    }
                    .padding(vertical = 4.dp)
            )
        }
    }
}

/**
 * Full-Screen Account Page matching Screenshot 2, with "Coming Soon" on unavailable items!
 */
@Composable
fun EchoAccountScreen(
    settings: PrivacyAndDisplaySettings,
    onBack: () -> Unit,
    onToggleAdBlock: (Boolean) -> Unit,
    onToggleTrackerProtection: (Boolean) -> Unit,
    onOpenPhoneAndroidAutoSettings: () -> Unit,
    onOpenGoogleMaps: () -> Unit,
    onOpenSystemUpdate: () -> Unit
) {
    BackHandler(onBack = onBack)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(EchoPitchBlack)
            .windowInsetsPadding(WindowInsets.statusBars)
            .testTag("echo_account_screen"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 14.dp)
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Account",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        item { EchoSectionHeader("SWITCH ACCOUNTS") }

        item {
            SegmentedSettingRow(
                title = "Yug Chhabra",
                subtitle = "chhabrayug37@gmail.com",
                position = SegmentPosition.TOP,
                trailingContent = {
                    OutlinedButton(
                        onClick = onBack,
                        shape = RoundedCornerShape(50)
                    ) {
                        Text("Active", color = Color.White)
                    }
                }
            )
        }
        item {
            SegmentedSettingRow(
                title = "Add another account",
                icon = Icons.Default.Add,
                position = SegmentPosition.BOTTOM,
                isComingSoon = true
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        item { EchoSectionHeader("LOGIN WITH TOKEN") }

        item {
            SegmentedSettingRow(
                title = "Tap to show token",
                icon = Icons.Default.Token,
                position = SegmentPosition.SINGLE,
                isComingSoon = true
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        item { EchoSectionHeader("BROWSER & CONTENT") }

        item {
            SegmentedSettingRow(
                title = "Built-in Ad & Video Shield",
                subtitle = "Block YouTube video ads & web trackers",
                icon = Icons.Default.Security,
                position = SegmentPosition.TOP,
                trailingContent = {
                    EchoSwitch(
                        checked = settings.adBlockEnabled,
                        onCheckedChange = onToggleAdBlock
                    )
                }
            )
        }
        item {
            SegmentedSettingRow(
                title = "Auto sync with account",
                subtitle = "Cloud sync bookmarks across devices",
                icon = Icons.Default.Sync,
                position = SegmentPosition.BOTTOM,
                isComingSoon = true
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        item { EchoSectionHeader("INTEGRATIONS") }

        item {
            SegmentedSettingRow(
                title = "Android Auto Settings",
                subtitle = "Redirect to Android Auto settings in phone",
                icon = Icons.Default.DirectionsCar,
                position = SegmentPosition.TOP,
                onClick = onOpenPhoneAndroidAutoSettings
            )
        }
        item {
            SegmentedSettingRow(
                title = "Google Maps",
                subtitle = "Connected for live turn-by-turn navigation",
                icon = Icons.Default.Map,
                position = SegmentPosition.MIDDLE,
                onClick = onOpenGoogleMaps
            )
        }
        item {
            SegmentedSettingRow(
                title = "Cloud Music & Social Sync",
                subtitle = "External account integrations",
                icon = Icons.Default.MusicNote,
                position = SegmentPosition.BOTTOM,
                isComingSoon = true
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            SignatureFooterCard()
            Spacer(modifier = Modifier.height(36.dp))
        }
    }
}
