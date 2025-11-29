package com.reclaim.reclaim.ui.theme

import androidx.compose.ui.graphics.Color

// ------------------------------------------------------------
// Primary brand palette (teal + purple + orange accents)
// ------------------------------------------------------------

// Teal family (primary brand color)
val TealPrimary = Color(0xFF00B4A0)   // main teal used for primary actions
val TealLight   = Color(0xFF4FD3C4)   // lighter teal for hover/secondary states
val TealDark    = Color(0xFF008373)   // darker teal for emphasis or dark mode

// Purple accents (secondary brand color)
val PurpleAccent = Color(0xFFA14E89)  // accent purple for highlights
val PurpleDark   = Color(0xFF6C2F5C)  // deeper purple for dark mode or contrast

// Orange accents (tertiary brand color)
val OrangeAccent = Color(0xFFFFA94D)  // bright orange for warnings or highlights
val OrangeDark   = Color(0xFFCC7C1F)  // darker orange for contrast

// ------------------------------------------------------------
// Neutral backgrounds and surfaces
// ------------------------------------------------------------

val BeigeBackground = Color(0xFFF7EDE2) // soft beige background for light mode
val SurfaceLight    = Color(0xFFFFFFFF) // pure white surface
val SurfaceDark     = Color(0xFF1F1B24) // dark surface for dark mode

// Text colors
val TextPrimaryDark  = Color(0xFF1C1B1F) // dark text on light backgrounds
val TextPrimaryLight = Color(0xFFFFFFFF) // light text on dark backgrounds

// ------------------------------------------------------------
// Trigger-specific palette (used in TriggerMapScreen)
// ------------------------------------------------------------

val SoftBeige  = Color(0xFFF8EFE7) // neutral beige for buttons/backgrounds
val Violet     = Color(0xFF7C3AED) // violet for depression trigger + headings
val Turquoise  = Color(0xFF00B4A0) // turquoise for social events trigger
val Orange     = Color(0xFFF59E0B) // orange for boredom trigger
val Red        = Color(0xFFEF4444) // red for stress trigger
val Blue       = Color(0xFF3B82F6) // blue for loneliness trigger
val Green      = Color(0xFF22C55E) // green for health trigger