package com.aza.apkkonseling.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.aza.apkkonseling.data.UserRole
import com.aza.apkkonseling.ui.admin.*
import com.aza.apkkonseling.ui.auth.LoginScreen
import com.aza.apkkonseling.ui.auth.RegisterStudentScreen
import com.aza.apkkonseling.ui.components.BkBottomNavBar
import com.aza.apkkonseling.ui.components.BottomNavItem
import com.aza.apkkonseling.ui.student.*

@Composable
fun BkAppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "login"

    val isStudentRoute = currentRoute.startsWith("student_")
    val isAdminRoute = currentRoute.startsWith("admin_")

    val studentNavItems = listOf(
        BottomNavItem("student_dashboard", "Beranda", Icons.Default.Home),
        BottomNavItem("student_chat_teachers", "Chat BK", Icons.AutoMirrored.Filled.Chat),
        BottomNavItem("student_appointment", "Janji", Icons.Default.CalendarMonth),
        BottomNavItem("student_mood_diary", "Mood", Icons.Default.Mood),
        BottomNavItem("student_articles", "Pustaka", Icons.AutoMirrored.Filled.MenuBook)
    )

    val adminNavItems = listOf(
        BottomNavItem("admin_dashboard", "Beranda", Icons.Default.Dashboard),
        BottomNavItem("admin_counseling_notes", "Rekam BK", Icons.Default.Description),
        BottomNavItem("admin_chat_list", "Chat", Icons.Default.ChatBubble),
        BottomNavItem("admin_schedule_appointment", "Jadwal", Icons.Default.EventAvailable),
        BottomNavItem("admin_students", "Siswa", Icons.Default.Groups)
    )

    val activeStudentRoute = when {
        currentRoute.startsWith("student_chat_") -> "student_chat_teachers"
        currentRoute == "student_appointment" -> "student_appointment"
        currentRoute == "student_mood_diary" -> "student_mood_diary"
        currentRoute == "student_articles" -> "student_articles"
        else -> "student_dashboard"
    }

    val activeAdminRoute = when {
        currentRoute == "admin_counseling_notes" || currentRoute == "admin_case_conferences" -> "admin_counseling_notes"
        currentRoute.startsWith("admin_chat_") -> "admin_chat_list"
        currentRoute == "admin_schedule_appointment" -> "admin_schedule_appointment"
        currentRoute == "admin_students" -> "admin_students"
        else -> "admin_dashboard"
    }

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            if (isStudentRoute) {
                BkBottomNavBar(
                    items = studentNavItems,
                    currentRoute = activeStudentRoute,
                    onItemSelected = { targetRoute ->
                        navController.navigate(targetRoute) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            } else if (isAdminRoute) {
                BkBottomNavBar(
                    items = adminNavItems,
                    currentRoute = activeAdminRoute,
                    onItemSelected = { targetRoute ->
                        navController.navigate(targetRoute) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            // ==========================================
            // AUTHENTICATION ROUTES
            // ==========================================
            composable("login") {
                LoginScreen(
                    onNavigateToRegister = {
                        navController.navigate("register_student")
                    },
                    onLoginSuccess = { role ->
                        if (role == UserRole.STUDENT) {
                            navController.navigate("student_dashboard") {
                                popUpTo("login") { inclusive = true }
                            }
                        } else {
                            navController.navigate("admin_dashboard") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    }
                )
            }

            composable("register_student") {
                RegisterStudentScreen(
                    onNavigateBackToLogin = {
                        navController.popBackStack()
                    },
                    onRegisterSuccess = {
                        navController.navigate("login") {
                            popUpTo("register_student") { inclusive = true }
                        }
                    }
                )
            }

            // ==========================================
            // STUDENT SIDE ROUTES
            // ==========================================
            composable("student_dashboard") {
                StudentDashboardScreen(
                    onNavigateToMenu = { route ->
                        navController.navigate(route)
                    },
                    onLogout = {
                        navController.navigate("login") {
                            popUpTo("student_dashboard") { inclusive = true }
                        }
                    }
                )
            }

            composable("student_chat_teachers") {
                StudentTeacherSelectScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onTeacherSelected = { teacherId, isAnonymous ->
                        navController.navigate("student_chat_room/$teacherId/$isAnonymous")
                    }
                )
            }

            composable(
                route = "student_chat_room/{teacherId}/{isAnonymous}",
                arguments = listOf(
                    navArgument("teacherId") { type = NavType.StringType },
                    navArgument("isAnonymous") { type = NavType.BoolType }
                )
            ) { backStackEntry ->
                val teacherId = backStackEntry.arguments?.getString("teacherId") ?: "T-1"
                val isAnonymous = backStackEntry.arguments?.getBoolean("isAnonymous") ?: false
                StudentChatRoomScreen(
                    teacherId = teacherId,
                    isAnonymous = isAnonymous,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable("student_appointment") {
                StudentAppointmentScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable("student_articles") {
                StudentArticleScreens(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable("student_mood_diary") {
                StudentMoodDiaryScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable("student_quiz") {
                StudentQuizScreens(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            // ==========================================
            // ADMIN / BK TEACHER SIDE ROUTES
            // ==========================================
            composable("admin_dashboard") {
                AdminDashboardScreen(
                    onNavigateToMenu = { route ->
                        navController.navigate(route)
                    },
                    onLogout = {
                        navController.navigate("login") {
                            popUpTo("admin_dashboard") { inclusive = true }
                        }
                    }
                )
            }

            composable("admin_counseling_notes") {
                AdminCounselingNotesScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable("admin_chat_list") {
                AdminChatListScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onChatSelected = { roomId ->
                        navController.navigate("admin_chat_room/$roomId")
                    }
                )
            }

            composable(
                route = "admin_chat_room/{roomId}",
                arguments = listOf(
                    navArgument("roomId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val roomId = backStackEntry.arguments?.getString("roomId") ?: "STU-1_T-1_real"
                AdminChatRoomScreen(
                    roomId = roomId,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable("admin_schedule_appointment") {
                AdminScheduleAppointmentScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable("admin_students") {
                AdminStudentManagementScreens(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable("admin_articles") {
                AdminArticleManagementScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable("admin_mood_diary_monitor") {
                AdminMoodDiaryMonitorScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable("admin_case_conferences") {
                AdminCaseConferenceScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(
                route = "admin_sos_detail/{sosId}",
                arguments = listOf(
                    navArgument("sosId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val sosId = backStackEntry.arguments?.getString("sosId") ?: ""
                AdminSosDetailScreen(
                    sosId = sosId,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
