package com.aza.apkkonseling.ui.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aza.apkkonseling.data.MockRepository
import com.aza.apkkonseling.data.UserRole
import com.aza.apkkonseling.theme.*
import com.aza.apkkonseling.ui.components.bkButtonColors

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: (UserRole) -> Unit
) {
    var selectedRole by remember { mutableStateOf(UserRole.STUDENT) }
    var nisnOrUsername by remember { mutableStateOf("0054321987") }
    var password by remember { mutableStateOf("password123") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    BkGradientBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Header Icon & Title
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(DenimBlue),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = "Logo BK",
                    tint = PureWhite,
                    modifier = Modifier.size(42.dp)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Bimbingan & Konseling",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = DeepNavy
                ),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Layanan Konseling Digital Ramah Siswa",
                style = MaterialTheme.typography.bodyMedium.copy(color = DeepNavyVariant),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Main Card Container
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Segmented Role Selection Tabs
                    TabRow(
                        selectedTabIndex = if (selectedRole == UserRole.STUDENT) 0 else 1,
                        containerColor = IceBlue,
                        contentColor = DenimBlue,
                        modifier = Modifier.clip(RoundedCornerShape(14.dp))
                    ) {
                        Tab(
                            selected = selectedRole == UserRole.STUDENT,
                            onClick = {
                                selectedRole = UserRole.STUDENT
                                nisnOrUsername = "0054321987"
                                password = "password123"
                                errorMessage = null
                            },
                            text = {
                                Text(
                                    "Siswa",
                                    fontWeight = if (selectedRole == UserRole.STUDENT) FontWeight.Bold else FontWeight.Medium,
                                    color = if (selectedRole == UserRole.STUDENT) DeepNavy else SteelBlue
                                )
                            }
                        )
                        Tab(
                            selected = selectedRole == UserRole.ADMIN,
                            onClick = {
                                selectedRole = UserRole.ADMIN
                                nisnOrUsername = "197805122003122001"
                                password = "adminpassword"
                                errorMessage = null
                            },
                            text = {
                                Text(
                                    "Guru BK / Admin",
                                    fontWeight = if (selectedRole == UserRole.ADMIN) FontWeight.Bold else FontWeight.Medium,
                                    color = if (selectedRole == UserRole.ADMIN) DeepNavy else SteelBlue
                                )
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    // Input Field (NISN or Username/NIP)
                    OutlinedTextField(
                        value = nisnOrUsername,
                        onValueChange = {
                            nisnOrUsername = it
                            errorMessage = null
                        },
                        label = {
                            Text(if (selectedRole == UserRole.STUDENT) "NISN Siswa (10 Digit)" else "Username / NIP Guru BK")
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = null, tint = DenimBlue)
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = if (selectedRole == UserRole.STUDENT) KeyboardType.Number else KeyboardType.Text
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Password Field with Visibility Toggle
                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            errorMessage = null
                        },
                        label = { Text("Kata Sandi") },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = DenimBlue)
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = if (passwordVisible) "Sembunyikan sandi" else "Tampilkan sandi",
                                    tint = SteelBlue
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )

                    AnimatedVisibility(visible = errorMessage != null) {
                        Column {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = errorMessage ?: "",
                                color = SosRed,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Login Button
                    Button(
                        onClick = {
                            if (nisnOrUsername.isBlank() || password.isBlank()) {
                                errorMessage = "Mohon isi semua data login."
                            } else {
                                MockRepository.currentUserRole = selectedRole
                                onLoginSuccess(selectedRole)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = bkButtonColors(DenimBlue)
                    ) {
                        Text(
                            text = if (selectedRole == UserRole.STUDENT) "Masuk sebagai Siswa" else "Masuk sebagai Guru BK",
                            style = MaterialTheme.typography.titleMedium.copy(color = PureWhite, fontWeight = FontWeight.Bold)
                        )
                    }

                    // Register link for students
                    if (selectedRole == UserRole.STUDENT) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Belum memiliki akun? ",
                                style = MaterialTheme.typography.bodySmall.copy(color = DeepNavyVariant)
                            )
                            TextButton(onClick = onNavigateToRegister) {
                                Text(
                                    text = "Daftar Siswa",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = SteelBlue,
                                        fontWeight = FontWeight.Bold
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

@Composable
fun RegisterStudentScreen(
    onNavigateBackToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    var nisn by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    BkGradientBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Pendaftaran Akun Siswa",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = DeepNavy
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Buat akun BK dengan NISN resmi sekolah Anda",
                style = MaterialTheme.typography.bodyMedium.copy(color = DeepNavyVariant),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp)
                ) {
                    OutlinedTextField(
                        value = nisn,
                        onValueChange = {
                            nisn = it
                            errorMessage = null
                        },
                        label = { Text("Masukkan NISN (10 digit)") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = DenimBlue) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            errorMessage = null
                        },
                        label = { Text("Buat Kata Sandi") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = DenimBlue) },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = null,
                                    tint = SteelBlue
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = {
                            confirmPassword = it
                            errorMessage = null
                        },
                        label = { Text("Konfirmasi Kata Sandi") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = DenimBlue) },
                        trailingIcon = {
                            IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                Icon(
                                    imageVector = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = null,
                                    tint = SteelBlue
                                )
                            }
                        },
                        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )

                    AnimatedVisibility(visible = errorMessage != null) {
                        Column {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = errorMessage ?: "",
                                color = SosRed,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            when {
                                nisn.isBlank() || password.isBlank() || confirmPassword.isBlank() -> {
                                    errorMessage = "Semua kolom wajib diisi."
                                }
                                nisn.length < 5 -> {
                                    errorMessage = "NISN minimal 5 digit angka."
                                }
                                password.length < 6 -> {
                                    errorMessage = "Kata sandi minimal 6 karakter."
                                }
                                password != confirmPassword -> {
                                    errorMessage = "Konfirmasi kata sandi tidak cocok."
                                }
                                else -> {
                                    errorMessage = null
                                    showSuccessDialog = true
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = bkButtonColors(DenimBlue)
                    ) {
                        Text(
                            text = "Daftar Sekarang",
                            style = MaterialTheme.typography.titleMedium.copy(color = PureWhite, fontWeight = FontWeight.Bold)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedButton(
                        onClick = onNavigateBackToLogin,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Kembali ke Halaman Masuk", color = DenimBlue, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            shape = RoundedCornerShape(20.dp),
            containerColor = PureWhite,
            title = { Text("Pendaftaran Berhasil", fontWeight = FontWeight.Bold, color = DeepNavy) },
            text = { Text("Akun siswa dengan NISN $nisn berhasil dibuat. Silakan masuk untuk memulai layanan BK.", color = DeepNavyVariant) },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        onRegisterSuccess()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DenimBlue),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Masuk", color = PureWhite, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

@Preview(name = "Autentikasi - Masuk", showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    BkCounselingTheme {
        LoginScreen(
            onNavigateToRegister = {},
            onLoginSuccess = {}
        )
    }
}

@Preview(name = "Autentikasi - Registrasi Siswa", showBackground = true, showSystemUi = true)
@Composable
fun RegisterStudentScreenPreview() {
    BkCounselingTheme {
        RegisterStudentScreen(
            onNavigateBackToLogin = {},
            onRegisterSuccess = {}
        )
    }
}
