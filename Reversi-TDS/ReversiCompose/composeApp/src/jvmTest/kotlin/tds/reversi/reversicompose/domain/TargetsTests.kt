package tds.reversi.reversicompose.domain

import tds.reversi.reversicompose.model.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class TargetsTests {

    @Test
    fun `targets on enables targets`() {
        val game = Game.createInitial(name = "", myPlayer = Player.BLACK)

        val cmd = Targets()
        val result = cmd.execute(
            context = CommandContext.WithGame(game),
            params = listOf("on")
        ) as CommandResult.Success

        assertTrue(result.game.showTargets)
    }

    @Test
    fun `targets off disables targets`() {
        val game = Game.createInitial(name = "", myPlayer = Player.BLACK)
            .setTargets(true)

        val cmd = Targets()
        val result = cmd.execute(
            context = CommandContext.WithGame(game),
            params = listOf("off")
        ) as CommandResult.Success

        assertEquals(false, result.game.showTargets)
    }

    @Test
    fun `targets does not change board or current player`() {
        val game = Game.createInitial(name = "", myPlayer = Player.BLACK)

        val cmd = Targets()
        val result = cmd.execute(
            context = CommandContext.WithGame(game),
            params = listOf("on")
        ) as CommandResult.Success

        assertEquals(game.board, result.game.board)
        assertEquals(game.currentPlayer, result.game.currentPlayer)
    }

    @Test
    fun `targets fails on invalid parameter`() {
        val game = Game.createInitial(name = "", myPlayer = Player.BLACK)

        val cmd = Targets()

        assertFailsWith<CommandException.InvalidParameters> {
            cmd.execute(
                context = CommandContext.WithGame(game),
                params = listOf("maybe")
            )
        }
    }
}
