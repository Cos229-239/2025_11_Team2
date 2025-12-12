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
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
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
import androidx.navigation.NavHostController
import com.reclaim.reclaim.data.model.TriggerType
import com.reclaim.reclaim.ui.components.HamburgerMenu
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TriggerMapScreen(
    navController: NavHostController,
    viewModel: TriggerMapViewModel = hiltViewModel()
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val dailyTriggers by viewModel.triggers.collectAsState(initial = emptyMap<LocalDate, List<TriggerType>>())
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedTriggerForAffirmation by remember { mutableStateOf<String?>(null) }
    val triggersForSelectedDate = dailyTriggers[selectedDate].orEmpty()


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            HamburgerMenu(
                navController = navController,
                currentRoute = "triggerMap",
                onClose = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Trigger Map",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.onSurface,
                        navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            ) {

                Text(
                    text = "Track triggers by day",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(Modifier.height(16.dp))

                QuickActionsRow(
                    onMeditationClick = { /* TODO hook up */ },
                    onJournalingClick = { /* TODO hook up */ }
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

                DaySummarySection(
                    date = selectedDate,
                    triggers = triggersForSelectedDate,
                    onTriggerClick = { triggerName ->
                        selectedTriggerForAffirmation = triggerName}
                )

                Spacer(Modifier.height(16.dp))

                LogTriggerButton(
                    selectedDate = selectedDate,
                    onLogTrigger = { type ->
                        viewModel.logTrigger(selectedDate, type)
                    }
                )
            }
        }
    }
    if (selectedTriggerForAffirmation != null) {
        AffirmationDialog(
            triggerName = selectedTriggerForAffirmation!!,
            onDismiss = { selectedTriggerForAffirmation = null }
        )
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
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            )
        ) {
            Text(text = "Meditation")
        }

        Button(
            onClick = onJournalingClick,
            modifier = Modifier
                .weight(1f)
                .shadow(elevation = 8.dp, shape = RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            )
        ) {
            Text(text = "Journaling")
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
            .shadow(12.dp, RoundedCornerShape(24.dp), clip = false)
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = { onMonthChange(currentMonth.minusMonths(1)) }) { Text("<") }
            Spacer(Modifier.weight(1f))
            Text(
                text = "${
                    currentMonth.month.name.lowercase().replaceFirstChar { it.uppercase() }
                } ${currentMonth.year}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.weight(1f))
            TextButton(onClick = { onMonthChange(currentMonth.plusMonths(1)) }) { Text(">") }
        }

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf("S", "M", "T", "W", "T", "F", "S").forEach { label ->
                Text(
                    text = label,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        val firstOfMonth = currentMonth.atDay(1)
        val daysInMonth = currentMonth.lengthOfMonth()
        val firstDayOfWeekIndex = (firstOfMonth.dayOfWeek.value % 7)

        val baseCells: List<LocalDate?> =
            List(firstDayOfWeekIndex) { null } +
                    (1..daysInMonth).map { day ->
                        currentMonth.atDay(day)
                    }
        val remainder = baseCells.size % 7
        val dayCells =
            if (remainder == 0) baseCells
            else baseCells + List(7 - remainder) { null }

        val weeks = dayCells.chunked(7)
        weeks.forEach { week ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                week.forEach { date ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        if (date != null) {
                            val isSelected = date == selectedDate
                            val triggersForDate = dailyTriggers[date].orEmpty()

                            DayCell(
                                date = date,
                                isSelected = isSelected,
                                triggers = triggersForDate,
                                onClick = { onDateSelected(date) }
                            )
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
    val backgroundColor =
        if (isSelected) MaterialTheme.colorScheme.secondary else Color.Transparent
    val textColor =
        if (isSelected) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurface

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
            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                triggers.take(3).forEach { trigger ->
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
    triggers: List<TriggerType>,
    onTriggerClick: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = date.toPrettyString(),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        if (triggers.isEmpty()) {
            Spacer(Modifier.height(4.dp))
            Text(
                text = "No triggers logged for this day.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        } else {
            Spacer(Modifier.height(4.dp))
            Text(
                text = "${triggers.size} triggers logged",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(4.dp))

            triggers
                .groupingBy { it }
                .eachCount()
                .forEach { (type, count) ->
                    val triggerName = type.prettyName()

                    Text(
                        text = "• $triggerName ($count)",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onTriggerClick(triggerName) }
                            .padding(vertical = 2.dp)
                    )
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
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary
        )
    ) {
        Text("Log trigger for this date")
    }
}

@Composable
fun AffirmationDialog(
    triggerName: String,
    onDismiss: () -> Unit
) {
    val affirmation = remember(triggerName) {
        getAffirmationForTrigger(triggerName)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Got it")
            }
        },
        title = {
            Text(text = triggerName)
        },
        text = {
            Text(text = affirmation)
        }
    )
}

fun getAffirmationForTrigger(triggerName: String): String {
    return when (triggerName.lowercase()) {
        "social events" -> "I am worthy of being seen and heard just as I am. I can take up as much or as little space as feels safe today."
        "depression" -> "This heaviness is real, but it is not forever. I can move through today one gentle step at a time, and that is enough."
        "stress" -> "I do not have to carry everything at once. It’s okay to pause, breathe, and choose one thing to focus on."
        "loneliness" -> "Even when I feel alone, I am still valuable and deserving of care. I can offer myself the warmth I wish others would give me."
        "boredom" -> "Slow moments are allowed. I can use this time to rest, dream, or try something small that nourishes me."
        else -> "I’m doing the best I can with what I have today. That is more than enough."
    }
}