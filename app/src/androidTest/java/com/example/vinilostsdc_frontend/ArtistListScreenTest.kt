package com.example.vinilostsdc_frontend

import androidx.compose.ui.test.assertCountEquals
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.runner.screenshot.Screenshot
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ArtistListScreenTest {

    val timeout: Long = 10_000

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    /**
     * Ir a la pantalla de Listado de Artistas.
     */
    fun navigateToArtistListScreen() {
        val appTitle = "Vinilos"
        val visitorMenuItem = "Visitante"
        val artistsMenuItem = "Listado de artistas"
        val artistsScreenTitle = "Artistas"
        val artistsListTag = "artistListItem"

        // Pantalla principal de selección de roles Vinilos
        // Ir a la pantalla de Visitante
        composeRule.onNodeWithText(appTitle).assertIsDisplayed()

        val captureSetup1 = Screenshot.capture(composeRule.activity)
        captureSetup1.name = "hu03_setup1_vinilos"
        captureSetup1.process()

        composeRule.onNodeWithText(visitorMenuItem).performClick()
        composeRule.waitUntil(timeoutMillis = timeout) {
            composeRule.onAllNodesWithText(artistsMenuItem)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
        composeRule.onNodeWithText(visitorMenuItem).assertIsDisplayed()

        // Pantalla de Visitante
        // Ir a la pantalla de Listado de Artistas
        composeRule.onNodeWithText(artistsMenuItem).assertIsDisplayed()

        val captureSetup2 = Screenshot.capture(composeRule.activity)
        captureSetup2.name = "hu03_setup2_visitante"
        captureSetup2.process()

        composeRule.onNodeWithText(artistsMenuItem).performClick()
        composeRule.waitUntil(timeoutMillis = timeout) {
            composeRule.onAllNodesWithText(artistsScreenTitle).fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText(artistsScreenTitle).assertIsDisplayed()
        composeRule.waitUntil(timeoutMillis = timeout) {
            composeRule.onAllNodesWithTag(artistsListTag).fetchSemanticsNodes().isNotEmpty()
        }
    }

    /**
     * Test Case 1: Listado - Al abrir la pantalla hay un listado con 1 artista.
     */
    @Test
    fun hu03_testCase1_openArtistItems() {
        val artistsListTag = "artistListItem"
        val expectedArtistItemsCount = 1

        this.navigateToArtistListScreen()

        val capture3 = Screenshot.capture(composeRule.activity)
        capture3.name = "hu03_testCase1_openArtistItems"
        capture3.process()

        composeRule.onAllNodesWithTag(artistsListTag).assertCountEquals(expectedArtistItemsCount)
    }

    /**
     * Test Case 2: Filtrar items - Buscar el artista de nombre "Sol"
     */
    @Test
    fun hu03_testCase2_filterArtistItems1() {
        val artistSearchString = "Sol"
        val expectedArtistItemsCount = 0

        this.navigateToArtistListScreen()

        val searchBox = composeRule.onNodeWithTag("searchBox")
        searchBox.performTextClearance()
        searchBox.performTextInput(artistSearchString)
        composeRule.waitForIdle()

        val capture4 = Screenshot.capture(composeRule.activity)
        capture4.name = "hu03_testCase2_filterArtistItems1"
        capture4.process()

        composeRule.onAllNodesWithTag("artistListItem").assertCountEquals(expectedArtistItemsCount)
    }

    /**
     * Test Case 3: Filtrar items - Buscar artista de nombre "Blades"
     */
    @Test
    fun hu03_testCase3_filterArtistItems2() {
        val artistSearchString = "Blades"
        val artistName = "Rubén Blades Bellido de Luna"
        val expectedArtistItemsCount = 1

        this.navigateToArtistListScreen()

        val searchBox = composeRule.onNodeWithTag("searchBox")
        searchBox.performTextClearance()
        searchBox.performTextInput(artistSearchString)
        composeRule.waitForIdle()

        val capture5 = Screenshot.capture(composeRule.activity)
        capture5.name = "hu03_testCase3_filterArtistItems2"
        capture5.process()

        composeRule.onAllNodesWithTag("artistListItem").assertCountEquals(expectedArtistItemsCount)
        composeRule.onNodeWithText(artistName).assertIsDisplayed()
    }

    /**
     * Test Case 4: Detalle - Abrir pantalla de detalle del artista al seleccionarlo
     */
    @Test
    fun hu03_testCase4_openArtistDetail() {
        val artistName = "Rubén Blades Bellido de Luna"
        val expectedAlbumsCount = 2

        this.navigateToArtistListScreen()

        composeRule.onNodeWithText(artistName).performClick()
        composeRule.waitForIdle()

        val capture6 = Screenshot.capture(composeRule.activity)
        capture6.name = "hu03_testCase4_openArtistDetail"
        capture6.process()

        composeRule.onNodeWithText(artistName).assertIsDisplayed()
        composeRule.onNodeWithText("$expectedAlbumsCount Álbumes").assertIsDisplayed()
    }
}