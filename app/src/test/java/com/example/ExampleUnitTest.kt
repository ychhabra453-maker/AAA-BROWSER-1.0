package com.example

import com.example.data.DEFAULT_BOOKMARKS
import com.example.data.WebDarkModeStyle
import com.example.engine.HindiEnglishVoiceEngine
import com.example.engine.PrivacyShieldEngine
import com.example.engine.VoiceCommandAction
import com.example.engine.WebDarkModeEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun defaultLocaleIsEnglishIndia() {
        val locale = HindiEnglishVoiceEngine.SYSTEM_LOCALE_EN_IN
        assertEquals("en", locale.language)
        assertEquals("IN", locale.country)
    }

    @Test
    fun defaultBookmarksContainRequiredFourDomains() {
        val domains = DEFAULT_BOOKMARKS.map { it.displayDomain }
        assertEquals(
            listOf(
                "www.youtube.com",
                "www.google.com",
                "www.netflix.com",
                "Android Auto settings"
            ),
            domains
        )
    }

    @Test
    fun privacyShieldBlocksAdsAndTrackers() {
        val adVerdict = PrivacyShieldEngine.evaluateRequest(
            requestUrl = "https://pagead2.googlesyndication.com/pagead/js/adsbygoogle.js",
            adBlockEnabled = true,
            trackerProtectionEnabled = true
        )
        assertTrue(adVerdict.shouldBlock)
        assertEquals("AD_NETWORK", adVerdict.category)

        val trackerVerdict = PrivacyShieldEngine.evaluateRequest(
            requestUrl = "https://www.google-analytics.com/analytics.js",
            adBlockEnabled = true,
            trackerProtectionEnabled = true
        )
        assertTrue(trackerVerdict.shouldBlock)
        assertEquals("TRACKER", trackerVerdict.category)

        val legitimateVerdict = PrivacyShieldEngine.evaluateRequest(
            requestUrl = "https://www.youtube.com/watch?v=123",
            adBlockEnabled = true,
            trackerProtectionEnabled = true
        )
        assertFalse(legitimateVerdict.shouldBlock)
    }

    @Test
    fun voiceCommandsProduceEnglishIndiaSystemPrompts() {
        val ytAction = HindiEnglishVoiceEngine.parseVoiceCommand(
            rawTranscript = "Open YouTube",
            adsBlockedCount = 100,
            trackersBlockedCount = 200
        )
        assertTrue(ytAction is VoiceCommandAction.OpenUrl)
        val openYt = ytAction as VoiceCommandAction.OpenUrl
        assertEquals("https://www.youtube.com", openYt.url)
        assertTrue(openYt.englishIndiaSpokenFeedback.contains("Opening YouTube"))

        val mapsAction = HindiEnglishVoiceEngine.parseVoiceCommand(
            rawTranscript = "Open Google Maps fuel stations near me",
            adsBlockedCount = 100,
            trackersBlockedCount = 200
        )
        assertTrue(mapsAction is VoiceCommandAction.OpenGoogleMapsRoute)
    }

    @Test
    fun webDarkModeEngineProducesDarkStylesheet() {
        val js = WebDarkModeEngine.buildSeamlessDarkModeJs(WebDarkModeStyle.SMART_DARK)
        assertTrue(js.contains("color-scheme: dark"))
        assertTrue(js.contains("#0A0E17"))

        val ytAudioJs = WebDarkModeEngine.buildYouTubeAdFreeAudioModeJs(audioOnlyHideVideo = true)
        assertTrue(ytAudioJs.contains("ytp-ad-skip-button"))
        assertTrue(ytAudioJs.contains("aaa-yt-audio-only-style"))

        val readerJs = WebDarkModeEngine.buildExtractReaderArticleJs()
        assertTrue(readerJs.contains("article"))
    }
}
