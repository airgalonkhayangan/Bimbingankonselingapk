package com.aza.apkkonseling.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.aza.apkkonseling.ui.components.BkSnackbarHost
import com.aza.apkkonseling.ui.components.ConfirmDeleteDialog
import com.aza.apkkonseling.ui.components.EmptyStateView
import com.aza.apkkonseling.ui.components.bkButtonColors
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminArticleManagementScreen(
    onNavigateBack: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var editingArticle by remember { mutableStateOf<Article?>(null) }
    var articleToDelete by remember { mutableStateOf<Article?>(null) }

    var titleInput by remember { mutableStateOf("") }
    var categoryInput by remember { mutableStateOf("Kesehatan Mental") }
    var contentInput by remember { mutableStateOf("") }

    val categories = listOf("Kesehatan Mental", "Bimbingan Belajar", "Perencanaan Karir", "Hubungan Sosial")
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    fun openDialog(article: Article?) {
        if (article != null) {
            editingArticle = article
            titleInput = article.title
            categoryInput = article.category
            contentInput = article.content
        } else {
            editingArticle = null
            titleInput = ""
            categoryInput = "Kesehatan Mental"
            contentInput = ""
        }
        showDialog = true
    }

    BkGradientBackground {
        Scaffold(
            containerColor = Color.Transparent,
            snackbarHost = { BkSnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = { Text("Perpustakaan Konseling", fontWeight = FontWeight.Bold, color = DeepNavy) },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = DeepNavy)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Artikel BK Terpublikasi",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = DeepNavy)
                        )
                        Text(
                            text = "Kelola konten edukasi untuk siswa",
                            style = MaterialTheme.typography.bodySmall.copy(color = NeutralGrey)
                        )
                    }
                    Button(
                        onClick = { openDialog(null) },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DenimBlue),
                        modifier = Modifier.height(48.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Tulis Artikel", color = PureWhite, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (MockRepository.articles.isEmpty()) {
                    EmptyStateView(
                        icon = Icons.Default.PostAdd,
                        title = "Belum Ada Artikel",
                        description = "Tulis artikel edukasi untuk membantu bimbingan dan perkembangan siswa.",
                        actionText = "Tulis Artikel Sekarang",
                        onActionClick = { openDialog(null) }
                    )
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(MockRepository.articles) { article ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(18.dp),
                                colors = CardDefaults.cardColors(containerColor = PureWhite),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
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
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    color = DenimBlue,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 11.sp
                                                )
                                            )
                                        }
                                        Row {
                                            IconButton(
                                                onClick = { openDialog(article) },
                                                modifier = Modifier.size(30.dp)
                                            ) {
                                                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = SteelBlue, modifier = Modifier.size(18.dp))
                                            }
                                            IconButton(
                                                onClick = { articleToDelete = article },
                                                modifier = Modifier.size(30.dp)
                                            ) {
                                                Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = SosRed, modifier = Modifier.size(18.dp))
                                            }
                                        }
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
                                        style = MaterialTheme.typography.bodyMedium.copy(color = DeepNavyVariant),
                                        maxLines = 2
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = "Penulis: ${article.author} • ${article.date} (${article.readTime})",
                                        style = MaterialTheme.typography.bodySmall.copy(color = NeutralGrey)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Add / Edit Dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            shape = RoundedCornerShape(20.dp),
            containerColor = PureWhite,
            title = {
                Text(
                    text = if (editingArticle == null) "Tulis Artikel Baru" else "Edit Artikel",
                    fontWeight = FontWeight.Bold,
                    color = DeepNavy
                )
            },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    OutlinedTextField(
                        value = titleInput,
                        onValueChange = { titleInput = it },
                        label = { Text("Judul Artikel") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Kategori Artikel:", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, color = DeepNavy)
                    Spacer(modifier = Modifier.height(4.dp))
                    Column {
                        categories.forEach { cat ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { categoryInput = cat }
                                    .padding(vertical = 2.dp)
                            ) {
                                RadioButton(
                                    selected = categoryInput == cat,
                                    onClick = { categoryInput = cat },
                                    colors = RadioButtonDefaults.colors(selectedColor = DenimBlue)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(cat, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = contentInput,
                        onValueChange = { contentInput = it },
                        label = { Text("Isi Konten Artikel") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 8,
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (titleInput.isNotBlank() && contentInput.isNotBlank()) {
                            if (editingArticle != null) {
                                MockRepository.updateArticle(
                                    id = editingArticle!!.id,
                                    title = titleInput,
                                    category = categoryInput,
                                    content = contentInput
                                )
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Artikel berhasil diperbarui")
                                }
                            } else {
                                MockRepository.addArticle(
                                    title = titleInput,
                                    category = categoryInput,
                                    content = contentInput
                                )
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Artikel baru berhasil dipublikasikan")
                                }
                            }
                            showDialog = false
                        }
                    },
                    colors = bkButtonColors(DenimBlue),
                    shape = RoundedCornerShape(12.dp),
                    enabled = titleInput.isNotBlank() && contentInput.isNotBlank()
                ) {
                    Text("Publikasikan", color = PureWhite, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Batal", color = NeutralGrey)
                }
            }
        )
    }

    // Delete Confirmation Dialog
    ConfirmDeleteDialog(
        visible = articleToDelete != null,
        title = "Hapus Artikel",
        message = "Apakah Anda yakin ingin menghapus artikel \"${articleToDelete?.title}\"? Tindakan ini tidak dapat dibatalkan.",
        onConfirm = {
            articleToDelete?.let {
                MockRepository.deleteArticle(it.id)
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Artikel telah dihapus")
                }
            }
            articleToDelete = null
        },
        onDismiss = { articleToDelete = null }
    )
}

@Preview(name = "Admin - Kelola Artikel Konseling", showBackground = true, showSystemUi = true)
@Composable
fun AdminArticleManagementScreenPreview() {
    BkCounselingTheme {
        AdminArticleManagementScreen(onNavigateBack = {})
    }
}
