package com.example.vinilostsdc_frontend

import androidx.compose.ui.test.assertCountEquals
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.compose.ui.test.printToLog
import androidx.test.runner.screenshot.Screenshot
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@RunWith(AndroidJUnit4::class)
class AlbumCreateScreenTest {

    val timeout: Long = 10_000

    object FieldTags {
        val coverUrlField = "coverUrlField"
        val albumNameField = "albumNameField"
        val releaseDateField = "releaseDateField"
        val genreField = "genreField"
        val recordLabelField = "recordLabelField"
        val descriptionField = "descriptionField"
        val trackNameField = "trackNameField"
        val trackMinField = "trackMinField"
        val trackSecField = "trackSecField"
    }

    object ButtonTags {
        val datePickerButton = "datePickerButton"
        val addTrackButton = "addTrackButton"
        val removeTrackButton = "removeTrackButton"
        val saveButton = "saveButton"
    }

    object FixtureData {
        val album1: Map<String, String> = mapOf(
            "coverUrl" to "https://upload.wikimedia.org/wikipedia/commons/thumb/4/45/Metallica_%28Black_Album%29_by_Metallica_%28Album-CD%29_%28EU-1991%29.jpg/330px-Metallica_%28Black_Album%29_by_Metallica_%28Album-CD%29_%28EU-1991%29.jpg",
            "name" to "Metallica",
            "releaseDate" to "1991-08-11",
            "genre" to "Rock",
            "recordLabel" to "Sony Music",
            "description" to "Metallica (commonly known as The Black Album) is the fifth studio album by American heavy metal band Metallica."
        )

        val track11: Map<String, String> = mapOf(
            "title" to "Enter Sandman",
            "min" to "5",
            "sec" to "29"
        )

        val album2: Map<String, String> = mapOf(
            "coverUrl" to "https://upload.wikimedia.org/wikipedia/en/b/b4/Metallica_-_Load_cover.jpg",
            "name" to "Load",
            "releaseDate" to "1996-06-04",
            "genre" to "Rock",
            "recordLabel" to "Sony Music",
            "description" to "Load is the sixth studio album by American heavy metal band Metallica."
        )

        val track21: Map<String, String> = mapOf(
            "title" to "2 X 4",
            "min" to "5",
            "sec" to "28"
        )

        val track22: Map<String, String> = mapOf(
            "title" to "Hero of the Day",
            "min" to "4",
            "sec" to "22"
        )
    }

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    /**
     * Ir a la pantalla de Crear Album.
     */
    fun navigateToCreateAlbumScreen() {
        val appTitle = "Vinilos"
        val collectorMenuItem = "Coleccionista"
        val createAlbumMenuItem = "Crear álbum"
        val createAlbumTitle = "Información del Álbum"

        // Pantalla principal de selección de roles Vinilos
        // Ir a la pantalla de Coleccionista
        composeRule.onNodeWithText(appTitle).assertIsDisplayed()

        val captureSetup1 = Screenshot.capture(composeRule.activity)
        captureSetup1.name = "hu07_setup1_vinilos"
        captureSetup1.process()

        composeRule.onNodeWithText(collectorMenuItem).performClick()
        composeRule.waitUntil(timeoutMillis = timeout) {
            composeRule.onAllNodesWithText(createAlbumMenuItem).fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText(collectorMenuItem).assertIsDisplayed()

        // Pantalla de Coleccionista
        // Ir a la pantalla de Crear Album
        composeRule.onNodeWithText(createAlbumMenuItem).assertIsDisplayed()

        val captureSetup2 = Screenshot.capture(composeRule.activity)
        captureSetup2.name = "hu07_setup2_coleccionista"
        captureSetup2.process()

        composeRule.onNodeWithText(createAlbumMenuItem).performClick()
        composeRule.waitUntil(timeoutMillis = timeout) {
            composeRule.onAllNodesWithText(createAlbumTitle).fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText(createAlbumMenuItem).assertIsDisplayed()

        val captureSetup3 = Screenshot.capture(composeRule.activity)
        captureSetup3.name = "hu07_setup3_createAlbum"
        captureSetup3.process()
    }

    /**
     * Test Case 1: Creación de álbum con 1 track.
     */
    @Test
    fun hu07_testCase1_createNewAlbumWithOneTrack() {
        val trackItemLabelTag = "trackItem"
        val collectorMenuItem = "Coleccionista"
        val createAlbumMenuItem = "Crear álbum"

        this.navigateToCreateAlbumScreen()

        composeRule.onNodeWithTag(FieldTags.coverUrlField).performTextInput(FixtureData.album1["coverUrl"] ?: "")
        composeRule.onNodeWithTag(FieldTags.albumNameField).performTextInput(FixtureData.album1["name"] ?: "")
        composeRule.onNodeWithTag(FieldTags.releaseDateField).performTextReplacement(FixtureData.album1["releaseDate"] ?: "")
        composeRule.onNodeWithTag(FieldTags.genreField).performTextInput(FixtureData.album1["genre"] ?: "")
        composeRule.onNodeWithTag(FieldTags.recordLabelField).performTextInput(FixtureData.album1["recordLabel"] ?: "")
        composeRule.onNodeWithTag(FieldTags.descriptionField).performTextInput(FixtureData.album1["description"] ?: "")

        composeRule.onNodeWithTag(FieldTags.trackNameField).performTextInput(FixtureData.track11["title"] ?: "")
        composeRule.onNodeWithTag(FieldTags.trackMinField).performTextInput(FixtureData.track11["min"] ?: "")
        composeRule.onNodeWithTag(FieldTags.trackSecField).performTextInput(FixtureData.track11["sec"] ?: "")

        composeRule.onNodeWithTag(ButtonTags.addTrackButton).performClick()
        composeRule.waitUntil(timeoutMillis = timeout) {
            composeRule.onAllNodesWithText(trackItemLabelTag).fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithText(FixtureData.track11["title"] ?: "").assertIsDisplayed()

        val capture1 = Screenshot.capture(composeRule.activity)
        capture1.name = "hu07_testCase1_createNewAlbumWithOneTrack"
        capture1.process()

        composeRule.onNodeWithTag(ButtonTags.saveButton).performClick()
        composeRule.waitUntil(timeoutMillis = timeout) {
            composeRule.onAllNodesWithText(collectorMenuItem).fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithText(createAlbumMenuItem).assertIsDisplayed()
    }

    /**
     * Test Case 2: Creación de álbum con 2 tracks.
     */
    @Test
    fun hu07_testCase2_createNewAlbumWithTwoTracks() {
        val trackItemLabelTag = "trackItem"
        val collectorMenuItem = "Coleccionista"
        val createAlbumMenuItem = "Crear álbum"

        this.navigateToCreateAlbumScreen()

        composeRule.onNodeWithTag(FieldTags.coverUrlField).performTextInput(FixtureData.album2["coverUrl"] ?: "")
        composeRule.onNodeWithTag(FieldTags.albumNameField).performTextInput(FixtureData.album2["name"] ?: "")
        composeRule.onNodeWithTag(FieldTags.releaseDateField).performTextReplacement(FixtureData.album2["releaseDate"] ?: "")
        composeRule.onNodeWithTag(FieldTags.genreField).performTextInput(FixtureData.album2["genre"] ?: "")
        composeRule.onNodeWithTag(FieldTags.recordLabelField).performTextInput(FixtureData.album2["recordLabel"] ?: "")
        composeRule.onNodeWithTag(FieldTags.descriptionField).performTextInput(FixtureData.album2["description"] ?: "")

        composeRule.onNodeWithTag(FieldTags.trackNameField).performTextInput(FixtureData.track21["title"] ?: "")
        composeRule.onNodeWithTag(FieldTags.trackMinField).performTextInput(FixtureData.track21["min"] ?: "")
        composeRule.onNodeWithTag(FieldTags.trackSecField).performTextInput(FixtureData.track21["sec"] ?: "")

        composeRule.onNodeWithTag(ButtonTags.addTrackButton).performClick()
        composeRule.waitUntil(timeoutMillis = timeout) {
            composeRule.onAllNodesWithText(trackItemLabelTag).fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithText(FixtureData.track21["title"] ?: "").assertIsDisplayed()

        composeRule.onNodeWithTag(FieldTags.trackNameField).performTextInput(FixtureData.track22["title"] ?: "")
        composeRule.onNodeWithTag(FieldTags.trackMinField).performTextInput(FixtureData.track22["min"] ?: "")
        composeRule.onNodeWithTag(FieldTags.trackSecField).performTextInput(FixtureData.track22["sec"] ?: "")

        composeRule.onNodeWithTag(ButtonTags.addTrackButton).performClick()
        composeRule.waitUntil(timeoutMillis = timeout) {
            composeRule.onAllNodesWithText(trackItemLabelTag).fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithText(FixtureData.track22["title"] ?: "").assertIsDisplayed()

        val capture1 = Screenshot.capture(composeRule.activity)
        capture1.name = "hu07_testCase2_createNewAlbumWithTwoTracks"
        capture1.process()

        composeRule.onNodeWithTag(ButtonTags.saveButton).performClick()
        composeRule.waitUntil(timeoutMillis = timeout) {
            composeRule.onAllNodesWithText(collectorMenuItem).fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithText(createAlbumMenuItem).assertIsDisplayed()
    }
}