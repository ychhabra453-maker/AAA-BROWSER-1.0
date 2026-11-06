package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Tab
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.data.BlockedEventEntity
import com.example.data.BookmarkEntity
import com.example.data.BrowserTab
import com.example.data.HistoryEntity
import com.example.data.PrivacyAndDisplaySettings
import com.example.data.WebDarkModeStyle
import com.example.engine.HindiEnglishVoiceEngine
import com.example.ui.theme.AmberRoute
import com.example.ui.theme.CoralAlert
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.EmeraldShield
import com.example.ui.theme.MapsGreen
import com.example.ui.theme.ObsidianBackground
import com.example.ui.theme.ObsidianCardElevated
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.OutlineDark
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondaryDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyShieldAndDarkModeSheet(
    settings: PrivacyAndDisplaySettings,
    releaseUpdateInfo: com.example.engine.ReleaseUpdateInfo,
    currentTab: BrowserTab,
    blockedEvents: List<BlockedEventEntity>,
    onDismiss: () -> Unit,
    onCheckForUpdates: (String) -> Unit,
    onDownloadAndInstallUpdate: (apkUrl: String, releasesUrl: String) -> Unit,
    onOpenReleasesInBrowser: (String) -> Unit,
    onToggleAdBlock: (Boolean) -> Unit,
    onToggleTrackerProtection: (Boolean) -> Unit,
    onToggleStripParams: (Boolean) -> Unit,
    onToggleBlockThirdPartyCookies: (Boolean) -> Unit,
    onToggleAntiFingerprinting: (Boolean) -> Unit,
    onToggleHttpsUpgrade: (Boolean) -> Unit,
    onSelectDarkModeStyle: (WebDarkModeStyle) -> Unit,
    onToggleDesktopSite: (Boolean) -> Unit,
    onClearBlockedLog: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var repoSlugInput by remember(settings.updateRepositorySlug) {
        mutableStateOf(settings.updateRepositorySlug)
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = ObsidianSurface
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = EmeraldShield,
                            modifier = Modifier.size(26.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "AAA Browser Settings & Update",
                                style = MaterialTheme.typography.titleLarge,
                                color = TextPrimaryDark
                            )
                            Text(
                                text = "On this page: ${currentTab.pageAdsBlocked} Ads • ${currentTab.pageTrackersBlocked} Trackers blocked",
                                style = MaterialTheme.typography.bodySmall,
                                color = EmeraldShield
                            )
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextSecondaryDark)
                    }
                }
            }

            // App Update Center Card
            item {
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = ObsidianCardElevated,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.5.dp, EmeraldShield.copy(alpha = 0.6f), RoundedCornerShape(18.dp))
                        .testTag("settings_app_update_card")
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = null,
                                    tint = EmeraldShield,
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "App Update Center (OTA)",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = TextPrimaryDark,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Installed: ${releaseUpdateInfo.currentVersion} • Latest: ${releaseUpdateInfo.latestVersionTag}",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = ElectricCyan
                                    )
                                }
                            }
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = EmeraldShield.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = if (releaseUpdateInfo.isChecking) "CHECKING…" else "OTA READY",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = EmeraldShield,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Text(
                            text = releaseUpdateInfo.statusMessage,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondaryDark
                        )

                        OutlinedTextField(
                            value = repoSlugInput,
                            onValueChange = { repoSlugInput = it },
                            label = { Text("GitHub Update Repository (owner/repo)") },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("update_repo_slug_input")
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { onCheckForUpdates(repoSlugInput) },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(46.dp)
                                    .testTag("check_updates_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = null,
                                    tint = ElectricCyan,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Check Update", color = ElectricCyan)
                            }

                            Button(
                                onClick = {
                                    onDownloadAndInstallUpdate(
                                        releaseUpdateInfo.apkDownloadUrl,
                                        releaseUpdateInfo.releasesPageUrl
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = EmeraldShield,
                                    contentColor = Color(0xFF002116)
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(46.dp)
                                    .testTag("download_apk_update_button")
                            ) {
                                Text(
                                    text = "Update App APK",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Text(
                            text = "Open GitHub Releases Page in Browser →",
                            style = MaterialTheme.typography.labelLarge,
                            color = ElectricCyan,
                            modifier = Modifier
                                .clickable {
                                    val slug = com.example.engine.AppUpdateManager.normalizeRepoSlug(repoSlugInput)
                                    onOpenReleasesInBrowser("https://github.com/$slug/releases")
                                }
                                .padding(vertical = 4.dp)
                        )
                    }
                }
            }

            // Universal Website Dark Mode Selector
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = ObsidianCardElevated,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, ElectricCyan.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.DarkMode,
                                contentDescription = null,
                                tint = ElectricCyan,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Seamless Website Dark Mode",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimaryDark
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        WebDarkModeStyle.entries.forEach { style ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onSelectDarkModeStyle(style) }
                                    .padding(vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = settings.webDarkModeStyle == style,
                                    onClick = { onSelectDarkModeStyle(style) },
                                    colors = RadioButtonDefaults.colors(selectedColor = ElectricCyan)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text(
                                        text = style.label,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = TextPrimaryDark,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = style.subtitle,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextSecondaryDark
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Privacy Toggles
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = ObsidianCardElevated,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        PrivacyToggleRow(
                            title = "Built-in Ad Blocker",
                            subtitle = "Blocks banner, video, and pop-up ad networks + cosmetic CSS filtering",
                            checked = settings.adBlockEnabled,
                            onCheckedChange = onToggleAdBlock
                        )
                        HorizontalDivider(color = OutlineDark)
                        PrivacyToggleRow(
                            title = "Privacy Tracking Protection",
                            subtitle = "Blocks cross-site analytics, pixels & behavioral trackers",
                            checked = settings.trackerProtectionEnabled,
                            onCheckedChange = onToggleTrackerProtection
                        )
                        HorizontalDivider(color = OutlineDark)
                        PrivacyToggleRow(
                            title = "Strip Tracking URL Parameters",
                            subtitle = "Removes utm_*, fbclid, gclid, msclkid & referral trackers",
                            checked = settings.stripTrackingParams,
                            onCheckedChange = onToggleStripParams
                        )
                        HorizontalDivider(color = OutlineDark)
                        PrivacyToggleRow(
                            title = "Block Third-Party Cookies",
                            subtitle = "Prevents cross-domain cookie profiling across websites",
                            checked = settings.blockThirdPartyCookies,
                            onCheckedChange = onToggleBlockThirdPartyCookies
                        )
                        HorizontalDivider(color = OutlineDark)
                        PrivacyToggleRow(
                            title = "Anti-Fingerprinting & Global Privacy Control",
                            subtitle = "Sends DNT=1, Sec-GPC=1 & locks navigator locale to en-IN",
                            checked = settings.antiFingerprintingEnabled,
                            onCheckedChange = onToggleAntiFingerprinting
                        )
                        HorizontalDivider(color = OutlineDark)
                        PrivacyToggleRow(
                            title = "HTTPS Smart Upgrade",
                            subtitle = "Automatically upgrades insecure HTTP connections to HTTPS",
                            checked = settings.httpsUpgradeEnabled,
                            onCheckedChange = onToggleHttpsUpgrade
                        )
                        HorizontalDivider(color = OutlineDark)
                        PrivacyToggleRow(
                            title = "Desktop Site Mode",
                            subtitle = "Request full desktop layouts instead of mobile web",
                            checked = settings.desktopModeEnabled,
                            onCheckedChange = onToggleDesktopSite
                        )
                    }
                }
            }

            // Live Intercepted Ad & Tracker Log
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent Blocked Ads & Trackers (${blockedEvents.size})",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimaryDark
                    )
                    if (blockedEvents.isNotEmpty()) {
                        Text(
                            text = "Clear Log",
                            style = MaterialTheme.typography.labelLarge,
                            color = ElectricCyan,
                            modifier = Modifier.clickable { onClearBlockedLog() }
                        )
                    }
                }
            }

            if (blockedEvents.isEmpty()) {
                item {
                    Text(
                        text = "Browse any website (YouTube, Google, Netflix, Amazon) to see real-time intercepted ad and tracker domains here.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondaryDark,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                }
            } else {
                items(blockedEvents.take(15)) { event ->
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = ObsidianBackground,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = event.blockedDomain,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = CoralAlert
                                )
                                Text(
                                    text = event.pageUrl.ifBlank { "Sub-resource request" },
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondaryDark,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = CoralAlert.copy(alpha = 0.16f)
                            ) {
                                Text(
                                    text = event.category,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = CoralAlert,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }

            item {
                SignatureFooterCard()
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun PrivacyToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = TextPrimaryDark,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondaryDark
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = EmeraldShield,
                checkedTrackColor = EmeraldShield.copy(alpha = 0.35f)
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarksAndHistorySheet(
    bookmarks: List<BookmarkEntity>,
    history: List<HistoryEntity>,
    onDismiss: () -> Unit,
    onOpenUrl: (String) -> Unit,
    onAddCustomBookmark: (title: String, url: String) -> Unit,
    onDeleteBookmark: (Int) -> Unit,
    onRestoreDefaults: () -> Unit,
    onClearHistory: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var newTitle by remember { mutableStateOf("") }
    var newUrl by remember { mutableStateOf("") }
    var showAddForm by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = ObsidianSurface
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Bookmark,
                            contentDescription = null,
                            tint = ElectricCyan,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Bookmarks & Browsing History",
                            style = MaterialTheme.typography.titleLarge,
                            color = TextPrimaryDark
                        )
                    }
                    Row {
                        IconButton(onClick = onRestoreDefaults) {
                            Icon(
                                imageVector = Icons.Default.Restore,
                                contentDescription = "Restore Default Bookmarks",
                                tint = EmeraldShield
                            )
                        }
                        IconButton(onClick = { showAddForm = !showAddForm }) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Bookmark",
                                tint = ElectricCyan
                            )
                        }
                    }
                }
            }

            if (showAddForm) {
                item {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = ObsidianCardElevated,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Add New Bookmark",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimaryDark
                            )
                            OutlinedTextField(
                                value = newTitle,
                                onValueChange = { newTitle = it },
                                label = { Text("Title (e.g., Wikipedia)") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = newUrl,
                                onValueChange = { newUrl = it },
                                label = { Text("Website URL (e.g., www.wikipedia.org)") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Button(
                                onClick = {
                                    if (newUrl.isNotBlank()) {
                                        onAddCustomBookmark(newTitle, newUrl)
                                        newTitle = ""
                                        newUrl = ""
                                        showAddForm = false
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Save Bookmark")
                            }
                        }
                    }
                }
            }

            items(bookmarks, key = { it.id }) { bm ->
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = ObsidianCardElevated,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenUrl(bm.url) }
                        .testTag("sheet_bookmark_${bm.displayDomain}")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color(bm.badgeColorHex).copy(alpha = 0.22f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Bookmark,
                                    contentDescription = null,
                                    tint = Color(bm.badgeColorHex),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = bm.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = TextPrimaryDark
                                )
                                Text(
                                    text = bm.displayDomain,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = ElectricCyan
                                )
                            }
                        }
                        if (!bm.isDefaultPinned) {
                            IconButton(onClick = { onDeleteBookmark(bm.id) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete bookmark",
                                    tint = TextSecondaryDark
                                )
                            }
                        } else {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = EmeraldShield.copy(alpha = 0.16f)
                            ) {
                                Text(
                                    text = "PINNED",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = EmeraldShield,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Browsing History Header
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null,
                            tint = TextSecondaryDark,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Recent Browsing History",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimaryDark
                        )
                    }
                    if (history.isNotEmpty()) {
                        Text(
                            text = "Clear History",
                            style = MaterialTheme.typography.labelLarge,
                            color = CoralAlert,
                            modifier = Modifier.clickable { onClearHistory() }
                        )
                    }
                }
            }

            if (history.isEmpty()) {
                item {
                    Text(
                        text = "No pages in history yet.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondaryDark
                    )
                }
            } else {
                items(history.take(20), key = { it.id }) { item ->
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = ObsidianBackground,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenUrl(item.url) }
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextPrimaryDark,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = item.url,
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondaryDark,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            item {
                SignatureFooterCard()
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun HindiEnglishVoiceSheet(
    lastSpokenInput: String,
    systemPromptBanner: String,
    onDismiss: () -> Unit,
    onStartHardwareSpeechRecognition: () -> Unit,
    onExecuteVoiceCommand: (String) -> Unit,
    onSpeakCurrentPromptAloud: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var customVoiceQuery by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = ObsidianSurface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = null,
                        tint = EmeraldShield,
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "AAA Browser Voice Search",
                            style = MaterialTheme.typography.titleLarge,
                            color = TextPrimaryDark
                        )
                        Text(
                            text = "Default System & UI Language: English (India) [en-IN]",
                            style = MaterialTheme.typography.bodySmall,
                            color = ElectricCyan
                        )
                    }
                }
                IconButton(onClick = onSpeakCurrentPromptAloud) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = "Speak System Prompt in English India",
                        tint = ElectricCyan
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = ObsidianCardElevated,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "SYSTEM STATUS (ENGLISH INDIA):",
                        style = MaterialTheme.typography.labelSmall,
                        color = EmeraldShield
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = systemPromptBanner,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPrimaryDark
                    )
                    if (lastSpokenInput.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Last Voice Command: \"$lastSpokenInput\"",
                            style = MaterialTheme.typography.bodySmall,
                            color = ElectricCyan
                        )
                    }
                }
            }

            Button(
                onClick = onStartHardwareSpeechRecognition,
                colors = ButtonDefaults.buttonColors(
                    containerColor = EmeraldShield,
                    contentColor = Color(0xFF002116)
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("start_speech_recognizer_button")
            ) {
                Icon(imageVector = Icons.Default.Mic, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Tap to Speak (English India)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = customVoiceQuery,
                    onValueChange = { customVoiceQuery = it },
                    placeholder = { Text("Enter voice command or search query…") },
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = {
                        if (customVoiceQuery.isNotBlank()) {
                            onExecuteVoiceCommand(customVoiceQuery)
                            customVoiceQuery = ""
                        }
                    },
                    modifier = Modifier.height(52.dp)
                ) {
                    Text("Run")
                }
            }

            Text(
                text = "Quick Voice Actions:",
                style = MaterialTheme.typography.labelLarge,
                color = TextSecondaryDark
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                HindiEnglishVoiceEngine.QUICK_VOICE_COMMANDS.forEach { cmd ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = ObsidianCardElevated,
                        modifier = Modifier
                            .border(1.dp, OutlineDark, RoundedCornerShape(12.dp))
                            .clickable { onExecuteVoiceCommand(cmd) }
                    ) {
                        Text(
                            text = cmd,
                            style = MaterialTheme.typography.labelLarge,
                            color = TextPrimaryDark,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            SignatureFooterCard()
            Spacer(modifier = Modifier.height(14.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoogleMapsAssistantSheet(
    onDismiss: () -> Unit,
    onOpenMapsInBrowser: (String) -> Unit,
    onLaunchMapsApp: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var destinationQuery by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = ObsidianSurface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
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
                        text = "Google Maps & Route Assistant",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimaryDark
                    )
                    Text(
                        text = "Use interactive Google Maps inside browser or launch Turn-by-Turn app",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondaryDark
                    )
                }
            }

            OutlinedTextField(
                value = destinationQuery,
                onValueChange = { destinationQuery = it },
                label = { Text("Enter destination, city, or place…") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("maps_destination_input")
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { onOpenMapsInBrowser(destinationQuery) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ElectricCyan,
                        contentColor = Color(0xFF001F24)
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Icon(Icons.Default.Map, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Open in Browser", fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = {
                        onLaunchMapsApp(destinationQuery.ifBlank { "Google Maps" })
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                ) {
                    Icon(Icons.Default.Navigation, contentDescription = null, tint = MapsGreen, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Maps App", color = MapsGreen, fontWeight = FontWeight.Bold)
                }
            }

            SignatureFooterCard()
            Spacer(modifier = Modifier.height(14.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabsSwitcherSheet(
    tabs: List<BrowserTab>,
    activeTabId: String,
    onDismiss: () -> Unit,
    onSelectTab: (String) -> Unit,
    onCloseTab: (String) -> Unit,
    onNewTab: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = ObsidianSurface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Tab, contentDescription = null, tint = ElectricCyan)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Open Browser Tabs (${tabs.size})",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextPrimaryDark
                    )
                }
                Button(onClick = onNewTab) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("New Tab")
                }
            }

            LazyColumn(
                modifier = Modifier.heightIn(max = 360.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(tabs, key = { it.id }) { tab ->
                    val isSelected = tab.id == activeTabId
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (isSelected) ObsidianCardElevated else ObsidianBackground,
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) ElectricCyan else OutlineDark,
                                shape = RoundedCornerShape(14.dp)
                            )
                            .clickable { onSelectTab(tab.id) }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = tab.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = TextPrimaryDark,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = if (tab.isHomeSpeedDial) "Speed Dial • Bookmarks" else tab.url,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = ElectricCyan,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            IconButton(onClick = { onCloseTab(tab.id) }) {
                                Icon(Icons.Default.Close, contentDescription = "Close tab", tint = TextSecondaryDark)
                            }
                        }
                    }
                }
            }

            SignatureFooterCard()
            Spacer(modifier = Modifier.height(14.dp))
        }
    }
}
