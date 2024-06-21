package presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

@Composable
fun Dp.toSp() = with(LocalDensity.current) { toSp() }

@Composable
fun TextUnit.toDp() = with(LocalDensity.current) { toDp() }

@Composable
fun Int.toDp() = with(LocalDensity.current) { toDp() }

fun Int.toDp(density: Density) = with(density) { toDp() }

@Composable
fun Dp.toPx() = with(LocalDensity.current) { toPx() }