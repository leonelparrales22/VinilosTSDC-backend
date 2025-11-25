package com.example.vinilostsdc_frontend

import androidx.compose.ui.test.assertCountEquals
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.runner.screenshot.Screenshot
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CollectorDetailScreenTest {

    val timeout: Long = 10_000

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    /**
     * Ir a la pantalla de Listado de Coleccionistas.
     */
    fun navigateToCollectorListScreen() {
        val appTitle = "Vinilos"
        val visitorMenuItem = "Visitante"
        val collectorsMenuItem = "Listado de coleccionistas"
        val collectorsScreenTitle = "Coleccionistas"
        val collectorsListTag = "collectorListItem"

        // Pantalla principal de selección de roles Vinilos
        // Ir a la pantalla de Visitante
        composeRule.onNodeWithText(appTitle).assertIsDisplayed()

        val captureSetup1 = Screenshot.capture(composeRule.activity)
        captureSetup1.name = "hu05_setup1_vinilos"
        captureSetup1.process()

        composeRule.onNodeWithText(visitorMenuItem).performClick()
        composeRule.waitUntil(timeoutMillis = timeout) {
            composeRule.onAllNodesWithText(collectorsMenuItem)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
        composeRule.onNodeWithText(visitorMenuItem).assertIsDisplayed()

        // Pantalla de Visitante
        // Ir a la pantalla de Listado de Coleccionistas
        composeRule.onNodeWithText(collectorsMenuItem).assertIsDisplayed()

        val captureSetup2 = Screenshot.capture(composeRule.activity)
        captureSetup2.name = "hu05_setup2_visitante"
        captureSetup2.process()

        composeRule.onNodeWithText(collectorsMenuItem).performClick()
        composeRule.waitUntil(timeoutMillis = timeout) {
            composeRule.onAllNodesWithText(collectorsScreenTitle).fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText(collectorsScreenTitle).assertIsDisplayed()
        composeRule.waitUntil(timeoutMillis = timeout) {
            composeRule.onAllNodesWithTag(collectorsListTag).fetchSemanticsNodes().isNotEmpty()
        }
    }

    /**
     * Test Case 1: CollectorDetailScreenTest
     */
    @Test
    fun hu06_testCase1_openCollectorList() {
        val collectorsListTag = "collectorListItem"
        val expectedCollectorItemsCount = 2

        this.navigateToCollectorListScreen()

        val capture3 = Screenshot.capture(composeRule.activity)
        capture3.name = "hu06_testCase1_openCollectorList"
        capture3.process()

        composeRule.onAllNodesWithTag(collectorsListTag).assertCountEquals(expectedCollectorItemsCount)
    }

    /**
     * Test Case 2: Detalle - Seleccionar coleccionista "Manolo Bellon".
     */
    @Test
    fun hu06_testCase2_selectCollector1() {
        val collectorName = "Manolo Bellon"
        val collectorPhone = "3502457896"
        val collectorEmail = "manollo@caracol.com.co"
        val albumsListTag = "albumListItem"
        val expectedAlbumItemsCount = 1

        this.navigateToCollectorListScreen()

        composeRule.onNodeWithText(collectorName).performClick()
        composeRule.waitUntil(timeoutMillis = timeout) {
            composeRule.onAllNodesWithText(collectorName).fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText(collectorPhone).assertIsDisplayed()
        composeRule.onNodeWithText(collectorEmail).assertIsDisplayed()

        val capture4 = Screenshot.capture(composeRule.activity)
        capture4.name = "hu06_testCase2_selectCollector1"
        capture4.process()

        composeRule.onAllNodesWithTag(albumsListTag).assertCountEquals(expectedAlbumItemsCount)
    }

    /**
     * Test Case 3: Detalle - Seleccionar coleccionista "Jaime Monsalve".
     */
    @Test
    fun hu06_testCase3_selectCollector2() {
        val collectorName = "Jaime Monsalve"
        val collectorPhone = "3012357936"
        val collectorEmail = "jmonsalve@rtvc.com.co"
        val albumsListTag = "albumListItem"
        val expectedAlbumItemsCount = 1

        this.navigateToCollectorListScreen()

        composeRule.onNodeWithText(collectorName).performClick()
        composeRule.waitUntil(timeoutMillis = timeout) {
            composeRule.onAllNodesWithText(collectorName).fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText(collectorPhone).assertIsDisplayed()
        composeRule.onNodeWithText(collectorEmail).assertIsDisplayed()

        val capture4 = Screenshot.capture(composeRule.activity)
        capture4.name = "hu06_testCase3_selectCollector2"
        capture4.process()

        composeRule.onAllNodesWithTag(albumsListTag).assertCountEquals(expectedAlbumItemsCount)
    }
}