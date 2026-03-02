package main.kotlin

import java.io.File

data class RouletteNumberStat(val number: Int, var count: Int, var prizes: Int)

data class Statistics(var gamesPlayed: Int = 0, var coinsStored: Int = 0)

object FileAccess {
    private const val DELIM = ';'

    fun readStatistics(fileName: String): Statistics {
        val file = File(fileName)
        if (!file.exists() || file.readText().isBlank()) {
            file.writeText("0\n0\n")
            return Statistics()
        }
        val lines = file.readLines()
        val games = lines.getOrNull(0)?.toIntOrNull() ?: 0
        val coins = lines.getOrNull(1)?.toIntOrNull() ?: 0
        return Statistics(games, coins)
    }

    fun writeStatistics(fileName: String, stats: Statistics) {
        val file = File(fileName)
        file.writeText("${stats.gamesPlayed}\n${stats.coinsStored}\n")
    }

    fun readRouletteStats(fileName: String): MutableList<RouletteNumberStat> {
        val file = File(fileName)
        if (!file.exists()) {
            return (0..13).map { RouletteNumberStat(it, 0, 0) }.toMutableList()
        }
        val lines = file.readLines().filter { it.isNotBlank() }
        val stats = mutableListOf<RouletteNumberStat>()
        for (line in lines) {
            val parts = line.split(DELIM)
            if (parts.size == 3) {
                val number = parts[0].toIntOrNull() ?: continue
                val count = parts[1].toIntOrNull() ?: 0
                val prizes = parts[2].toIntOrNull() ?: 0
                stats.add(RouletteNumberStat(number, count, prizes))
            }
        }
        return stats
    }

    fun writeRouletteStats(fileName: String, stats: List<RouletteNumberStat>) {
        val file = File(fileName)
        val lines = stats.map { "${it.number}$DELIM${it.count}$DELIM${it.prizes}" }
        file.writeText(lines.joinToString("\n") + "\n")
    }
}

fun main() {
    val statsFile = "statistics.txt"
    val rouletteFile = "Roulette_Stats.txt"

    // Ler estatísticas
    val stats = FileAccess.readStatistics(statsFile)
    println("Jogos: ${stats.gamesPlayed}, Moedas: ${stats.coinsStored}")

    // Atualizar estatísticas e gravar
    stats.gamesPlayed += 1
    stats.coinsStored += 5
    FileAccess.writeStatistics(statsFile, stats)
    println("Estatísticas atualizadas e gravadas.")

    // Ler estatísticas da roleta
    val rouletteStats = FileAccess.readRouletteStats(rouletteFile)
    println("Estatísticas da Roleta:")
    rouletteStats.forEach {
        println("Número ${it.number}: saiu ${it.count} vezes, prêmios: ${it.prizes}")
    }

    // Atualizar estatística de um número e gravar
    if (rouletteStats.isNotEmpty()) {
        rouletteStats[0].count += 1
        rouletteStats[0].prizes += 10
        FileAccess.writeRouletteStats(rouletteFile, rouletteStats)
        println("Estatísticas da roleta atualizadas e gravadas.")
    }
}