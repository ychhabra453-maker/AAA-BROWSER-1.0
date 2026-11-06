package com.example.engine

import android.net.Uri
import android.webkit.WebResourceResponse
import java.io.ByteArrayInputStream
import java.util.Locale

data class InterceptVerdict(
    val shouldBlock: Boolean,
    val matchedDomain: String = "",
    val category: String = "" // "AD_NETWORK" or "TRACKER"
)

data class UrlSanitizeResult(
    val cleanUrl: String,
    val strippedParamsCount: Int,
    val strippedParamNames: List<String>
)

object PrivacyShieldEngine {

    private val AD_NETWORK_DOMAINS = setOf(
        "doubleclick.net",
        "googlesyndication.com",
        "googleadservices.com",
        "adservice.google.com",
        "adservice.google.co.in",
        "pagead2.googlesyndication.com",
        "tpc.googlesyndication.com",
        "adnxs.com",
        "adsrvr.org",
        "adform.net",
        "adcolony.com",
        "amazon-adsystem.com",
        "aax.amazon-adsystem.com",
        "criteo.com",
        "criteo.net",
        "taboola.com",
        "trc.taboola.com",
        "outbrain.com",
        "widgets.outbrain.com",
        "pubmatic.com",
        "rubiconproject.com",
        "openx.net",
        "moatads.com",
        "medianet.com",
        "media.net",
        "popads.net",
        "propellerads.com",
        "revcontent.com",
        "smartadserver.com",
        "teads.tv",
        "unityads.unity3d.com",
        "vungle.com",
        "yieldmo.com",
        "zedo.com",
        "2mdn.net",
        "adroll.com",
        "advertising.com",
        "applovin.com",
        "bidswitch.net",
        "casalemedia.com",
        "indexexchange.com",
        "lijit.com",
        "sharethrough.com",
        "sovrn.com",
        "triplelift.com"
    )

    private val TRACKER_DOMAINS = setOf(
        "google-analytics.com",
        "ssl.google-analytics.com",
        "googletagmanager.com",
        "googletagservices.com",
        "scorecardresearch.com",
        "sb.scorecardresearch.com",
        "connect.facebook.net",
        "graph.facebook.com",
        "pixel.facebook.com",
        "analytics.tiktok.com",
        "ads-twitter.com",
        "analytics.twitter.com",
        "t.co/i/adsct",
        "snap.licdn.com",
        "px.ads.linkedin.com",
        "bat.bing.com",
        "clarity.ms",
        "hotjar.com",
        "static.hotjar.com",
        "mixpanel.com",
        "api.mixpanel.com",
        "segment.io",
        "segment.com",
        "amplitude.com",
        "api.amplitude.com",
        "fullstory.com",
        "mouseflow.com",
        "crazyegg.com",
        "quantserve.com",
        "pixel.quantserve.com",
        "newrelic.com",
        "bam.nr-data.net",
        "branch.io",
        "app.link",
        "appsflyer.com",
        "adjust.com",
        "chartbeat.com",
        "Chartbeat.net",
        "omtrdc.net",
        "demdex.net",
        "everesttech.net",
        "krxd.net",
        "bluekai.com",
        "exelator.com",
        "tapad.com",
        "rlcdn.com",
        "Pardot.com",
        "hubspot.com",
        "hs-analytics.net",
        "hs-scripts.com",
        "bugsnag.com",
        "sentry.io",
        "loggly.com",
        "optimizely.com",
        "clevertap.com",
        "moengage.com",
        "webengage.com"
    )

    private val TRACKING_QUERY_PARAMS = setOf(
        "utm_source",
        "utm_medium",
        "utm_campaign",
        "utm_term",
        "utm_content",
        "utm_id",
        "fbclid",
        "gclid",
        "gclsrc",
        "dclid",
        "msclkid",
        "twclid",
        "ttclid",
        "mc_cid",
        "mc_eid",
        "_ga",
        "_gl",
        "igshid",
        "vero_id",
        "oly_enc_id",
        "oly_anon_id",
        "rb_clickid",
        "s_kwcid"
    )

    private val AD_PATH_KEYWORDS = listOf(
        "/pagead/",
        "/ads/ga-audiences",
        "/adserver/",
        "/adframe",
        "/ads.js",
        "/adbanner",
        "/prebid",
        "/gpt.js",
        "/show_ads.js"
    )

    private fun extractHostCompat(url: String): String {
        return runCatching {
            val withoutScheme = url.substringAfter("://", "")
            withoutScheme.substringBefore("/").substringBefore("?").substringBefore(":").trim()
        }.getOrDefault("")
    }

    fun evaluateRequest(
        requestUrl: String,
        adBlockEnabled: Boolean,
        trackerProtectionEnabled: Boolean
    ): InterceptVerdict {
        val lower = requestUrl.lowercase(Locale.ROOT)
        val host = extractHostCompat(lower)
        if (host.isEmpty()) return InterceptVerdict(false)

        if (adBlockEnabled) {
            val matchedAdDomain = AD_NETWORK_DOMAINS.firstOrNull { domain ->
                host == domain || host.endsWith(".$domain")
            }
            if (matchedAdDomain != null) {
                return InterceptVerdict(
                    shouldBlock = true,
                    matchedDomain = matchedAdDomain,
                    category = "AD_NETWORK"
                )
            }
            if (AD_PATH_KEYWORDS.any { lower.contains(it) }) {
                return InterceptVerdict(
                    shouldBlock = true,
                    matchedDomain = host,
                    category = "AD_NETWORK"
                )
            }
        }

        if (trackerProtectionEnabled) {
            val matchedTracker = TRACKER_DOMAINS.firstOrNull { domain ->
                host == domain || host.endsWith(".$domain")
            }
            if (matchedTracker != null) {
                return InterceptVerdict(
                    shouldBlock = true,
                    matchedDomain = matchedTracker,
                    category = "TRACKER"
                )
            }
        }

        return InterceptVerdict(shouldBlock = false)
    }

    fun createEmptyBlockedResponse(): WebResourceResponse {
        return WebResourceResponse(
            "text/plain",
            "UTF-8",
            ByteArrayInputStream(ByteArray(0))
        )
    }

    fun sanitizeInputUrl(
        rawInput: String,
        stripTrackingParams: Boolean,
        httpsUpgrade: Boolean
    ): UrlSanitizeResult {
        val trimmed = rawInput.trim()
        if (trimmed.isEmpty()) {
            return UrlSanitizeResult("https://www.google.com", 0, emptyList())
        }

        // Check if input is a search query rather than a domain or URL
        val looksLikeUrl = trimmed.startsWith("http://", ignoreCase = true) ||
            trimmed.startsWith("https://", ignoreCase = true) ||
            trimmed.startsWith("geo:", ignoreCase = true) ||
            (!trimmed.contains(" ") && trimmed.contains(".") && trimmed.substringAfterLast(".").length >= 2)

        var workingUrl = if (!looksLikeUrl) {
            "https://www.google.com/search?hl=en-IN&gl=IN&q=${Uri.encode(trimmed)}"
        } else if (!trimmed.startsWith("http://", true) &&
            !trimmed.startsWith("https://", true) &&
            !trimmed.startsWith("geo:", true)
        ) {
            "https://$trimmed"
        } else {
            trimmed
        }

        if (httpsUpgrade && workingUrl.startsWith("http://", ignoreCase = true)) {
            val host = runCatching { Uri.parse(workingUrl).host.orEmpty() }.getOrDefault("")
            if (host != "localhost" && !host.startsWith("127.") && !host.startsWith("192.168.")) {
                workingUrl = "https://" + workingUrl.substring(7)
            }
        }

        if (!stripTrackingParams) {
            return UrlSanitizeResult(workingUrl, 0, emptyList())
        }

        return runCatching {
            val uri = Uri.parse(workingUrl)
            if (uri.isOpaque || uri.queryParameterNames.isEmpty()) {
                return@runCatching UrlSanitizeResult(workingUrl, 0, emptyList())
            }
            val stripped = mutableListOf<String>()
            val builder = uri.buildUpon().clearQuery()
            for (name in uri.queryParameterNames) {
                val lowerName = name.lowercase(Locale.ROOT)
                if (lowerName in TRACKING_QUERY_PARAMS || lowerName.startsWith("utm_")) {
                    stripped.add(name)
                } else {
                    uri.getQueryParameters(name).forEach { value ->
                        builder.appendQueryParameter(name, value)
                    }
                }
            }
            UrlSanitizeResult(
                cleanUrl = builder.build().toString(),
                strippedParamsCount = stripped.size,
                strippedParamNames = stripped
            )
        }.getOrDefault(UrlSanitizeResult(workingUrl, 0, emptyList()))
    }

    fun getPrivacyHeaders(): Map<String, String> = mapOf(
        "Accept-Language" to "en-IN,en;q=0.9",
        "DNT" to "1",
        "Sec-GPC" to "1"
    )

    fun buildPrivacyAndCosmeticAdBlockJs(
        adBlockEnabled: Boolean,
        antiFingerprintingEnabled: Boolean
    ): String {
        return """
            (function() {
                try {
                    if ($antiFingerprintingEnabled) {
                        Object.defineProperty(navigator, 'doNotTrack', { get: () => '1' });
                        Object.defineProperty(navigator, 'globalPrivacyControl', { get: () => true });
                        Object.defineProperty(navigator, 'language', { get: () => 'en-IN' });
                        Object.defineProperty(navigator, 'languages', { get: () => ['en-IN', 'en'] });
                        if (navigator.sendBeacon) {
                            navigator.sendBeacon = function() { return true; };
                        }
                    }
                    if ($adBlockEnabled) {
                        var styleId = 'aegis-cosmetic-adblock-style';
                        if (!document.getElementById(styleId)) {
                            var style = document.createElement('style');
                            style.id = styleId;
                            style.innerHTML = `
                                .adsbygoogle,
                                ins.adsbygoogle,
                                [id^="google_ads_iframe"],
                                [id^="div-gpt-ad"],
                                [class*="ad-container"],
                                [class*="ad-banner"],
                                [class*="taboola"],
                                [class*="outbrain"],
                                [data-ad-slot],
                                [data-google-query-id],
                                ytd-ad-slot-renderer,
                                ytd-in-feed-ad-layout-renderer,
                                ytd-promoted-sparkles-web-renderer,
                                .ytp-ad-overlay-container,
                                .ytp-ad-message-container {
                                    display: none !important;
                                    visibility: hidden !important;
                                    height: 0 !important;
                                    min-height: 0 !important;
                                    pointer-events: none !important;
                                }
                            `;
                            (document.head || document.documentElement).appendChild(style);
                        }
                    }
                } catch(e) {}
            })();
        """.trimIndent()
    }
}
