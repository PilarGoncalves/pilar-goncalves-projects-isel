import isel.leic.utils.Time

// Ler teclas. Funcoes retornam ’0’..’9’, ’A’..’D’, ’#’ ,’*’ ou NONE.
object KBD {
    const val NONE = 0
    const val DVAL_MASK = 0x10
    const val D_MASK = 0x0F
    const val ACK_MASK = 0x80
    const val TECLADO = "147*2580369#ABCD"

    // Inicia a classe
    fun init() = HAL.clrBits(ACK_MASK)

    // Retorna de imediato a tecla premida ou NONE se nao ha tecla premida.
    fun getKey(): Char{
        if (!HAL.isBit(DVAL_MASK)) return NONE.toChar()

        val index = HAL.readBits(D_MASK)
        val key = TECLADO[index]

        HAL.setBits(ACK_MASK)
        while(HAL.isBit(DVAL_MASK)); // Aguarda que a tecla seja solta
        HAL.clrBits(ACK_MASK)

        return key
    }

    // Retorna a tecla premida, caso ocorra antes do ’timeout’ (em milissegundos),
    // ou NONE caso contrario.
    fun waitKey(timeout: Long): Char{
        val endTime = Time.getTimeInMillis() + timeout
        do {
            val key = getKey()
            if (key != NONE.toChar()) return key
        } while (Time.getTimeInMillis()  < endTime )

        return NONE.toChar() // Retorna NONE se o tempo acabar
    }
}

fun main() {
    HAL.init()
    KBD.init()
    while(true) {
        val key = KBD.waitKey(5000) // Espera ate 5 segundos por uma tecla
        if (key != KBD.NONE.toChar()) { // So imprime se for uma tecla valida
            println("Tecla pressionada: $key")
        }
    }
}