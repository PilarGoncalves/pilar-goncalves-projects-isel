package main.kotlin

import isel.leic.utils.Time

object CoinDeposit {
    private var stats: Statistics? = null

    fun initFromFile(fileName: String = "statistics.txt") {
        stats = FileAccess.readStatistics(fileName)
    }

    fun getStatistics(): Statistics? = stats

    // Verifica se há moeda, aceita-a e atualiza apenas coinsStored
    fun checkAndAccept(): Int {
        val currentStats = stats ?: return 0

        if (CoinAcceptor.hasCoin()) {
            val coinValue = CoinAcceptor.getCoinValue()
            CoinAcceptor.acceptCoin()
            currentStats.coinsStored += coinValue
            return coinValue
        }
        return 0
    }

    fun saveToFile(fileName: String = "statistics.txt") {
        if (stats != null) {
            FileAccess.writeStatistics(fileName, stats!!)
        }
    }

    fun registerGame(winnings: Int) {
        val currentStats = stats ?: return
        currentStats.gamesPlayed = (currentStats.gamesPlayed ?: 0) + 1
        currentStats.coinsStored -= winnings
    }
}

fun main() {
    HAL.init()
    CoinAcceptor.init()

    CoinDeposit.initFromFile()

    println("À espera de moedas...")
    while (true) {
        val value = CoinDeposit.checkAndAccept()
        if (value > 0) {
            println("Moeda aceite: $value créditos")
            println("Total moedas: ${CoinDeposit.getStatistics()?.coinsStored}")
            CoinDeposit.saveToFile()
        }
        Time.sleep(100)
    }
}
