import isel.leic.utils.Time

object RouletteDisplay {
    const val UPDATE_DISPLAY = 0x06
    const val DISPLAY_ON = 0x07
    const val DISPLAY_OFF = 0x0F
    const val CMD_MASK = 0x07
    const val DATA_MASK = 0x1F

    private var startTime: Long = 0
    private var currentPosition: Int = 0
    private var isAnimating: Boolean = false
    private var lastUpdate: Long = 0
    val positions = listOf(0x11, 0x16, 0x15, 0x14, 0x13, 0x12)

    fun init() {
        SerialEmitter.send(SerialEmitter.Destination.ROULETTE, DISPLAY_ON and CMD_MASK, 8)
        SerialEmitter.send(SerialEmitter.Destination.ROULETTE, UPDATE_DISPLAY and CMD_MASK, 8)
    }

    fun animation() {
        if (!isAnimating) {
            startTime = Time.getTimeInMillis()
            currentPosition = 0
            isAnimating = true
        }

        if (isAnimating) {
            val currentTime = Time.getTimeInMillis()
            if (currentTime - startTime >= 5000L) {
                isAnimating = false
                return
            }

            if (currentTime - lastUpdate >= 15) {
                val currentSymbol = positions[currentPosition]
                setValue(currentSymbol)
                currentPosition = (currentPosition + 1) % positions.size
                lastUpdate = currentTime
            }
        }
    }

    // Exibe o mesmo símbolo (value) em todos os dígitos do display
    fun setValue( value: Int ) {
        val values = IntArray(6) { value }

        for (i in 0..5) {
            val cmd = i and CMD_MASK
            val frame = (0 and DATA_MASK shl 3) or cmd
            SerialEmitter.send(SerialEmitter.Destination.ROULETTE, frame, 8)
        }

        for (i in 0..5) {
            val digit = values[i] // Envia o símbolo inteiro para cada posição
            val cmd = i and CMD_MASK
            val frame = (digit and DATA_MASK shl 3) or cmd

            SerialEmitter.send(SerialEmitter.Destination.ROULETTE, frame, 8)
        }

        SerialEmitter.send(SerialEmitter.Destination.ROULETTE, UPDATE_DISPLAY, 8)
    }

    // Exibe um número inteiro, convertendo-o para dígitos individuais no display
    fun setNumber(number: Int) {
        val numberStr = number.toString()
        val values = IntArray(6) { 0x1F }

        for (i in numberStr.indices) {
            val pos = i
            values[pos] = numberStr[numberStr.length - 1 - i].digitToInt()
        }
        for (i in 0..5) {
            val cmd = i and CMD_MASK
            val frame = (0 and DATA_MASK shl 3) or cmd
            SerialEmitter.send(SerialEmitter.Destination.ROULETTE, frame, 8)
            Time.sleep(2)
        }

        for (i in 0..5) {
            val digit = values[i] and DATA_MASK
            val cmd = i and CMD_MASK
            val frame = (digit shl 3) or cmd
            SerialEmitter.send(SerialEmitter.Destination.ROULETTE, frame, 8)
            Time.sleep(2)
        }

        SerialEmitter.send(SerialEmitter.Destination.ROULETTE, UPDATE_DISPLAY, 8)
        Time.sleep(2)
    }

    fun off( value: Boolean ) {
        if (value) {
            SerialEmitter.send(SerialEmitter.Destination.ROULETTE, DISPLAY_OFF, 8)
            Time.sleep(2)
        } else {
            SerialEmitter.send(SerialEmitter.Destination.ROULETTE, DISPLAY_ON, 8)
            Time.sleep(2)
        }
    }

    // Exibe uma sequência personalizada de símbolos nos 6 dígitos do display
    fun setValues(values: IntArray) {
        for (i in 0..5) {
            val cmd = i and CMD_MASK
            val frame = (0 and DATA_MASK shl 3) or cmd
            SerialEmitter.send(SerialEmitter.Destination.ROULETTE, frame, 8)
            Time.sleep(2)
        }

        for (i in 0..5) {
            val digit = values[5 - i] and DATA_MASK // inverte a ordem
            val cmd = i and CMD_MASK
            val frame = (digit shl 3) or cmd
            SerialEmitter.send(SerialEmitter.Destination.ROULETTE, frame, 8)
            Time.sleep(2)
        }

        SerialEmitter.send(SerialEmitter.Destination.ROULETTE, UPDATE_DISPLAY, 8)
        Time.sleep(2)
    }

    fun clear(){
        val vazio = IntArray(6) { 0x1F }
        setValues(vazio)
        // ou setValue(0x1F)
    }

    fun mapCharToCode(ch: Char): Int = when (ch) {
        in '0'..'9' -> ch.digitToInt()
        'A' -> 0x0A
        'B' -> 0x0B
        'C' -> 0x0C
        'D' -> 0x0D
        else -> 0x1F
    }
}

fun main() {
    HAL.init()
    SerialEmitter.init()
    RouletteDisplay.init()

    val start = Time.getTimeInMillis()
    while (Time.getTimeInMillis() - start < 5000L) {
        RouletteDisplay.animation()
        Time.sleep(100)
    }
    RouletteDisplay.off(true)
    Time.sleep(1000)
    RouletteDisplay.off(false)
    RouletteDisplay.setValue(0x0B)
    Time.sleep(1000)
    RouletteDisplay.setNumber(100)
    Time.sleep(1000)
    RouletteDisplay.setValues(intArrayOf(0x1F, 0x1F, 1, 9, 0, 4))
}