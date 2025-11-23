package com.reclaim.reclaim.ui.trigger

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.YearMonth
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.Scaffold
import com.reclaim.reclaim.ui.components.BottomNavBar
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush

val SoftBeige = Color(0xFFF8EFE7)
val Violet = Color(0xFF7C3AED)
val Turquoise = Color(0xFF00B4A0)
val Orange = Color(0xFFF59E0B)
val Red = Color(0xFFEF4444)
val Blue = Color(0xFF3B82F6)
val Green = Color(0xFF22C55E)

enum class TriggerType {
    STRESS,
    LONELINESS,
    DEPRESSION,
    BOREDOM,
    SOCIAL_EVENTS,
    HEALTH,
    LIFE_CHANGES
}

private fun TriggerType.prettyName(): String =
    when (this) {
        TriggerType.STRESS -> "Stress"
        TriggerType.LONELINESS -> "Loneliness"
        TriggerType.DEPRESSION -> "Depression"
        TriggerType.BOREDOM -> "Boredom"
        TriggerType.SOCIAL_EVENTS -> "Social events"
        TriggerType.HEALTH -> "Health"
        TriggerType.LIFE_CHANGES -> "Life changes"
    }

private fun triggerColor(trigger: TriggerType): Color =
    when (trigger) {
        TriggerType.STRESS -> Red
        TriggerType.LONELINESS -> Blue
        TriggerType.DEPRESSION -> Violet
        TriggerType.BOREDOM -> Orange
        TriggerType.SOCIAL_EVENTS -> Turquoise
        TriggerType.HEALTH -> Green
        TriggerType.LIFE_CHANGES -> Color(0xFF6366F1)
    }

private fun LocalDate.toPrettyString(): String {
    val monthName = month.name.lowercase().replaceFirstChar { it.uppercase() }
    return "$dayOfMonth $monthName $year"
}

private fun sampleTriggerData(): Map<LocalDate, List<TriggerType>> {
    val today = LocalDate.now()
    return mapOf(
        today.minusDays(1) to listOf(TriggerType.STRESS, TriggerType.BOREDOM),
        today to listOf(TriggerType.STRESS, TriggerType.LONELINESS),
        today.plusDays(2) to listOf(TriggerType.SOCIAL_EVENTS, TriggerType.LIFE_CHANGES)
    )
}

@Composable
fun TriggerMapScreen(
    navController: NavHostController,
    dailyTriggers: Map<LocalDate, List<TriggerType>> = sampleTriggerData(),
    onMeditationClick: () -> Unit = {},
    onJournalingClick: () -> Unit = {},
    onLogTriggerClick: (LocalDate) -> Unit = {}
) {
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    val triggersForSelectedDate = dailyTriggers[selectedDate].orEmpty()

    Scaffold(
        containerColor = Turquoise,
        bottomBar = {
            BottomNavBar(navController = navController)
        }
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
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Track triggers by day",
                style = MaterialTheme.typography.bodyMedium,
                color = Violet
            )

            Spacer(modifier = Modifier.height(16.dp))

            QuickActionsRow(
                onMeditationClick = onMeditationClick,
                onJournalingClick = onJournalingClick
            )

            Spacer(modifier = Modifier.height(24.dp))

            TriggerCalendar(
                currentMonth = currentMonth,
                selectedDate = selectedDate,
                dailyTriggers = dailyTriggers,
                onMonthChange = { currentMonth = it },
                onDateSelected = { selectedDate = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            DaySummarySection(
                date = selectedDate,
                triggers = triggersForSelectedDate
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onLogTriggerClick(selectedDate) },
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
                Text(text = "Log trigger for this date")
            }

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
            modifier = Modifier
                .weight(1f)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(20.dp),
                    clip = false
                ),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SoftBeige,
                contentColor = Violet
            )
        ) {
            Text("Meditation")
        }

        Button(
            onClick = onJournalingClick,
            modifier = Modifier
                .weight(1f)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(20.dp),
                    clip = false
                ),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SoftBeige,
                contentColor = Violet
            )
        ) {
            Text("Journaling")
        }
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
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(24.dp),
                clip = false
            )
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White.copy(alpha = 0.75f))
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = { onMonthChange(currentMonth.minusMonths(1)) }) {
                Text("<")
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${currentMonth.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${currentMonth.year}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Violet
            )
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = { onMonthChange(currentMonth.plusMonths(1)) }) {
                Text(">")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val daysOfWeek = listOf("S", "M", "T", "W", "T", "F", "S")
            daysOfWeek.forEach { label ->
                Text(
                    text = label,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        val firstOfMonth = currentMonth.atDay(1)
        val daysInMonth = currentMonth.lengthOfMonth()
        val firstDayOfWeekIndex = (firstOfMonth.dayOfWeek.value % 7)

        val totalCells = firstDayOfWeekIndex + daysInMonth
        val rows = (totalCells / 7) + if (totalCells % 7 != 0) 1 else 0

        Column {
            var dayNumber = 1
            repeat(rows) { rowIndex ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    for (column in 0 until 7) {
                        val cellIndex = rowIndex * 7 + column
                        if (cellIndex < firstDayOfWeekIndex || dayNumber > daysInMonth) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                            )
                        } else {
                            val date = currentMonth.atDay(dayNumber)
                            val isSelected = date == selectedDate
                            val triggers = dailyTriggers[date].orEmpty()

                            DayCell(
                                date = date,
                                isSelected = isSelected,
                                triggers = triggers,
                                onClick = { onDateSelected(date) }
                            )
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
    onClick: () -> Unit,
    modifier: Modifier = Modifier
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
            Spacer(modifier = Modifier.height(2.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                triggers
                    .take(3) // max 3 dots
                    .forEach { trigger ->
                        Box(
                            modifier = Modifier
                                .size(5.dp)
                                .clip(CircleShape)
                                .background(triggerColor(trigger))
                        )
                    }
            }
        }
    }
}

@Composable
private fun DaySummarySection(
    date: LocalDate,
    triggers: List<TriggerType>
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = date.toPrettyString(),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        if (triggers.isEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "No triggers logged for this day.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        } else {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${triggers.size} triggers logged",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            triggers
                .groupingBy { it }
                .eachCount()
                .forEach { (type, count) ->
                    Text("• ${type.prettyName()} ($count)")
                }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TriggerMapScreenPreview() {
    val navController = rememberNavController()

    MaterialTheme {
        TriggerMapScreen(navController = navController)
    }
}
