package com.aza.apkkonseling.ui.student

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
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
import com.aza.apkkonseling.data.ChatMessage
import com.aza.apkkonseling.data.MockRepository
import com.aza.apkkonseling.data.UserRole
import com.aza.apkkonseling.theme.*
import com.aza.apkkonseling.ui.components.BkSnackbarHost
import com.aza.apkkonseling.ui.components.ConfirmDeleteDialog
import com.aza.apkkonseling.ui.components.EmptyStateView
import com.aza.apkkonseling.ui.components.bkButtonColors
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentTeacherSelectScreen(
    onNavigateBack: () -> Unit,
    onTeacherSelected: (teacherId: String, isAnonymous: Boolean) -> Unit
) {
    var isAnonymousMode by remember { mutableStateOf(false) }

    BkGradientBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text("Pilih Guru BK", fontWeight = FontWeight.Bold, color = DeepNavy) },
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
                // Identity Mode Toggle Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = if (isAnonymousMode) WarmCream else PureWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (isAnonymousMode) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = null,
                                    tint = if (isAnonymousMode) AmberAccent else DenimBlue
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isAnonymousMode) "Mode Anonim Aktif" else "Mode Nama Asli",
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (isAnonymousMode)
                                    "Identitas Anda disamarkan sebagai 'Siswa Anonim'"
                                else
                                    "Guru BK dapat melihat nama & kelas Anda (${MockRepository.currentStudentName})",
                                style = MaterialTheme.typography.bodySmall.copy(color = NeutralGrey)
                            )
                        }
                        Switch(
                            checked = isAnonymousMode,
                            onCheckedChange = { isAnonymousMode = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = AmberAccent,
                                checkedTrackColor = SoftYellow
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Daftar Guru BK yang Siap Dihubungi:",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = DeepNavy
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(MockRepository.teachers) { teacher ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(18.dp)),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = PureWhite),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clip(CircleShape)
                                        .background(IceBlue),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        tint = DenimBlue,
                                        modifier = Modifier.size(30.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(14.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = teacher.name,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = DeepNavy
                                        )
                                    )
                                    Text(
                                        text = "Spesialisasi: ${teacher.specialization}",
                                        style = MaterialTheme.typography.bodySmall.copy(color = NeutralGrey)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Surface(
                                        color = if (teacher.status.contains("Online")) SoftIceBlue else WarmCream,
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = teacher.status,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (teacher.status.contains("Online")) DenimBlue else AmberAccent
                                            )
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    onClick = { onTeacherSelected(teacher.id, isAnonymousMode) },
                                    colors = ButtonDefaults.buttonColors(containerColor = DenimBlue),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Mulai Chat", fontSize = 12.sp, color = PureWhite, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentChatRoomScreen(
    teacherId: String,
    isAnonymous: Boolean,
    onNavigateBack: () -> Unit
) {
    val teacher = MockRepository.teachers.find { it.id == teacherId } ?: MockRepository.teachers.first()
    val currentRoomId = MockRepository.getRoomId(MockRepository.currentStudentId, teacher.id, isAnonymous)
    val roomMessages = MockRepository.chatMessages.filter { it.roomId == currentRoomId && !it.isDeleted }

    var inputText by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // State for message options (Edit / Delete)
    var selectedMessageForOption by remember { mutableStateOf<ChatMessage?>(null) }
    var editingMessageId by remember { mutableStateOf<String?>(null) }
    var messageToDelete by remember { mutableStateOf<ChatMessage?>(null) }

    BkGradientBackground {
        Scaffold(
            containerColor = Color.Transparent,
            snackbarHost = { BkSnackbarHost(snackbarHostState) },
            topBar = {
                Column {
                    TopAppBar(
                        title = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(IceBlue),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Person, contentDescription = null, tint = DenimBlue, modifier = Modifier.size(24.dp))
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(teacher.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DeepNavy)
                                    Text(
                                        text = teacher.status,
                                        fontSize = 12.sp,
                                        color = SteelBlue
                                    )
                                }
                            }
                        },
                        navigationIcon = {
                            IconButton(onClick = onNavigateBack) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = DeepNavy)
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = PureWhite)
                    )

                    // Identity Banner at Top of Chat
                    Surface(
                        color = if (isAnonymous) WarmCream else SoftIceBlue,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (isAnonymous) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = null,
                                tint = if (isAnonymous) AmberAccent else DenimBlue,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isAnonymous)
                                    "Mode Anonim Aktif: Identitas Anda disamarkan sebagai 'Siswa Anonim'"
                                else
                                    "Identitas Asli Aktif: Guru BK mengetahui nama & kelas Anda",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = DeepNavy
                                )
                            )
                        }
                    }
                }
            },
            bottomBar = {
                // Input Bar (WhatsApp-style with inline Edit indicator)
                Surface(
                    color = PureWhite,
                    shadowElevation = 8.dp
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // Inline Edit Mode Indicator
                        AnimatedVisibility(visible = editingMessageId != null) {
                            Surface(
                                color = SoftYellow.copy(alpha = 0.5f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Edit, contentDescription = null, tint = AmberAccent, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Mengedit pesan...",
                                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = DeepNavy)
                                        )
                                    }
                                    IconButton(
                                        onClick = {
                                            editingMessageId = null
                                            inputText = ""
                                        },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(Icons.Default.Close, contentDescription = "Batal", tint = NeutralGrey, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = inputText,
                                onValueChange = { inputText = it },
                                placeholder = { Text("Ketik pesan konseling...") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(24.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedContainerColor = LightGrey,
                                    focusedContainerColor = PureWhite
                                ),
                                maxLines = 4
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(
                                onClick = {
                                    if (inputText.isNotBlank()) {
                                        val activeEditId = editingMessageId
                                        if (activeEditId != null) {
                                            MockRepository.editChatMessage(activeEditId, inputText)
                                            editingMessageId = null
                                            inputText = ""
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar("Pesan berhasil diperbarui")
                                            }
                                        } else {
                                            val senderDisplayName = if (isAnonymous) "Siswa Anonim" else MockRepository.currentStudentName
                                            MockRepository.sendChatMessage(
                                                roomId = currentRoomId,
                                                studentId = MockRepository.currentStudentId,
                                                teacherId = teacher.id,
                                                senderRole = "STUDENT",
                                                senderName = senderDisplayName,
                                                isAnonymous = isAnonymous,
                                                text = inputText
                                            )
                                            inputText = ""
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar("Pesan terkirim")
                                            }
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(DenimBlue)
                            ) {
                                Icon(
                                    imageVector = if (editingMessageId != null) Icons.Default.Check else Icons.AutoMirrored.Filled.Send,
                                    contentDescription = "Kirim",
                                    tint = PureWhite
                                )
                            }
                        }
                    }
                }
            }
        ) { paddingValues ->
            if (roomMessages.isEmpty()) {
                EmptyStateView(
                    icon = Icons.Default.ChatBubbleOutline,
                    title = "Belum Ada Pesan",
                    description = "Mulai percakapan aman dan nyaman dengan ${teacher.name} sekarang.",
                    modifier = Modifier.padding(paddingValues)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    reverseLayout = false
                ) {
                    items(roomMessages) { message ->
                        val isMe = message.senderRole == "STUDENT"
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalAlignment = if (isMe) Alignment.End else Alignment.Start
                        ) {
                            Card(
                                modifier = Modifier
                                    .widthIn(max = 290.dp)
                                    .clip(
                                        RoundedCornerShape(
                                            topStart = 16.dp,
                                            topEnd = 16.dp,
                                            bottomStart = if (isMe) 16.dp else 4.dp,
                                            bottomEnd = if (isMe) 4.dp else 16.dp
                                        )
                                    ),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isMe) DenimBlue else PureWhite
                                ),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(horizontal = 14.dp, vertical = 10.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = message.senderName,
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = if (isMe) SoftYellow else SteelBlue
                                            )
                                        )
                                        if (isMe) {
                                            IconButton(
                                                onClick = { selectedMessageForOption = message },
                                                modifier = Modifier.size(20.dp)
                                            ) {
                                                Icon(
                                                    Icons.Default.MoreVert,
                                                    contentDescription = "Opsi",
                                                    tint = PureWhite.copy(alpha = 0.85f),
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = message.content,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = if (isMe) PureWhite else DeepNavy
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        if (message.isEdited) {
                                            Text(
                                                text = "(diedit) ",
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    fontSize = 10.sp,
                                                    color = if (isMe) PureWhite.copy(alpha = 0.7f) else NeutralGrey
                                                )
                                            )
                                        }
                                        Text(
                                            text = message.timestamp,
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontSize = 10.sp,
                                                color = if (isMe) PureWhite.copy(alpha = 0.7f) else NeutralGrey
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Message Options Modal Bottom Sheet
    if (selectedMessageForOption != null) {
        val msg = selectedMessageForOption!!
        ModalBottomSheet(
            onDismissRequest = { selectedMessageForOption = null },
            containerColor = PureWhite,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Opsi Pesan",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = DeepNavy)
                )
                Spacer(modifier = Modifier.height(14.dp))
                ListItem(
                    headlineContent = { Text("Sunting / Edit Pesan", color = DeepNavy) },
                    leadingContent = { Icon(Icons.Default.Edit, contentDescription = null, tint = DenimBlue) },
                    modifier = Modifier.clickable {
                        editingMessageId = msg.id
                        inputText = msg.content
                        selectedMessageForOption = null
                    }
                )
                HorizontalDivider(color = DividerGrey)
                ListItem(
                    headlineContent = { Text("Hapus Pesan", color = SosRed) },
                    leadingContent = { Icon(Icons.Default.Delete, contentDescription = null, tint = SosRed) },
                    modifier = Modifier.clickable {
                        messageToDelete = msg
                        selectedMessageForOption = null
                    }
                )
            }
        }
    }

    // Delete Confirmation Dialog
    ConfirmDeleteDialog(
        visible = messageToDelete != null,
        title = "Hapus Pesan",
        message = "Apakah Anda yakin ingin menghapus pesan ini?",
        onConfirm = {
            messageToDelete?.let {
                MockRepository.deleteChatMessage(it.id)
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Pesan telah dihapus")
                }
            }
            messageToDelete = null
        },
        onDismiss = { messageToDelete = null }
    )
}

@Preview(name = "Siswa - Pilih Guru BK", showBackground = true, showSystemUi = true)
@Composable
fun StudentTeacherSelectScreenPreview() {
    BkCounselingTheme {
        StudentTeacherSelectScreen(
            onNavigateBack = {},
            onTeacherSelected = { _, _ -> }
        )
    }
}

@Preview(name = "Siswa - Ruang Chat BK", showBackground = true, showSystemUi = true)
@Composable
fun StudentChatRoomScreenPreview() {
    BkCounselingTheme {
        StudentChatRoomScreen(
            teacherId = MockRepository.teachers.first().id,
            isAnonymous = false,
            onNavigateBack = {}
        )
    }
}
