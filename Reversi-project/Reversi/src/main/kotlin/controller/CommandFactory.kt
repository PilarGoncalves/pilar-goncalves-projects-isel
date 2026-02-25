package controller

import storage.Storage
import storage.GameState

/**
 * Converts a string representing a command to the corresponding [Command] object.
 * This function maps the string input to the appropriate command instance, which will later be executed.
 *
 * @param storage The storage instance used by some commands to persist or retrieve game states.
 * @return The corresponding [Command] instance based on the string input.
 * @throws CommandException.Unknown if the string does not correspond to a known command.
 */
@Suppress("UNCHECKED_CAST")
fun String.toCommand(storage: Storage<String, GameState>): Command<CommandContext> = when (this) {
    "new" -> New(storage)
    "join" -> Join(storage)
    "play" -> Play(storage)
    "pass" -> Pass(storage)
    "targets" -> Targets()
    "refresh" -> Refresh(storage)
    "show" -> Show()
    "exit" -> Exit()
    else -> throw CommandException.Unknown() // Throws an exception if the command is not recognized
} as Command<CommandContext>
