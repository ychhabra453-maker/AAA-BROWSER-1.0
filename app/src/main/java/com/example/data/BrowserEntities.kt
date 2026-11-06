package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

const val ANDROID_AUTO_SETTINGS_URI = "aegis://android_auto_settings"
const val SIGNATURE_FOOTER_TEXT = "Made by Yug Chhabra In India With❤️"

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val url: String,
    val displayDomain: String,
    val category: String,
    val badgeColorHex: Long,
    val isDefaultPinned: Boolean = false,
    val sortOrder: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "browsing_history")
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val url: String,
    val adsBlocked: Int = 0,
    val trackersBlocked: Int = 0,
    val visitedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "blocked_events")
data class BlockedEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val blockedDomain: String,
    val pageUrl: String,
    val category: String, // "AD_NETWORK", "TRACKER", "TRACKING_PARAM", "FINGERPRINT"
    val blockedAt: Long = System.currentTimeMillis()
)

enum class WebDarkModeStyle(val label: String, val subtitle: String) {
    SMART_DARK("Smart Contrast Dark", "Adapts light websites while preserving media & Maps"),
    AMOLED_BLACK("AMOLED Pitch Black", "True #000000 backgrounds for night driving & OLED screens"),
    WARM_NIGHT("Eye-Comfort Warm Dark", "Reduced blue light for late-night reading & driving"),
    SITE_DEFAULT("Website Default", "Uses website's native stylesheet without forced inversion")
}

data class BrowserTab(
    val id: String,
    val title: String = "New Tab",
    val url: String = "https://www.google.com",
    val isHomeSpeedDial: Boolean = true,
    val isLoading: Boolean = false,
    val progress: Int = 0,
    val canGoBack: Boolean = false,
    val canGoForward: Boolean = false,
    val pageAdsBlocked: Int = 0,
    val pageTrackersBlocked: Int = 0,
    val isSecureHttps: Boolean = true
)

val DEFAULT_BOOKMARKS = listOf(
    BookmarkEntity(
        id = 1,
        title = "YouTube",
        url = "https://www.youtube.com",
        displayDomain = "www.youtube.com",
        category = "Video & Streaming",
        badgeColorHex = 0xFFFF0033,
        isDefaultPinned = true,
        sortOrder = 1
    ),
    BookmarkEntity(
        id = 2,
        title = "Google",
        url = "https://www.google.com",
        displayDomain = "www.google.com",
        category = "Search & Web",
        badgeColorHex = 0xFF4285F4,
        isDefaultPinned = true,
        sortOrder = 2
    ),
    BookmarkEntity(
        id = 3,
        title = "Netflix",
        url = "https://www.netflix.com",
        displayDomain = "www.netflix.com",
        category = "Movies & Shows",
        badgeColorHex = 0xFFE50914,
        isDefaultPinned = true,
        sortOrder = 3
    ),
    BookmarkEntity(
        id = 4,
        title = "Android Auto settings",
        url = ANDROID_AUTO_SETTINGS_URI,
        displayDomain = "Android Auto settings",
        category = "Phone Car Settings",
        badgeColorHex = 0xFF00E5FF,
        isDefaultPinned = true,
        sortOrder = 4
    )
)
