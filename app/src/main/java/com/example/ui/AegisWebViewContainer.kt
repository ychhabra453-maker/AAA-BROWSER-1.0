package com.example.ui

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.GeolocationPermissions
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.data.ANDROID_AUTO_SETTINGS_URI
import com.example.data.PrivacyAndDisplaySettings
import com.example.data.SIGNATURE_FOOTER_TEXT
import com.example.data.WebDarkModeStyle
import com.example.engine.PrivacyShieldEngine
import com.example.engine.WebDarkModeEngine
import com.example.ui.theme.EchoCardSurface
import com.example.ui.theme.EchoPillBorder
import com.example.ui.theme.EchoPitchBlack
import com.example.ui.theme.EchoSubtleLabel
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.EmeraldShield
import kotlinx.coroutines.delay

private const val MOBILE_CHROME_USER_AGENT =
    "Mozilla/5.0 (Linux; Android 14; Pixel 8) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Mobile Safari/537.36"
private const val DESKTOP_CHROME_USER_AGENT =
    "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36"

class WebViewController {
    var webView: WebView? = null

    fun goBack(): Boolean {
        val wv = webView ?: return false
        return if (wv.canGoBack()) {
            wv.goBack()
            true
        } else {
            false
        }
    }

    fun goForward(): Boolean {
        val wv = webView ?: return false
        return if (wv.canGoForward()) {
            wv.goForward()
            true
        } else {
            false
        }
    }

    fun reload() {
        webView?.reload()
    }

    fun toggleMediaPlayPause(onStateChanged: ((Boolean) -> Unit)? = null) {
        val js = """
            (function() {
                var v = document.querySelector('video') || document.querySelector('audio');
                if (!v) return 'NO_MEDIA';
                if (v.paused) { v.play(); return 'PLAYING'; }
                else { v.pause(); return 'PAUSED'; }
            })();
        """.trimIndent()
        webView?.evaluateJavascript(js) { res ->
            val playing = res?.contains("PLAYING") == true
            onStateChanged?.invoke(playing)
        }
    }

    fun seekBySeconds(deltaSeconds: Int) {
        val js = """
            (function() {
                var v = document.querySelector('video') || document.querySelector('audio');
                if (v) { v.currentTime = Math.max(0, v.currentTime + ($deltaSeconds)); }
            })();
        """.trimIndent()
        webView?.evaluateJavascript(js, null)
    }

    fun skipToNextYouTubeTrack() {
        val js = """
            (function() {
                var nextBtn = document.querySelector('.ytp-next-button') ||
                              document.querySelector('button[aria-label*="Next"]') ||
                              document.querySelector('a.ytp-next-button');
                if (nextBtn) { nextBtn.click(); return; }
                var items = document.querySelectorAll('ytm-media-item a, ytd-compact-video-renderer a#thumbnail');
                if (items.length > 1) { items[1].click(); }
            })();
        """.trimIndent()
        webView?.evaluateJavascript(js, null)
    }

    fun extractReaderArticle(onExtracted: (String) -> Unit) {
        val wv = webView
        if (wv == null) {
            onExtracted("")
            return
        }
        wv.evaluateJavascript(WebDarkModeEngine.buildExtractReaderArticleJs()) { raw ->
            val cleaned = raw
                ?.removeSurrounding("\"")
                ?.replace("\\n", "\n")
                ?.replace("\\\"", "\"")
                ?.replace("\\t", " ")
                ?.trim()
                .orEmpty()
            onExtracted(cleaned)
        }
    }

    fun findInPage(query: String) {
        val wv = webView ?: return
        if (query.isBlank()) {
            wv.clearMatches()
        } else {
            wv.findAllAsync(query.trim())
        }
    }

    fun findNextMatch(forward: Boolean = true) {
        webView?.findNext(forward)
    }

    fun clearFindMatches() {
        webView?.clearMatches()
    }

    fun setPageTextZoom(zoomPercent: Int) {
        webView?.settings?.textZoom = zoomPercent.coerceIn(50, 200)
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun AegisWebViewContainer(
    url: String,
    settings: PrivacyAndDisplaySettings,
    audioOnlyHideVideo: Boolean = false,
    reloadTrigger: Int,
    controller: WebViewController,
    onPageStarted: (String) -> Unit,
    onPageProgress: (Int) -> Unit,
    onPageFinished: (title: String, url: String, canGoBack: Boolean, canGoForward: Boolean, summary: String) -> Unit,
    onRequestBlocked: (domain: String, pageUrl: String, category: String) -> Unit,
    onExternalMapsIntent: (Uri) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Defer WebView creation until after the Activity's initial window SurfaceSyncGroup completes
    var isSurfaceReadyForWebView by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        withFrameNanos { }
        withFrameNanos { }
        delay(220)
        isSurfaceReadyForWebView = true
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(EchoPitchBlack)
    ) {
        if (!isSurfaceReadyForWebView) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(EchoPitchBlack),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(
                        color = EmeraldShield,
                        modifier = Modifier.size(32.dp),
                        strokeWidth = 2.5.dp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "AAA Browser Privacy Shield Ready…",
                        style = MaterialTheme.typography.bodySmall,
                        color = EchoSubtleLabel
                    )
                }
            }
        } else {
            DeferredWebViewHost(
                context = context,
                url = url,
                settings = settings,
                audioOnlyHideVideo = audioOnlyHideVideo,
                reloadTrigger = reloadTrigger,
                controller = controller,
                onPageStarted = onPageStarted,
                onPageProgress = onPageProgress,
                onPageFinished = onPageFinished,
                onRequestBlocked = onRequestBlocked,
                onExternalMapsIntent = onExternalMapsIntent,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
        }

        // Persistent Signature Footer at the bottom of every browsed page
        PageSignatureFooterBar()
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun DeferredWebViewHost(
    context: Context,
    url: String,
    settings: PrivacyAndDisplaySettings,
    audioOnlyHideVideo: Boolean,
    reloadTrigger: Int,
    controller: WebViewController,
    onPageStarted: (String) -> Unit,
    onPageProgress: (Int) -> Unit,
    onPageFinished: (title: String, url: String, canGoBack: Boolean, canGoForward: Boolean, summary: String) -> Unit,
    onRequestBlocked: (domain: String, pageUrl: String, category: String) -> Unit,
    onExternalMapsIntent: (Uri) -> Unit,
    modifier: Modifier = Modifier
) {
    val webViewInstance = remember(context) {
        WebView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            // Software layer avoids competing SurfaceSyncGroup / hardware RenderNode deadlocks in emulators
            setLayerType(View.LAYER_TYPE_SOFTWARE, null)
            setBackgroundColor(android.graphics.Color.parseColor("#000000"))

            this.settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                loadsImagesAutomatically = true
                useWideViewPort = true
                loadWithOverviewMode = true
                builtInZoomControls = true
                displayZoomControls = false
                setSupportZoom(true)
                mediaPlaybackRequiresUserGesture = false
                setGeolocationEnabled(true)
                mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
                userAgentString = MOBILE_CHROME_USER_AGENT

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    isAlgorithmicDarkeningAllowed = true
                }
                @Suppress("DEPRECATION")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    forceDark = WebSettings.FORCE_DARK_ON
                }
            }
        }
    }

    DisposableEffect(webViewInstance) {
        controller.webView = webViewInstance
        onDispose {
            if (controller.webView === webViewInstance) {
                controller.webView = null
            }
            runCatching {
                webViewInstance.stopLoading()
            }
        }
    }

    LaunchedEffect(settings, audioOnlyHideVideo, reloadTrigger) {
        runCatching {
            val cookieManager = CookieManager.getInstance()
            cookieManager.setAcceptCookie(true)
            cookieManager.setAcceptThirdPartyCookies(webViewInstance, !settings.blockThirdPartyCookies)
        }

        webViewInstance.settings.userAgentString = if (settings.desktopModeEnabled) {
            DESKTOP_CHROME_USER_AGENT
        } else {
            MOBILE_CHROME_USER_AGENT
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            webViewInstance.settings.isAlgorithmicDarkeningAllowed =
                settings.webDarkModeStyle != WebDarkModeStyle.SITE_DEFAULT
        }

        val darkJs = WebDarkModeEngine.buildSeamlessDarkModeJs(settings.webDarkModeStyle)
        val privacyJs = PrivacyShieldEngine.buildPrivacyAndCosmeticAdBlockJs(
            adBlockEnabled = settings.adBlockEnabled,
            antiFingerprintingEnabled = settings.antiFingerprintingEnabled
        )
        val ytAudioJs = WebDarkModeEngine.buildYouTubeAdFreeAudioModeJs(audioOnlyHideVideo)
        webViewInstance.evaluateJavascript(darkJs, null)
        webViewInstance.evaluateJavascript(privacyJs, null)
        webViewInstance.evaluateJavascript(ytAudioJs, null)
    }

    LaunchedEffect(reloadTrigger) {
        if (reloadTrigger > 0) {
            webViewInstance.reload()
        }
    }

    AndroidView(
        factory = {
            webViewInstance.apply {
                webViewClient = object : WebViewClient() {

                    override fun shouldInterceptRequest(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): WebResourceResponse? {
                        val reqUrl = request?.url?.toString() ?: return null
                        val verdict = PrivacyShieldEngine.evaluateRequest(
                            requestUrl = reqUrl,
                            adBlockEnabled = settings.adBlockEnabled,
                            trackerProtectionEnabled = settings.trackerProtectionEnabled
                        )
                        if (verdict.shouldBlock) {
                            view?.post {
                                onRequestBlocked(verdict.matchedDomain, view.url.orEmpty(), verdict.category)
                            }
                            return PrivacyShieldEngine.createEmptyBlockedResponse()
                        }
                        return super.shouldInterceptRequest(view, request)
                    }

                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        val targetUri = request?.url ?: return false
                        val scheme = targetUri.scheme?.lowercase() ?: return false
                        val fullUrl = targetUri.toString()

                        if (fullUrl == ANDROID_AUTO_SETTINGS_URI) {
                            launchPhoneAndroidAutoSettings(context)
                            return true
                        }

                        if (scheme == "geo" || scheme == "google.navigation") {
                            onExternalMapsIntent(targetUri)
                            return true
                        }
                        if (scheme == "intent") {
                            runCatching {
                                val parsed = Intent.parseUri(fullUrl, Intent.URI_INTENT_SCHEME)
                                val fallbackUrl = parsed.getStringExtra("browser_fallback_url")
                                if (!fallbackUrl.isNullOrBlank()) {
                                    view?.loadUrl(fallbackUrl, PrivacyShieldEngine.getPrivacyHeaders())
                                } else {
                                    parsed.data?.let { onExternalMapsIntent(it) }
                                }
                            }
                            return true
                        }

                        if (scheme == "http" || scheme == "https") {
                            val sanitized = PrivacyShieldEngine.sanitizeInputUrl(
                                rawInput = fullUrl,
                                stripTrackingParams = settings.stripTrackingParams,
                                httpsUpgrade = settings.httpsUpgradeEnabled
                            )
                            if (sanitized.cleanUrl != fullUrl) {
                                view?.loadUrl(sanitized.cleanUrl, PrivacyShieldEngine.getPrivacyHeaders())
                                return true
                            }
                        }
                        return false
                    }

                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        super.onPageStarted(view, url, favicon)
                        val safeUrl = url ?: return
                        onPageStarted(safeUrl)
                        view?.evaluateJavascript(
                            WebDarkModeEngine.buildSeamlessDarkModeJs(settings.webDarkModeStyle),
                            null
                        )
                        view?.evaluateJavascript(
                            PrivacyShieldEngine.buildPrivacyAndCosmeticAdBlockJs(
                                adBlockEnabled = settings.adBlockEnabled,
                                antiFingerprintingEnabled = settings.antiFingerprintingEnabled
                            ),
                            null
                        )
                        view?.evaluateJavascript(
                            WebDarkModeEngine.buildYouTubeAdFreeAudioModeJs(audioOnlyHideVideo),
                            null
                        )
                    }

                    override fun onPageCommitVisible(view: WebView?, url: String?) {
                        super.onPageCommitVisible(view, url)
                        view?.evaluateJavascript(
                            WebDarkModeEngine.buildSeamlessDarkModeJs(settings.webDarkModeStyle),
                            null
                        )
                        view?.evaluateJavascript(
                            WebDarkModeEngine.buildYouTubeAdFreeAudioModeJs(audioOnlyHideVideo),
                            null
                        )
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        val wv = view ?: return
                        val finalUrl = url ?: wv.url.orEmpty()
                        wv.evaluateJavascript(
                            WebDarkModeEngine.buildSeamlessDarkModeJs(settings.webDarkModeStyle),
                            null
                        )
                        wv.evaluateJavascript(
                            PrivacyShieldEngine.buildPrivacyAndCosmeticAdBlockJs(
                                adBlockEnabled = settings.adBlockEnabled,
                                antiFingerprintingEnabled = settings.antiFingerprintingEnabled
                            ),
                            null
                        )
                        wv.evaluateJavascript(
                            WebDarkModeEngine.buildYouTubeAdFreeAudioModeJs(audioOnlyHideVideo),
                            null
                        )
                        val extractSummaryJs = """
                            (function() {
                                try {
                                    var title = document.title || '';
                                    var p = document.querySelector('p');
                                    var snippet = p ? (p.innerText || '').substring(0, 220) : '';
                                    return (title + '. ' + snippet).replace(/\s+/g, ' ').trim();
                                } catch(e) { return document.title || ''; }
                            })();
                        """.trimIndent()
                        wv.evaluateJavascript(extractSummaryJs) { rawResult ->
                            val cleanedSummary = rawResult
                                ?.removeSurrounding("\"")
                                ?.replace("\\n", " ")
                                ?.replace("\\\"", "\"")
                                ?.trim()
                                .orEmpty()
                            onPageFinished(
                                wv.title.orEmpty(),
                                finalUrl,
                                wv.canGoBack(),
                                wv.canGoForward(),
                                cleanedSummary
                            )
                        }
                    }

                    override fun onRenderProcessGone(
                        view: WebView?,
                        detail: android.webkit.RenderProcessGoneDetail?
                    ): Boolean {
                        view?.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
                        return true
                    }
                }

                webChromeClient = object : WebChromeClient() {
                    override fun onProgressChanged(view: WebView?, newProgress: Int) {
                        super.onProgressChanged(view, newProgress)
                        onPageProgress(newProgress)
                    }

                    override fun onGeolocationPermissionsShowPrompt(
                        origin: String?,
                        callback: GeolocationPermissions.Callback?
                    ) {
                        callback?.invoke(origin, true, false)
                    }
                }

                loadUrl(url, PrivacyShieldEngine.getPrivacyHeaders())
            }
        },
        update = { wv ->
            val currentLoaded = wv.url.orEmpty()
            if (url.isNotBlank() && currentLoaded != url && !currentLoaded.startsWith(url)) {
                wv.loadUrl(url, PrivacyShieldEngine.getPrivacyHeaders())
            }
        },
        modifier = modifier.background(EchoPitchBlack)
    )
}

@Composable
fun PageSignatureFooterBar(modifier: Modifier = Modifier) {
    Surface(
        color = EchoCardSurface,
        modifier = modifier
            .fillMaxWidth()
            .border(0.5.dp, EchoPillBorder)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp, horizontal = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = SIGNATURE_FOOTER_TEXT,
                style = MaterialTheme.typography.labelMedium,
                color = ElectricCyan,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        }
    }
}

fun launchPhoneAndroidAutoSettings(context: Context) {
    val gearheadPackage = "com.google.android.projection.gearhead"

    val candidateActivities = listOf(
        "com.google.android.projection.gearhead.companion.settings.DefaultSettingsActivity",
        "com.google.android.projection.gearhead.companion.settings.CarSettingsActivity",
        "com.google.android.projection.gearhead.companion.MainActivity"
    )
    for (activityName in candidateActivities) {
        val explicitIntent = Intent().apply {
            component = ComponentName(gearheadPackage, activityName)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        if (runCatching { context.startActivity(explicitIntent) }.isSuccess) {
            return
        }
    }

    val packageLaunchIntent = context.packageManager.getLaunchIntentForPackage(gearheadPackage)?.apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    if (packageLaunchIntent != null && runCatching { context.startActivity(packageLaunchIntent) }.isSuccess) {
        return
    }

    val connectedDeviceIntent = Intent("android.settings.CONNECTED_DEVICE_SETTINGS").apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    if (runCatching { context.startActivity(connectedDeviceIntent) }.isSuccess) {
        Toast.makeText(
            context,
            "Opened Connected Devices → Select Android Auto",
            Toast.LENGTH_SHORT
        ).show()
        return
    }

    runCatching {
        val settingsIntent = Intent(Settings.ACTION_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(settingsIntent)
    }
}

fun launchExternalGoogleMaps(context: Context, queryOrUri: String) {
    runCatching {
        val uri = if (queryOrUri.startsWith("geo:") || queryOrUri.startsWith("google.navigation:") || queryOrUri.startsWith("http")) {
            Uri.parse(queryOrUri)
        } else {
            Uri.parse("geo:0,0?q=${Uri.encode(queryOrUri)}")
        }
        val mapIntent = Intent(Intent.ACTION_VIEW, uri).apply {
            setPackage("com.google.android.apps.maps")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        if (mapIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(mapIntent)
        } else {
            val fallbackIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.google.com/maps/search/${Uri.encode(queryOrUri)}?hl=en-IN&gl=IN")
            ).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(fallbackIntent)
        }
    }
}
