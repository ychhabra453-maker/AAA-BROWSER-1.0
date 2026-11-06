package com.example.engine

import com.example.data.SIGNATURE_FOOTER_TEXT
import com.example.data.WebDarkModeStyle

object WebDarkModeEngine {

    /**
     * Builds JavaScript that seamlessly applies dark mode across all websites
     * and appends the signature footer ("Made by Yug Chhabra In India With❤️") at the end of every page.
     */
    fun buildSeamlessDarkModeJs(style: WebDarkModeStyle): String {
        val signatureJs = """
            try {
                if (document.body && !document.getElementById('aaa-browser-yug-footer')) {
                    var footer = document.createElement('div');
                    footer.id = 'aaa-browser-yug-footer';
                    footer.textContent = '$SIGNATURE_FOOTER_TEXT';
                    footer.style.cssText = 'width:100%;padding:14px 12px;margin-top:24px;text-align:center;font-family:sans-serif;font-size:13px;font-weight:600;color:#00E5FF;background:#000000;border-top:1px solid #1C1D24;letter-spacing:0.4px;z-index:99999;';
                    document.body.appendChild(footer);
                }
            } catch(e) {}
        """.trimIndent()

        if (style == WebDarkModeStyle.SITE_DEFAULT) {
            return """
                (function() {
                    var el = document.getElementById('aegis-universal-dark-style');
                    if (el) el.remove();
                    $signatureJs
                })();
            """.trimIndent()
        }

        val (bgHex, surfaceHex, textHex, linkHex, borderHex) = when (style) {
            WebDarkModeStyle.AMOLED_BLACK -> listOf("#000000", "#121318", "#F1F5F9", "#38BDF8", "#1C1D24")
            WebDarkModeStyle.WARM_NIGHT -> listOf("#12100E", "#1C1917", "#F5F5F4", "#FBBF24", "#292524")
            else -> listOf("#0A0E17", "#161820", "#E2E8F0", "#00E5FF", "#262834")
        }

        return """
            (function() {
                try {
                    var docEl = document.documentElement;
                    if (!docEl) return;

                    var meta = document.querySelector('meta[name="color-scheme"]');
                    if (!meta) {
                        meta = document.createElement('meta');
                        meta.name = 'color-scheme';
                        meta.content = 'dark';
                        (document.head || docEl).appendChild(meta);
                    } else {
                        meta.content = 'dark';
                    }
                    docEl.style.colorScheme = 'dark';
                    docEl.setAttribute('dark', 'true');
                    docEl.setAttribute('data-theme', 'dark');
                    docEl.setAttribute('data-bs-theme', 'dark');

                    var host = (window.location.hostname || '').toLowerCase();
                    var isMaps = host.indexOf('maps.google') !== -1 || window.location.pathname.indexOf('/maps') !== -1;
                    if (!isMaps) {
                        var styleId = 'aegis-universal-dark-style';
                        var existing = document.getElementById(styleId);
                        if (!existing) {
                            existing = document.createElement('style');
                            existing.id = styleId;
                            (document.head || docEl).appendChild(existing);
                        }

                        existing.textContent = `
                            :root {
                                color-scheme: dark !important;
                            }
                            html, body {
                                background-color: $bgHex !important;
                                color: $textHex !important;
                            }
                            article, section, main, nav, header, footer, aside,
                            div:not([class*="player"]):not([class*="video"]):not([class*="thumb"]):not([style*="background-image"]),
                            ul, ol, li, dl, dt, dd, table, thead, tbody, tr, td, th, form, fieldset {
                                background-color: transparent;
                                color: inherit;
                                border-color: $borderHex !important;
                            }
                            header, nav, [role="navigation"], [role="banner"],
                            .card, .panel, .box, .modal, .dropdown-menu,
                            input, textarea, select, button {
                                background-color: $surfaceHex !important;
                                color: $textHex !important;
                                border-color: $borderHex !important;
                            }
                            h1, h2, h3, h4, h5, h6, p, span, label, li, td, th, blockquote, strong, b, em {
                                color: $textHex !important;
                            }
                            a, a * {
                                color: $linkHex !important;
                            }
                            img, video, canvas, picture, iframe, svg,
                            [style*="background-image"] {
                                filter: brightness(0.95) contrast(1.03) !important;
                            }
                            ::placeholder {
                                color: #94A3B8 !important;
                            }
                        `;
                    }
                    $signatureJs
                } catch (e) {}
            })();
        """.trimIndent()
    }

    /**
     * Injects ad-free YouTube playback + optional Audio-Only (No-Video) mode so the user can listen
     * to YouTube music like Spotify with zero ads and without distracting video frames.
     */
    fun buildYouTubeAdFreeAudioModeJs(audioOnlyHideVideo: Boolean): String {
        return """
            (function() {
                try {
                    // 1. Install continuous YouTube ad skipper & cosmetic ad cleaner
                    if (!window.__aaaYtAdFreeTimer) {
                        window.__aaaYtAdFreeTimer = setInterval(function() {
                            try {
                                // Click any skip ad button immediately
                                var skipSelectors = [
                                    '.ytp-ad-skip-button',
                                    '.ytp-ad-skip-button-modern',
                                    '.ytp-skip-ad-button',
                                    '.ytp-ad-overlay-close-button',
                                    'button[class*="ad-skip"]'
                                ];
                                for (var i = 0; i < skipSelectors.length; i++) {
                                    var btn = document.querySelector(skipSelectors[i]);
                                    if (btn) { btn.click(); }
                                }
                                // If an ad video is currently playing, jump to end immediately
                                var adShowing = document.querySelector('.ad-showing, .ad-interrupting');
                                var video = document.querySelector('video');
                                if (adShowing && video) {
                                    video.muted = true;
                                    if (isFinite(video.duration) && video.duration > 0) {
                                        video.currentTime = video.duration;
                                    } else {
                                        video.playbackRate = 16.0;
                                    }
                                } else if (video && video.playbackRate > 2.0) {
                                    video.playbackRate = 1.0;
                                    video.muted = false;
                                }
                                // Remove companion ad slots
                                var adNodes = document.querySelectorAll(
                                    'ytd-ad-slot-renderer, ytm-promoted-sparkles-web-renderer, ytm-companion-ad-renderer, .ytp-ad-module, #masthead-ad'
                                );
                                for (var j = 0; j < adNodes.length; j++) {
                                    adNodes[j].remove();
                                }
                            } catch(e) {}
                        }, 300);
                    }

                    // 2. Apply or remove Audio-Only (Hide Video) CSS while keeping audio playing
                    var audioStyleId = 'aaa-yt-audio-only-style';
                    var existingStyle = document.getElementById(audioStyleId);
                    if ($audioOnlyHideVideo) {
                        if (!existingStyle) {
                            existingStyle = document.createElement('style');
                            existingStyle.id = audioStyleId;
                            (document.head || document.documentElement).appendChild(existingStyle);
                        }
                        existingStyle.textContent = `
                            video, .html5-video-container {
                                opacity: 0.01 !important;
                                filter: brightness(0) !important;
                            }
                            .player-container, #player-container-id, #movie_player {
                                background: #000000 !important;
                            }
                        `;
                    } else if (existingStyle) {
                        existingStyle.remove();
                    }
                } catch(e) {}
            })();
        """.trimIndent()
    }

    /**
     * Extracts clean article paragraphs for Reader Mode.
     */
    fun buildExtractReaderArticleJs(): String {
        return """
            (function() {
                try {
                    var root = document.querySelector('article') || document.querySelector('main') || document.body;
                    if (!root) return '';
                    var nodes = root.querySelectorAll('h1, h2, h3, p, blockquote, li');
                    var lines = [];
                    var seen = {};
                    for (var i = 0; i < nodes.length; i++) {
                        var text = (nodes[i].innerText || '').replace(/\s+/g, ' ').trim();
                        if (text.length >= 28 && !seen[text]) {
                            seen[text] = true;
                            var tag = nodes[i].tagName.toLowerCase();
                            if (tag === 'h1' || tag === 'h2' || tag === 'h3') {
                                lines.push('## ' + text);
                            } else {
                                lines.push(text);
                            }
                        }
                        if (lines.length >= 45) break;
                    }
                    if (lines.length === 0) {
                        return (document.body.innerText || '').substring(0, 4000);
                    }
                    return lines.join('\n\n');
                } catch(e) {
                    return '';
                }
            })();
        """.trimIndent()
    }
}
