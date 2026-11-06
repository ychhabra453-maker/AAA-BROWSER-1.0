package com.example

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.ANDROID_AUTO_SETTINGS_URI
import com.example.data.BookmarkEntity
import com.example.data.WebDarkModeStyle
import com.example.engine.AppUpdateManager
import com.example.engine.HindiEnglishVoiceEngine
import com.example.ui.ActiveModalSheet
import com.example.ui.AegisWebViewContainer
import com.example.ui.AndroidAutoDrivingConsole
import com.example.ui.BookmarksAndHistorySheet
import com.example.ui.BrowserScreenMode
import com.example.ui.BrowserViewModel
import com.example.ui.EchoAccountScreen
import com.example.ui.EchoFloatingBottomBar
import com.example.ui.EchoFullSettingsScreen
import com.example.ui.EchoPageInfoSheet
import com.example.ui.EchoProfileQuickSheet
import com.example.ui.EchoReaderModeOverlay
import com.example.ui.EchoSearchOmniboxSheet
import com.example.ui.EchoTopAppBar
import com.example.ui.GoogleMapsAssistantSheet
import com.example.ui.HindiEnglishVoiceSheet
import com.example.ui.PrivacyShieldAndDarkModeSheet
import com.example.ui.TabsSwitcherSheet
import com.example.ui.WebViewController
import com.example.ui.YouTubeAdFreeMusicModeBar
import com.example.ui.launchExternalGoogleMaps
import com.example.ui.launchPhoneAndroidAutoSettings
import com.example.ui.theme.EchoCardSurface
import com.example.ui.theme.EchoPillBorder
import com.example.ui.theme.EchoPitchBlack
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.TextPrimaryDark
import java.util.Locale

class MainActivity : ComponentActivity() {

    override fun attachBaseContext(newBase: Context) {
        // Force English (India) [en-IN] as the single default language for all UI and system prompts
        val englishIndiaLocale = HindiEnglishVoiceEngine.SYSTEM_LOCALE_EN_IN
        Locale.setDefault(englishIndiaLocale)
        val config = Configuration(newBase.resources.configuration)
        config.setLocale(englishIndiaLocale)
        val localizedContext = newBase.createConfigurationContext(config)
        super.attachBaseContext(localizedContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Locale.setDefault(HindiEnglishVoiceEngine.SYSTEM_LOCALE_EN_IN)
        enableEdgeToEdge()

        val initialIntentUrl = intent?.dataString
        val isCarDockMode = (resources.configuration.uiMode and Configuration.UI_MODE_TYPE_MASK) ==
            Configuration.UI_MODE_TYPE_CAR

        setContent {
            MyApplicationTheme {
                val vm: BrowserViewModel = viewModel(
                    factory = BrowserViewModel.Factory(applicationContext)
                )

                LaunchedEffect(initialIntentUrl, isCarDockMode) {
                    if (isCarDockMode) {
                        vm.setScreenMode(BrowserScreenMode.ANDROID_AUTO_CONSOLE)
                    }
                    if (!initialIntentUrl.isNullOrBlank()) {
                        vm.loadUrlOrSearch(initialIntentUrl)
                    }
                }

                AegisBrowserApp(viewModel = vm)
            }
        }
    }
}

@Composable
fun AegisBrowserApp(
    viewModel: BrowserViewModel
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val bookmarks by viewModel.bookmarks.collectAsStateWithLifecycle()
    val history by viewModel.history.collectAsStateWithLifecycle()
    val blockedEvents by viewModel.blockedEvents.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val releaseUpdateInfo by viewModel.releaseUpdateInfo.collectAsStateWithLifecycle()
    val tabs by viewModel.tabs.collectAsStateWithLifecycle()
    val activeTabId by viewModel.activeTabId.collectAsStateWithLifecycle()
    val omniboxText by viewModel.omniboxText.collectAsStateWithLifecycle()
    val screenMode by viewModel.screenMode.collectAsStateWithLifecycle()
    val activeSheet by viewModel.activeSheet.collectAsStateWithLifecycle()
    val isReaderModeActive by viewModel.isReaderModeActive.collectAsStateWithLifecycle()
    val readerArticleText by viewModel.readerArticleText.collectAsStateWithLifecycle()
    val isMusicModeActive by viewModel.isMusicModeActive.collectAsStateWithLifecycle()
    val audioOnlyHideVideo by viewModel.audioOnlyHideVideo.collectAsStateWithLifecycle()
    val systemPromptBanner by viewModel.systemPromptBanner.collectAsStateWithLifecycle()
    val lastSpokenInput by viewModel.lastSpokenHindiInput.collectAsStateWithLifecycle()
    val latestSummary by viewModel.latestPageReadableSummary.collectAsStateWithLifecycle()
    val reloadTrigger by viewModel.webViewReloadTrigger.collectAsStateWithLifecycle()

    val currentTab = remember(tabs, activeTabId) {
        tabs.firstOrNull { it.id == activeTabId } ?: tabs.first()
    }

    val webViewController = remember { WebViewController() }

    // Lazy TextToSpeech locked to English (India) [en-IN] so first-frame SurfaceSyncGroup is never blocked
    val ttsHolder = remember { arrayOfNulls<TextToSpeech>(1) }
    DisposableEffect(Unit) {
        onDispose {
            ttsHolder[0]?.stop()
            ttsHolder[0]?.shutdown()
            ttsHolder[0] = null
        }
    }

    val speakEnglishIndia: (String) -> Unit = remember(settings.voiceReadoutEnabled, context) {
        { message ->
            if (settings.voiceReadoutEnabled) {
                val existing = ttsHolder[0]
                if (existing != null) {
                    existing.language = HindiEnglishVoiceEngine.SYSTEM_LOCALE_EN_IN
                    existing.speak(message, TextToSpeech.QUEUE_FLUSH, null, "aegis_en_in_prompt")
                } else {
                    var newTts: TextToSpeech? = null
                    newTts = TextToSpeech(context.applicationContext) { status ->
                        if (status == TextToSpeech.SUCCESS) {
                            newTts?.language = HindiEnglishVoiceEngine.SYSTEM_LOCALE_EN_IN
                            newTts?.speak(message, TextToSpeech.QUEUE_FLUSH, null, "aegis_en_in_prompt")
                        }
                    }
                    ttsHolder[0] = newTts
                }
            }
        }
    }

    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val matches = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val spokenText = matches?.firstOrNull()
            if (!spokenText.isNullOrBlank()) {
                viewModel.processHindiOrEnglishVoiceInput(
                    transcript = spokenText,
                    onSpeakEnglishIndia = speakEnglishIndia
                )
            }
        }
    }

    val launchSpeechIntent = {
        runCatching {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                )
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-IN")
                putExtra(
                    RecognizerIntent.EXTRA_PROMPT,
                    "AAA Browser Voice Search (English India)"
                )
            }
            speechLauncher.launch(intent)
        }.onFailure {
            Toast.makeText(
                context,
                "Hardware speech service unavailable; use the voice command sheet.",
                Toast.LENGTH_SHORT
            ).show()
            viewModel.openModalSheet(ActiveModalSheet.VOICE_ASSISTANT)
        }
    }

    val audioPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { _ ->
        launchSpeechIntent()
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { _ ->
        viewModel.openGoogleMapsWithinBrowser("")
    }

    // Extract clean article paragraphs whenever Reader Mode is activated
    LaunchedEffect(isReaderModeActive, currentTab.url) {
        if (isReaderModeActive) {
            webViewController.extractReaderArticle { extracted ->
                viewModel.updateReaderArticleText(extracted)
            }
        }
    }

    // Handle back navigation inside web pages
    BackHandler(enabled = screenMode == BrowserScreenMode.STANDARD_BROWSER) {
        if (isReaderModeActive) {
            viewModel.closeReaderMode()
        } else if (!webViewController.goBack()) {
            viewModel.navigateHomeSpeedDial()
        }
    }

    val handleBookmarkOrUrlClick: (String) -> Unit = { targetUrl ->
        focusManager.clearFocus()
        if (targetUrl == ANDROID_AUTO_SETTINGS_URI ||
            targetUrl.equals("Android Auto settings", ignoreCase = true)
        ) {
            viewModel.closeModalSheet()
            launchPhoneAndroidAutoSettings(context)
        } else {
            viewModel.setScreenMode(BrowserScreenMode.STANDARD_BROWSER)
            viewModel.loadUrlOrSearch(targetUrl)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(EchoPitchBlack)
    ) {
        // Keep the Browser WebView Layer alive so YouTube background audio continues playing in Android Auto or Reader Mode!
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(EchoPitchBlack)
                .alpha(if (screenMode == BrowserScreenMode.STANDARD_BROWSER) 1f else 0.01f)
        ) {
            // 1. Echo Top App Bar ("AAA Browser" + Reader Mode + YouTube Music Mode + History + Shield + Car + "Y" Avatar)
            EchoTopAppBar(
                title = "AAA Browser",
                isReaderModeActive = isReaderModeActive,
                isMusicModeActive = isMusicModeActive,
                onHistoryClick = {
                    viewModel.openModalSheet(ActiveModalSheet.BOOKMARKS_AND_HISTORY)
                },
                onPrivacyStatsClick = {
                    viewModel.openModalSheet(ActiveModalSheet.PRIVACY_SHIELD)
                },
                onToggleReaderMode = {
                    viewModel.toggleReaderMode()
                },
                onToggleMusicMode = {
                    viewModel.toggleMusicMode()
                },
                onAndroidAutoClick = {
                    viewModel.setScreenMode(BrowserScreenMode.ANDROID_AUTO_CONSOLE)
                    speakEnglishIndia("Android Auto Driving Console activated.")
                },
                onProfileAvatarClick = {
                    viewModel.openModalSheet(ActiveModalSheet.PROFILE_QUICK_SHEET)
                }
            )

            // 2. Pinned Bookmarks Strip (YouTube, Google, Netflix, Android Auto settings)
            BookmarksQuickStrip(
                bookmarks = bookmarks,
                isLoading = currentTab.isLoading,
                progress = currentTab.progress,
                onBookmarkClick = handleBookmarkOrUrlClick
            )

            // 3. Optional YouTube Ad-Free Music Mode Controller Bar (Spotify-like experience with 0 ads & optional hidden video!)
            AnimatedVisibility(visible = isMusicModeActive) {
                YouTubeAdFreeMusicModeBar(
                    currentTitle = currentTab.title,
                    currentUrl = currentTab.url,
                    hideVideoCanvas = audioOnlyHideVideo,
                    onToggleHideVideoCanvas = { hide ->
                        viewModel.setAudioOnlyHideVideo(hide)
                    },
                    onSearchYouTubeSong = { songQuery ->
                        viewModel.openYouTubeAdFreeMusic(songQuery)
                    },
                    onPlayPause = {
                        webViewController.toggleMediaPlayPause()
                    },
                    onRewind10s = {
                        webViewController.seekBySeconds(-10)
                    },
                    onForward10s = {
                        webViewController.seekBySeconds(10)
                    },
                    onNextTrack = {
                        webViewController.skipToNextYouTubeTrack()
                    },
                    onCloseMusicMode = {
                        viewModel.toggleMusicMode()
                    }
                )
            }

            // 4. Main Browser WebView + Optional Reader Mode Overlay
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 80.dp)
            ) {
                AegisWebViewContainer(
                    url = currentTab.url,
                    settings = settings,
                    audioOnlyHideVideo = audioOnlyHideVideo || screenMode == BrowserScreenMode.ANDROID_AUTO_CONSOLE,
                    reloadTrigger = reloadTrigger,
                    controller = webViewController,
                    onPageStarted = viewModel::onWebPageStarted,
                    onPageProgress = viewModel::onWebPageProgress,
                    onPageFinished = { title, url, canGoBack, canGoForward, summary ->
                        viewModel.onWebPageFinished(title, url, canGoBack, canGoForward, summary)
                        if (isReaderModeActive) {
                            webViewController.extractReaderArticle { extracted ->
                                viewModel.updateReaderArticleText(extracted)
                            }
                        }
                    },
                    onRequestBlocked = viewModel::onRequestInterceptedAndBlocked,
                    onExternalMapsIntent = { uri ->
                        launchExternalGoogleMaps(context, uri.toString())
                    }
                )

                // Distraction-Free Reader Mode Overlay when active
                if (isReaderModeActive) {
                    EchoReaderModeOverlay(
                        pageTitle = currentTab.title,
                        pageUrl = currentTab.url,
                        extractedText = readerArticleText,
                        fallbackSummary = latestSummary,
                        onSpeakAloud = speakEnglishIndia,
                        onCloseReaderMode = viewModel::closeReaderMode
                    )
                }
            }
        }

        // Bottom Bar in Standard Browser Mode:
        // Far Left = Home Button, Center = Search Bar with Suggestions, Far Right = Cute Circle Page Info ("i") Button!
        if (screenMode == BrowserScreenMode.STANDARD_BROWSER) {
            Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                EchoFloatingBottomBar(
                    currentTab = currentTab,
                    isReaderModeActive = isReaderModeActive,
                    isMusicModeActive = isMusicModeActive,
                    onHomeLeftClick = {
                        viewModel.navigateHomeSpeedDial()
                    },
                    onSearchBarClick = {
                        viewModel.openModalSheet(ActiveModalSheet.SEARCH_OMNIBOX)
                    },
                    onToggleReaderMode = {
                        viewModel.toggleReaderMode()
                    },
                    onToggleMusicMode = {
                        viewModel.toggleMusicMode()
                    },
                    onVoiceSearchClick = {
                        viewModel.openModalSheet(ActiveModalSheet.VOICE_ASSISTANT)
                    },
                    onPageInfoRightClick = {
                        viewModel.openModalSheet(ActiveModalSheet.PAGE_INFO_SHEET)
                    }
                )
            }
        }

        // Full-Screen Overlays (Settings, Account, Android Auto Console)
        when (screenMode) {
            BrowserScreenMode.FULL_SETTINGS -> {
                EchoFullSettingsScreen(
                    settings = settings,
                    releaseUpdateInfo = releaseUpdateInfo,
                    onBack = { viewModel.setScreenMode(BrowserScreenMode.STANDARD_BROWSER) },
                    onOpenAccountScreen = { viewModel.setScreenMode(BrowserScreenMode.FULL_ACCOUNT) },
                    onOpenPrivacySheet = { viewModel.openModalSheet(ActiveModalSheet.PRIVACY_SHIELD) },
                    onOpenAndroidAutoConsole = {
                        viewModel.setScreenMode(BrowserScreenMode.ANDROID_AUTO_CONSOLE)
                    },
                    onOpenPhoneAndroidAutoSettings = {
                        launchPhoneAndroidAutoSettings(context)
                    },
                    onOpenGoogleMaps = {
                        viewModel.setScreenMode(BrowserScreenMode.STANDARD_BROWSER)
                        locationPermissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    },
                    onOpenYouTubeMusicMode = {
                        viewModel.setScreenMode(BrowserScreenMode.STANDARD_BROWSER)
                        viewModel.openYouTubeAdFreeMusic("latest hindi & punjabi songs")
                    },
                    onClearHistoryAndCache = viewModel::clearBrowsingHistory,
                    onRestoreDefaultBookmarks = viewModel::restoreDefaultFourBookmarks,
                    onCheckForUpdates = { slug -> viewModel.checkForAppUpdates(slug) },
                    onDownloadUpdateApk = { apkUrl, releasesUrl ->
                        AppUpdateManager.downloadApkUpdate(context, apkUrl, releasesUrl)
                    },
                    onOpenReleasesInBrowser = { releasesUrl ->
                        viewModel.setScreenMode(BrowserScreenMode.STANDARD_BROWSER)
                        viewModel.loadUrlOrSearch(releasesUrl)
                    }
                )
            }

            BrowserScreenMode.FULL_ACCOUNT -> {
                EchoAccountScreen(
                    settings = settings,
                    onBack = { viewModel.setScreenMode(BrowserScreenMode.FULL_SETTINGS) },
                    onToggleAdBlock = viewModel::toggleAdBlock,
                    onToggleTrackerProtection = viewModel::toggleTrackerProtection,
                    onOpenPhoneAndroidAutoSettings = {
                        launchPhoneAndroidAutoSettings(context)
                    },
                    onOpenGoogleMaps = {
                        viewModel.setScreenMode(BrowserScreenMode.STANDARD_BROWSER)
                        viewModel.openGoogleMapsWithinBrowser("")
                    },
                    onOpenSystemUpdate = {
                        viewModel.setScreenMode(BrowserScreenMode.FULL_SETTINGS)
                        viewModel.checkForAppUpdates()
                    }
                )
            }

            BrowserScreenMode.ANDROID_AUTO_CONSOLE -> {
                AndroidAutoDrivingConsole(
                    bookmarks = bookmarks,
                    settings = settings,
                    currentPageTitle = currentTab.title,
                    currentPageUrl = currentTab.url,
                    latestPageSummary = latestSummary,
                    systemPromptBanner = systemPromptBanner,
                    audioOnlyHideVideo = audioOnlyHideVideo,
                    onToggleAudioOnlyHideVideo = { hide ->
                        viewModel.setAudioOnlyHideVideo(hide)
                    },
                    onPlayYouTubeMusicInBackground = { query ->
                        viewModel.openYouTubeAdFreeMusic(query)
                    },
                    onMediaPlayPause = {
                        webViewController.toggleMediaPlayPause()
                    },
                    onMediaRewind10s = {
                        webViewController.seekBySeconds(-10)
                    },
                    onMediaForward10s = {
                        webViewController.seekBySeconds(10)
                    },
                    onMediaNextTrack = {
                        webViewController.skipToNextYouTubeTrack()
                    },
                    onExitCarMode = { viewModel.setScreenMode(BrowserScreenMode.STANDARD_BROWSER) },
                    onOpenBookmarkInBrowser = handleBookmarkOrUrlClick,
                    onOpenPhoneAndroidAutoSettings = {
                        launchPhoneAndroidAutoSettings(context)
                    },
                    onOpenGoogleMapsInBrowser = { query ->
                        viewModel.setScreenMode(BrowserScreenMode.STANDARD_BROWSER)
                        viewModel.openGoogleMapsWithinBrowser(query)
                    },
                    onLaunchExternalGoogleMaps = { query ->
                        launchExternalGoogleMaps(context, query)
                    },
                    onTriggerVoiceCommand = {
                        viewModel.openModalSheet(ActiveModalSheet.VOICE_ASSISTANT)
                    },
                    onSpeakTextInEnglishIndia = speakEnglishIndia,
                    onToggleDrivingMotionSafety = viewModel::toggleDrivingSafetyMode
                )
            }

            BrowserScreenMode.STANDARD_BROWSER -> Unit
        }
    }

    // Active Modal Bottom Sheets
    when (activeSheet) {
        ActiveModalSheet.PAGE_INFO_SHEET -> {
            EchoPageInfoSheet(
                currentTab = currentTab,
                settings = settings,
                bookmarks = bookmarks,
                isReaderModeActive = isReaderModeActive,
                isMusicModeActive = isMusicModeActive,
                onDismiss = viewModel::closeModalSheet,
                onToggleReaderMode = {
                    viewModel.closeModalSheet()
                    viewModel.toggleReaderMode()
                },
                onToggleMusicMode = {
                    viewModel.closeModalSheet()
                    viewModel.toggleMusicMode()
                },
                onReloadPage = {
                    viewModel.closeModalSheet()
                    webViewController.reload()
                },
                onGoBack = {
                    viewModel.closeModalSheet()
                    webViewController.goBack()
                },
                onGoForward = {
                    viewModel.closeModalSheet()
                    webViewController.goForward()
                },
                onBookmarkCurrentPage = {
                    viewModel.addBookmarkForCurrentPage()
                    viewModel.closeModalSheet()
                },
                onFindInPage = { query ->
                    webViewController.findInPage(query)
                },
                onFindNext = {
                    webViewController.findNextMatch(true)
                },
                onChangeTextZoom = { zoomPercent ->
                    webViewController.setPageTextZoom(zoomPercent)
                },
                onOpenBookmarkUrl = handleBookmarkOrUrlClick,
                onOpenFullSettings = {
                    viewModel.setScreenMode(BrowserScreenMode.FULL_SETTINGS)
                }
            )
        }

        ActiveModalSheet.PROFILE_QUICK_SHEET -> {
            EchoProfileQuickSheet(
                settings = settings,
                onDismiss = viewModel::closeModalSheet,
                onOpenAccountScreen = {
                    viewModel.closeModalSheet()
                    viewModel.setScreenMode(BrowserScreenMode.FULL_ACCOUNT)
                },
                onOpenPrivacyHub = {
                    viewModel.openModalSheet(ActiveModalSheet.PRIVACY_SHIELD)
                },
                onOpenDarkModeSettings = {
                    viewModel.openModalSheet(ActiveModalSheet.PRIVACY_SHIELD)
                },
                onToggleUniversalDarkMode = { enabled ->
                    viewModel.selectWebDarkModeStyle(
                        if (enabled) WebDarkModeStyle.SMART_DARK else WebDarkModeStyle.SITE_DEFAULT
                    )
                },
                onToggleAdBlockAndSync = viewModel::toggleAdBlock,
                onOpenFullSettingsScreen = {
                    viewModel.closeModalSheet()
                    viewModel.setScreenMode(BrowserScreenMode.FULL_SETTINGS)
                },
                onOpenSystemUpdate = {
                    viewModel.closeModalSheet()
                    viewModel.setScreenMode(BrowserScreenMode.FULL_SETTINGS)
                    viewModel.checkForAppUpdates()
                }
            )
        }

        ActiveModalSheet.SEARCH_OMNIBOX -> {
            EchoSearchOmniboxSheet(
                initialQuery = "",
                bookmarks = bookmarks,
                history = history,
                onDismiss = viewModel::closeModalSheet,
                onSubmitUrlOrSearch = { input ->
                    viewModel.closeModalSheet()
                    handleBookmarkOrUrlClick(input)
                },
                onOpenYouTubeMusicSearch = { songQuery ->
                    viewModel.closeModalSheet()
                    viewModel.openYouTubeAdFreeMusic(songQuery)
                },
                onTriggerVoiceSearch = {
                    viewModel.openModalSheet(ActiveModalSheet.VOICE_ASSISTANT)
                }
            )
        }

        ActiveModalSheet.PRIVACY_SHIELD -> {
            PrivacyShieldAndDarkModeSheet(
                settings = settings,
                releaseUpdateInfo = releaseUpdateInfo,
                currentTab = currentTab,
                blockedEvents = blockedEvents,
                onDismiss = viewModel::closeModalSheet,
                onCheckForUpdates = { slug ->
                    viewModel.checkForAppUpdates(slug)
                },
                onDownloadAndInstallUpdate = { apkUrl, releasesUrl ->
                    AppUpdateManager.downloadApkUpdate(context, apkUrl, releasesUrl)
                },
                onOpenReleasesInBrowser = { releasesUrl ->
                    viewModel.loadUrlOrSearch(releasesUrl)
                },
                onToggleAdBlock = viewModel::toggleAdBlock,
                onToggleTrackerProtection = viewModel::toggleTrackerProtection,
                onToggleStripParams = viewModel::toggleStripTrackingParams,
                onToggleBlockThirdPartyCookies = viewModel::toggleBlockThirdPartyCookies,
                onToggleAntiFingerprinting = viewModel::toggleAntiFingerprinting,
                onToggleHttpsUpgrade = viewModel::toggleHttpsUpgrade,
                onSelectDarkModeStyle = viewModel::selectWebDarkModeStyle,
                onToggleDesktopSite = viewModel::toggleDesktopSite,
                onClearBlockedLog = viewModel::clearBlockedTrackerLog
            )
        }

        ActiveModalSheet.BOOKMARKS_AND_HISTORY -> {
            BookmarksAndHistorySheet(
                bookmarks = bookmarks,
                history = history,
                onDismiss = viewModel::closeModalSheet,
                onOpenUrl = handleBookmarkOrUrlClick,
                onAddCustomBookmark = viewModel::addCustomBookmark,
                onDeleteBookmark = viewModel::deleteBookmark,
                onRestoreDefaults = viewModel::restoreDefaultFourBookmarks,
                onClearHistory = viewModel::clearBrowsingHistory
            )
        }

        ActiveModalSheet.GOOGLE_MAPS_HUB -> {
            GoogleMapsAssistantSheet(
                onDismiss = viewModel::closeModalSheet,
                onOpenMapsInBrowser = viewModel::openGoogleMapsWithinBrowser,
                onLaunchMapsApp = { query ->
                    viewModel.closeModalSheet()
                    launchExternalGoogleMaps(context, query)
                }
            )
        }

        ActiveModalSheet.VOICE_ASSISTANT -> {
            HindiEnglishVoiceSheet(
                lastSpokenInput = lastSpokenInput,
                systemPromptBanner = systemPromptBanner,
                onDismiss = viewModel::closeModalSheet,
                onStartHardwareSpeechRecognition = {
                    audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                },
                onExecuteVoiceCommand = { phrase ->
                    viewModel.processHindiOrEnglishVoiceInput(
                        transcript = phrase,
                        onSpeakEnglishIndia = speakEnglishIndia
                    )
                },
                onSpeakCurrentPromptAloud = {
                    speakEnglishIndia(systemPromptBanner)
                }
            )
        }

        ActiveModalSheet.TABS_SWITCHER -> {
            TabsSwitcherSheet(
                tabs = tabs,
                activeTabId = activeTabId,
                onDismiss = viewModel::closeModalSheet,
                onSelectTab = viewModel::selectTab,
                onCloseTab = viewModel::closeTab,
                onNewTab = { viewModel.createNewTab() }
            )
        }

        ActiveModalSheet.NONE -> Unit
    }
}

@Composable
private fun BookmarksQuickStrip(
    bookmarks: List<BookmarkEntity>,
    isLoading: Boolean,
    progress: Int,
    onBookmarkClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(EchoPitchBlack)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 14.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            bookmarks.forEach { bm ->
                val dotColor = Color(bm.badgeColorHex)
                Surface(
                    shape = RoundedCornerShape(50),
                    color = EchoCardSurface,
                    modifier = Modifier
                        .border(1.dp, EchoPillBorder, RoundedCornerShape(50))
                        .clickable { onBookmarkClick(bm.url) }
                        .testTag("top_strip_bookmark_${bm.displayDomain}")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(dotColor)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = bm.displayDomain,
                            style = MaterialTheme.typography.labelMedium,
                            color = TextPrimaryDark
                        )
                    }
                }
            }
        }

        AnimatedVisibility(visible = isLoading) {
            LinearProgressIndicator(
                progress = { (progress.coerceIn(5, 100)) / 100f },
                color = ElectricCyan,
                trackColor = EchoPitchBlack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
            )
        }
    }
}
