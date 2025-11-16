package com.example.vinilostsdc_frontend

import android.content.ContentValues
import android.graphics.Bitmap
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.atomic.AtomicBoolean

@RunWith(AndroidJUnit4::class)
class ArtistDetailScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    companion object {
        private val screenshotsTaken = AtomicBoolean(false)
    }

    /**
     * Navegación hasta la lista de artistas.
     * Toma pantallazos solo en la primera ejecución.
     */
    private fun navigateToArtistList() {
        val shouldCapture = screenshotsTaken.compareAndSet(false, true)

        // Pantalla inicial: "Vinilos"
        composeRule.onNodeWithText("Vinilos").assertIsDisplayed()
        if (shouldCapture) takeScreenshot("01_role_selection_screen")

        // Seleccionar Visitante
        composeRule.onNodeWithText("Visitante").performClick()
        composeRule.waitForIdle()
        if (shouldCapture) takeScreenshot("02_visitante_clicked")

        // Esperar menú Visitante
        composeRule.waitUntil(5000) {
            composeRule.onAllNodesWithText("Listado de artistas")
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText("Listado de artistas").assertIsDisplayed()
        if (shouldCapture) takeScreenshot("03_visitante_menu_screen")

        // Ingresar a Listado de artistas
        composeRule.onNodeWithText("Listado de artistas").performClick()
        composeRule.waitForIdle()

        // Esperar pantalla "Artistas"
        composeRule.waitUntil(10_000) {
            composeRule.onAllNodesWithText("Artistas").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText("Artistas").assertIsDisplayed()
        if (shouldCapture) takeScreenshot("04_artist_list_screen")
    }

    /**
     * Guardar screenshot en almacenamiento interno y en galería
     */
    private fun takeScreenshot(name: String) {
        try {
            val bitmap: Bitmap = composeRule.onRoot().captureToImage().asAndroidBitmap()

            val screenshotsDir =
                composeRule.activity.getExternalFilesDir("ScreenshotsVinilosArtists")
            screenshotsDir?.mkdirs()
            val privateFile = File(screenshotsDir, "$name.png")

            FileOutputStream(privateFile).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            Log.d("ArtistDetailScreenTest", "Screenshot app: ${privateFile.absolutePath}")

            val resolver = composeRule.activity.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "$name.png")
                put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/ScreenshotsVinilosArtists")
                    put(MediaStore.Images.Media.IS_PENDING, 1)
                }
            }

            val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            uri?.let {
                resolver.openOutputStream(it)?.use { out ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    contentValues.clear()
                    contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                    resolver.update(uri, contentValues, null, null)
                }
            }
        } catch (e: Exception) {
            Log.e("ArtistDetailScreenTest", "Error screenshot $name", e)
        }
    }

    // ---------------------------------------------------------
    // TEST #1 — Validar que el nombre del artista aparece en el detalle
    // ---------------------------------------------------------
    @Test
    fun test1_alSeleccionarArtista_seMuestraElNombreEnDetalle() {
        val artistName = "Rubén Blades Bellido de Luna"

        navigateToArtistList()

        // Esperar artista en lista
        composeRule.waitUntil(10_000) {
            composeRule.onAllNodesWithText(artistName)
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText(artistName).performClick()
        composeRule.waitForIdle()

        // Verificar título del detalle
        composeRule.waitUntil(5_000) {
            composeRule.onAllNodesWithText(artistName)
                .fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText(artistName).assertIsDisplayed()

        takeScreenshot("detalle_artista_nombre")
    }

    // ---------------------------------------------------------
    // TEST #2 — Validar que aparece la imagen del artista
    // ---------------------------------------------------------
    @Test
    fun test2_alSeleccionarArtista_seMuestraLaImagen() {
        val artistName = "Rubén Blades Bellido de Luna"

        navigateToArtistList()

        composeRule.waitUntil(10_000) {
            composeRule.onAllNodesWithText(artistName).fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText(artistName).performClick()
        composeRule.waitForIdle()

        // Esperar imagen (contentDescription="Foto del artista")
        composeRule.waitUntil(5_000) {
            composeRule.onAllNodes(hasContentDescription("Foto del artista"))
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithContentDescription("Foto del artista").assertIsDisplayed()

        takeScreenshot("detalle_artista_imagen")
    }

    // ---------------------------------------------------------
    // TEST #3 — Validar los álbumes del artista
    // ---------------------------------------------------------
    @Test
    fun test3_alSeleccionarArtista_seMuestranSusAlbumes() {
        val artistName = "Rubén Blades Bellido de Luna"

        navigateToArtistList()

        composeRule.waitUntil(10_000) {
            composeRule.onAllNodesWithText(artistName).fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText(artistName).performClick()
        composeRule.waitForIdle()

        // Validar que aparece el texto "<n> Álbumes"
        composeRule.waitUntil(5_000) {
            composeRule.onAllNodesWithText("Álbumes", substring = true)
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithText("Álbumes", substring = true).assertIsDisplayed()

        // Validar al menos un álbum si existe
        // Ajusta el nombre según tus datos reales:
        composeRule.onAllNodes(hasText("(", substring = true))
            .onFirst()
            .assertExists()

        takeScreenshot("detalle_artista_albumes")
    }

    // ---------------------------------------------------------
    // TEST #4 — Validar botón atrás
    // ---------------------------------------------------------
    @Test
    fun test4_atras_desdeDetalle_regresaAListaDeArtistas() {
        val artistName = "Rubén Blades Bellido de Luna"

        navigateToArtistList()

        composeRule.waitUntil(10_000) {
            composeRule.onAllNodesWithText(artistName).fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText(artistName).performClick()
        composeRule.waitForIdle()

        // Pantallazo antes de regresar
        takeScreenshot("detalle_artista_before_back")

        composeRule.onNodeWithContentDescription("Atrás")
            .assertExists("No se encontró el botón Atrás en la pantalla de detalle")
        composeRule.onNodeWithContentDescription("Atrás").performClick()
        composeRule.waitForIdle()

        // Validar que volvemos a "Artistas"
        composeRule.waitUntil(5_000) {
            composeRule.onAllNodesWithText("Artistas").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText("Artistas").assertIsDisplayed()

        takeScreenshot("detalle_artista_after_back")
    }
}
