package tds.reversi.reversicompose.viewmodel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import tds.reversi.reversicompose.model.Player
import tds.reversi.reversicompose.storage.GameState
import tds.reversi.reversicompose.storage.InMemoryStorage

@OptIn(ExperimentalCoroutinesApi::class)
class ReversiViewModelTests {

    private val dispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun makeVm(): ReversiViewModel {
        val storage = InMemoryStorage<String, GameState>()
        return ReversiViewModel(storage = storage)
    }

    private fun TestScope.waitUntilTrue(
        timeoutMs: Long = 2_000,
        stepSleepMs: Long = 10,
        predicate: () -> Boolean
    ) {
        val deadline = System.currentTimeMillis() + timeoutMs
        while (!predicate()) {
            runCurrent()

            if (System.currentTimeMillis() >= deadline) {
                throw AssertionError("Timeout waiting for condition")
            }

            Thread.sleep(stepSleepMs)
        }
    }

    private fun TestScope.startLocal(vm: ReversiViewModel) {
        vm.startLocalGame(Player.BLACK)
        waitUntilTrue { vm.uiState.game != null }
    }

    @Test
    fun `startLocalGame sets uiState game`() = runTest {
        val vm = makeVm()
        assertNull(vm.uiState.game)

        startLocal(vm)

        assertNotNull(vm.uiState.game)
    }

    @Test
    fun `toggleTargets flips showTargets`() = runTest {
        val vm = makeVm()
        startLocal(vm)

        val before = vm.uiState.game!!.showTargets

        vm.toggleTargets()
        waitUntilTrue { vm.uiState.game!!.showTargets != before }

        assertTrue(vm.uiState.game!!.showTargets != before)
    }

    @Test
    fun `exitGame clears uiState game`() = runTest {
        val vm = makeVm()
        startLocal(vm)
        assertNotNull(vm.uiState.game)

        vm.exitGame()
        waitUntilTrue { vm.uiState.game == null }

        assertNull(vm.uiState.game)
    }

    @Test
    fun `shouldShowWaitingMessage is false in local game`() = runTest {
        val vm = makeVm()
        startLocal(vm)

        assertFalse(vm.shouldShowWaitingMessage())
    }

    @Test
    fun `auto refresh cannot be toggled in local game`() = runTest {
        val vm = makeVm()
        startLocal(vm)

        assertFalse(vm.canToggleAutoRefresh())

        vm.toggleAutoRefresh()
        assertFalse(vm.canToggleAutoRefresh())
    }

    @Test
    fun `playAt keeps game non-null`() = runTest {
        val vm = makeVm()
        startLocal(vm)

        vm.playAt(0, 0)

        waitUntilTrue { vm.uiState.game != null }
        assertNotNull(vm.uiState.game)
    }

    @Test
    fun `clearError clears errorMessage`() = runTest {
        val vm = makeVm()
        startLocal(vm)

        vm.playAt(0, 0)
        runCurrent()
        Thread.sleep(20)

        vm.clearError()
        assertNull(vm.uiState.errorMessage)
    }
}
