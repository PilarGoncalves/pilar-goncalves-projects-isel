import isel.leic.utils.Time
import java.io.Serial

// Escreve no LCD usando a interface a 4 bits
object LCD {

    // Dimensao do display
    private const val LINES = 2
    private const val COLS = 16
    // Define se a interface e Serie ou Paralela
    private const val SERIAL_INTERFACE = true

    // Mascaras para os pinos do controlo
    private const val RS_MASK = 0x10  // Register Select (RS) - Define comando ou dado
    private const val E_MASK = 0x20   // Enable (E) - Ativa o LCD
    private const val DATA_MASK = 0x0F // Mascara para os 4 bits de dados (D4 a D7)
    private const val CGRAM_MASK = 0x40 //Mascara para escrever o caracter especial
    private const val DDRAM_MASK = 0x80

    // Escreve um byte de comando / dados no LCD em paralelo
    private fun writeNibbleParallel(rs: Boolean, data: Int) {
        if (rs) HAL.setBits(RS_MASK) else HAL.clrBits(RS_MASK)
        HAL.setBits(E_MASK)
        HAL.writeBits(DATA_MASK, data)
        HAL.clrBits(E_MASK)
        //HAL.clrBits(RS_MASK)
    }

    // Escreve um byte de comando / dados no LCD em serie
    private fun writeNibbleSerial(rs: Boolean, data: Int) {
        val nibbleWithRS = (if (rs) data.shl(1) or 1 else data.shl(1) or 0)
        //val nibbleWithRS = (if (rs) RS_MASK else 0x00) or (data and DATA_MASK)
        SerialEmitter.send(SerialEmitter.Destination.LCD , nibbleWithRS, 5)
    }

    // Escreve um nibble de comando / dados no LCD
    private fun writeNibble(rs: Boolean, data: Int) {
        if (SERIAL_INTERFACE) {
            writeNibbleSerial(rs, data)
        } else {
            writeNibbleParallel(rs, data)
        }
    }

    // Escreve um byte de comando / dados no LCD
    private fun writeByte(rs: Boolean, data: Int) {
        writeNibble(rs, (data shr 4) and DATA_MASK) // Envia os 4 bits mais significativos
        writeNibble(rs, data and DATA_MASK) // Envia os 4 bits menos significativos
    }

    // Escreve um comando no LCD
    private fun writeCMD(data: Int) {
        writeByte(false, data) // RS = 0 (Comando)
    }

    // Escreve um dado no LCD
    private fun writeDATA(data: Int) {
        writeByte(true, data) // RS = 1 (Dado)
    }

    // Envia a sequencia de iniciacao para comunicacao a 4 bits
    fun init() {

        Time.sleep(50)

        // Inicialização especial (modo 8 bits temporario)
        writeNibble(false, 0x03)
        Time.sleep(5) // Espera 5ms

        writeNibble(false, 0x03)
        Time.sleep(1) // Espera 1ms

        writeNibble(false, 0x03)

        // Ativa o modo 4 bits
        writeNibble(false, 0x02)

        // Configuração do LCD
        writeCMD(0x28) // Modo 4 bits, 2 linhas, fonte 5x8
        writeCMD(0x08) // Display Off
        writeCMD(0x01) // Limpa o display
        writeCMD(0x06) // Cursor move para a direita
        writeCMD(0x0C) // Display On
    }

    // Escreve um carater na posicao corrente
    fun write(c: Char) {
        writeDATA(c.code) // Envia codigo ASCII do carater
    }

    // Escreve uma string na posicao corrente
    fun write(text: String) {
        var currentLine = 0
        var currentColumn = 0

        // Escreve o texto a partir da posiçao atual do cursor
        for (i in text.indices) {
            this.write(text[i])
            currentColumn++

            // Se atingir o fim da linha (16 colunas) e ainda houver texto
            if (currentColumn >= COLS && i < text.length - 1) {
                if (currentLine == 0) { // So muda para a linha 1 se estiver na linha 0
                    currentLine = 1
                    cursor(currentLine, 0)
                    currentColumn = 0
                } else {
                    break // Para se ja estiver na linha 1
                }
            }
        }
    }

    // Envia comando para posicionar cursor (’line’: 0 .. LINES-1, ’column’: 0 .. COLS-1)
    fun cursor(line: Int, column: Int) {
        var finalLine = line
        var finalColumn = column

        // Garante que a linha esta dentro dos limites (0 ou 1)
        if (finalLine < 0) finalLine = 0
        if (finalLine >= LINES) finalLine = LINES - 1 // Maximo e LINES-1

        // Garante que a coluna esta dentro dos limites (0 a 15)
        if (finalColumn < 0) finalColumn = 0
        if (finalColumn >= COLS) finalColumn = COLS - 1 // Maximo e COLS-1

        // Define o endereço base da linha
        val address = if (finalLine == 0) 0x80 else 0xC0

        // Move o cursor para a posiçao desejada
        writeCMD(address + finalColumn)
    }

    // Envia o comando para limpar o ecra e posicionar o cursor em (0, 0)
    fun clear( ) {
        writeCMD(0x01) // Comando para limpar o display
    }

    fun specialChar() {
        val chars = arrayOf(
            byteArrayOf(0b00000, 0b01110, 0b01110, 0b11111, 0b11111, 0b00100, 0b01110, 0b00000), // Paus
            byteArrayOf(0b00000, 0b00000, 0b01010, 0b10101, 0b10001, 0b01010, 0b00100, 0b00000), // Copas
            byteArrayOf(0b00000, 0b00100, 0b01110, 0b11111, 0b11111, 0b00100, 0b01110, 0b00000), // Espadas
            byteArrayOf(0b00000, 0b00100, 0b01010, 0b10001, 0b10001, 0b01010, 0b00100, 0b00000)  // Ouros
        )

        for ((charIndex, pattern) in chars.withIndex()) {
            writeCMD(CGRAM_MASK + (charIndex * 8))
            for (line in pattern) {
                writeDATA(line.toInt())
            }
        }
    }

    fun writeSpecialChar(index: Int) {
        writeDATA(index)
    }

    fun returnToDDRAM() {
        writeCMD(DDRAM_MASK)
    }
}


fun main() {
    HAL.init()  // Inicializa o HAL
    LCD.init() // Inicializa o LCD
    LCD.clear() // Limpa a tela
    LCD.specialChar()
    LCD.returnToDDRAM()

    LCD.write("HELLO WORLD!!") // Escreve uma mensagem
    LCD.cursor(1, 0) // Move o cursor para a segunda linha
    LCD.writeSpecialChar(0)
    LCD.writeSpecialChar(1)
    LCD.writeSpecialChar(2)
    LCD.writeSpecialChar(3)
}