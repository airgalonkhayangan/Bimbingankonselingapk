package com.aza.apkkonseling.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

enum class UserRole {
    STUDENT,
    ADMIN
}

// --------------------------------------------------
// 1. SOS Model & State (Enhanced with Progress % & Final Report)
// --------------------------------------------------
data class SosReport(
    val id: String,
    val studentName: String,
    val nisn: String,
    val className: String,
    val type: String, // "Perundungan (Bullying)", "Krisis Mental/Emosional", "Kekerasan Fisik", "Darurat Lainnya"
    val description: String,
    val timestamp: String,
    val status: String, // "Perlu Tindakan", "Sedang Ditangani", "Selesai"
    val progressPercent: Int = 0, // 0 to 100%
    val progressNotes: String = "",
    // Final result report fields (populated when completed or progress reaches 100%):
    val actionsTaken: String = "",
    val partiesInvolved: String = "",
    val finalResolution: String = "",
    val monitoringRecommendation: String = "",
    val completedDate: String = ""
)

// --------------------------------------------------
// 2. Chat Model & State
// --------------------------------------------------
data class Teacher(
    val id: String,
    val name: String,
    val nip: String,
    val status: String, // "Tersedia / Online", "Konseling Offline", "Sibuk"
    val specialization: String
)

data class ChatMessage(
    val id: String,
    val roomId: String, // Unique format: "${studentId}_${teacherId}_${if (isAnonymous) "anon" else "real"}"
    val studentId: String,
    val teacherId: String,
    val isAnonymous: Boolean,
    val senderRole: String, // "STUDENT" or "ADMIN"
    val senderName: String,
    val text: String,
    val timestamp: String,
    val isEdited: Boolean = false,
    val isDeleted: Boolean = false
) {
    val content: String get() = text
    val channelId: String get() = teacherId
}

data class ChatRoomSummary(
    val roomId: String,
    val studentId: String,
    val studentName: String,
    val studentClass: String,
    val teacherId: String,
    val teacherName: String,
    val isAnonymous: Boolean,
    val lastMessage: String,
    val lastTimestamp: String
)

// --------------------------------------------------
// 3. Appointment Model & State
// --------------------------------------------------
data class Appointment(
    val id: String,
    val studentName: String,
    val nisn: String,
    val teacherName: String,
    val mode: String, // "Offline (Ruang BK)", "Online (Chat BK)"
    val date: String,
    val timeSlot: String,
    val reason: String,
    val status: String // "Menunggu Konfirmasi", "Disetujui", "Ditolak"
)

data class TeacherScheduleSlot(
    val id: String,
    val dayOfWeek: String, // "Senin", "Selasa", "Rabu", "Kamis", "Jumat"
    val timeSlot: String,  // "08:00 - 09:00", "09:30 - 10:30", etc.
    val mode: String       // "Offline", "Online"
)

// --------------------------------------------------
// 4. Perpustakaan Konseling (formerly Article) Model & State
// --------------------------------------------------
data class Article(
    val id: String,
    val title: String,
    val category: String, // "Kesehatan Mental", "Bimbingan Belajar", "Perencanaan Karir", "Hubungan Sosial"
    val author: String,
    val content: String,
    val date: String,
    val readTime: String
)

// --------------------------------------------------
// 5. Mood Board & Diary Model & State
// --------------------------------------------------
data class MoodEntry(
    val id: String,
    val studentName: String,
    val moodEmoji: String,
    val moodLabel: String,
    val note: String,
    val date: String // YYYY-MM-DD
)

data class DiaryEntry(
    val id: String,
    val studentName: String,
    val title: String,
    val content: String,
    val date: String,
    val time: String
)

// --------------------------------------------------
// 6. Quiz Model & State
// --------------------------------------------------
data class QuizQuestion(
    val id: Int,
    val question: String,
    val options: List<String>,
    val trait: String
)

data class QuizResult(
    val personalityType: String,
    val summary: String,
    val dominantTraits: List<String>,
    val careerRecommendations: List<String>
)

// --------------------------------------------------
// 7. Counseling Notes Model & State
// --------------------------------------------------
data class CounselingNote(
    val id: String,
    val studentName: String,
    val nisn: String,
    val className: String,
    val academicYear: String,
    val semester: String, // "Ganjil" / "Genap"
    val date: String,
    val mode: String, // "Offline", "Online"
    val serviceDomain: String, // "Pribadi", "Sosial", "Belajar", "Karir"
    val serviceType: String,   // "Konseling Individual", "Konseling Kelompok", "Konsultasi"
    val complaint: String,
    val symptoms: String,
    val backgroundCause: String,
    val approachTechnique: String,
    val solutionOptions: String,
    val result: String,
    val followUpPlan: String, // Rencana Tindak Lanjut (RTL)
    val status: String         // "Proses", "Perlu Tindak Lanjut", "Selesai"
)

// --------------------------------------------------
// 8. Expanded Student Profile & Intake Models
// --------------------------------------------------
data class PsychologicalTestResult(
    val personalityType: String,
    val mainInterests: String,
    val dominantAptitudes: String,
    val testDate: String,
    val counselorNotes: String
)

data class AcademicSemesterRecord(
    val id: String,
    val academicYear: String,
    val semester: String, // "Semester 1 (Ganjil)" / "Semester 2 (Genap)"
    val averageScore: Double,
    val rankInClass: Int,
    val totalStudentsInClass: Int = 36
) {
    val classRank: Int get() = rankInClass
}

data class NonAcademicAchievement(
    val id: String,
    val achievementName: String,
    val category: String, // "Olahraga", "Seni & Budaya", "Organisasi", "Lomba Akademik / Sains"
    val level: String, // "Tingkat Sekolah", "Tingkat Kota / Kabupaten", "Tingkat Provinsi", "Tingkat Nasional"
    val year: String
) {
    val title: String get() = achievementName
}

data class StudentProfile(
    val id: String,
    val nisn: String,
    val name: String,
    val className: String,
    val academicYear: String, // "2026/2027", "2025/2026"
    val gender: String,       // "Laki-laki", "Perempuan"
    val intakeType: String,   // "Reguler / Siswa Baru", "Pindahan", "Intake Tahun Lalu"
    val birthPlaceDate: String = "Jakarta, 14 Mei 2008",
    val address: String = "Jl. Merdeka No. 45, RT 02/05, Jakarta Timur",
    val parentName: String = "H. Hendra Fauzi",
    val parentPhone: String = "0812-3456-7890",
    val psychologicalTest: PsychologicalTestResult? = null,
    val academicRecords: List<AcademicSemesterRecord> = emptyList(),
    val nonAcademicAchievements: List<NonAcademicAchievement> = emptyList()
)

// --------------------------------------------------
// 9. Gelar Kasus (Case Conference) Models
// --------------------------------------------------
data class CaseConferenceAttendee(
    val id: String,
    val name: String,
    val role: String, // "Orang Tua / Wali", "Guru BK", "Wali Kelas", "Kepala Sekolah", "Siswa Terkait"
    val status: String // "Hadir", "Izin", "Tidak Hadir" (Note: "Hadir" uses IceBlue/DenimBlue, NEVER GREEN!)
)

data class CaseConferenceDoc(
    val id: String,
    val title: String,
    val timestamp: String,
    val description: String
)

data class CaseConference(
    val id: String,
    val title: String,
    val date: String,
    val time: String,
    val location: String,
    val chairPerson: String, // Pimpinan Rapat
    val status: String, // "Selesai", "Dalam Proses", "Menunggu Pelaksanaan"
    val discussionSummary: String,
    val jointAgreement: String,
    val followUpRecommendation: String,
    val attendees: List<CaseConferenceAttendee>,
    val documentations: List<CaseConferenceDoc>
)

// --------------------------------------------------
// 10. Monthly Statistics Model
// --------------------------------------------------
data class MonthlyCaseStat(
    val month: String,
    val completed: Int,
    val inProgress: Int
)

// ==================================================
// Singleton In-Memory Repository
// ==================================================
object MockRepository {
    // Current user context
    var currentUserRole by mutableStateOf(UserRole.STUDENT)
    var currentStudentId by mutableStateOf("STU-1")
    var currentStudentNisn by mutableStateOf("0054321987")
    var currentStudentName by mutableStateOf("Ahmad Fauzi")
    var currentStudentClass by mutableStateOf("XII IPA 1")
    var currentTeacherId by mutableStateOf("T-1")
    var currentTeacherNip by mutableStateOf("197805122003122001")
    var currentTeacherName by mutableStateOf("Dra. Siti Rahayu, M.Pd")

    // Room ID Generator: "${studentId}_${teacherId}_${if (isAnonymous) "anon" else "real"}"
    fun getRoomId(studentId: String, teacherId: String, isAnonymous: Boolean): String {
        return "${studentId}_${teacherId}_${if (isAnonymous) "anon" else "real"}"
    }

    // --- SOS Reports (with Progress % and Final Result Report) ---
    val sosReports = mutableStateListOf(
        SosReport(
            id = "SOS-001",
            studentName = "Ahmad Fauzi",
            nisn = "0054321987",
            className = "XII IPA 1",
            type = "Perundungan (Bullying)",
            description = "Saya diintimidasi secara verbal dan ditekan di area kantin belakang oleh kakak kelas saat jam istirahat.",
            timestamp = "Hari ini, 09:15",
            status = "Sedang Ditangani",
            progressPercent = 50,
            progressNotes = "Tahap investigasi fakta dan pemanggilan saksi mata di area kantin telah selesai dilaksanakan.",
            actionsTaken = "Memanggil saksi mata dan berkoordinasi dengan petugas keamanan sekolah.",
            partiesInvolved = "Guru BK, Wali Kelas XII IPA 1, Tim Ketertiban Sekolah",
            finalResolution = "",
            monitoringRecommendation = "",
            completedDate = ""
        ),
        SosReport(
            id = "SOS-002",
            studentName = "Budi Pratama",
            nisn = "0051239876",
            className = "XI IPS 2",
            type = "Krisis Mental/Emosional",
            description = "Merasa sangat tertekan menjelang ujian akhir dan membutuhkan pendampingan segera.",
            timestamp = "Kemarin, 14:20",
            status = "Sedang Ditangani",
            progressPercent = 75,
            progressNotes = "Sesi konseling relaksasi 4-7-8 dan penjadwalan ulang target belajar sudah dimulai.",
            actionsTaken = "Sesi konseling individu dan restrukturisasi kognitif mengenai kecemasan ujian.",
            partiesInvolved = "Guru BK, Budi Pratama",
            finalResolution = "Siswa mulai merasa tenang dan telah menyusun jadwal belajar realistis.",
            monitoringRecommendation = "Check-in singkat setiap hari Jumat sebelum jam pulang.",
            completedDate = ""
        ),
        SosReport(
            id = "SOS-003",
            studentName = "Rian Hidayat",
            nisn = "0061234567",
            className = "X-A",
            type = "Kekerasan Fisik",
            description = "Terjadi perselisihan fisik di lapangan basket saat jam istirahat kedua.",
            timestamp = "20 Nov 2026, 11:00",
            status = "Selesai",
            progressPercent = 100,
            progressNotes = "Kasus perselisihan di lapangan olahraga terselesaikan dengan mediasi kekeluargaan.",
            actionsTaken = "Mediasi langsung kedua belah pihak bersama wali murid dan wali kelas X-A.",
            partiesInvolved = "Guru BK (Dra. Siti Rahayu), Wali Kelas X-A, Siswa Terkait, Kedua Orang Tua",
            finalResolution = "Kedua siswa saling meminta maaf, menandatangani pakta perdamaian sekolah, dan berkomitmen tidak mengulangi.",
            monitoringRecommendation = "Pemantauan interaksi pertemanan oleh guru olahraga dan wali kelas selama 2 pekan.",
            completedDate = "21 Nov 2026"
        )
    )

    fun addSosReport(type: String, description: String) {
        val newReport = SosReport(
            id = "SOS-${System.currentTimeMillis() % 10000}",
            studentName = currentStudentName,
            nisn = currentStudentNisn,
            className = "XII IPA 1",
            type = type,
            description = description,
            timestamp = "Baru saja",
            status = "Perlu Tindakan",
            progressPercent = 0,
            progressNotes = "Laporan baru diterima dari siswa."
        )
        sosReports.add(0, newReport)
    }

    fun updateSosStatus(id: String, newStatus: String) {
        val index = sosReports.indexOfFirst { it.id == id }
        if (index != -1) {
            val updatedProgress = if (newStatus == "Selesai") 100 else sosReports[index].progressPercent
            sosReports[index] = sosReports[index].copy(status = newStatus, progressPercent = updatedProgress)
        }
    }

    fun updateSosProgress(id: String, percent: Int, notes: String) {
        val index = sosReports.indexOfFirst { it.id == id }
        if (index != -1) {
            val newStatus = when {
                percent >= 100 -> "Selesai"
                percent > 0 -> "Sedang Ditangani"
                else -> "Perlu Tindakan"
            }
            sosReports[index] = sosReports[index].copy(
                progressPercent = percent,
                progressNotes = notes,
                status = newStatus
            )
        }
    }

    fun saveSosFinalReport(
        id: String,
        actionsTaken: String,
        partiesInvolved: String,
        finalResolution: String,
        monitoringRecommendation: String,
        completedDate: String
    ) {
        val index = sosReports.indexOfFirst { it.id == id }
        if (index != -1) {
            sosReports[index] = sosReports[index].copy(
                actionsTaken = actionsTaken,
                partiesInvolved = partiesInvolved,
                finalResolution = finalResolution,
                monitoringRecommendation = monitoringRecommendation,
                completedDate = completedDate.ifBlank { "Hari ini" },
                status = "Selesai",
                progressPercent = 100
            )
        }
    }

    // --- Teachers List ---
    val teachers = mutableStateListOf(
        Teacher("T-1", "Dra. Siti Rahayu, M.Pd", "197805122003122001", "Tersedia / Online", "Konseling Pribadi & Sosial"),
        Teacher("T-2", "Bambang Wijaya, S.Pd, Kons", "198304152008011004", "Tersedia / Online", "Bimbingan Karir & Studi Lanjut"),
        Teacher("T-3", "Nurul Hidayati, M.Psi", "199009182015032002", "Konseling Offline", "Psikologi Remaja & Belajar")
    )

    // --- Chat Messages: Distinct Rooms for Each Student & Teacher ---
    val chatMessages = mutableStateListOf(
        // Room 1: Ahmad Fauzi (STU-1) & Dra. Siti Rahayu (T-1) - Real Identity
        ChatMessage(
            id = "MSG-1",
            roomId = "STU-1_T-1_real",
            studentId = "STU-1",
            teacherId = "T-1",
            isAnonymous = false,
            senderRole = "ADMIN",
            senderName = "Dra. Siti Rahayu, M.Pd",
            text = "Halo Ahmad, ada hal yang ingin kamu ceritakan hari ini seputar sekolah?",
            timestamp = "08:30"
        ),
        ChatMessage(
            id = "MSG-2",
            roomId = "STU-1_T-1_real",
            studentId = "STU-1",
            teacherId = "T-1",
            isAnonymous = false,
            senderRole = "STUDENT",
            senderName = "Ahmad Fauzi",
            text = "Selamat pagi Ibu Siti. Iya bu, saya agak ragu memilih jurusan kuliah teknik vs sains.",
            timestamp = "08:32"
        ),
        ChatMessage(
            id = "MSG-3",
            roomId = "STU-1_T-1_real",
            studentId = "STU-1",
            teacherId = "T-1",
            isAnonymous = false,
            senderRole = "ADMIN",
            senderName = "Dra. Siti Rahayu, M.Pd",
            text = "Tentu Ahmad, mari kita jadwalkan sesi pemetaan minat dan nilai rapormu ya.",
            timestamp = "08:35"
        ),

        // Room 2: Dewi Lestari (STU-2) & Bambang Wijaya (T-2) - Real Identity
        ChatMessage(
            id = "MSG-4",
            roomId = "STU-2_T-2_real",
            studentId = "STU-2",
            teacherId = "T-2",
            isAnonymous = false,
            senderRole = "STUDENT",
            senderName = "Dewi Lestari",
            text = "Pak Bambang, saya kesulitan membagi waktu antara kegiatan OSIS dan persiapan ujian akhir.",
            timestamp = "09:10"
        ),
        ChatMessage(
            id = "MSG-5",
            roomId = "STU-2_T-2_real",
            studentId = "STU-2",
            teacherId = "T-2",
            isAnonymous = false,
            senderRole = "ADMIN",
            senderName = "Bambang Wijaya, S.Pd, Kons",
            text = "Halo Dewi, jangan cemas. Besok kita bisa buat matriks prioritas bersama di ruang konseling.",
            timestamp = "09:15"
        ),

        // Room 3: Siswa Anonim (STU-3) & Dra. Siti Rahayu (T-1) - Anonymous Mode
        ChatMessage(
            id = "MSG-6",
            roomId = "STU-3_T-1_anon",
            studentId = "STU-3",
            teacherId = "T-1",
            isAnonymous = true,
            senderRole = "STUDENT",
            senderName = "Siswa Anonim",
            text = "Bu Siti, saya merasa tertekan dan dikucilkan teman-teman sekelas belakangan ini...",
            timestamp = "10:00"
        ),
        ChatMessage(
            id = "MSG-7",
            roomId = "STU-3_T-1_anon",
            studentId = "STU-3",
            teacherId = "T-1",
            isAnonymous = true,
            senderRole = "ADMIN",
            senderName = "Dra. Siti Rahayu, M.Pd",
            text = "Halo anakku, terima kasih sudah berani bercerita. Ruang cerita ini 100% aman dan rahasia bersama Ibu.",
            timestamp = "10:05"
        )
    )

    fun sendChatMessage(
        roomId: String,
        studentId: String,
        teacherId: String,
        senderRole: String,
        senderName: String,
        isAnonymous: Boolean,
        text: String
    ) {
        val msg = ChatMessage(
            id = "MSG-${System.currentTimeMillis()}",
            roomId = roomId,
            studentId = studentId,
            teacherId = teacherId,
            isAnonymous = isAnonymous,
            senderRole = senderRole,
            senderName = senderName,
            text = text,
            timestamp = "Baru saja"
        )
        chatMessages.add(msg)
    }

    fun editChatMessage(messageId: String, newText: String) {
        val index = chatMessages.indexOfFirst { it.id == messageId }
        if (index != -1) {
            chatMessages[index] = chatMessages[index].copy(text = newText, isEdited = true)
        }
    }

    fun deleteChatMessage(messageId: String) {
        val index = chatMessages.indexOfFirst { it.id == messageId }
        if (index != -1) {
            chatMessages[index] = chatMessages[index].copy(isDeleted = true)
        }
    }

    fun getChatRooms(): List<ChatRoomSummary> {
        val roomIds = chatMessages.map { it.roomId }.distinct()
        return roomIds.map { rId ->
            val msgs = chatMessages.filter { it.roomId == rId }
            val lastActive = msgs.lastOrNull { !it.isDeleted } ?: msgs.last()
            val stu = students.find { it.id == lastActive.studentId }
            val tchr = teachers.find { it.id == lastActive.teacherId }
            val studentDisplayName = if (lastActive.isAnonymous) "Siswa Anonim (Rahasia)" else (stu?.name ?: lastActive.senderName)
            val studentClass = if (lastActive.isAnonymous) "Rahasia" else (stu?.className ?: "XII IPA 1")
            val teacherName = tchr?.name ?: "Guru BK"
            ChatRoomSummary(
                roomId = rId,
                studentId = lastActive.studentId,
                studentName = studentDisplayName,
                studentClass = studentClass,
                teacherId = lastActive.teacherId,
                teacherName = teacherName,
                isAnonymous = lastActive.isAnonymous,
                lastMessage = if (lastActive.isDeleted) "Pesan ini telah dihapus" else lastActive.text,
                lastTimestamp = lastActive.timestamp
            )
        }
    }

    // --- Schedule Slots (Teacher Admin) ---
    val teacherScheduleSlots = mutableStateListOf(
        TeacherScheduleSlot("SLOT-1", "Senin", "08:00 - 09:00", "Offline"),
        TeacherScheduleSlot("SLOT-2", "Senin", "10:00 - 11:00", "Online"),
        TeacherScheduleSlot("SLOT-3", "Rabu", "09:00 - 10:00", "Offline"),
        TeacherScheduleSlot("SLOT-4", "Kamis", "13:00 - 14:00", "Online"),
        TeacherScheduleSlot("SLOT-5", "Jumat", "08:30 - 09:30", "Offline")
    )

    fun addScheduleSlot(day: String, time: String, mode: String) {
        teacherScheduleSlots.add(TeacherScheduleSlot("SLOT-${System.currentTimeMillis() % 1000}", day, time, mode))
    }

    fun deleteScheduleSlot(id: String) {
        teacherScheduleSlots.removeAll { it.id == id }
    }

    // --- Appointments ---
    val appointments = mutableStateListOf(
        Appointment(
            id = "APT-101",
            studentName = "Ahmad Fauzi",
            nisn = "0054321987",
            teacherName = "Dra. Siti Rahayu, M.Pd",
            mode = "Offline (Ruang BK)",
            date = "2026-11-25",
            timeSlot = "08:00 - 09:00",
            reason = "Konsultasi pemilihan jurusan perguruan tinggi teknik vs sains.",
            status = "Disetujui"
        ),
        Appointment(
            id = "APT-102",
            studentName = "Dewi Lestari",
            nisn = "0057891234",
            teacherName = "Bambang Wijaya, S.Pd, Kons",
            mode = "Online (Chat BK)",
            date = "2026-11-26",
            timeSlot = "13:00 - 14:00",
            reason = "Sulit mengatur waktu belajar dan tugas kelompok.",
            status = "Menunggu Konfirmasi"
        )
    )

    fun requestAppointment(teacherName: String, mode: String, date: String, timeSlot: String, reason: String) {
        appointments.add(0, Appointment(
            id = "APT-${System.currentTimeMillis() % 1000}",
            studentName = currentStudentName,
            nisn = currentStudentNisn,
            teacherName = teacherName,
            mode = mode,
            date = date,
            timeSlot = timeSlot,
            reason = reason,
            status = "Menunggu Konfirmasi"
        ))
    }

    fun updateAppointmentStatus(id: String, newStatus: String) {
        val index = appointments.indexOfFirst { it.id == id }
        if (index != -1) {
            appointments[index] = appointments[index].copy(status = newStatus)
        }
    }

    // --- Perpustakaan Konseling (formerly Articles) ---
    val articles = mutableStateListOf(
        Article(
            id = "ART-1",
            title = "Mengelola Kecemasan Menjelang Ujian Nasional",
            category = "Kesehatan Mental",
            author = "Dra. Siti Rahayu, M.Pd",
            content = "Menjelang ujian akhir, wajar bila timbul rasa cemas. Namun, rasa cemas yang berlebihan dapat mengganggu konsentrasi. Pelajari teknik pernapasan 4-7-8, buat jadwal belajar realistis, dan jangan ragu untuk berbagi cerita dengan guru BK Anda jika merasa kewalahan.",
            date = "20 Nov 2026",
            readTime = "4 menit"
        ),
        Article(
            id = "ART-2",
            title = "Panduan Memilih Jurusan Kuliah Sesuai Minat & Bakat",
            category = "Perencanaan Karir",
            author = "Bambang Wijaya, S.Pd, Kons",
            content = "Kenali passion Anda sejak dini. Gunakan hasil tes minat bakat sebagai pijakan awal untuk mengeksplorasi prospek karir masa depan, bukan hanya mengikuti tren teman sebaya.",
            date = "18 Nov 2026",
            readTime = "5 menit"
        ),
        Article(
            id = "ART-3",
            title = "Membangun Hubungan Pertemanan yang Sehat di Sekolah",
            category = "Hubungan Sosial",
            author = "Nurul Hidayati, M.Psi",
            content = "Lingkungan pertemanan yang positif akan mendukung prestasi dan kesehatan mental. Kenali tanda-tanda 'toxic friendship' dan bagaimana cara menetapkan batasan diri (boundaries) secara tegas namun santun.",
            date = "15 Nov 2026",
            readTime = "3 menit"
        )
    )

    fun addArticle(title: String, category: String, content: String) {
        articles.add(0, Article(
            id = "ART-${System.currentTimeMillis() % 1000}",
            title = title,
            category = category,
            author = currentTeacherName,
            content = content,
            date = "Hari ini",
            readTime = "3 menit"
        ))
    }

    fun updateArticle(id: String, title: String, category: String, content: String) {
        val index = articles.indexOfFirst { it.id == id }
        if (index != -1) {
            articles[index] = articles[index].copy(title = title, category = category, content = content)
        }
    }

    fun deleteArticle(id: String) {
        articles.removeAll { it.id == id }
    }

    // --- Mood & Diary ---
    val moodEntries = mutableStateListOf(
        MoodEntry("MD-1", "Ahmad Fauzi", "😊", "Senang", "Mendapat nilai bagus di kuis matematika!", "2026-11-22"),
        MoodEntry("MD-2", "Ahmad Fauzi", "😐", "Biasa", "Hari yang cukup tenang di kelas.", "2026-11-21"),
        MoodEntry("MD-3", "Ahmad Fauzi", "😴", "Lelah", "Banyak tugas kelompok yang harus diselesaikan.", "2026-11-20"),
        MoodEntry("MD-4", "Ahmad Fauzi", "😊", "Senang", "Bisa diskusi santai dengan guru BK.", "2026-11-19"),
        MoodEntry("MD-5", "Dewi Lestari", "😢", "Sedih", "Merasa kurang dimengerti oleh teman sekelompok.", "2026-11-22"),
        MoodEntry("MD-6", "Rian Hidayat", "😡", "Marah", "Kecewa karena ada tugas yang dinilai tidak adil.", "2026-11-22"),
        MoodEntry("MD-7", "Siti Aisyah", "😴", "Lelah", "Kelelahan setelah latihan persiapan olimpiade.", "2026-11-22")
    )

    fun addMoodEntry(emoji: String, label: String, note: String) {
        moodEntries.add(0, MoodEntry(
            id = "MD-${System.currentTimeMillis() % 1000}",
            studentName = currentStudentName,
            moodEmoji = emoji,
            moodLabel = label,
            note = note,
            date = "2026-11-22"
        ))
    }

    val diaryEntries = mutableStateListOf(
        DiaryEntry(
            id = "DRY-1",
            studentName = "Ahmad Fauzi",
            title = "Refleksi Akhir Pekan",
            content = "Minggu ini terasa sangat cepat berlalu. Aku belajar bahwa mengutarakan kekhawatiran ke orang yang tepat membuat hati jauh lebih lapang. Mulai minggu depan aku bertekad tidur lebih teratur.",
            date = "21 Nov 2026",
            time = "21:30"
        ),
        DiaryEntry(
            id = "DRY-2",
            studentName = "Ahmad Fauzi",
            title = "Target Bulan Depan",
            content = "Menyelesaikan persiapan try-out pertama dan aktif berkonsultasi mengenai beasiswa perguruan tinggi.",
            date = "18 Nov 2026",
            time = "19:45"
        ),
        DiaryEntry(
            id = "DRY-3",
            studentName = "Dewi Lestari",
            title = "Menemukan Titik Temu",
            content = "Setelah berbincang dengan Pak Bambang, aku jadi sadar bahwa tidak semua hal harus diselesaikan sendiri. Mendelegasikan tugas ke rekan OSIS ternyata berhasil mengurangi beban pikiranku.",
            date = "22 Nov 2026",
            time = "14:15"
        )
    )

    fun addDiaryEntry(title: String, content: String) {
        diaryEntries.add(0, DiaryEntry(
            id = "DRY-${System.currentTimeMillis() % 1000}",
            studentName = currentStudentName,
            title = title,
            content = content,
            date = "Hari ini",
            time = "Sekarang"
        ))
    }

    fun updateDiaryEntry(id: String, title: String, content: String) {
        val index = diaryEntries.indexOfFirst { it.id == id }
        if (index != -1) {
            diaryEntries[index] = diaryEntries[index].copy(title = title, content = content)
        }
    }

    fun deleteDiaryEntry(id: String) {
        diaryEntries.removeAll { it.id == id }
    }

    // --- Quiz Questions ---
    val quizQuestions = listOf(
        QuizQuestion(1, "Ketika menghadapi tugas kelompok yang rumit, apa yang biasanya Anda lakukan?", listOf("Mengambil inisiatif membagi peran", "Fokus mengerjakan bagian analisis data", "Membantu menjaga keharmonisan anggota tim", "Mencari ide kreatif di luar kebiasaan"), "Pribadi"),
        QuizQuestion(2, "Bagaimana perasaan Anda saat harus berbicara di depan audiens atau kelas besar?", listOf("Sangat percaya diri dan antusias", "Perlu persiapan matang dengan catatan tertulis", "Lebih nyaman jika dalam bentuk dialog/tanya jawab", "Lebih suka menyampaikan visual atau presentasi"), "Sosial"),
        QuizQuestion(3, "Gaya belajar manakah yang paling efektif bagi pemahaman Anda?", listOf("Diskusi langsung dan studi kasus praktis", "Membaca mendalam dan membuat bagan konsep", "Menghubungkan teori dengan pengalaman sehari-hari", "Bereksperimen langsung dengan alat / proyek"), "Belajar"),
        QuizQuestion(4, "Bidang masa depan apa yang paling memicu rasa ingin tahu Anda?", listOf("Kepemimpinan, bisnis, dan manajemen publik", "Teknologi informasi, sains murni, dan rekayasa", "Pendidikan, psikologi, dan pelayanan kesehatan", "Desain komunikasi visual, seni, dan media kreatif"), "Karir")
    )

    val sampleQuizResult = QuizResult(
        personalityType = "Inisiator Analitis (The Strategic Thinker)",
        summary = "Anda memiliki kombinasi kuat antara kemampuan berpikir logis sistematis dan kepedulian sosial yang tinggi. Anda cepat melihat pola masalah dan terampil merancang solusi praktis.",
        dominantTraits = listOf("Problem Solving 88%", "Komunikasi Empatis 82%", "Kreativitas Terapan 79%", "Adaptabilitas 85%"),
        careerRecommendations = listOf("Teknik Informatika / Rekayasa Perangkat Lunak", "Psikologi Industri & Organisasi", "Manajemen Bisnis & Kewirausahaan", "Sains Data & Analitika")
    )

    // --- Counseling Records (Admin Notes) ---
    val counselingNotes = mutableStateListOf(
        CounselingNote(
            id = "CN-001",
            studentName = "Ahmad Fauzi",
            nisn = "0054321987",
            className = "XII IPA 1",
            academicYear = "2026/2027",
            semester = "Ganjil",
            date = "2026-11-20 09:00",
            mode = "Offline",
            serviceDomain = "Karir",
            serviceType = "Konseling Individual",
            complaint = "Siswa merasa ragu memilih antara Fakultas Teknik dan Fakultas Sains, terdapat sedikit perbedaan pandangan dengan orang tua.",
            symptoms = "Kurang fokus saat jam pelajaran tertentu, sering bertanya tentang prospek kerja.",
            backgroundCause = "Kurangnya pemaparan informasi kurikulum perkuliahan dan belum terpetakannya minat karir secara komprehensif.",
            approachTechnique = "Konseling Realitas & Wawancara Eksploratif Karir (Trait and Factor).",
            solutionOptions = "1. Mengerjakan inventori minat bakat. 2. Menyusun tabel perbandingan kurikulum prodi. 3. Melakukan diskusi terarah dengan orang tua.",
            result = "Siswa telah memahami potensi diri dan bersedia mengikuti tes minat bakat sekolah.",
            followUpPlan = "Pertemuan lanjutan pada 25 November untuk membahas hasil tes dan simulasi pemilihan prodi SNBP.",
            status = "Proses"
        ),
        CounselingNote(
            id = "CN-002",
            studentName = "Dewi Lestari",
            nisn = "0057891234",
            className = "XI IPS 2",
            academicYear = "2026/2027",
            semester = "Ganjil",
            date = "2026-11-18 10:30",
            mode = "Online",
            serviceDomain = "Belajar",
            serviceType = "Konseling Individual",
            complaint = "Kesulitan manajemen waktu antara kegiatan ekstrakurikuler OSIS dan persiapan ujian.",
            symptoms = "Tugas sekolah terlambat dikumpulkan, sering merasa kelelahan.",
            backgroundCause = "Beban kerja organisasi yang menumpuk menjelang acara tahunan sekolah.",
            approachTechnique = "Behavioral Contract & Manajemen Waktu Matriks Eisenhower.",
            solutionOptions = "Menyusun skala prioritas harian dan mendelegasikan tugas kepanitiaan OSIS.",
            result = "Siswa berhasil membuat jadwal belajar mingguan yang terstruktur.",
            followUpPlan = "Monitoring berkala progres tugas sekolah melalui wali kelas.",
            status = "Selesai"
        )
    )

    fun addCounselingNote(note: CounselingNote) {
        counselingNotes.add(0, note)
    }

    fun updateCounselingNote(note: CounselingNote) {
        val index = counselingNotes.indexOfFirst { it.id == note.id }
        if (index != -1) {
            counselingNotes[index] = note
        }
    }

    fun deleteCounselingNote(id: String) {
        counselingNotes.removeAll { it.id == id }
    }

    // --- Students List & Expanded Profile Data (4 Groups) ---
    val students = mutableStateListOf(
        StudentProfile(
            id = "STU-1",
            nisn = "0054321987",
            name = "Ahmad Fauzi",
            className = "XII IPA 1",
            academicYear = "2026/2027",
            gender = "Laki-laki",
            intakeType = "Reguler / Siswa Baru",
            birthPlaceDate = "Bandung, 12 Maret 2008",
            address = "Jl. Sukajadi No. 24, RT 03/04, Bandung",
            parentName = "Ir. Agus Fauzi",
            parentPhone = "0812-8877-6655",
            psychologicalTest = PsychologicalTestResult(
                personalityType = "Inisiator Analitis (The Strategic Thinker)",
                mainInterests = "Teknologi Komputer, Desain Perangkat Lunak, Robotika",
                dominantAptitudes = "Penalaran Logis Matematis (92%), Spasial 3D (85%)",
                testDate = "15 Oktober 2026",
                counselorNotes = "Siswa menunjukkan potensi tinggi dalam bidang sains rekayasa. Disarankan diarahkan ke Prodi Teknik Informatika atau Elektro."
            ),
            academicRecords = listOf(
                AcademicSemesterRecord("AC-1", "2025/2026", "Semester 1 (Ganjil)", 89.2, 2, 36),
                AcademicSemesterRecord("AC-2", "2025/2026", "Semester 2 (Genap)", 91.0, 1, 36),
                AcademicSemesterRecord("AC-3", "2026/2027", "Semester 1 (Ganjil)", 90.5, 2, 36)
            ),
            nonAcademicAchievements = listOf(
                NonAcademicAchievement("NA-1", "Juara 1 Olimpiade Robotika Pelajar", "Lomba Akademik / Sains", "Tingkat Provinsi", "2025"),
                NonAcademicAchievement("NA-2", "Ketua Divisi IT & Publikasi OSIS", "Organisasi", "Tingkat Sekolah", "2026")
            )
        ),
        StudentProfile(
            id = "STU-2",
            nisn = "0057891234",
            name = "Dewi Lestari",
            className = "XI IPS 2",
            academicYear = "2026/2027",
            gender = "Perempuan",
            intakeType = "Reguler / Siswa Baru",
            birthPlaceDate = "Semarang, 25 Agustus 2009",
            address = "Jl. Diponegoro No. 10, Semarang",
            parentName = "Dra. Endang Lestari",
            parentPhone = "0813-2233-4455",
            psychologicalTest = PsychologicalTestResult(
                personalityType = "Empatik Komunikator (The Diplomat)",
                mainInterests = "Komunikasi Publik, Manajemen Hubungan Sosial, Jurnalistik",
                dominantAptitudes = "Verbal Linguistik (90%), Kepemimpinan Sosial (88%)",
                testDate = "10 September 2026",
                counselorNotes = "Kemampuan komunikasi verbal dan organisasi sangat menonjol. Cocok untuk Ilmu Komunikasi atau Hubungan Internasional."
            ),
            academicRecords = listOf(
                AcademicSemesterRecord("AC-4", "2025/2026", "Semester 1 (Ganjil)", 86.5, 4, 34),
                AcademicSemesterRecord("AC-5", "2025/2026", "Semester 2 (Genap)", 88.0, 3, 34)
            ),
            nonAcademicAchievements = listOf(
                NonAcademicAchievement("NA-3", "Juara 2 Debat Bahasa Indonesia Pelajar", "Lomba Akademik / Sains", "Tingkat Kota / Kabupaten", "2025"),
                NonAcademicAchievement("NA-4", "Sekretaris Umum MPK Sekolah", "Organisasi", "Tingkat Sekolah", "2026")
            )
        ),
        StudentProfile(
            id = "STU-3",
            nisn = "0061234567",
            name = "Rian Hidayat",
            className = "X-A",
            academicYear = "2026/2027",
            gender = "Laki-laki",
            intakeType = "Siswa Baru (Tahun Ini)",
            birthPlaceDate = "Surabaya, 18 Juli 2010",
            address = "Jl. Pemuda No. 8, Surabaya",
            parentName = "Bambang Hidayat",
            parentPhone = "0819-7788-9900",
            psychologicalTest = PsychologicalTestResult(
                personalityType = "Kinestetik Eksploratif (The Athlete)",
                mainInterests = "Olahraga Prestasi, Manajemen Olahraga, Kepanduan",
                dominantAptitudes = "Kinestetik Jasmani (95%), Spasial Taktis (82%)",
                testDate = "22 Agustus 2026",
                counselorNotes = "Perlu pendampingan untuk menjaga motivasi belajar akademik seimbang dengan latihan fisik."
            ),
            academicRecords = listOf(
                AcademicSemesterRecord("AC-6", "2026/2027", "Semester 1 (Ganjil)", 82.4, 12, 35)
            ),
            nonAcademicAchievements = listOf(
                NonAcademicAchievement("NA-5", "Medali Emas Kejuaraan Bola Basket Antar Sekolah", "Olahraga", "Tingkat Kota / Kabupaten", "2026")
            )
        ),
        StudentProfile(
            id = "STU-4",
            nisn = "0049876543",
            name = "Siti Aisyah",
            className = "XII IPA 2",
            academicYear = "2025/2026",
            gender = "Perempuan",
            intakeType = "Intake Tahun Lalu",
            birthPlaceDate = "Yogyakarta, 04 Januari 2008",
            address = "Jl. Kaliurang Km 5, Sleman",
            parentName = "Suryanto",
            parentPhone = "0856-1122-3344",
            psychologicalTest = PsychologicalTestResult(
                personalityType = "Peneliti Cermat (The Investigator)",
                mainInterests = "Sains Hayati, Kedokteran, Bioteknologi",
                dominantAptitudes = "Analisis Biologis (94%), Konsistensi Studi (90%)",
                testDate = "05 November 2025",
                counselorNotes = "Fokus belajar sangat tinggi dan stabil. Ditargetkan masuk Fakultas Kedokteran melalui jalur prestasi."
            ),
            academicRecords = listOf(
                AcademicSemesterRecord("AC-7", "2024/2025", "Semester 1 (Ganjil)", 92.5, 1, 36),
                AcademicSemesterRecord("AC-8", "2024/2025", "Semester 2 (Genap)", 93.8, 1, 36)
            ),
            nonAcademicAchievements = listOf(
                NonAcademicAchievement("NA-6", "Finalis Karya Tulis Ilmiah Remaja Nasional", "Lomba Akademik / Sains", "Tingkat Nasional", "2025")
            )
        )
    )

    fun addStudent(
        nisn: String,
        name: String,
        className: String,
        academicYear: String,
        gender: String,
        intakeType: String,
        birthPlaceDate: String = "Jakarta, 01 Januari 2008",
        address: String = "Alamat Siswa",
        parentName: String = "Orang Tua Siswa",
        parentPhone: String = "0812-0000-0000"
    ) {
        students.add(0, StudentProfile(
            id = "STU-${System.currentTimeMillis() % 1000}",
            nisn = nisn,
            name = name,
            className = className,
            academicYear = academicYear,
            gender = gender,
            intakeType = intakeType,
            birthPlaceDate = birthPlaceDate,
            address = address,
            parentName = parentName,
            parentPhone = parentPhone
        ))
    }

    fun updateStudent(
        id: String,
        nisn: String,
        name: String,
        className: String,
        academicYear: String,
        gender: String,
        intakeType: String,
        birthPlaceDate: String,
        address: String,
        parentName: String,
        parentPhone: String
    ) {
        val index = students.indexOfFirst { it.id == id }
        if (index != -1) {
            students[index] = students[index].copy(
                nisn = nisn,
                name = name,
                className = className,
                academicYear = academicYear,
                gender = gender,
                intakeType = intakeType,
                birthPlaceDate = birthPlaceDate,
                address = address,
                parentName = parentName,
                parentPhone = parentPhone
            )
        }
    }

    fun deleteStudent(id: String) {
        students.removeAll { it.id == id }
    }

    fun updateStudentPsychologicalTest(studentId: String, test: PsychologicalTestResult) {
        val index = students.indexOfFirst { it.id == studentId }
        if (index != -1) {
            students[index] = students[index].copy(psychologicalTest = test)
        }
    }

    fun addAcademicRecord(studentId: String, record: AcademicSemesterRecord) {
        val index = students.indexOfFirst { it.id == studentId }
        if (index != -1) {
            val updated = students[index].academicRecords.toMutableList().apply { add(0, record) }
            students[index] = students[index].copy(academicRecords = updated)
        }
    }

    fun deleteAcademicRecord(studentId: String, recordId: String) {
        val index = students.indexOfFirst { it.id == studentId }
        if (index != -1) {
            val updated = students[index].academicRecords.filter { it.id != recordId }
            students[index] = students[index].copy(academicRecords = updated)
        }
    }

    fun addNonAcademicAchievement(studentId: String, achievement: NonAcademicAchievement) {
        val index = students.indexOfFirst { it.id == studentId }
        if (index != -1) {
            val updated = students[index].nonAcademicAchievements.toMutableList().apply { add(0, achievement) }
            students[index] = students[index].copy(nonAcademicAchievements = updated)
        }
    }

    fun deleteNonAcademicAchievement(studentId: String, achievementId: String) {
        val index = students.indexOfFirst { it.id == studentId }
        if (index != -1) {
            val updated = students[index].nonAcademicAchievements.filter { it.id != achievementId }
            students[index] = students[index].copy(nonAcademicAchievements = updated)
        }
    }

    // --- Gelar Kasus (Case Conferences) ---
    val caseConferences = mutableStateListOf(
        CaseConference(
            id = "CC-001",
            title = "Gelar Kasus Tawuran Antar Pelajar — Pertemuan Orang Tua & Wali Murid",
            date = "22 Nov 2026",
            time = "10:00 - 12:30 WIB",
            location = "Ruang Rapat Utama BK",
            chairPerson = "Dra. Siti Rahayu, M.Pd",
            status = "Selesai",
            discussionSummary = "Pembahasan komprehensif insiden gesekan antar kelompok siswa di luar area sekolah seusai jam kepulangan. Klarifikasi pemicu kesalahpahaman serta pemeriksaan rekaman CCTV sekitar sekolah.",
            jointAgreement = "1. Pihak orang tua sepakat meningkatkan pengawasan kepulangan siswa.\n2. Siswa diberikan bimbingan konseling perilaku intensif selama 1 bulan tanpa skorsing akademik.\n3. Ganti rugi fasilitas umum diselesaikan secara mufakat oleh kedua pihak orang tua.",
            followUpRecommendation = "Monitoring harian kehadiran oleh wali kelas dan sesi konseling kelompok mingguan bersama guru BK.",
            attendees = listOf(
                CaseConferenceAttendee("ATT-1", "Dra. Siti Rahayu, M.Pd", "Guru BK", "Hadir"),
                CaseConferenceAttendee("ATT-2", "Bambang Wijaya, S.Pd", "Wali Kelas XII", "Hadir"),
                CaseConferenceAttendee("ATT-3", "H. Hendra Fauzi", "Orang Tua / Wali", "Hadir"),
                CaseConferenceAttendee("ATT-4", "Ahmad Fauzi", "Siswa Terkait", "Hadir"),
                CaseConferenceAttendee("ATT-5", "Drs. H. Mulyadi", "Kepala Sekolah", "Izin")
            ),
            documentations = listOf(
                CaseConferenceDoc("DOC-1", "Foto Pembukaan Rapat Mediasi", "22 Nov 2026, 10:15", "Pemaparan kronologi insiden oleh Pimpinan Rapat kepada orang tua."),
                CaseConferenceDoc("DOC-2", "Penandatanganan Berita Acara", "22 Nov 2026, 12:00", "Penandatanganan surat kesepakatan damai oleh wali murid dan siswa.")
            )
        ),
        CaseConference(
            id = "CC-002",
            title = "Konferensi Kasus Penanganan Penurunan Prestasi & Indikasi Depresi Siswa",
            date = "25 Nov 2026",
            time = "09:00 - 11:00 WIB",
            location = "Ruang Konseling Khusus BK",
            chairPerson = "Bambang Wijaya, S.Pd, Kons",
            status = "Dalam Proses",
            discussionSummary = "Evaluasi penurunan drastis kehadiran dan motivasi belajar siswa kelas XI akibat masalah keluarga.",
            jointAgreement = "Rencana penjadwalan home visit bersama psikolog mitra dan penyusunan modul belajar mandiri.",
            followUpRecommendation = "Dukungan emosional intensif dan pemeriksaan kesehatan berkala.",
            attendees = listOf(
                CaseConferenceAttendee("ATT-6", "Bambang Wijaya, S.Pd, Kons", "Guru BK", "Hadir"),
                CaseConferenceAttendee("ATT-7", "Nurul Hidayati, M.Psi", "Psikolog Sekolah", "Hadir"),
                CaseConferenceAttendee("ATT-8", "Wali Kelas XI IPS 2", "Wali Kelas", "Hadir"),
                CaseConferenceAttendee("ATT-9", "Orang Tua Siswa", "Orang Tua / Wali", "Hadir")
            ),
            documentations = listOf(
                CaseConferenceDoc("DOC-3", "Sesi Pembahasan Awal Rekam Kasus", "25 Nov 2026, 09:30", "Telaah rekam jejak akademik dan catatan mood harian.")
            )
        )
    )

    fun addCaseConference(caseConf: CaseConference) {
        caseConferences.add(0, caseConf)
    }

    fun updateCaseConference(caseConf: CaseConference) {
        val index = caseConferences.indexOfFirst { it.id == caseConf.id }
        if (index != -1) {
            caseConferences[index] = caseConf
        }
    }

    fun deleteCaseConference(id: String) {
        caseConferences.removeAll { it.id == id }
    }

    fun updateAttendeeStatus(caseId: String, attendeeId: String, newStatus: String) {
        val caseIdx = caseConferences.indexOfFirst { it.id == caseId }
        if (caseIdx != -1) {
            val updatedAttendees = caseConferences[caseIdx].attendees.map { att ->
                if (att.id == attendeeId) att.copy(status = newStatus) else att
            }
            caseConferences[caseIdx] = caseConferences[caseIdx].copy(attendees = updatedAttendees)
        }
    }

    fun addAttendee(caseId: String, attendee: CaseConferenceAttendee) {
        val caseIdx = caseConferences.indexOfFirst { it.id == caseId }
        if (caseIdx != -1) {
            val updatedAttendees = caseConferences[caseIdx].attendees.toMutableList().apply { add(attendee) }
            caseConferences[caseIdx] = caseConferences[caseIdx].copy(attendees = updatedAttendees)
        }
    }

    fun addDocumentation(caseId: String, doc: CaseConferenceDoc) {
        val caseIdx = caseConferences.indexOfFirst { it.id == caseId }
        if (caseIdx != -1) {
            val updatedDocs = caseConferences[caseIdx].documentations.toMutableList().apply { add(doc) }
            caseConferences[caseIdx] = caseConferences[caseIdx].copy(documentations = updatedDocs)
        }
    }

    // --- Dynamic Statistics Calculations ---
    fun getTotalStudentsCount(): Int = students.size

    fun getTotalCasesCount(): Int = counselingNotes.size + caseConferences.size + sosReports.size

    fun getCompletedCasesCount(): Int {
        val notesDone = counselingNotes.count { it.status == "Selesai" }
        val confDone = caseConferences.count { it.status == "Selesai" }
        val sosDone = sosReports.count { it.status == "Selesai" || it.progressPercent >= 100 }
        return notesDone + confDone + sosDone
    }

    fun getTotalReportsCount(): Int = sosReports.size + caseConferences.size

    val monthlyCaseStats = listOf(
        MonthlyCaseStat("Jul", completed = 6, inProgress = 2),
        MonthlyCaseStat("Ags", completed = 8, inProgress = 3),
        MonthlyCaseStat("Sep", completed = 11, inProgress = 4),
        MonthlyCaseStat("Okt", completed = 14, inProgress = 5),
        MonthlyCaseStat("Nov", completed = 16, inProgress = 4)
    )
}
