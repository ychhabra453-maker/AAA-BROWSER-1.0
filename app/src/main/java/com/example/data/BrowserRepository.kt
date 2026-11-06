package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.browserDataStore: DataStore<Preferences> by preferencesDataStore(name = "aegis_browser_prefs")

data class PrivacyAndDisplaySettings(
    val adBlockEnabled: Boolean = true,
    val trackerProtectionEnabled: Boolean = true,
    val stripTrackingParams: Boolean = true,
    val blockThirdPartyCookies: Boolean = true,
    val antiFingerprintingEnabled: Boolean = true,
    val httpsUpgradeEnabled: Boolean = true,
    val webDarkModeStyle: WebDarkModeStyle = WebDarkModeStyle.SMART_DARK,
    val desktopModeEnabled: Boolean = false,
    val voiceReadoutEnabled: Boolean = true,
    val drivingMotionSafetyMode: Boolean = false,
    val totalAdsBlocked: Int = 142,
    val totalTrackersBlocked: Int = 318,
    val totalParamsStripped: Int = 46,
    val updateRepositorySlug: String = "ychhabra453/AAA-Browser"
)

class BrowserRepository(
    private val context: Context,
    private val dao: BrowserDao
) {
    private object Keys {
        val AD_BLOCK = booleanPreferencesKey("ad_block_enabled")
        val TRACKER_PROTECTION = booleanPreferencesKey("tracker_protection_enabled")
        val STRIP_PARAMS = booleanPreferencesKey("strip_tracking_params")
        val BLOCK_COOKIES = booleanPreferencesKey("block_third_party_cookies")
        val ANTI_FINGERPRINT = booleanPreferencesKey("anti_fingerprinting")
        val HTTPS_UPGRADE = booleanPreferencesKey("https_upgrade")
        val DARK_MODE_STYLE = stringPreferencesKey("web_dark_mode_style")
        val DESKTOP_MODE = booleanPreferencesKey("desktop_mode")
        val VOICE_READOUT = booleanPreferencesKey("voice_readout_en_in")
        val DRIVING_SAFETY = booleanPreferencesKey("driving_motion_safety")
        val TOTAL_ADS = intPreferencesKey("total_ads_blocked")
        val TOTAL_TRACKERS = intPreferencesKey("total_trackers_blocked")
        val TOTAL_PARAMS = intPreferencesKey("total_params_stripped")
        val UPDATE_REPO_SLUG = stringPreferencesKey("update_repo_slug")
    }

    val bookmarksFlow: Flow<List<BookmarkEntity>> = dao.getAllBookmarks()
    val historyFlow: Flow<List<HistoryEntity>> = dao.getRecentHistory()
    val blockedEventsFlow: Flow<List<BlockedEventEntity>> = dao.getRecentBlockedEvents()

    val settingsFlow: Flow<PrivacyAndDisplaySettings> = context.browserDataStore.data.map { prefs ->
        val styleName = prefs[Keys.DARK_MODE_STYLE] ?: WebDarkModeStyle.SMART_DARK.name
        val style = runCatching { WebDarkModeStyle.valueOf(styleName) }.getOrDefault(WebDarkModeStyle.SMART_DARK)
        PrivacyAndDisplaySettings(
            adBlockEnabled = prefs[Keys.AD_BLOCK] ?: true,
            trackerProtectionEnabled = prefs[Keys.TRACKER_PROTECTION] ?: true,
            stripTrackingParams = prefs[Keys.STRIP_PARAMS] ?: true,
            blockThirdPartyCookies = prefs[Keys.BLOCK_COOKIES] ?: true,
            antiFingerprintingEnabled = prefs[Keys.ANTI_FINGERPRINT] ?: true,
            httpsUpgradeEnabled = prefs[Keys.HTTPS_UPGRADE] ?: true,
            webDarkModeStyle = style,
            desktopModeEnabled = prefs[Keys.DESKTOP_MODE] ?: false,
            voiceReadoutEnabled = prefs[Keys.VOICE_READOUT] ?: true,
            drivingMotionSafetyMode = prefs[Keys.DRIVING_SAFETY] ?: false,
            totalAdsBlocked = prefs[Keys.TOTAL_ADS] ?: 142,
            totalTrackersBlocked = prefs[Keys.TOTAL_TRACKERS] ?: 318,
            totalParamsStripped = prefs[Keys.TOTAL_PARAMS] ?: 46,
            updateRepositorySlug = prefs[Keys.UPDATE_REPO_SLUG] ?: "ychhabra453/AAA-Browser"
        )
    }

    suspend fun ensureDefaultBookmarksSeeded() {
        dao.deleteBookmarkByUrl("https://www.amazon.com")
        dao.deleteBookmarkByUrl("www.amazon.com")
        dao.insertBookmarks(DEFAULT_BOOKMARKS)
    }

    suspend fun resetDefaultBookmarks() {
        dao.insertBookmarks(DEFAULT_BOOKMARKS)
    }

    suspend fun addBookmark(title: String, url: String, category: String = "Custom") {
        val domain = extractDomain(url)
        val badgeColor = when {
            domain.contains("youtube") -> 0xFFFF0033
            domain.contains("google") -> 0xFF4285F4
            domain.contains("netflix") -> 0xFFE50914
            domain.contains("amazon") -> 0xFFFF9900
            else -> 0xFF00E5FF
        }
        dao.insertBookmark(
            BookmarkEntity(
                title = title.ifBlank { domain },
                url = url,
                displayDomain = domain,
                category = category,
                badgeColorHex = badgeColor,
                isDefaultPinned = false,
                sortOrder = 99
            )
        )
    }

    suspend fun removeBookmark(id: Int) {
        dao.deleteBookmarkById(id)
    }

    suspend fun recordHistory(title: String, url: String, adsBlocked: Int, trackersBlocked: Int) {
        if (url.isBlank() || url == "about:blank") return
        dao.insertHistory(
            HistoryEntity(
                title = title.ifBlank { extractDomain(url) },
                url = url,
                adsBlocked = adsBlocked,
                trackersBlocked = trackersBlocked
            )
        )
    }

    suspend fun clearHistory() {
        dao.clearHistory()
    }

    suspend fun recordBlockedEvent(domain: String, pageUrl: String, category: String) {
        dao.insertBlockedEvent(
            BlockedEventEntity(
                blockedDomain = domain,
                pageUrl = pageUrl,
                category = category
            )
        )
        context.browserDataStore.edit { prefs ->
            when (category) {
                "AD_NETWORK" -> prefs[Keys.TOTAL_ADS] = (prefs[Keys.TOTAL_ADS] ?: 142) + 1
                "TRACKING_PARAM" -> prefs[Keys.TOTAL_PARAMS] = (prefs[Keys.TOTAL_PARAMS] ?: 46) + 1
                else -> prefs[Keys.TOTAL_TRACKERS] = (prefs[Keys.TOTAL_TRACKERS] ?: 318) + 1
            }
        }
    }

    suspend fun clearBlockedEvents() {
        dao.clearBlockedEvents()
    }

    suspend fun updateAdBlock(enabled: Boolean) {
        context.browserDataStore.edit { it[Keys.AD_BLOCK] = enabled }
    }

    suspend fun updateTrackerProtection(enabled: Boolean) {
        context.browserDataStore.edit { it[Keys.TRACKER_PROTECTION] = enabled }
    }

    suspend fun updateStripParams(enabled: Boolean) {
        context.browserDataStore.edit { it[Keys.STRIP_PARAMS] = enabled }
    }

    suspend fun updateBlockThirdPartyCookies(enabled: Boolean) {
        context.browserDataStore.edit { it[Keys.BLOCK_COOKIES] = enabled }
    }

    suspend fun updateAntiFingerprinting(enabled: Boolean) {
        context.browserDataStore.edit { it[Keys.ANTI_FINGERPRINT] = enabled }
    }

    suspend fun updateHttpsUpgrade(enabled: Boolean) {
        context.browserDataStore.edit { it[Keys.HTTPS_UPGRADE] = enabled }
    }

    suspend fun updateWebDarkModeStyle(style: WebDarkModeStyle) {
        context.browserDataStore.edit { it[Keys.DARK_MODE_STYLE] = style.name }
    }

    suspend fun updateDesktopMode(enabled: Boolean) {
        context.browserDataStore.edit { it[Keys.DESKTOP_MODE] = enabled }
    }

    suspend fun updateVoiceReadout(enabled: Boolean) {
        context.browserDataStore.edit { it[Keys.VOICE_READOUT] = enabled }
    }

    suspend fun updateDrivingSafetyMode(enabled: Boolean) {
        context.browserDataStore.edit { it[Keys.DRIVING_SAFETY] = enabled }
    }

    suspend fun updateRepoSlug(slug: String) {
        context.browserDataStore.edit { it[Keys.UPDATE_REPO_SLUG] = slug }
    }

    private fun extractDomain(url: String): String {
        return url
            .removePrefix("https://")
            .removePrefix("http://")
            .substringBefore("/")
            .substringBefore("?")
            .ifBlank { url }
    }
}
