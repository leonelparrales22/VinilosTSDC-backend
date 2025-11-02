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
class AlbumDetailScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    companion object {
        private val screenshotsTaken = AtomicBoolean(false)
    }

    /**
     * Navegación hasta la lista de álbumes.
     * Toma pantallazos solo en la primera ejecución.
     */
    private fun navigateToAlbumList() {
        val shouldCapture = screenshotsTaken.compareAndSet(false, true)

        // Pantalla inicial - título "Vinilos"
        composeRule.onNodeWithText("Vinilos").assertIsDisplayed()
        if (shouldCapture) takeScreenshot("01_role_selection_screen")

        // Seleccionar rol Visitante
        composeRule.onNodeWithText("Visitante").performClick()
        composeRule.waitForIdle()
        if (shouldCapture) takeScreenshot("02_role_visitante_clicked")

        // Esperar y verificar menú
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithText("Catálogo de álbumes").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText("Catálogo de álbumes").assertIsDisplayed()
        if (shouldCapture) takeScreenshot("03_visitante_menu_screen")

        // Entrar a Catálogo de álbumes
        composeRule.onNodeWithText("Catálogo de álbumes").performClick()
        composeRule.waitForIdle()

        // Esperar a que los álbumes estén visibles
        composeRule.waitUntil(timeoutMillis = 10_000) {
            composeRule.onAllNodesWithText("Álbumes").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText("Álbumes").assertIsDisplayed()
        if (shouldCapture) takeScreenshot("04_album_list_screen")
    }

    /**
     * Toma un pantallazo de la pantalla actual y lo guarda
     * tanto en el directorio privado de la app como en la galería.
     */
    private fun takeScreenshot(name: String) {
        try {
            val bitmap: Bitmap = composeRule.onRoot().captureToImage().asAndroidBitmap()

            // Guardar en directorio privado de la app
            val screenshotsDir = composeRule.activity.getExternalFilesDir("ScreenshotsVinilos")
            screenshotsDir?.mkdirs()
            val privateFile = File(screenshotsDir, "$name.png")
            FileOutputStream(privateFile).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            Log.d("AlbumDetailScreenTest", "Screenshot guardado en app: ${privateFile.absolutePath}")

            // Guardar en galería del dispositivo (MediaStore)
            val resolver = composeRule.activity.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "$name.png")
                put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/ScreenshotsVinilos")
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
                Log.d("AlbumDetailScreenTest", "Screenshot guardado en galería: $uri")
            }
        } catch (e: Exception) {
            Log.e("AlbumDetailScreenTest", "Error al guardar screenshot: $name", e)
        }
    }

    /**
     * Test #1 - Validar que al seleccionar un álbum
     * se muestra su nombre en la pantalla de detalle.
     */
    @Test
    fun test1_alSeleccionarAlbum_seMuestraElNombreEnDetalle() {
        val albumName = "Buscando América"

        navigateToAlbumList()

        // Esperar y seleccionar el álbum
        composeRule.waitUntil(timeoutMillis = 10_000) {
            composeRule.onAllNodesWithText(albumName).fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText(albumName).performClick()
        composeRule.waitForIdle()

        // Verificar que en la pantalla detalle el título se muestra
        composeRule.waitUntil(timeoutMillis = 5_000) {
            composeRule.onAllNodesWithText(albumName).fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText(albumName).assertIsDisplayed()
        takeScreenshot("detalle_album_nombre")
    }

    /**
     * Test #2 - Validar que al seleccionar un álbum
     * se muestra la imagen de portada.
     */
    @Test
    fun test2_alSeleccionarAlbum_seMuestraImagenDePortada() {
        val albumName = "Buscando América"

        navigateToAlbumList()

        // Esperar que el álbum aparezca y hacer clic
        composeRule.waitUntil(timeoutMillis = 10_000) {
            composeRule.onAllNodesWithText(albumName).fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText(albumName).performClick()
        composeRule.waitForIdle()

        // Buscar la imagen de portada usando contentDescription = "Portada"
        composeRule.waitUntil(timeoutMillis = 5_000) {
            composeRule.onAllNodes(hasContentDescription("Portada")).fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithContentDescription("Portada").assertIsDisplayed()
        takeScreenshot("detalle_album_imagen")
    }

    /**
     * Test #3 - Validar que se muestra la cantidad y los nombres de los tracks.
     */
    @Test
    fun test3_alSeleccionarAlbum_seMuestraCantidadDeTracks() {
        val albumName = "Buscando América"

        navigateToAlbumList()

        composeRule.waitUntil(timeoutMillis = 10_000) {
            composeRule.onAllNodesWithText(albumName).fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText(albumName).performClick()
        composeRule.waitForIdle()

        // Verificar el texto de cantidad de tracks
        composeRule.waitUntil(timeoutMillis = 5_000) {
            composeRule.onAllNodesWithText("2 tracks").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText("2 tracks").assertIsDisplayed()

        // Verificar tracks individuales (substrings)
        composeRule.onNodeWithText("Decisiones", substring = true).assertIsDisplayed()
        composeRule.onNodeWithText("Desapariciones", substring = true).assertIsDisplayed()

        takeScreenshot("detalle_album_tracks")
    }

    /**
     * Test #4 - Validar que al presionar el botón "Atrás" se regresa a la lista de álbumes.
     */
    @Test
    fun test4_atras_desdeDetalle_regresaALaListaDeAlbumes_porNavBackTag() {
        val albumName = "Buscando América"

        navigateToAlbumList()

        composeRule.waitUntil(timeoutMillis = 10_000) {
            composeRule.onAllNodesWithText(albumName).fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText(albumName).performClick()
        composeRule.waitForIdle()

        composeRule.waitUntil(timeoutMillis = 5_000) {
            composeRule.onAllNodesWithText(albumName).fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText(albumName).assertIsDisplayed()
        takeScreenshot("detalle_album_before_back")

        composeRule.onNodeWithTag("nav_back", useUnmergedTree = true)
            .assertExists("No se encontró el botón nav_back en la pantalla de detalle")
        composeRule.onNodeWithTag("nav_back", useUnmergedTree = true)
            .performClick()
        composeRule.waitForIdle()

        // Verificar que la navegación se haya realizado hacía 'Álbumes'
        composeRule.waitUntil(timeoutMillis = 5_000) {
            composeRule.onAllNodesWithText("Álbumes").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText("Álbumes").assertIsDisplayed()
        takeScreenshot("detalle_album_after_back")
    }
}
