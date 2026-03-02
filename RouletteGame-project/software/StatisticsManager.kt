package main.kotlin

object StatisticsManager {
    private var stats: MutableList<RouletteNumberStat>? = null

    fun initFromFile(fileName: String = "Roulette_Stats.txt") {
        stats = FileAccess.readRouletteStats(fileName)
    }

    fun getStats(): MutableList<RouletteNumberStat>? = stats

    private fun symbolToIndex(ch: Char): Int? = when (ch) {
        in '0'..'9' -> ch.digitToInt()
        'A' -> 10
        'B' -> 11
        'C' -> 12
        'D' -> 13
        else -> null
    }

    // Atualiza os dados de um número sorteado
    fun registerOutcome(number: Char, totalPrize: Int) {
        val statsList = stats ?: return
        val index = symbolToIndex(number) ?: return
        val entry = statsList.getOrNull(index) ?: return
        entry.count += 1
        entry.prizes += totalPrize
    }

    fun saveToFile(fileName: String = "Roulette_Stats.txt") {
        val currentStats = stats
        if (currentStats != null) {
            FileAccess.writeRouletteStats(fileName, currentStats)
        }
    }
}

fun main() {
    StatisticsManager.initFromFile()
    StatisticsManager.registerOutcome('A', 30)
    StatisticsManager.registerOutcome('3', 45)
    // Guarda os dados atualizados no ficheiro
    StatisticsManager.saveToFile()
    println("Estatísticas atualizadas com sucesso.")
}
