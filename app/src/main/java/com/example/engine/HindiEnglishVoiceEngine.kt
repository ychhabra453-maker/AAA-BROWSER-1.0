package com.example.engine

import java.net.URLEncoder
import java.util.Locale

sealed class VoiceCommandAction {
    data class OpenUrl(
        val url: String,
        val englishIndiaSpokenFeedback: String
    ) : VoiceCommandAction()

    data class OpenGoogleMapsRoute(
        val destinationQuery: String,
        val englishIndiaSpokenFeedback: String
    ) : VoiceCommandAction()

    data class ToggleDarkMode(
        val englishIndiaSpokenFeedback: String
    ) : VoiceCommandAction()

    data class ReadCurrentPageAloud(
        val englishIndiaSpokenFeedback: String
    ) : VoiceCommandAction()

    data class AnnouncePrivacyReport(
        val englishIndiaSpokenFeedback: String
    ) : VoiceCommandAction()
}

object HindiEnglishVoiceEngine {

    /**
     * All UI elements and system prompts strictly use English (India) (`en-IN`).
     */
    val SYSTEM_LOCALE_EN_IN: Locale = Locale("en", "IN")

    private fun urlEncode(value: String): String =
        runCatching { URLEncoder.encode(value, "UTF-8") }.getOrDefault(value)

    val QUICK_VOICE_COMMANDS: List<String> = listOf(
        "Open YouTube",
        "Open Google Maps",
        "Fuel stations near me",
        "Open Netflix",
        "Open Amazon",
        "Read this page aloud",
        "Privacy Shield report"
    )

    fun parseVoiceCommand(
        rawTranscript: String,
        adsBlockedCount: Int,
        trackersBlockedCount: Int
    ): VoiceCommandAction {
        val text = rawTranscript.trim()
        val lower = text.lowercase(Locale.ROOT)

        // 1. YouTube
        if (lower.contains("youtube")) {
            val searchPart = lower
                .replace("open youtube", "")
                .replace("search youtube for", "")
                .replace("youtube", "")
                .trim()
            return if (searchPart.isNotBlank()) {
                VoiceCommandAction.OpenUrl(
                    url = "https://www.youtube.com/results?search_query=${urlEncode(searchPart)}",
                    englishIndiaSpokenFeedback = "Searching YouTube for $searchPart."
                )
            } else {
                VoiceCommandAction.OpenUrl(
                    url = "https://www.youtube.com",
                    englishIndiaSpokenFeedback = "Opening YouTube with Ad-Blocker and Dark Mode enabled."
                )
            }
        }

        // 2. Netflix
        if (lower.contains("netflix")) {
            return VoiceCommandAction.OpenUrl(
                url = "https://www.netflix.com",
                englishIndiaSpokenFeedback = "Opening Netflix."
            )
        }

        // 3. Amazon
        if (lower.contains("amazon")) {
            val searchPart = lower
                .replace("open amazon", "")
                .replace("search amazon for", "")
                .replace("amazon", "")
                .trim()
            return if (searchPart.isNotBlank()) {
                VoiceCommandAction.OpenUrl(
                    url = "https://www.amazon.com/s?k=${urlEncode(searchPart)}",
                    englishIndiaSpokenFeedback = "Searching Amazon for $searchPart."
                )
            } else {
                VoiceCommandAction.OpenUrl(
                    url = "https://www.amazon.com",
                    englishIndiaSpokenFeedback = "Opening Amazon with tracking protection active."
                )
            }
        }

        // 4. Google Maps / Navigation
        if (lower.contains("map") ||
            lower.contains("navigate") ||
            lower.contains("directions") ||
            lower.contains("fuel") ||
            lower.contains("petrol") ||
            lower.contains("charging") ||
            lower.contains("restaurant")
        ) {
            val query = lower
                .replace("open google maps", "")
                .replace("google maps", "")
                .replace("open maps", "")
                .replace("navigate to", "")
                .replace("directions to", "")
                .trim()
                .ifBlank { "Nearby places" }
            return VoiceCommandAction.OpenGoogleMapsRoute(
                destinationQuery = query,
                englishIndiaSpokenFeedback = "Opening Google Maps for $query."
            )
        }

        // 5. Read Page Aloud
        if (lower.contains("read page") || lower.contains("read this page") || lower.contains("read aloud")) {
            return VoiceCommandAction.ReadCurrentPageAloud(
                englishIndiaSpokenFeedback = "Reading the current webpage summary."
            )
        }

        // 6. Privacy Shield Status
        if (lower.contains("shield") || lower.contains("tracker") || lower.contains("privacy")) {
            return VoiceCommandAction.AnnouncePrivacyReport(
                englishIndiaSpokenFeedback = "AAA Browser Privacy Shield has blocked $adsBlockedCount advertisements and $trackersBlockedCount trackers so far."
            )
        }

        // 7. Dark Mode
        if (lower.contains("dark mode") || lower.contains("night mode")) {
            return VoiceCommandAction.ToggleDarkMode(
                englishIndiaSpokenFeedback = "Switching website dark mode profile."
            )
        }

        // 8. Google Bookmark
        if (lower == "google" || lower.contains("open google")) {
            return VoiceCommandAction.OpenUrl(
                url = "https://www.google.com",
                englishIndiaSpokenFeedback = "Opening Google homepage in dark mode."
            )
        }

        // 9. Default: Search on Google (en-IN)
        val cleanSearch = text.removePrefix("search ").removePrefix("Search ").trim().ifBlank { text }
        return VoiceCommandAction.OpenUrl(
            url = "https://www.google.com/search?hl=en-IN&gl=IN&q=${urlEncode(cleanSearch)}",
            englishIndiaSpokenFeedback = "Searching Google for $cleanSearch."
        )
    }
}
