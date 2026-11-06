package com.example.ui

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.BlockedEventEntity
import com.example.data.BookmarkEntity
import com.example.data.BrowserDatabase
import com.example.data.BrowserRepository
import com.example.data.BrowserTab
import com.example.data.DEFAULT_BOOKMARKS
import com.example.data.HistoryEntity
import com.example.data.PrivacyAndDisplaySettings
import com.example.data.WebDarkModeStyle
import com.example.engine.AppUpdateManager
import com.example.engine.HindiEnglishVoiceEngine
import com.example.engine.PrivacyShieldEngine
import com.example.engine.ReleaseUpdateInfo
import com.example.engine.VoiceCommandAction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

enum class BrowserScreenMode {
    STANDARD_BROWSER,
    ANDROID_AUTO_CONSOLE,
    FULL_SETTINGS,
    FULL_ACCOUNT
}

enum class ActiveModalSheet {
    NONE,
    PROFILE_QUICK_SHEET,
    SEARCH_OMNIBOX,
    PAGE_INFO_SHEET,
    PRIVACY_SHIELD,
    BOOKMARKS_AND_HISTORY,
    GOOGLE_MAPS_HUB,
    VOICE_ASSISTANT,
    TABS_SWITCHER
}

class BrowserViewModel(
    private val repository: BrowserRepository
) : ViewModel() {

    val bookmarks: StateFlow<List<BookmarkEntity>> = repository.bookmarksFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DEFAULT_BOOKMARKS)

    val history: StateFlow<List<HistoryEntity>> = repository.historyFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val blockedEvents: StateFlow<List<BlockedEventEntity>> = repository.blockedEventsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val settings: StateFlow<PrivacyAndDisplaySettings> = repository.settingsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PrivacyAndDisplaySettings())

    private val _releaseUpdateInfo = MutableStateFlow(ReleaseUpdateInfo())
    val releaseUpdateInfo: StateFlow<ReleaseUpdateInfo> = _releaseUpdateInfo.asStateFlow()

    // No Speed Dial: starts directly in the clean browser on Google India
    private val _tabs = MutableStateFlow(
        listOf(
            BrowserTab(
                id = "tab-main",
                title = "Google",
                url = "https://www.google.com",
                isHomeSpeedDial = false
            )
        )
    )
    val tabs: StateFlow<List<BrowserTab>> = _tabs.asStateFlow()

    private val _activeTabId = MutableStateFlow("tab-main")
    val activeTabId: StateFlow<String> = _activeTabId.asStateFlow()

    private val _omniboxText = MutableStateFlow("https://www.google.com")
    val omniboxText: StateFlow<String> = _omniboxText.asStateFlow()

    private val _screenMode = MutableStateFlow(BrowserScreenMode.STANDARD_BROWSER)
    val screenMode: StateFlow<BrowserScreenMode> = _screenMode.asStateFlow()

    private val _primaryTab = MutableStateFlow(EchoPrimaryTab.HOME)
    val primaryTab: StateFlow<EchoPrimaryTab> = _primaryTab.asStateFlow()

    private val _activeSheet = MutableStateFlow(ActiveModalSheet.NONE)
    val activeSheet: StateFlow<ActiveModalSheet> = _activeSheet.asStateFlow()

    private val _isReaderModeActive = MutableStateFlow(false)
    val isReaderModeActive: StateFlow<Boolean> = _isReaderModeActive.asStateFlow()

    private val _readerArticleText = MutableStateFlow("")
    val readerArticleText: StateFlow<String> = _readerArticleText.asStateFlow()

    private val _isMusicModeActive = MutableStateFlow(false)
    val isMusicModeActive: StateFlow<Boolean> = _isMusicModeActive.asStateFlow()

    private val _audioOnlyHideVideo = MutableStateFlow(false)
    val audioOnlyHideVideo: StateFlow<Boolean> = _audioOnlyHideVideo.asStateFlow()

    private val _systemPromptBanner = MutableStateFlow(
        "AAA Browser v1.1 • English (India) [en-IN] • Privacy Shield & Dark Mode Active"
    )
    val systemPromptBanner: StateFlow<String> = _systemPromptBanner.asStateFlow()

    private val _lastSpokenHindiInput = MutableStateFlow("")
    val lastSpokenHindiInput: StateFlow<String> = _lastSpokenHindiInput.asStateFlow()

    private val _latestPageReadableSummary = MutableStateFlow(
        "AAA Browser v1.1 ready. Reader Mode, Live Search Suggestions, YouTube Ad-Free Music Mode, and Google Maps support are active."
    )
    val latestPageReadableSummary: StateFlow<String> = _latestPageReadableSummary.asStateFlow()

    private val _webViewReloadTrigger = MutableStateFlow(0)
    val webViewReloadTrigger: StateFlow<Int> = _webViewReloadTrigger.asStateFlow()

    init {
        viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            repository.ensureDefaultBookmarksSeeded()
        }
    }

    fun getActiveTab(): BrowserTab {
        val currentId = _activeTabId.value
        return _tabs.value.firstOrNull { it.id == currentId } ?: _tabs.value.first()
    }

    fun setOmniboxText(text: String) {
        _omniboxText.value = text
    }

    fun selectPrimaryTab(tab: EchoPrimaryTab) {
        _primaryTab.value = tab
        _screenMode.value = BrowserScreenMode.STANDARD_BROWSER
    }

    fun setScreenMode(mode: BrowserScreenMode) {
        _screenMode.value = mode
        if (mode == BrowserScreenMode.ANDROID_AUTO_CONSOLE) {
            // In Android Auto, automatically hide distracting video frames while keeping YouTube audio playing!
            _audioOnlyHideVideo.value = true
        }
        _systemPromptBanner.value = when (mode) {
            BrowserScreenMode.ANDROID_AUTO_CONSOLE ->
                "Android Auto Driving Console Active • Video Hidden, Audio-Only YouTube Active"
            BrowserScreenMode.FULL_SETTINGS ->
                "AAA Browser v1.1 Settings • System Update & Privacy Configuration"
            BrowserScreenMode.FULL_ACCOUNT ->
                "Account & Integrations • Yug Chhabra"
            BrowserScreenMode.STANDARD_BROWSER ->
                "AAA Browser v1.1 • Privacy Shield & Universal Dark Mode Active"
        }
    }

    fun toggleReaderMode() {
        _isReaderModeActive.update { !it }
    }

    fun closeReaderMode() {
        _isReaderModeActive.value = false
    }

    fun updateReaderArticleText(text: String) {
        _readerArticleText.value = text
    }

    fun toggleMusicMode() {
        val next = !_isMusicModeActive.value
        _isMusicModeActive.value = next
        if (next) {
            val currentUrl = getActiveTab().url
            if (!currentUrl.contains("youtube.com", ignoreCase = true)) {
                loadUrlOrSearch("https://m.youtube.com/results?search_query=latest+hindi+songs")
            }
            _systemPromptBanner.value = "YouTube Ad-Free Music Mode Active • 0 Video Ads"
        } else {
            _audioOnlyHideVideo.value = false
        }
    }

    fun setAudioOnlyHideVideo(hideVideo: Boolean) {
        _audioOnlyHideVideo.value = hideVideo
        _systemPromptBanner.value = if (hideVideo) {
            "Audio-Only Mode Active • Video Stopped, Music Playing in Background"
        } else {
            "Video Display Restored • Ad-Free YouTube Active"
        }
    }

    fun openYouTubeAdFreeMusic(query: String = "") {
        _isMusicModeActive.value = true
        _audioOnlyHideVideo.value = true
        val targetUrl = if (query.isBlank()) {
            "https://m.youtube.com"
        } else {
            "https://m.youtube.com/results?search_query=${Uri.encode(query)}"
        }
        loadUrlOrSearch(targetUrl)
        _systemPromptBanner.value = "Playing YouTube in Ad-Free Music Mode (Video Hidden)"
    }

    fun openModalSheet(sheet: ActiveModalSheet) {
        _activeSheet.value = sheet
    }

    fun closeModalSheet() {
        _activeSheet.value = ActiveModalSheet.NONE
    }

    fun loadUrlOrSearch(rawInput: String) {
        val currentSettings = settings.value
        val sanitizeResult = PrivacyShieldEngine.sanitizeInputUrl(
            rawInput = rawInput,
            stripTrackingParams = currentSettings.stripTrackingParams,
            httpsUpgrade = currentSettings.httpsUpgradeEnabled
        )
        if (sanitizeResult.strippedParamsCount > 0) {
            viewModelScope.launch {
                repository.recordBlockedEvent(
                    domain = "Stripped: ${sanitizeResult.strippedParamNames.joinToString(", ")}",
                    pageUrl = sanitizeResult.cleanUrl,
                    category = "TRACKING_PARAM"
                )
            }
            _systemPromptBanner.value =
                "Privacy Shield stripped ${sanitizeResult.strippedParamsCount} tracking parameter(s) from URL."
        }

        val targetUrl = sanitizeResult.cleanUrl
        _omniboxText.value = targetUrl
        _isReaderModeActive.value = false
        updateTab(_activeTabId.value) { tab ->
            tab.copy(
                url = targetUrl,
                isHomeSpeedDial = false,
                isLoading = true,
                progress = 15,
                pageAdsBlocked = 0,
                pageTrackersBlocked = 0,
                isSecureHttps = targetUrl.startsWith("https://", ignoreCase = true)
            )
        }
        _activeSheet.value = ActiveModalSheet.NONE
    }

    fun navigateHomeSpeedDial() {
        _isReaderModeActive.value = false
        loadUrlOrSearch("https://www.google.com")
        _systemPromptBanner.value = "Home • Google Search India"
    }

    fun openGoogleMapsWithinBrowser(query: String = "") {
        val mapsUrl = if (query.isBlank()) {
            "https://www.google.com/maps?hl=en-IN&gl=IN"
        } else {
            "https://www.google.com/maps/search/${Uri.encode(query)}?hl=en-IN&gl=IN"
        }
        loadUrlOrSearch(mapsUrl)
        _systemPromptBanner.value = if (query.isBlank()) {
            "Opened Google Maps (India) with GPS location support."
        } else {
            "Searching Google Maps for: $query"
        }
    }

    fun createNewTab(initialUrl: String? = null) {
        val newId = "tab-${UUID.randomUUID().toString().take(6)}"
        val cleanUrl = initialUrl?.takeIf { it.isNotBlank() } ?: "https://www.google.com"
        val newTab = BrowserTab(
            id = newId,
            title = "Google",
            url = cleanUrl,
            isHomeSpeedDial = false
        )
        _tabs.update { it + newTab }
        _activeTabId.value = newId
        _omniboxText.value = cleanUrl
        _activeSheet.value = ActiveModalSheet.NONE
    }

    fun selectTab(tabId: String) {
        _activeTabId.value = tabId
        val selected = _tabs.value.firstOrNull { it.id == tabId }
        if (selected != null) {
            _omniboxText.value = selected.url
        }
        _activeSheet.value = ActiveModalSheet.NONE
    }

    fun closeTab(tabId: String) {
        val currentList = _tabs.value
        if (currentList.size <= 1) {
            navigateHomeSpeedDial()
            return
        }
        val updated = currentList.filterNot { it.id == tabId }
        _tabs.value = updated
        if (_activeTabId.value == tabId) {
            val next = updated.last()
            _activeTabId.value = next.id
            _omniboxText.value = next.url
        }
    }

    fun onWebPageStarted(url: String) {
        _omniboxText.value = url
        updateTab(_activeTabId.value) { tab ->
            tab.copy(
                url = url,
                isHomeSpeedDial = false,
                isLoading = true,
                isSecureHttps = url.startsWith("https://", ignoreCase = true)
            )
        }
    }

    fun onWebPageProgress(progress: Int) {
        updateTab(_activeTabId.value) { tab ->
            tab.copy(
                progress = progress,
                isLoading = progress < 100
            )
        }
    }

    fun onWebPageFinished(
        title: String,
        url: String,
        canGoBack: Boolean,
        canGoForward: Boolean,
        extractedSummary: String
    ) {
        val activeTab = getActiveTab()
        val displayTitle = title.ifBlank { url }
        updateTab(_activeTabId.value) { tab ->
            tab.copy(
                title = displayTitle,
                url = url,
                isLoading = false,
                progress = 100,
                canGoBack = canGoBack,
                canGoForward = canGoForward
            )
        }
        if (extractedSummary.isNotBlank()) {
            _latestPageReadableSummary.value = extractedSummary
        }
        viewModelScope.launch {
            repository.recordHistory(
                title = displayTitle,
                url = url,
                adsBlocked = activeTab.pageAdsBlocked,
                trackersBlocked = activeTab.pageTrackersBlocked
            )
        }
    }

    fun onRequestInterceptedAndBlocked(matchedDomain: String, pageUrl: String, category: String) {
        updateTab(_activeTabId.value) { tab ->
            if (category == "AD_NETWORK") {
                tab.copy(pageAdsBlocked = tab.pageAdsBlocked + 1)
            } else {
                tab.copy(pageTrackersBlocked = tab.pageTrackersBlocked + 1)
            }
        }
        viewModelScope.launch {
            repository.recordBlockedEvent(
                domain = matchedDomain,
                pageUrl = pageUrl,
                category = category
            )
        }
    }

    fun triggerReload() {
        _webViewReloadTrigger.update { it + 1 }
    }

    fun addBookmarkForCurrentPage() {
        val active = getActiveTab()
        val targetUrl = active.url.ifBlank { "https://www.google.com" }
        val targetTitle = active.title.ifBlank { "Google" }
        viewModelScope.launch {
            repository.addBookmark(title = targetTitle, url = targetUrl)
            _systemPromptBanner.value = "Saved '$targetTitle' to Bookmarks."
        }
    }

    fun addCustomBookmark(title: String, rawUrl: String) {
        val sanitized = PrivacyShieldEngine.sanitizeInputUrl(
            rawInput = rawUrl,
            stripTrackingParams = true,
            httpsUpgrade = true
        )
        viewModelScope.launch {
            repository.addBookmark(title = title, url = sanitized.cleanUrl)
            _systemPromptBanner.value = "Bookmark added: ${ title.ifBlank { sanitized.cleanUrl } }"
        }
    }

    fun deleteBookmark(id: Int) {
        viewModelScope.launch {
            repository.removeBookmark(id)
        }
    }

    fun restoreDefaultFourBookmarks() {
        viewModelScope.launch {
            repository.resetDefaultBookmarks()
            _systemPromptBanner.value =
                "Default bookmarks restored: YouTube, Google, Netflix, Android Auto settings."
        }
    }

    fun clearBrowsingHistory() {
        viewModelScope.launch {
            repository.clearHistory()
            _systemPromptBanner.value = "Browsing history cleared."
        }
    }

    fun clearBlockedTrackerLog() {
        viewModelScope.launch {
            repository.clearBlockedEvents()
            _systemPromptBanner.value = "Intercepted tracker log cleared."
        }
    }

    fun toggleAdBlock(enabled: Boolean) {
        viewModelScope.launch {
            repository.updateAdBlock(enabled)
            triggerReload()
        }
    }

    fun toggleTrackerProtection(enabled: Boolean) {
        viewModelScope.launch {
            repository.updateTrackerProtection(enabled)
            triggerReload()
        }
    }

    fun toggleStripTrackingParams(enabled: Boolean) {
        viewModelScope.launch {
            repository.updateStripParams(enabled)
        }
    }

    fun toggleBlockThirdPartyCookies(enabled: Boolean) {
        viewModelScope.launch {
            repository.updateBlockThirdPartyCookies(enabled)
            triggerReload()
        }
    }

    fun toggleAntiFingerprinting(enabled: Boolean) {
        viewModelScope.launch {
            repository.updateAntiFingerprinting(enabled)
            triggerReload()
        }
    }

    fun toggleHttpsUpgrade(enabled: Boolean) {
        viewModelScope.launch {
            repository.updateHttpsUpgrade(enabled)
        }
    }

    fun selectWebDarkModeStyle(style: WebDarkModeStyle) {
        viewModelScope.launch {
            repository.updateWebDarkModeStyle(style)
            _systemPromptBanner.value = "Website Dark Mode set to: ${style.label}"
            triggerReload()
        }
    }

    fun toggleDesktopSite(enabled: Boolean) {
        viewModelScope.launch {
            repository.updateDesktopMode(enabled)
            triggerReload()
        }
    }

    fun toggleVoiceReadout(enabled: Boolean) {
        viewModelScope.launch {
            repository.updateVoiceReadout(enabled)
        }
    }

    fun toggleDrivingSafetyMode(enabled: Boolean) {
        viewModelScope.launch {
            repository.updateDrivingSafetyMode(enabled)
            _systemPromptBanner.value = if (enabled) {
                "Driving Motion Safety Active • Hands-free English (India) audio readout enabled."
            } else {
                "Parked Mode Active • Full interactive web browsing enabled."
            }
        }
    }

    fun checkForAppUpdates(customRepoInput: String? = null) {
        val targetRepo = (customRepoInput ?: settings.value.updateRepositorySlug).trim()
        val normalized = AppUpdateManager.normalizeRepoSlug(targetRepo)
        viewModelScope.launch {
            repository.updateRepoSlug(normalized)
            _releaseUpdateInfo.update {
                it.copy(isChecking = true, statusMessage = "Checking GitHub Releases for $normalized…")
            }
            val result = AppUpdateManager.checkGitHubReleaseUpdate(normalized)
            _releaseUpdateInfo.value = result
            _systemPromptBanner.value = result.statusMessage
        }
    }

    fun processHindiOrEnglishVoiceInput(
        transcript: String,
        onSpeakEnglishIndia: (String) -> Unit
    ) {
        _lastSpokenHindiInput.value = transcript
        val currentSettings = settings.value
        val action = HindiEnglishVoiceEngine.parseVoiceCommand(
            rawTranscript = transcript,
            adsBlockedCount = currentSettings.totalAdsBlocked,
            trackersBlockedCount = currentSettings.totalTrackersBlocked
        )

        when (action) {
            is VoiceCommandAction.OpenUrl -> {
                _systemPromptBanner.value = action.englishIndiaSpokenFeedback
                loadUrlOrSearch(action.url)
                onSpeakEnglishIndia(action.englishIndiaSpokenFeedback)
            }
            is VoiceCommandAction.OpenGoogleMapsRoute -> {
                _systemPromptBanner.value = action.englishIndiaSpokenFeedback
                openGoogleMapsWithinBrowser(action.destinationQuery)
                onSpeakEnglishIndia(action.englishIndiaSpokenFeedback)
            }
            is VoiceCommandAction.ToggleDarkMode -> {
                val nextStyle = when (currentSettings.webDarkModeStyle) {
                    WebDarkModeStyle.SMART_DARK -> WebDarkModeStyle.AMOLED_BLACK
                    WebDarkModeStyle.AMOLED_BLACK -> WebDarkModeStyle.WARM_NIGHT
                    WebDarkModeStyle.WARM_NIGHT -> WebDarkModeStyle.SMART_DARK
                    WebDarkModeStyle.SITE_DEFAULT -> WebDarkModeStyle.SMART_DARK
                }
                selectWebDarkModeStyle(nextStyle)
                val msg = "${action.englishIndiaSpokenFeedback} Active mode: ${nextStyle.label}."
                _systemPromptBanner.value = msg
                onSpeakEnglishIndia(msg)
            }
            is VoiceCommandAction.ReadCurrentPageAloud -> {
                val summary = _latestPageReadableSummary.value
                val msg = "${action.englishIndiaSpokenFeedback} $summary"
                _systemPromptBanner.value = action.englishIndiaSpokenFeedback
                onSpeakEnglishIndia(msg)
            }
            is VoiceCommandAction.AnnouncePrivacyReport -> {
                _systemPromptBanner.value = action.englishIndiaSpokenFeedback
                onSpeakEnglishIndia(action.englishIndiaSpokenFeedback)
            }
        }
    }

    private fun updateTab(tabId: String, transform: (BrowserTab) -> BrowserTab) {
        _tabs.update { list ->
            list.map { tab ->
                if (tab.id == tabId) transform(tab) else tab
            }
        }
    }

    class Factory(private val context: Context) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val db = BrowserDatabase.getInstance(context)
            val repo = BrowserRepository(context.applicationContext, db.browserDao())
            return BrowserViewModel(repo) as T
        }
    }
}
