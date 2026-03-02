import isel.leic.utils.Time

object TUI {

    fun tui() {

        LCD.clear() // Limpa o display
        LCD.cursor(0, 0) // Posiciona o cursor no inicio do display
        Time.sleep(50) // Pequena pausa para garantir a inicializaçao

        var column = 0 // Controla a posiçao da coluna
        var line = 0   // Controla a linha atual

        while (true) {
            val key = KBD.waitKey(5000) // Espera ate 5 segundos por uma tecla
            if (key != KBD.NONE.toChar()) { // Se uma tecla valida foi pressionada
                when (key) {
                    '#' -> { // '#' limpa o display e reseta o cursor
                        LCD.clear()
                        Time.sleep(20) // Aguarda o LCD processar o comando de limpar
                        line = 0
                        column = 0
                        LCD.cursor(line, column) // Garante que o cursor volte ao inicio
                    }
                    '*' -> { // '*' muda para a proxima linha
                        if (line == 0) {
                            line = 1
                        } else {
                            line = 0
                        }
                        column = 0
                        LCD.cursor(line, column)
                    }
                    else -> {
                        LCD.write(key)
                        column++
                        if (column >= 16) { // Se atingir o fim da linha
                            column = 0
                            line++ // Avança para a proxima linha
                            if (line >= 2) line = 0 // Volta para a primeira linha se atingir o limite (2 linhas)
                            LCD.cursor(line, column) // Atualiza a posiçao do cursor
                        }
                    }
                }
                println("Tecla pressionada: $key")
            }
        }
    }

    fun writeCentered(line: Int, text: String) {
        val startCol = maxOf((16 - text.length) / 2, 0)
        LCD.cursor(line, startCol)
        LCD.write(text)
    }

    fun showMessage(line: Int, text: String, durationMs: Long = 2000) {
        LCD.clear()
        writeCentered(line, text)
        Time.sleep(durationMs)
        LCD.clear()
    }

    fun showLoading(line: Int, ciclos: Int = 3, delayMs: Long = 400) {
        for (i in 1..ciclos * 3) {
            val pontos = (i - 1) % 3 + 1
            val texto = "Shutting down" + ".".repeat(pontos)
            LCD.cursor(line, 0)
            val final = texto + " ".repeat(16 - texto.length)
            LCD.write(final)
            Time.sleep(delayMs)
        }
        LCD.clear()
    }

    fun readInput(prompt: String, maxLen: Int = 10): String {
        LCD.clear()
        LCD.write(prompt)
        var input = ""
        var column = prompt.length
        LCD.cursor(1, 0)

        while (input.length < maxLen) {
            val key = KBD.waitKey(10000)
            if (key == '#') break
            if (key != KBD.NONE.toChar()) {
                input += key
                LCD.write(key)
                column++
            }
        }
        return input
    }

    fun scrollText(line: Int, text: String, durationMs: Long = 5000, delayMs: Long = 300) {
        if (text.length <= 16) {
            LCD.cursor(line, 0)
            LCD.write(text.padEnd(16, ' '))
            Time.sleep(durationMs)
            return
        }

        val start = Time.getTimeInMillis()
        while (Time.getTimeInMillis() - start < durationMs) {
            for (i in 0..(text.length - 16)) {
                if (Time.getTimeInMillis() - start >= durationMs) return
                LCD.cursor(line, 0)
                LCD.write(text.substring(i, i + 16))
                Time.sleep(delayMs)
            }
        }
    }

    fun blinkMessage(line: Int, text: String, vezes: Int = 4, intervalo: Long = 300) {
        val inicio = maxOf((16 - text.length) / 2, 0) // para centrar

        repeat(vezes) {
            LCD.cursor(line, inicio)
            LCD.write(text) // mostra o texto
            Time.sleep(intervalo)

            LCD.cursor(line, inicio)
            LCD.write(" ".repeat(text.length)) // apaga o texto
            Time.sleep(intervalo)
        }
    }

    fun write(line: Int, column: Int, text: String) {
        LCD.cursor(line, column)
        LCD.write(text)
    }

    fun clear() = LCD.clear()

    fun waitKey(timeout: Long) = KBD.waitKey(timeout)
}

fun main() {
    HAL.init()
    KBD.init()
    SerialEmitter.init()
    LCD.init()
    //TUI.tui() // Testa escrever no LCD com o teclado


    while (true) {
        LCD.clear()
        TUI.writeCentered(0, "Escolha uma opcao")
        TUI.writeCentered(1, "* A B C D 0 #")

        val key = KBD.waitKey(10000)

        if (key != KBD.NONE.toChar()) {
            LCD.clear()  // limpa o LCD antes de qualquer ação

            when (key) {
                'A' -> TUI.writeCentered(0, "Jogo Da Roleta")
                'B' -> TUI.showMessage(0, "Bem-vindo", 3000)
                'C' -> TUI.showLoading(0)
                'D' -> {
                    val id = TUI.readInput("Id: ")
                    TUI.showMessage(0, "Ola jogador num $id")
                }

                '*' -> TUI.scrollText(0, "Tenta a tua sorte no Jogo da Roleta!", 8500, 200)
                '0' -> TUI.blinkMessage(0, "Estas pronto?", 6, 250)
                '#' -> {
                    LCD.clear()
                    TUI.writeCentered(0, "Teste Terminado")
                    break
                }

                else -> TUI.writeCentered(1, "Tecla: $key")
            }

            Time.sleep(1500)
        }
    }
}
