package com.example.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.EvStation
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SettingsApplications
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.ANDROID_AUTO_SETTINGS_URI
import com.example.data.BookmarkEntity
import com.example.data.HistoryEntity
import com.example.data.PrivacyAndDisplaySettings
import com.example.data.SIGNATURE_FOOTER_TEXT
import com.example.ui.theme.AmberRoute
import com.example.ui.theme.EchoCardSurface
import com.example.ui.theme.EchoPillBorder
import com.example.ui.theme.EchoPillSelected
import com.example.ui.theme.EchoPitchBlack
import com.example.ui.theme.EchoSubtleLabel
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.EmeraldShield
import com.example.ui.theme.MapsGreen
import com.example.ui.theme.TextPrimaryDark

private data class EchoHeroCardItem(
    val title: String,
    val subtitle: String,
    val tag: String,
    val targetUrl: String,
    val accentColor: Color,
    val secondaryColor: Color,
    val icon: ImageVector,
    val isAndroidAutoAction: Boolean = false,
    val isMapsAction: Boolean = false
)

/**
 * Echo-styled Home Screen matching Screenshot 1!
 * Features:
 * 1. Horizontal category pills ("Speed Dial", "Streaming", "Google Maps", "Android Auto", "Privacy Shield")
 * 2. Large 310.dp bordered Hero Carousel (HorizontalPager with peek-adjacent cards on left & right)
 * 3. Section header with 44.dp rounded thumbnail + uppercase overline + bold title
 * 4. Horizontal album-art style square cards for Bookmarks (YouTube, Google, Netflix, Android Auto settings)
 * 5. Second section for Android Auto Console & Google Maps quick routes
 * 6. Signature Footer ("Made by Yug Chhabra In India With❤️")
 */
@Composable
fun SpeedDialHomeView(
    bookmarks: List<BookmarkEntity>,
    settings: PrivacyAndDisplaySettings,
    systemPromptBanner: String,
    onBookmarkClick: (String) -> Unit,
    onOpenPrivacyShield: () -> Unit,
    onOpenBookmarksSheet: () -> Unit,
    onOpenGoogleMapsInBrowser: (String) -> Unit,
    onOpenExternalGoogleMaps: (String) -> Unit,
    onSwitchToAndroidAutoMode: () -> Unit,
    modifier: Modifier = Modifier
) {
    val filterCategories = remember {
        listOf("Speed Dial", "Streaming", "Google Maps", "Android Auto", "Privacy Shield")
    }
    var selectedCategory by remember { mutableStateOf("Speed Dial") }

    val heroItems = remember(settings.totalAdsBlocked, settings.webDarkModeStyle) {
        listOf(
            EchoHeroCardItem(
                title = "YouTube (Ad-Free)",
                subtitle = "www.youtube.com • Dark Cinema",
                tag = "ZERO VIDEO ADS",
                targetUrl = "https://www.youtube.com",
                accentColor = Color(0xFFFF0033),
                secondaryColor = Color(0xFF280911),
                icon = Icons.Default.PlayArrow
            ),
            EchoHeroCardItem(
                title = "Google Search India",
                subtitle = "www.google.com • English (India)",
                tag = "CLEAN URL SHIELD",
                targetUrl = "https://www.google.com",
                accentColor = Color(0xFF4285F4),
                secondaryColor = Color(0xFF0A192F),
                icon = Icons.Default.Search
            ),
            EchoHeroCardItem(
                title = "Netflix Cinema",
                subtitle = "www.netflix.com • AMOLED Black",
                tag = "STREAMING",
                targetUrl = "https://www.netflix.com",
                accentColor = Color(0xFFE50914),
                secondaryColor = Color(0xFF24080B),
                icon = Icons.Default.Tv
            ),
            EchoHeroCardItem(
                title = "Android Auto Settings",
                subtitle = "Redirect to Phone Settings & Car Mode",
                tag = "IN-CAR READY",
                targetUrl = ANDROID_AUTO_SETTINGS_URI,
                accentColor = Color(0xFF00E676),
                secondaryColor = Color(0xFF072318),
                icon = Icons.Default.SettingsApplications
            ),
            EchoHeroCardItem(
                title = "Google Maps Live",
                subtitle = "Turn-by-Turn GPS & Route Finder",
                tag = "NAVIGATION",
                targetUrl = "https://www.google.com/maps",
                accentColor = MapsGreen,
                secondaryColor = Color(0xFF082215),
                icon = Icons.Default.Map,
                isMapsAction = true
            )
        )
    }

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { heroItems.size }
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(EchoPitchBlack)
            .testTag("speed_dial_home_list"),
        contentPadding = PaddingValues(top = 6.dp, bottom = 175.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // 1. Horizontal Category Filter Pills matching Screenshot 1 ("Romance", "Workout", "Relax", "Feel good")
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                filterCategories.forEach { category ->
                    val isSelected = selectedCategory == category
                    val bgColor by animateColorAsState(
                        targetValue = if (isSelected) EchoPillSelected else EchoCardSurface,
                        label = "category_pill_bg"
                    )
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = bgColor,
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .clickable {
                                selectedCategory = category
                                when (category) {
                                    "Google Maps" -> onOpenGoogleMapsInBrowser("")
                                    "Android Auto" -> onSwitchToAndroidAutoMode()
                                    "Privacy Shield" -> onOpenPrivacyShield()
                                }
                            }
                    ) {
                        Text(
                            text = category,
                            style = MaterialTheme.typography.titleMedium.copy(fontSize = 15.sp),
                            color = TextPrimaryDark,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp)
                        )
                    }
                }
            }
        }

        // 2. Large Bordered Hero Carousel Card with Adjacent Peek Cards (Exact match to Screenshot 1!)
        item {
            HorizontalPager(
                state = pagerState,
                contentPadding = PaddingValues(horizontal = 32.dp),
                pageSpacing = 12.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(315.dp)
            ) { pageIndex ->
                val item = heroItems[pageIndex]
                EchoHeroCarouselCard(
                    item = item,
                    adsBlocked = settings.totalAdsBlocked,
                    darkModeLabel = settings.webDarkModeStyle.label,
                    onClick = {
                        when {
                            item.isMapsAction -> onOpenGoogleMapsInBrowser("")
                            item.isAndroidAutoAction -> onSwitchToAndroidAutoMode()
                            else -> onBookmarkClick(item.targetUrl)
                        }
                    }
                )
            }
        }

        // 3. Section Header with 44.dp Thumbnail + Uppercase Overline + Bold Headline (Exact match to Screenshot 1!)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_privacy_hero_banner_1793933229717),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .border(1.dp, EchoPillBorder, RoundedCornerShape(10.dp))
                )
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = "FAST, AD-FREE & PRIVACY PROTECTED!",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontSize = 12.sp,
                            letterSpacing = 0.6.sp
                        ),
                        color = Color(0xFFD5D7E0),
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "INDIA'S TOP BOOKMARKS",
                        style = MaterialTheme.typography.headlineMedium.copy(fontSize = 21.sp),
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }

        // 4. Horizontal Square Album-Style Cards for the 4 Pinned Bookmarks (YouTube, Google, Netflix, Android Auto settings)
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 18.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(bookmarks, key = { it.id }) { bookmark ->
                    EchoSquareBookmarkCard(
                        bookmark = bookmark,
                        onClick = { onBookmarkClick(bookmark.url) }
                    )
                }
            }
        }

        // 5. Second Section: Android Auto & Google Maps Square Cards (visible below floating bar area just like bottom of Screenshot 1)
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "DRIVING CONSOLE & NAVIGATION",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontSize = 11.sp,
                                letterSpacing = 0.6.sp
                            ),
                            color = EchoSubtleLabel
                        )
                        Text(
                            text = "ANDROID AUTO & GOOGLE MAPS",
                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 19.sp),
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(50),
                        color = EchoCardSurface,
                        modifier = Modifier
                            .border(1.dp, EchoPillBorder, RoundedCornerShape(50))
                            .clickable { onOpenPrivacyShield() }
                            .testTag("open_settings_update_pill")
                    ) {
                        Text(
                            text = "Shield & OTA",
                            style = MaterialTheme.typography.labelMedium,
                            color = EmeraldShield,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 18.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        EchoFeatureSquareCard(
                            title = "Driving Console",
                            subtitle = "Android Auto UI",
                            tag = "CAR MODE",
                            accentColor = AmberRoute,
                            icon = Icons.Default.DirectionsCar,
                            onClick = onSwitchToAndroidAutoMode,
                            testTag = "launch_android_auto_button"
                        )
                    }
                    item {
                        EchoFeatureSquareCard(
                            title = "Google Maps",
                            subtitle = "Live GPS Traffic",
                            tag = "MAPS INDIA",
                            accentColor = MapsGreen,
                            icon = Icons.Default.Map,
                            onClick = { onOpenGoogleMapsInBrowser("") },
                            testTag = "open_google_maps_button"
                        )
                    }
                    item {
                        EchoFeatureSquareCard(
                            title = "Fuel Stations",
                            subtitle = "Nearby Petrol & CNG",
                            tag = "QUICK ROUTE",
                            accentColor = ElectricCyan,
                            icon = Icons.Default.LocalGasStation,
                            onClick = { onOpenGoogleMapsInBrowser("Fuel stations near me") }
                        )
                    }
                    item {
                        EchoFeatureSquareCard(
                            title = "EV Charging",
                            subtitle = "Fast Chargers Nearby",
                            tag = "GREEN ROUTE",
                            accentColor = EmeraldShield,
                            icon = Icons.Default.EvStation,
                            onClick = { onOpenGoogleMapsInBrowser("EV charging stations near me") }
                        )
                    }
                    item {
                        EchoFeatureSquareCard(
                            title = "Launch Maps App",
                            subtitle = "Turn-by-Turn Intent",
                            tag = "EXTERNAL",
                            accentColor = Color(0xFFA855F7),
                            icon = Icons.Default.Navigation,
                            onClick = { onOpenExternalGoogleMaps("Google Maps Navigation") }
                        )
                    }
                }
            }
        }

        // 6. Signature Footer at the bottom of the Home Screen
        item {
            Box(modifier = Modifier.padding(horizontal = 18.dp)) {
                SignatureFooterCard()
            }
        }
    }
}

@Composable
private fun EchoHeroCarouselCard(
    item: EchoHeroCardItem,
    adsBlocked: Int,
    darkModeLabel: String,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(28.dp),
        color = EchoPitchBlack,
        modifier = Modifier
            .fillMaxSize()
            .border(1.dp, EchoPillBorder, RoundedCornerShape(28.dp))
            .clip(RoundedCornerShape(28.dp))
            .clickable(onClick = onClick)
            .testTag("hero_privacy_shield_card")
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            EchoPitchBlack,
                            item.secondaryColor.copy(alpha = 0.65f),
                            EchoPitchBlack
                        )
                    )
                )
                .padding(22.dp)
        ) {
            // Top subtle status badge inside the hero card
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopStart),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = EchoCardSurface.copy(alpha = 0.9f),
                    modifier = Modifier.border(1.dp, EchoPillBorder, RoundedCornerShape(50))
                ) {
                    Text(
                        text = item.tag,
                        style = MaterialTheme.typography.labelSmall,
                        color = item.accentColor,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }

                Text(
                    text = "$adsBlocked Ads Blocked • $darkModeLabel",
                    style = MaterialTheme.typography.labelSmall,
                    color = EchoSubtleLabel
                )
            }

            // Center minimal glowing emblem
            Box(
                modifier = Modifier
                    .size(78.dp)
                    .align(Alignment.Center)
                    .clip(CircleShape)
                    .background(item.accentColor.copy(alpha = 0.14f))
                    .border(1.dp, item.accentColor.copy(alpha = 0.45f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    tint = item.accentColor,
                    modifier = Modifier.size(38.dp)
                )
            }

            // Bottom-left large bold title & subtitle matching "Lalkara (feat. Sultaan) / Diljit Dosanjh, Intense" in Screenshot 1
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.headlineLarge.copy(fontSize = 26.sp),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.subtitle,
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp),
                    color = EchoSubtleLabel,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

/**
 * Square Album-Art Card matching "HITS OF 2026 / Uncut Bollywood / Kollywood Hitlist" in Screenshot 1!
 */
@Composable
fun EchoSquareBookmarkCard(
    bookmark: BookmarkEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "square_card_scale"
    )

    val badgeColor = Color(bookmark.badgeColorHex)
    val isAndroidAutoSettings = bookmark.url == ANDROID_AUTO_SETTINGS_URI ||
        bookmark.displayDomain.contains("Android Auto", ignoreCase = true)

    val icon = when {
        isAndroidAutoSettings -> Icons.Default.SettingsApplications
        bookmark.displayDomain.contains("youtube", ignoreCase = true) -> Icons.Default.PlayArrow
        bookmark.displayDomain.contains("google", ignoreCase = true) -> Icons.Default.Search
        bookmark.displayDomain.contains("netflix", ignoreCase = true) -> Icons.Default.Tv
        else -> Icons.Default.Bookmark
    }

    Column(
        modifier = modifier
            .width(156.dp)
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = androidx.compose.material3.ripple(),
                onClick = onClick
            )
            .testTag("bookmark_card_${bookmark.displayDomain}")
    ) {
        // 156.dp Square Artwork Tile
        Box(
            modifier = Modifier
                .size(156.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            badgeColor.copy(alpha = 0.36f),
                            EchoCardSurface,
                            Color(0xFF101116)
                        )
                    )
                )
                .border(1.dp, badgeColor.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                .padding(14.dp)
        ) {
            // Top-left small play/shield circle icon matching the tiny circle badge in Screenshot 1
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.45f))
                    .align(Alignment.TopStart),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }

            // Top-right category tag pill ("SO FAR" style in Screenshot 1)
            Surface(
                shape = RoundedCornerShape(50),
                color = Color.Black.copy(alpha = 0.65f),
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Text(
                    text = bookmark.category.uppercase(),
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }

            // Center large brand icon
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(badgeColor.copy(alpha = 0.22f))
                    .border(1.5.dp, badgeColor, CircleShape)
                    .align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = bookmark.title,
                    tint = badgeColor,
                    modifier = Modifier.size(32.dp)
                )
            }

            // Bottom domain text inside tile
            Text(
                text = bookmark.displayDomain.uppercase(),
                style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 0.8.sp),
                color = Color.White.copy(alpha = 0.85f),
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.align(Alignment.BottomStart)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = bookmark.title,
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = bookmark.displayDomain,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
            color = EchoSubtleLabel,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun EchoFeatureSquareCard(
    title: String,
    subtitle: String,
    tag: String,
    accentColor: Color,
    icon: ImageVector,
    onClick: () -> Unit,
    testTag: String? = null
) {
    Column(
        modifier = Modifier
            .width(156.dp)
            .clickable(onClick = onClick)
            .then(if (testTag != null) Modifier.testTag(testTag) else Modifier)
    ) {
        Box(
            modifier = Modifier
                .size(156.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            accentColor.copy(alpha = 0.3f),
                            EchoCardSurface,
                            Color(0xFF101116)
                        )
                    )
                )
                .border(1.dp, accentColor.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                .padding(14.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(50),
                color = Color.Black.copy(alpha = 0.6f),
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Text(
                    text = tag,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                    color = accentColor,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }

            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.2f))
                    .border(1.dp, accentColor, CircleShape)
                    .align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = accentColor,
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
            color = EchoSubtleLabel,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/**
 * Echo-styled Library / Bookmarks Screen matching Screenshot 4 ("Library / Your Playlists")!
 */
@Composable
fun EchoBookmarksLibraryView(
    bookmarks: List<BookmarkEntity>,
    history: List<HistoryEntity>,
    onOpenUrl: (String) -> Unit,
    onOpenAddBookmarkSheet: () -> Unit,
    onDeleteBookmark: (Int) -> Unit,
    onRestoreDefaults: () -> Unit,
    onClearHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    val libraryTabs = remember { listOf("Bookmarks", "Pinned", "History", "Android Auto", "Local") }
    var selectedTab by remember { mutableStateOf("Bookmarks") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(EchoPitchBlack)
            .testTag("echo_bookmarks_library_view")
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 185.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            // 1. Horizontal Category Pills ("Bookmarks", "Pinned", "History", "Android Auto", "Local") matching Screenshot 4
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    libraryTabs.forEach { tab ->
                        val isSelected = selectedTab == tab
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = if (isSelected) EchoPillSelected else EchoCardSurface,
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .clickable { selectedTab = tab }
                        ) {
                            Text(
                                text = tab,
                                style = MaterialTheme.typography.titleMedium.copy(fontSize = 15.sp),
                                color = Color.White,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp)
                            )
                        }
                    }
                }
            }

            // 2. Connected User Status Card ("Yug Chhabra • Privacy Shield Connected") matching Screenshot 4!
            item {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = EchoCardSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 22.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF2B2D38)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PersonOutline,
                                contentDescription = null,
                                tint = Color(0xFFD5D7E0),
                                modifier = Modifier.size(26.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Yug Chhabra",
                                style = MaterialTheme.typography.titleLarge.copy(fontSize = 19.sp),
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(9.dp)
                                        .clip(CircleShape)
                                        .background(EmeraldShield)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Privacy Shield Connected",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = EchoSubtleLabel
                                )
                            }
                        }

                        IconButton(
                            onClick = onRestoreDefaults,
                            modifier = Modifier.testTag("restore_default_bookmarks_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Sync,
                                contentDescription = "Restore Default Bookmarks",
                                tint = Color(0xFFD5D7E0),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }

            if (selectedTab == "History") {
                // History Header
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Browsing History",
                            style = MaterialTheme.typography.headlineMedium.copy(fontSize = 22.sp),
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (history.isEmpty()) "0 items" else "Clear All",
                            style = MaterialTheme.typography.titleMedium,
                            color = EchoSubtleLabel,
                            modifier = Modifier.clickable { onClearHistory() }
                        )
                    }
                }

                if (history.isEmpty()) {
                    item {
                        SegmentedSettingRow(
                            title = "No browsing history yet",
                            subtitle = "Pages you visit appear here with ad-block counts",
                            icon = Icons.Default.History,
                            position = SegmentPosition.SINGLE
                        )
                    }
                } else {
                    items(history.size) { index ->
                        val entry = history[index]
                        val pos = when {
                            history.size == 1 -> SegmentPosition.SINGLE
                            index == 0 -> SegmentPosition.TOP
                            index == history.lastIndex -> SegmentPosition.BOTTOM
                            else -> SegmentPosition.MIDDLE
                        }
                        SegmentedSettingRow(
                            title = entry.title,
                            subtitle = "${entry.url} • ${entry.adsBlocked} ads blocked",
                            icon = Icons.Default.History,
                            position = pos,
                            onClick = { onOpenUrl(entry.url) }
                        )
                    }
                }
            } else {
                // 3. "Your Bookmarks" Header + Count matching "Your Playlists / 3 playlists" in Screenshot 4!
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Your Bookmarks",
                            style = MaterialTheme.typography.headlineMedium.copy(fontSize = 23.sp),
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${bookmarks.size} bookmarks",
                            style = MaterialTheme.typography.titleMedium,
                            color = EchoSubtleLabel
                        )
                    }
                }

                // 4. Segmented Stacked Bookmark Rows matching Screenshot 4!
                items(bookmarks.size) { index ->
                    val bm = bookmarks[index]
                    val pos = when {
                        bookmarks.size == 1 -> SegmentPosition.SINGLE
                        index == 0 -> SegmentPosition.TOP
                        index == bookmarks.lastIndex -> SegmentPosition.BOTTOM
                        else -> SegmentPosition.MIDDLE
                    }
                    EchoLibraryBookmarkRow(
                        bookmark = bm,
                        position = pos,
                        isFirstPinned = index == 0,
                        onClick = { onOpenUrl(bm.url) },
                        onDelete = { onDeleteBookmark(bm.id) }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(22.dp))
                SignatureFooterCard()
            }
        }

        // 5. Floating White Circular "+" Button on bottom-right matching Screenshot 4!
        FloatingActionButton(
            onClick = onOpenAddBookmarkSheet,
            shape = CircleShape,
            containerColor = Color(0xFFEAECEF),
            contentColor = Color(0xFF111216),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 22.dp, bottom = 165.dp)
                .size(60.dp)
                .testTag("manage_bookmarks_button")
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add or Manage Bookmarks",
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
private fun EchoLibraryBookmarkRow(
    bookmark: BookmarkEntity,
    position: SegmentPosition,
    isFirstPinned: Boolean,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val shape = segmentedCardShape(position)
    val badgeColor = Color(bookmark.badgeColorHex)
    val isAndroidAutoSettings = bookmark.url == ANDROID_AUTO_SETTINGS_URI ||
        bookmark.displayDomain.contains("Android Auto", ignoreCase = true)

    val icon = when {
        isFirstPinned -> Icons.Default.Favorite
        isAndroidAutoSettings -> Icons.Default.SettingsApplications
        bookmark.displayDomain.contains("youtube", ignoreCase = true) -> Icons.Default.PlayArrow
        bookmark.displayDomain.contains("google", ignoreCase = true) -> Icons.Default.Search
        bookmark.displayDomain.contains("netflix", ignoreCase = true) -> Icons.Default.Tv
        else -> Icons.Default.Bookmark
    }

    val tileBrush = if (isFirstPinned) {
        // Purple-to-mint gradient matching "Spotify Liked Songs" in Screenshot 4!
        Brush.linearGradient(
            colors = listOf(Color(0xFF6B4EFF), Color(0xFF9E82FF), Color(0xFFA7F3D0))
        )
    } else {
        Brush.linearGradient(
            colors = listOf(badgeColor, badgeColor.copy(alpha = 0.65f))
        )
    }

    Surface(
        shape = shape,
        color = EchoCardSurface,
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .clickable(onClick = onClick)
            .testTag("bookmark_row_${bookmark.displayDomain}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 54.dp Rounded-Square Icon Tile matching Screenshot 4
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(tileBrush),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = bookmark.title,
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = bookmark.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 17.sp),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${bookmark.displayDomain} • ${bookmark.category}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = EchoSubtleLabel,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (bookmark.isDefaultPinned) {
                Icon(
                    imageVector = Icons.Default.PushPin,
                    contentDescription = "Pinned",
                    tint = EmeraldShield,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Delete Bookmark",
                        tint = EchoSubtleLabel
                    )
                }
            }
        }
    }
}

@Composable
fun SignatureFooterCard(modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = EchoCardSurface,
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, EchoPillBorder, RoundedCornerShape(20.dp))
            .testTag("signature_footer_card")
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = SIGNATURE_FOOTER_TEXT,
                style = MaterialTheme.typography.titleSmall,
                color = Color(0xFFD5D7E0),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}
