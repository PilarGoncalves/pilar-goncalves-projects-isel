
// Envia tramas para os diferentes modulos Serial Receiver.
object SerialEmitter {
    enum class Destination {LCD, ROULETTE}

    const val SDX_MASK = 0x01
    const val SCLK_MASK = 0x02
    const val LCD_SEL_MASK = 0x04
    const val P_MASK = 0x01
    const val RD_SEL_MASK = 0x08

    // Inicia a classe
    fun init () = HAL.setBits(LCD_SEL_MASK or RD_SEL_MASK)

    // Envia uma trama para o Serial Receiver
    // identificado o destino em ’addr’,
    // os bits de dados em ’data’
    // e em ’size’ o numero de bits a enviar.
    fun send(addr: Destination, data: Int, size: Int) {

        val destinationBit = if (addr == Destination.LCD) LCD_SEL_MASK else RD_SEL_MASK

        HAL.clrBits(destinationBit)

        var parity = 0

        for (i in 0 until size) {
            val bit = (data shr i) and P_MASK
            if (bit == 1) HAL.setBits(SDX_MASK) else HAL.clrBits(SDX_MASK)

            HAL.setBits(SCLK_MASK)
            HAL.clrBits(SCLK_MASK)

            parity = parity xor bit
        }

        // Envia o bit de paridade
        if (parity == 0) HAL.setBits(SDX_MASK) else HAL.clrBits(SDX_MASK)

        HAL.setBits(SCLK_MASK)
        HAL.clrBits(SCLK_MASK)

        HAL.setBits(destinationBit)
    }
}

fun main() {
    HAL.init()
    SerialEmitter.init()
    SerialEmitter.send(SerialEmitter.Destination.LCD, 0x15, 5)
}

