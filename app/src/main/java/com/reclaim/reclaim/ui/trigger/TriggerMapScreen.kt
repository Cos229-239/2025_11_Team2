package com.reclaim.reclaim.ui.trigger

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.reclaim.reclaim.ui.components.BottomNavBar
import com.reclaim.reclaim.ui.theme.SoftBeige
import com.reclaim.reclaim.ui.theme.Turquoise
import com.reclaim.reclaim.ui.theme.Violet
import java.time.LocalDate
import java.time.YearMonth


@Composable
fun TriggerMapScreen(
    navController: NavHostController,
    viewModel: TriggerMapViewModel = hiltViewModel()
) {
    val dailyTriggers by viewModel.triggers.collectAsState(initial = emptyMap())
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    val triggersForSelectedDate = dailyTriggers[selectedDate].orEmpty()

    Scaffold(
        bottomBar = { BottomNavBar(navController = navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
                Text(
                    text = "Trigger Map",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            Spacer(Modifier.height(4.dp))
            Text(
                text = "Track triggers by day",
                style = MaterialTheme.typography.bodyMedium,
                color = Violet
            )

            Spacer(Modifier.height(16.dp))

            QuickActionsRow(
                onMeditationClick = { /* TODO */ },
                onJournalingClick = { /* TODO */ }
            )

            Spacer(Modifier.height(24.dp))

            TriggerCalendar(
                currentMonth = currentMonth,
                selectedDate = selectedDate,
                dailyTriggers = dailyTriggers,
                onMonthChange = { currentMonth = it },
                onDateSelected = { selectedDate = it }
            )

            Spacer(Modifier.height(16.dp))

            DaySummarySection(date = selectedDate, triggers = triggersForSelectedDate)

            Spacer(Modifier.height(16.dp))

            LogTriggerButton(
                selectedDate = selectedDate,
                onLogTrigger = { type -> viewModel.logTrigger(selectedDate, type) }
            )
        }
    }
}

@Composable
private fun QuickActionsRow(
    onMeditationClick: () -> Unit,
    onJournalingClick: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = onMeditationClick,
            modifier = Modifier.weight(1f).shadow(8.dp, RoundedCornerShape(20.dp), clip = false),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SoftBeige, contentColor = Violet)
        ) { Text("Meditation") }

        Button(
            onClick = onJournalingClick,
            modifier = Modifier.weight(1f).shadow(8.dp, RoundedCornerShape(20.dp), clip = false),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SoftBeige, contentColor = Violet)
        ) { Text("Journaling") }
    }
}

@Composable
private fun TriggerCalendar(
    currentMonth: YearMonth,
    selectedDate: LocalDate,
    dailyTriggers: Map<LocalDate, List<TriggerType>>,
    onMonthChange: (YearMonth) -> Unit,
    onDateSelected: (LocalDate) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(12.dp, RoundedCornerShape(24.dp), clip = false)
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White.copy(alpha = 0.75f))
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = { onMonthChange(currentMonth.minusMonths(1)) }) { Text("<") }
            Spacer(Modifier.weight(1f))
            Text(
                text = "${currentMonth.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${currentMonth.year}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Violet
            )
            Spacer(Modifier.weight(1f))
            TextButton(onClick = { onMonthChange(currentMonth.plusMonths(1)) }) { Text(">") }
        }

        Spacer(Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            listOf("S", "M", "T", "W", "T", "F", "S").forEach { label ->
                Text(
                    text = label,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        val firstOfMonth = currentMonth.atDay(1)
        val daysInMonth = currentMonth.lengthOfMonth()
        val firstDayOfWeekIndex = (firstOfMonth.dayOfWeek.value % 7)
        val totalCells = firstDayOfWeekIndex + daysInMonth
        val rows = (totalCells / 7) + if (totalCells % 7 != 0) 1 else 0

        Column {
            var dayNumber = 1
            repeat(rows) { rowIndex ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    for (column in 0 until 7) {
                        val cellIndex = rowIndex * 7 + column
                        if (cellIndex < firstDayOfWeekIndex || dayNumber > daysInMonth) {
                            Box(modifier = Modifier.weight(1f).aspectRatio(1f))
                        } else {
                            val date = currentMonth.atDay(dayNumber)
                            val isSelected = date == selectedDate
                            val triggers = dailyTriggers[date].orEmpty()
                            DayCell(date, isSelected, triggers, onClick = { onDateSelected(date) })
                            dayNumber++
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DayCell(
    date: LocalDate,
    isSelected: Boolean,
    triggers: List<TriggerType>,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) Turquoise else Color.Transparent
    val textColor = if (isSelected) Color.White else Color.Black

    Column(
        modifier = Modifier
            .padding(2.dp)
            .clip(RoundedCornerShape(999.dp))
            .clickable { onClick() }
            .background(backgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            color = textColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        if (triggers.isNotEmpty()) {
            Spacer(Modifier.height(2.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(2.dp), verticalAlignment = Alignment.CenterVertically) {
                triggers.take(3).forEach { trigger ->
                    Box(
                        modifier = Modifier.size(5.dp).clip(CircleShape).background(triggerColor(trigger))
                    )
                }
            }
        }
    }
}

@Composable
private fun DaySummarySection(date: LocalDate, triggers: List<TriggerType>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = date.toPrettyString(),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        if (triggers.isEmpty()) {
            Spacer(Modifier.height(4.dp))
            Text("No triggers logged for this day.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        } else {
            Spacer(Modifier.height(4.dp))
            Text("${triggers.size} triggers logged", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(4.dp))
            triggers.groupingBy { it }.eachCount().forEach { (type, count) ->
                Text("• ${type.prettyName()} ($count)")
            }
        }
    }
}
@Composable
private fun LogTriggerButton(
    selectedDate: LocalDate,
    onLogTrigger: (TriggerType) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Select a trigger") },
            text = {
                Column {
                    TriggerType.values().forEach { type ->
                        Text(
                            text = type.prettyName(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onLogTrigger(type)
                                    showDialog = false
                                }
                                .padding(8.dp)
                        )
                    }
                }
            },
            confirmButton = {}
        )
    }

    Button(
        onClick = { showDialog = true },
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(24.dp),
                clip = false
            )
            .height(52.dp),
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Violet,
            contentColor = Color.White
        )
    ) {
        Text("Log trigger for this date")
    }
}