package com.aza.apkkonseling.ui.student

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import com.aza.apkkonseling.data.MockRepository
import com.aza.apkkonseling.theme.*
import com.aza.apkkonseling.ui.components.bkButtonColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentQuizScreens(
    onNavigateBack: () -> Unit
) {
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var selectedAnswers by remember { mutableStateOf(mutableMapOf<Int, Int>()) }
    var isQuizCompleted by remember { mutableStateOf(false) }

    val questions = MockRepository.quizQuestions
    val totalQuestions = questions.size

    BkGradientBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            if (!isQuizCompleted) "Kuis Minat & Bakat" else "Hasil Asesmen Minat & Bakat",
                            fontWeight = FontWeight.Bold,
                            color = DeepNavy
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = DeepNavy)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { paddingValues ->
            if (!isQuizCompleted) {
                // Quiz Questions Screen
                val currentQuestion = questions[currentQuestionIndex]
                val targetProgress = (currentQuestionIndex + 1).toFloat() / totalQuestions
                val animatedProgress by animateFloatAsState(targetValue = targetProgress, label = "quizProgress")

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 20.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Progress Bar
                    LinearProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = DenimBlue,
                        trackColor = SoftIceBlue
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = SoftIceBlue,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Soal ${currentQuestionIndex + 1} dari $totalQuestions",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.bodySmall.copy(color = DeepNavy, fontWeight = FontWeight.Bold)
                            )
                        }

                        Text(
                            text = "Kategori: ${currentQuestion.trait}",
                            style = MaterialTheme.typography.bodySmall.copy(color = SteelBlue, fontWeight = FontWeight.SemiBold)
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = PureWhite),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                    ) {
                        Column(modifier = Modifier.padding(22.dp)) {
                            Text(
                                text = currentQuestion.question,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    lineHeight = 24.sp,
                                    color = DeepNavy
                                )
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            currentQuestion.options.forEachIndexed { optIndex, optionText ->
                                val isSelected = selectedAnswers[currentQuestion.id] == optIndex
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 5.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .clickable {
                                            selectedAnswers = selectedAnswers.toMutableMap().apply {
                                                put(currentQuestion.id, optIndex)
                                            }
                                        },
                                    shape = RoundedCornerShape(14.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isSelected) IceBlue else LightGrey
                                    ),
                                    border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, DenimBlue) else null
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(14.dp)
                                    ) {
                                        RadioButton(
                                            selected = isSelected,
                                            onClick = {
                                                selectedAnswers = selectedAnswers.toMutableMap().apply {
                                                    put(currentQuestion.id, optIndex)
                                                }
                                            },
                                            colors = RadioButtonDefaults.colors(selectedColor = DenimBlue)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = optionText,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                color = DeepNavy,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (currentQuestionIndex > 0) {
                            OutlinedButton(
                                onClick = { currentQuestionIndex-- },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.height(48.dp)
                            ) {
                                Text("Sebelumnya", color = DenimBlue, fontWeight = FontWeight.SemiBold)
                            }
                        } else {
                            Spacer(modifier = Modifier.width(1.dp))
                        }

                        Button(
                            onClick = {
                                if (currentQuestionIndex < totalQuestions - 1) {
                                    currentQuestionIndex++
                                } else {
                                    isQuizCompleted = true
                                }
                            },
                            enabled = selectedAnswers.containsKey(currentQuestion.id),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.height(48.dp),
                            colors = bkButtonColors(DenimBlue)
                        ) {
                            Text(
                                text = if (currentQuestionIndex == totalQuestions - 1) "Selesai & Lihat Hasil" else "Selanjutnya",
                                color = PureWhite,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            } else {
                // Quiz Result Screen
                val result = MockRepository.sampleQuizResult

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 20.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = PureWhite),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(22.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(SoftIceBlue),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = GoldenYellow, modifier = Modifier.size(36.dp))
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Tipe Kepribadian Anda:",
                                style = MaterialTheme.typography.bodyMedium.copy(color = NeutralGrey)
                            )
                            Text(
                                text = result.personalityType,
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = DeepNavy
                                )
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = result.summary,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    lineHeight = 22.sp,
                                    color = DeepNavyVariant
                                )
                            )

                            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = DividerGrey)

                            // Dominant Traits
                            Text(
                                text = "Kecenderungan Potensi Terkuat:",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = DeepNavy),
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            result.dominantTraits.forEach { trait ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                ) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = DenimBlue, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(trait, style = MaterialTheme.typography.bodyMedium.copy(color = DeepNavy, fontWeight = FontWeight.Medium))
                                }
                            }

                            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = DividerGrey)

                            // Career & Study Recommendations
                            Text(
                                text = "Rekomendasi Jurusan & Karir Masa Depan:",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = DeepNavy),
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            result.careerRecommendations.forEach { rec ->
                                Surface(
                                    color = SoftIceBlue,
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                ) {
                                    Text(
                                        text = rec,
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                        style = MaterialTheme.typography.bodyMedium.copy(color = DenimBlue, fontWeight = FontWeight.Bold)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Button(
                                onClick = {
                                    selectedAnswers = mutableMapOf()
                                    currentQuestionIndex = 0
                                    isQuizCompleted = false
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                shape = RoundedCornerShape(14.dp),
                                colors = bkButtonColors(DenimBlue)
                            ) {
                                Text("Ulangi Kuis Minat & Bakat", color = PureWhite, fontWeight = FontWeight.Bold)
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedButton(
                                onClick = onNavigateBack,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = DeepNavy)
                            ) {
                                Text("Kembali ke Portal Siswa", color = DeepNavy, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(28.dp))
                }
            }
        }
    }
}

@Preview(name = "Siswa - Kuis Minat & Bakat", showBackground = true, showSystemUi = true)
@Composable
fun StudentQuizScreensPreview() {
    BkCounselingTheme {
        StudentQuizScreens(onNavigateBack = {})
    }
}
