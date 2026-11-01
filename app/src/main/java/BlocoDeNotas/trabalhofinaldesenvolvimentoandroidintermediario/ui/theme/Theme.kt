
package BlocoDeNotas.trabalhofinaldesenvolvimentoandroidintermediario.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.isSystemInDarkTheme

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFA15C),
    onPrimary = Color(0xFF351507),

    secondary = Color(0xFFFFC078),
    onSecondary = Color(0xFF3B1B0D),

    tertiary = Color(0xFFB85C2E),
    onTertiary = Color(0xFFFFF0E5),

    background = Color(0xFF783016),
    onBackground = Color(0xFFFFF1E7),

    surface = Color(0xFF512716),
    onSurface = Color(0xFFFFF1E7),

    surfaceVariant = Color(0xFF32190F),
    onSurfaceVariant = Color(0xFFE9BFA7),

    primaryContainer = Color(0xFF713313),
    onPrimaryContainer = Color(0xFFFFE0C7),

    secondaryContainer = Color(0xFF653018),
    onSecondaryContainer = Color(0xFFFFE6D2),

    error = Color(0xFFFF8A80),
    onError = Color(0xFF3B0805)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFD94A00),
    onPrimary = Color.White,

    secondary = Color(0xFFFFB15C),
    onSecondary = Color(0xFF46200D),

    tertiary = Color(0xFFFFD18A),
    onTertiary = Color(0xFF46200D),

    background = Color(0xFFFF6B35),
    onBackground = Color(0xFF3B1D12),

    surface = Color(0xFFFFE6CE),
    onSurface = Color(0xFF3B1D12),

    surfaceVariant = Color(0xFFFFD18A),
    onSurfaceVariant = Color(0xFF75432B),

    primaryContainer = Color(0xFFFFD4B8),
    onPrimaryContainer = Color(0xFF54200A),

    secondaryContainer = Color(0xFFFFE0B2),
    onSecondaryContainer = Color(0xFF65300F),

    error = Color(0xFFBA1A1A),
    onError = Color.White
)

@Composable
fun TrabalhoFinalDesenvolvimentoAndroidIntermediarioTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun FundoTarefasCheck(): Brush {
    val cores = MaterialTheme.colorScheme

    return Brush.verticalGradient(
        colors = listOf(
            cores.background,
            cores.surfaceVariant
        )
    )
}
