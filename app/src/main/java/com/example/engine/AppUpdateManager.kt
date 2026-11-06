package com.example.engine

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

data class ReleaseUpdateInfo(
    val isChecking: Boolean = false,
    val currentVersion: String = "v${BuildConfig.VERSION_NAME} (Build ${BuildConfig.VERSION_CODE})",
    val latestVersionTag: String = "v${BuildConfig.VERSION_NAME}",
    val releaseTitle: String = "AAA Browser v1.1 Update",
    val releaseNotes: String = "v1.1 Update in Settings:\n• Reader Mode for distraction-free articles\n• Live Search Suggestions in Search Bar\n• YouTube Ad-Free Audio-Only Music Mode (Browser & Android Auto)\n• Cute Page Info (i) Circle Button on far right & Home Button on far left\n• 'Coming Soon' indicators on upcoming features",
    val apkDownloadUrl: String = "",
    val releasesPageUrl: String = "https://github.com/ychhabra453/AAA-Browser/releases",
    val statusMessage: String = "AAA Browser v1.1 Installed • All updates arrive here in Settings",
    val updateAvailable: Boolean = false,
    val lastCheckedLabel: String = "v1.1 Active"
)

object AppUpdateManager {

    fun normalizeRepoSlug(rawInput: String): String {
        val trimmed = rawInput.trim()
            .removePrefix("https://github.com/")
            .removePrefix("http://github.com/")
            .removePrefix("github.com/")
            .removeSuffix("/")
            .removeSuffix(".git")
        val parts = trimmed.split("/").filter { it.isNotBlank() }
        return if (parts.size >= 2) {
            "${parts[0]}/${parts[1]}"
        } else {
            "ychhabra453/AAA-Browser"
        }
    }

    suspend fun checkGitHubReleaseUpdate(repoInput: String): ReleaseUpdateInfo = withContext(Dispatchers.IO) {
        val slug = normalizeRepoSlug(repoInput)
        val releasesWebUrl = "https://github.com/$slug/releases"
        val apiEndpoint = "https://api.github.com/repos/$slug/releases/latest"

        runCatching {
            val connection = (URL(apiEndpoint).openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                connectTimeout = 8000
                readTimeout = 8000
                setRequestProperty("Accept", "application/vnd.github+json")
                setRequestProperty("User-Agent", "AAA-Browser-Android-Updater")
            }

            val code = connection.responseCode
            if (code in 200..299) {
                val responseBody = connection.inputStream.bufferedReader().use { it.readText() }
                val json = JSONObject(responseBody)
                val tagName = json.optString("tag_name", "v1.0.latest")
                val name = json.optString("name", "AAA Browser $tagName")
                val body = json.optString("body", "New build available on GitHub Releases.")
                val htmlUrl = json.optString("html_url", releasesWebUrl)

                var foundApkUrl = ""
                val assets = json.optJSONArray("assets")
                if (assets != null) {
                    for (i in 0 until assets.length()) {
                        val asset = assets.optJSONObject(i) ?: continue
                        val assetName = asset.optString("name", "")
                        val downloadUrl = asset.optString("browser_download_url", "")
                        if (assetName.endsWith(".apk", ignoreCase = true) && downloadUrl.isNotBlank()) {
                            foundApkUrl = downloadUrl
                            if (assetName.contains("release", ignoreCase = true)) {
                                break
                            }
                        }
                    }
                }

                val currentTag = "v${BuildConfig.VERSION_NAME}"
                val isNewer = tagName != currentTag

                ReleaseUpdateInfo(
                    isChecking = false,
                    latestVersionTag = tagName,
                    releaseTitle = name,
                    releaseNotes = body.take(280),
                    apkDownloadUrl = foundApkUrl,
                    releasesPageUrl = htmlUrl,
                    statusMessage = if (foundApkUrl.isNotBlank()) {
                        "Latest release $tagName found with downloadable APK!"
                    } else {
                        "Latest release $tagName found on GitHub."
                    },
                    updateAvailable = isNewer || foundApkUrl.isNotBlank(),
                    lastCheckedLabel = "Checked against github.com/$slug"
                )
            } else {
                ReleaseUpdateInfo(
                    isChecking = false,
                    latestVersionTag = "v${BuildConfig.VERSION_NAME}",
                    releasesPageUrl = releasesWebUrl,
                    statusMessage = "Connected to github.com/$slug • Tap 'Open Releases' or 'Download Latest APK' to update.",
                    updateAvailable = false,
                    lastCheckedLabel = "Checked github.com/$slug"
                )
            }
        }.getOrElse {
            ReleaseUpdateInfo(
                isChecking = false,
                latestVersionTag = "v${BuildConfig.VERSION_NAME}",
                releasesPageUrl = releasesWebUrl,
                statusMessage = "Update channel ready: github.com/$slug/releases",
                updateAvailable = false,
                lastCheckedLabel = "Offline / Ready"
            )
        }
    }

    fun downloadApkUpdate(context: Context, apkUrl: String, releasesPageUrl: String) {
        val targetUrl = apkUrl.ifBlank { releasesPageUrl }
        if (apkUrl.isNotBlank() && apkUrl.endsWith(".apk", ignoreCase = true)) {
            val dmResult = runCatching {
                val request = DownloadManager.Request(Uri.parse(apkUrl))
                    .setTitle("AAA Browser Update")
                    .setDescription("Downloading latest AAA Browser APK…")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setMimeType("application/vnd.android.package-archive")
                    .setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_DOWNLOADS,
                        "AAA-Browser-update.apk"
                    )
                val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                dm.enqueue(request)
                Toast.makeText(
                    context,
                    "Downloading AAA-Browser-update.apk to Downloads… Tap notification when complete to install!",
                    Toast.LENGTH_LONG
                ).show()
            }
            if (dmResult.isSuccess) return
        }

        // Fallback: open the direct APK or GitHub Releases URL in external browser / installer
        runCatching {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(targetUrl)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }
}
