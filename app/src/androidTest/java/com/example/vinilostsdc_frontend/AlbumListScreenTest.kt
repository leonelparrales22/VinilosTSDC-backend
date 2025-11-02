package com.example.vinilostsdc_frontend

import androidx.compose.ui.test.assertCountEquals
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.test.runner.screenshot.Screenshot
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.concurrent.timer

@RunWith(AndroidJUnit4::class)
class AlbumListScreenTest {

    val timeout: Long = 10_000

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun hu01_testCases() {
        // Arrange

        // Pantalla principal de selección de roles Vinilos
        // Ir a la pantalla de Visitante
        composeRule.onNodeWithText("Vinilos").assertIsDisplayed()

        val captureSetup1 = Screenshot.capture(composeRule.activity)
        captureSetup1.name = "hu01_setup1_vinilos"
        captureSetup1.process()

        composeRule.onNodeWithText("Visitante").performClick()
        composeRule.waitUntil(timeoutMillis = timeout) {
            composeRule.onAllNodesWithText("Catálogo de álbumes").fetchSemanticsNodes()
                .isNotEmpty()
        }
        composeRule.onNodeWithText("Visitante").assertIsDisplayed()

        // Pantalla de Visitante
        // Ir a la pantalla de Listado de Álbumes
        composeRule.onNodeWithText("Catálogo de álbumes").assertIsDisplayed()

        val captureSetup2 = Screenshot.capture(composeRule.activity)
        captureSetup2.name = "hu01_setup2_visitante"
        captureSetup2.process()

        composeRule.onNodeWithText("Catálogo de álbumes").performClick()
        composeRule.waitUntil(timeoutMillis = timeout) {
            composeRule.onAllNodesWithText("Álbumes").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText("Álbumes").assertIsDisplayed()
        composeRule.waitUntil(timeoutMillis = timeout) {
            composeRule.onAllNodesWithTag("albumListItem").fetchSemanticsNodes().isNotEmpty()
        }

        // Pantalla de Listado de Álbumes

        // Test Case 1: Inicio - Al abrir la pantalla hay un listado con 4 álbumes
        val capture1 = Screenshot.capture(composeRule.activity)
        capture1.name = "hu01_testCase1_openAlbumListScreen"
        capture1.process()

        composeRule.onNodeWithTag("albumList").assertExists("No se ha cargado el listado de álbumes")
        composeRule.onAllNodesWithTag("albumListItem").assertCountEquals(4)

        // Test Case 2: Filtrar items - Buscar el álbum de nombre "at"
        val searchBox = composeRule.onNodeWithTag("searchBox")
        searchBox.performTextInput("at")
        composeRule.waitForIdle()

        val capture2 = Screenshot.capture(composeRule.activity)
        capture2.name = "hu01_testCase2_filterAlbumList1"
        capture2.process()

        composeRule.onAllNodesWithTag("albumListItem").assertCountEquals(2)
        composeRule.onNodeWithText("A Night at the Opera").assertIsDisplayed()
        composeRule.onNodeWithText("A Day at the Races").assertIsDisplayed()

        // Test Case 3: Filtrar items - Buscar el álbum de nombre "Destiny"
        searchBox.performTextClearance()
        searchBox.performTextInput("Destiny")
        composeRule.waitForIdle()

        val capture3 = Screenshot.capture(composeRule.activity)
        capture3.name = "hu01_testCase3_filterAlbumList2"
        capture3.process()

        composeRule.onAllNodesWithTag("albumListItem").assertCountEquals(0)

        // Test Case 4: Filtrar items - Buscar el álbum de nombre "América"
        searchBox.performTextClearance()
        searchBox.performTextInput("América")
        composeRule.onNodeWithTag("searchButton").performClick()
        composeRule.waitForIdle()

        val capture4 = Screenshot.capture(composeRule.activity)
        capture4.name = "hu01_testCase4_filterAlbumList3"
        capture4.process()

        composeRule.onAllNodesWithTag("albumListItem").assertCountEquals(1)
        composeRule.onNodeWithText("Buscando América").assertIsDisplayed()

        // Test Case 5: Abrir pantalla de detalle de un álbum al dar click en el listado
        composeRule.onNodeWithText("Buscando América").performClick()
        composeRule.waitForIdle()

        val capture5 = Screenshot.capture(composeRule.activity)
        capture5.name = "hu01_testCase5_openAlbumDetail"
        capture5.process()

        composeRule.onNodeWithText("Buscando América").assertIsDisplayed()
        composeRule.onNodeWithText("2 tracks").assertIsDisplayed()
    }
}