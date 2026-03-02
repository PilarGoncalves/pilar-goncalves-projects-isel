package main.kotlin

import isel.leic.utils.Time
import kotlin.system.exitProcess

object M {
    private var statsRef: Statistics? = null
    private var rouletteStatsRef: MutableList<RouletteNumberStat>? = null
    private const val M_MASK = 0x80

    private var isTestDrawing = false
    private var testStartTime: Long = 0
    private var testDuration: Long = 0
    private var testAnimIndex = 0
    private var lastTestAnimUpdate: Long = 0

    fun enterMaintenance(stats: Statistics, rouletteStats: MutableList<RouletteNumberStat>) {
        statsRef = stats
        rouletteStatsRef = rouletteStats
        showMaintenanceSymbol()

        val messages = listOf("*-Play D-ShutD", "C-Stats A-Count")
        var toggle = true
        val switchInterval = 5000L

        while (true) {
            if (!onMaintenance()) return  // comentar esta linha, para testar M individualmente!!

            TUI.clear()
            TUI.writeCentered(0, "On Maintenance")
            TUI.write(1, 0, " ".repeat(16))
            TUI.writeCentered(1, if (toggle) messages[0] else messages[1])
            toggle = !toggle

            val start = Time.getTimeInMillis()
            while (Time.getTimeInMillis() - start < switchInterval) {
                val key = TUI.waitKey(200)
                when (key) {
                    'D' -> if (confirmShutdown()) return
                    'A' -> {
                        if (statsRef != null) showCounters(statsRef!!)
                        break
                    }
                    'C' -> {
                        if (rouletteStatsRef != null) showRouletteStats(rouletteStatsRef!!)
                        break
                    }
                    '*' -> {
                        startTestGame()
                        break
                    }
                    KBD.NONE.toChar() -> continue
                    else -> break
                }
            }
        }
    }

    private fun confirmShutdown(): Boolean {
        TUI.clear()
        TUI.writeCentered(0, "Shutdown")
        TUI.writeCentered(1, "5-Yes Other-No")
        val key = TUI.waitKey(10000)

        return if (key == '5') {
            if (statsRef != null && rouletteStatsRef != null) {
                FileAccess.writeStatistics("statistics.txt", statsRef!!)
                FileAccess.writeRouletteStats("Roulette_Stats.txt", rouletteStatsRef!!)
            }
            TUI.clear()
            //TUI.showMessage(0, "Shutting down...")
            TUI.showLoading(0)
            TUI.clear()
            exitProcess(0)
        } else {
            TUI.clear()
            TUI.writeCentered(0, "On Maintenance")
            false
        }
    }

    private fun showCounters(stats: Statistics) {
        TUI.clear()
        TUI.write(0, 0, "Games: ${stats.gamesPlayed}")
        TUI.write(1, 0, "Coins: ${stats.coinsStored}")

        val key = TUI.waitKey(5000)
        if (key == '*') {
            TUI.clear()
            TUI.writeCentered(0, "Clear counters")
            TUI.writeCentered(1, "5-Yes   other-No")

            val confirmKey = TUI.waitKey(5000)
            if (confirmKey == '5') {
                stats.gamesPlayed = 0
                stats.coinsStored = 0
                FileAccess.writeStatistics("statistics.txt", stats)
            }
        }
    }

    private fun showRouletteStats(stats: MutableList<RouletteNumberStat>) {
        var index = 0

        while (true) {
            TUI.clear()
            repeat(2) { line ->
                val statIndex = index + line
                if (statIndex < stats.size) {
                    val stat = stats[statIndex]
                    val symbol = when (stat.number) {
                        in 0..9 -> stat.number.toString()
                        10 -> "A"
                        11 -> "B"
                        12 -> "C"
                        13 -> "D"
                        else -> stat.number.toString()
                    }
                    TUI.write(line, 0, "$symbol: -> ${stat.count} \$:${stat.prizes}")
                }
            }

            val key = TUI.waitKey(5000)

            when (key) {
                '8' -> if (index + 2 < stats.size) index += 1
                '2' -> if (index > 0) index -= 1
                '*' -> {
                    TUI.clear()
                    TUI.writeCentered(0, "Clear statistics")
                    TUI.writeCentered(1, "5-Yes  other-No")
                    val confirm = KBD.waitKey(5000)
                    if (confirm == '5') {
                        stats.forEach {
                            it.count = 0
                            it.prizes = 0
                        }
                        FileAccess.writeRouletteStats("Roulette_Stats.txt", stats)
                    }
                    return
                }
                else -> return
            }
        }
    }

    private fun startTestGame() {
        var balance = 100
        val bets = mutableMapOf<Char, Int>()
        val validSymbols = "0123456789ABCD"
        var isBetting = true

        TUI.clear()
        TUI.writeCentered(1, validSymbols)
        RouletteDisplay.setNumber(balance)

        while (true) {
            if (isTestDrawing) {
                updateTestAnim(balance)
            }

            val key = TUI.waitKey(100)

            if ((isBetting || isTestDrawing) && key in validSymbols && balance > 0) {
                val current = bets[key] ?: 0
                if (current < 9) {
                    bets[key] = current + 1
                    balance -= 1

                    // Atualiza a linha 0 com quantidade apostada
                    val line = validSymbols.map { ch ->
                        val qty = bets[ch] ?: 0
                        if (qty == 0) ' ' else qty.digitToChar()
                    }.joinToString("")
                    TUI.write(0, 1, line + " ".repeat(14 - line.length))

                    // Atualiza o saldo
                    RouletteDisplay.setNumber(balance)
                }
            }

            if (key == '#' && isBetting && !isTestDrawing) {
                isTestDrawing = true
                testStartTime = Time.getTimeInMillis()
                testDuration = 5000L
                testAnimIndex = 0
                lastTestAnimUpdate = 0
            }

            if (isTestDrawing && Time.getTimeInMillis() - testStartTime >= testDuration) {
                val drawn = validSymbols.random()
                showResultOnDisplay(drawn, bets)
                Time.sleep(3000)
                isTestDrawing = false
                break
            }
            Time.sleep(10)
        }
        TUI.clear()
        RouletteDisplay.clear()
        showMaintenanceSymbol()
    }

    private fun showMaintenanceSymbol() = RouletteDisplay.setValue(0x17)

    private fun showResultOnDisplay(winner: Char, bets: Map<Char, Int>) {
        val validSymbols = "0123456789ABCD"
        val winnerIdx = validSymbols.indexOf(winner)
        var delay = 200L
        val delayIncrement = 300L

        for (i in 0..winnerIdx) {
            val code = RouletteDisplay.mapCharToCode(validSymbols[i])
            RouletteDisplay.setValue(code)
            Time.sleep(delay)
            delay += delayIncrement
        }

        val winnerCode = RouletteDisplay.mapCharToCode(winner)

        repeat(10) {
            RouletteDisplay.setValue(winnerCode)
            Time.sleep(250)
            RouletteDisplay.clear()
            Time.sleep(250)
        }

        val hits = bets[winner] ?: 0
        val winnings = hits * 2

        val digits = IntArray(6) { 0x1F }
        digits[0] = RouletteDisplay.mapCharToCode(winner)

        if (winnings > 0) {
            val resultStr = winnings.toString()
            val start = 6 - resultStr.length
            for (i in resultStr.indices) {
                digits[start + i] = resultStr[i].digitToInt()
            }
        } else {
            // Se perdeu créditos, mostra com símbolo - à esquerda
            val resultStr = (bets.values.sum()).toString()  // valor perdido foi o total apostado
            val start = 6 - resultStr.length - 1  // -1 para o símbolo -
            digits[start] = 0x10 // símbolo de menos
            for (i in resultStr.indices) {
                digits[start + 1 + i] = resultStr[i].digitToInt()
            }
        }
        RouletteDisplay.setValues(digits)
    }

    private fun updateTestAnim(balance: Int) {
        if (!isTestDrawing) return
        val currentTime = Time.getTimeInMillis()
        if (currentTime - testStartTime >= testDuration) return
        if (currentTime - lastTestAnimUpdate < 100) return

        val animSymbols = RouletteDisplay.positions
        val balanceStr = balance.toString()
        val balanceDigits = balanceStr.map { it.digitToInt() }

        val values = IntArray(6) { 0x1F }
        for (i in balanceDigits.indices) {
            values[5 - i] = balanceDigits[balanceDigits.size - 1 - i]
        }
        for (i in 0..< 6 - balanceDigits.size) {
            values[i] = animSymbols[testAnimIndex % animSymbols.size]
        }

        RouletteDisplay.setValues(values)
        testAnimIndex++
        lastTestAnimUpdate = currentTime
    }

    fun onMaintenance(): Boolean{
        return HAL.isBit(M_MASK)
    }
}

fun main() {
    HAL.init()
    KBD.init()
    LCD.init()
    SerialEmitter.init()
    RouletteDisplay.init()

    val stats = FileAccess.readStatistics("statistics.txt")
    val rouletteStats = FileAccess.readRouletteStats("Roulette_Stats.txt")
    M.enterMaintenance(stats, rouletteStats)
}
