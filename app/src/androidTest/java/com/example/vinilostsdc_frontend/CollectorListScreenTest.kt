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
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CollectorListScreenTest {

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
     * Test Case 1: Listado - Al abrir la pantalla hay un listado con 2 coleccionistas.
     */
    @Test
    fun hu05_testCase1_openCollectorItems() {
        val collectorsListTag = "collectorListItem"
        val expectedCollectorItemsCount = 2

        this.navigateToCollectorListScreen()

        val capture3 = Screenshot.capture(composeRule.activity)
        capture3.name = "hu05_testCase1_openCollectorItems"
        capture3.process()

        composeRule.onAllNodesWithTag(collectorsListTag).assertCountEquals(expectedCollectorItemsCount)
    }

    /**
     * Test Case 2: Filtrar items - Buscar coleccionista de nombre "Monte"
     */
    @Test
    fun hu05_testCase2_filterCollectorItems1() {
        val collectorSearchString = "Monte"
        val collectorsListTag = "collectorListItem"
        val expectedCollectorItemsCount = 0

        this.navigateToCollectorListScreen()

        val searchBox = composeRule.onNodeWithTag("searchBox")
        searchBox.performTextClearance()
        searchBox.performTextInput(collectorSearchString)
        composeRule.waitForIdle()

        val capture4 = Screenshot.capture(composeRule.activity)
        capture4.name = "hu05_testCase2_filterCollectorItems1"
        capture4.process()

        composeRule.onAllNodesWithTag(collectorsListTag).assertCountEquals(expectedCollectorItemsCount)
    }

    /**
     * Test Case 3: Filtrar items - Buscar coleccionista de nombre "Bellon"
     */
    @Test
    fun hu05_testCase3_filterCollectorItems2() {
        val collectorSearchString = "Bellon"
        val collectorsListTag = "collectorListItem"
        val collectorName = "Manolo Bellon"
        val expectedCollectorItemsCount = 1

        this.navigateToCollectorListScreen()

        val searchBox = composeRule.onNodeWithTag("searchBox")
        searchBox.performTextClearance()
        searchBox.performTextInput(collectorSearchString)
        composeRule.waitForIdle()

        val capture5 = Screenshot.capture(composeRule.activity)
        capture5.name = "hu05_testCase3_filterCollectorItems2"
        capture5.process()

        composeRule.onAllNodesWithTag(collectorsListTag).assertCountEquals(expectedCollectorItemsCount)
        composeRule.onNodeWithText(collectorName).assertIsDisplayed()
    }
}