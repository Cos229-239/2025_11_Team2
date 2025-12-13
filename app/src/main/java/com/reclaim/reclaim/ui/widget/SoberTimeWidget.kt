package com.reclaim.reclaim.ui.widget


import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.ImageProvider
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.wrapContentHeight
import androidx.glance.text.Text
import com.reclaim.reclaim.R
import com.reclaim.reclaim.ui.viewmodels.HomeViewModel
import com.reclaim.reclaim.ui.viewmodels.dataStore
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.temporal.ChronoUnit


class SoberTimeWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val prefs = context.applicationContext.dataStore.data.first()
        val soberStartEpoch = prefs[HomeViewModel.PreferencesKeys.SOBER_START_DATE]
        val soberStart = soberStartEpoch?.let { LocalDate.ofEpochDay(it) } ?: LocalDate.now()

        provideContent {
            GlanceTheme {
                SoberTimeContent(soberStart)
            }
        }
    }

    @Composable
    private fun SoberTimeContent(soberStart: LocalDate) {
        val today = LocalDate.now()
        val totalDays = ChronoUnit.DAYS.between(soberStart, today)

        val years = totalDays / 365
        val months = (totalDays % 365) / 30
        val days = (totalDays % 365) % 30

        Box(
            modifier = GlanceModifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(8.dp)
                .background(ImageProvider(R.drawable.rounded_card))        ){
            Column(
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "🎉 Sober Time")
                Text(
                    text = "$years Years, $months Months, $days Days",
                    modifier = GlanceModifier.padding(top = 8.dp)
                )
                Text(
                    text = "$totalDays total days sober",
                    modifier = GlanceModifier.padding(top = 4.dp)
                )
            }
        }
    }
}
