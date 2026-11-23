package com.aza.apkkonseling.ui.student

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aza.apkkonseling.data.Article
import com.aza.apkkonseling.data.MockRepository
import com.aza.apkkonseling.theme.*
import com.aza.apkkonseling.ui.components.BkSelectableChip
import com.aza.apkkonseling.ui.components.EmptyStateView
import com.aza.apkkonseling.ui.components.bkButtonColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentArticleScreens(
    onNavigateBack: () -> Unit
) {
    var selectedArticle by remember { mutableStateOf<Article?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Semua") }

    val categories = listOf("Semua", "Kesehatan Mental", "Bimbingan Belajar", "Perencanaan Karir", "Hubungan Sosial")

    val filteredArticles = MockRepository.articles.filter { article ->
        (selectedCategory == "Semua" || article.category == selectedCategory) &&
                (article.title.contains(searchQuery, ignoreCase = true) ||
                        article.content.contains(searchQuery, ignoreCase = true))
    }

    BkGradientBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            if (selectedArticle == null) "Perpustakaan Konseling" else "Baca Materi Konseling",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = DeepNavy
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                if (selectedArticle != null) selectedArticle = null else onNavigateBack()
                            }
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = DeepNavy)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { paddingValues ->
            if (selectedArticle == null) {
                // Article List with Search and Filter
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp)
                ) {
                    // Search Bar
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Cari topik atau judul artikel...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = DenimBlue) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = PureWhite,
                            focusedContainerColor = PureWhite,
                            unfocusedBorderColor = SoftSkyBlue.copy(alpha = 0.6f),
                            focusedBorderColor = DenimBlue
                        ),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Category Chips using BkSelectableChip
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(categories) { cat ->
                            BkSelectableChip(
                                selected = selectedCategory == cat,
                                onClick = { selectedCategory = cat },
                                label = cat
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (filteredArticles.isEmpty()) {
                        EmptyStateView(
                            icon = Icons.Default.BookmarkBorder,
                            title = "Artikel Tidak Ditemukan",
                            description = "Tidak ada artikel yang cocok dengan filter atau kata kunci Anda."
                        )
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(bottom = 28.dp)
                        ) {
                            items(filteredArticles) { article ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(18.dp))
                                        .clickable { selectedArticle = article },
                                    shape = RoundedCornerShape(18.dp),
                                    colors = CardDefaults.cardColors(containerColor = PureWhite),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, SoftSkyBlue.copy(alpha = 0.5f))
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        // Themed illustration icon cover (NO DUPLICATE TEXT)
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(96.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(
                                                    when (article.category) {
                                                        "Kesehatan Mental" -> IceBlue
                                                        "Bimbingan Belajar" -> SoftIceBlue
                                                        "Perencanaan Karir" -> WarmCream
                                                        else -> Color(0xFFF1F5F9)
                                                    }
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(52.dp)
                                                    .clip(CircleShape)
                                                    .background(PureWhite.copy(alpha = 0.85f)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = when (article.category) {
                                                        "Kesehatan Mental" -> Icons.Default.Psychology
                                                        "Bimbingan Belajar" -> Icons.AutoMirrored.Filled.MenuBook
                                                        "Perencanaan Karir" -> Icons.Default.Work
                                                        else -> Icons.Default.Groups
                                                    },
                                                    contentDescription = null,
                                                    tint = DenimBlue,
                                                    modifier = Modifier.size(28.dp)
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(12.dp))

                                        // Category Pill Badge + Reading Info
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Surface(
                                                color = SoftIceBlue,
                                                shape = RoundedCornerShape(6.dp)
                                            ) {
                                                Text(
                                                    text = article.category,
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                                    style = MaterialTheme.typography.bodySmall.copy(
                                                        color = DenimBlue,
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 11.sp
                                                    )
                                                )
                                            }
                                            Text(
                                                text = "${article.date} • ${article.readTime}",
                                                style = MaterialTheme.typography.bodySmall.copy(color = NeutralGrey, fontSize = 11.sp)
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Text(
                                            text = article.title,
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = DeepNavy
                                            )
                                        )

                                        Spacer(modifier = Modifier.height(4.dp))

                                        Text(
                                            text = article.content,
                                            style = MaterialTheme.typography.bodyMedium.copy(color = DeepNavyVariant, fontSize = 13.sp),
                                            maxLines = 2
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Person, contentDescription = null, tint = SteelBlue, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = article.author,
                                                style = MaterialTheme.typography.bodySmall.copy(color = SteelBlue, fontWeight = FontWeight.Medium, fontSize = 11.sp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // Article Detail Reader Screen
                val art = selectedArticle!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = PureWhite),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SoftSkyBlue.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Surface(
                                color = SoftIceBlue,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = art.category,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = DenimBlue,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = art.title,
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy
                                )
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Person, contentDescription = null, tint = SteelBlue, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = art.author,
                                        style = MaterialTheme.typography.bodySmall.copy(color = DeepNavy, fontWeight = FontWeight.SemiBold, fontSize = 11.sp)
                                    )
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.CalendarToday, contentDescription = null, tint = NeutralGrey, modifier = Modifier.size(13.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${art.date} (${art.readTime})",
                                        style = MaterialTheme.typography.bodySmall.copy(color = NeutralGrey, fontSize = 11.sp)
                                    )
                                }
                            }

                            HorizontalDivider(modifier = Modifier.padding(vertical = 14.dp), color = DividerGrey)

                            Text(
                                text = art.content,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    lineHeight = 24.sp,
                                    color = DeepNavy
                                )
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Button(
                                onClick = { selectedArticle = null },
                                modifier = Modifier.fillMaxWidth().height(48.dp),
                                shape = RoundedCornerShape(14.dp),
                                colors = bkButtonColors(DenimBlue)
                            ) {
                                Text("Kembali ke Daftar Artikel", color = PureWhite, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(28.dp))
                }
            }
        }
    }
}

@Preview(name = "Siswa - Perpustakaan Artikel", showBackground = true, showSystemUi = true)
@Composable
fun StudentArticleScreensPreview() {
    BkCounselingTheme {
        StudentArticleScreens(onNavigateBack = {})
    }
}
