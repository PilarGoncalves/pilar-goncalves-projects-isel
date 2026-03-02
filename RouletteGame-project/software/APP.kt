package main.kotlin
import isel.leic.utils.Time


object APP {
    private var balance = 0
    private val bets = mutableMapOf<Char, Int>()
    private val validSymbols = "0123456789ABCD"
    private var isBetting = false
    private var isDrawing = false
    private var drawStartTime: Long = 0
    private var drawDuration: Long = 0
    private var drawAnimIndex = 0
    private var lastAnimUpdate: Long = 0
    private var needsScreenUpdate = true

    fun init() {
        balance = 0
        bets.clear()
        isBetting = false
        needsScreenUpdate = true
    }

    fun startGame() {
        while (true) {
            appA()  // Funcao de mostrar a tela inicial
            appB()  // Funcao de animação (não-bloqueante)
            appC()  // Funcao de inserir moedas
            appD()  // Funcao de manutenção
            appE()  // Funcao de processamento de teclas (* e #)
            Time.sleep(10)
        }
    }

    private fun appA() {
        if (!isBetting && needsScreenUpdate) {
            showStartScreen()
            needsScreenUpdate = false
        }
    }

    private fun appB() {
        if (!isBetting && !isDrawing) {
            RouletteDisplay.animation()
        } else if (isDrawing) {
            updateDrawAnimation()
        }
    }

    private fun appC() {
        if (!isBetting && !isDrawing) {
            val value = CoinDeposit.checkAndAccept()
            if (value > 0) {
                balance += value
                CoinDeposit.saveToFile()
                needsScreenUpdate = true
            }
        }
    }

    private fun appD() {
        if (M.onMaintenance() && !isBetting && !isDrawing) {
            val stats = CoinDeposit.getStatistics()
            val rouletteStats = StatisticsManager.getStats()
            if (stats != null && rouletteStats != null) {
                M.enterMaintenance(stats, rouletteStats)
                repeat(5) {
                    CoinDeposit.checkAndAccept()
                }
                needsScreenUpdate = true
            }
        }
    }

    private fun appE() {
        val key = TUI.waitKey(100)

        if (!isBetting && key == '*') {
            if (balance == 0) return
            RouletteDisplay.setNumber(balance)
            isBetting = true
            prepareForBetting()
        }

        if ((isBetting || isDrawing) && key in validSymbols && balance > 0) {
            processBet(key)
            updateLCD()
            RouletteDisplay.setNumber(balance)
        }

        if (key == '#' && isBetting && !isDrawing) {
            startDraw()
        }

        if (isDrawing && Time.getTimeInMillis() - drawStartTime >= drawDuration) {
            val drawn = validSymbols.random()
            slowDownAndStopOnWinner(drawn)
            val winnings = calculateWinnings(drawn)
            CoinDeposit.registerGame(winnings)
            CoinDeposit.saveToFile()
            StatisticsManager.saveToFile()
            resetState()
            isDrawing = false
        }
    }

    private fun showStartScreen() {
        TUI.clear()
        TUI.writeCentered(0, "Roulette Game")

        val specialChars = listOf(0, 1, 2, 3).map { it.toChar() }
        val specialPart = specialChars.joinToString(" ")
        val balanceStr = "\$${balance}"

        val originalStartPos = maxOf(0, (16 - specialPart.length - balanceStr.length - 1))
        val startPos = maxOf(0, originalStartPos - 1)

        TUI.write(1, 0, " ".repeat(16))

        TUI.write(1, startPos, specialPart)

        TUI.write(1, 16 - balanceStr.length, balanceStr)
    }

    private fun prepareForBetting() {
        TUI.clear()
        TUI.writeCentered(1, validSymbols)
    }

    private fun processBet(key: Char) {
        val current = bets[key] ?: 0
        if (current < 9) {
            bets[key] = current + 1
            balance -= 1
        }
    }

    private fun updateLCD() {
        val line = validSymbols.map { ch ->
            val qty = bets[ch] ?: 0
            if (qty == 0) ' ' else qty.digitToChar()
        }.joinToString("")

        val spaces = " ".repeat(14 - line.length)
        TUI.write(0, 1, line + spaces)
    }

    private fun slowDownAndStopOnWinner(winner: Char) {
        val symbols = validSymbols.toList()
        val winnerIdx = symbols.indexOf(winner)
        var delay = 200L
        val delayIncrement = 300L

        for (i in 0..winnerIdx) {
            val code = RouletteDisplay.mapCharToCode(symbols[i])
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
            val resultStr = (bets.values.sum()).toString()
            val start = 6 - resultStr.length - 1
            digits[start] = 0x10
            for (i in resultStr.indices) {
                digits[start + 1 + i] = resultStr[i].digitToInt()
            }
        }

        RouletteDisplay.setValues(digits)
        Time.sleep(3000)
    }

    private fun startDraw() {
        isDrawing = true
        drawStartTime = Time.getTimeInMillis()
        drawDuration = 5000L
        drawAnimIndex = 0
        lastAnimUpdate = 0
    }

    private fun updateDrawAnimation() {
        if (!isDrawing) return
        val currentTime = Time.getTimeInMillis()
        if (currentTime - drawStartTime >= drawDuration) return
        if (currentTime - lastAnimUpdate < 100) return

        val animSymbols = RouletteDisplay.positions
        val balanceStr = balance.toString()
        val balanceDigits = balanceStr.map { it.digitToInt() }

        val values = IntArray(6) { 0x1F }
        for (i in balanceDigits.indices) {
            values[5 - i] = balanceDigits[balanceDigits.size - 1 - i]
        }
        for (i in 0..< 6 - balanceDigits.size) {
            values[i] = animSymbols[drawAnimIndex % animSymbols.size]
        }

        RouletteDisplay.setValues(values)
        drawAnimIndex++
        lastAnimUpdate = currentTime
    }

    private fun calculateWinnings(drawn: Char): Int {
        val hits = bets[drawn] ?: 0
        val winnings = hits * 2
        balance += winnings

        StatisticsManager.registerOutcome(drawn, winnings)
        StatisticsManager.saveToFile()
        return winnings
    }

    private fun resetState() {
        repeat(5) {
            CoinDeposit.checkAndAccept()
        }
        bets.clear()
        isBetting = false
        needsScreenUpdate = true
        Time.sleep(1000)
    }
}

fun main() {
    HAL.init()
    KBD.init()
    LCD.init()
    LCD.specialChar()
    LCD.returnToDDRAM()

    SerialEmitter.init()
    RouletteDisplay.init()

    CoinDeposit.initFromFile()
    StatisticsManager.initFromFile()
    CoinAcceptor.init()

    APP.init()
    APP.startGame()
}
