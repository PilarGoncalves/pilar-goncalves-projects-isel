import isel.leic.UsbPort

object HAL {
    private var currentState = 0x00

    // Inicia o objeto
    fun init() = UsbPort.write(currentState)

    // Retorna 'true' se o bit definido pela mask esta com o valor logico '1' no UsbPort
    fun isBit(mask: Int): Boolean{
        return (UsbPort.read() and mask) != 0
    }

    // Retorna os valores dos bits representados por mask presentes no UsbPort
    fun readBits(mask: Int): Int{
        return UsbPort.read() and mask
    }

    // Escreve nos bits representados por mask os valores dos bits correspondentes em value
    fun writeBits(mask: Int, value: Int){
        clrBits(mask)
        setBits(value and mask)
    }

    // Coloca os bits representados por mask no valor logico '1'
    fun setBits(mask: Int){
        currentState = currentState or mask
        UsbPort.write(currentState)  // Atualiza o estado no hardware
    }

    // Coloca os bits representados por mask no valor logico '0'
    fun clrBits(mask: Int){
        currentState = currentState and mask.inv()
        UsbPort.write(currentState)  // Atualiza o estado no hardware
    }
}

fun main(){
    HAL.init() // Inicializa o HAL (estado inicial = 0)
    HAL.isBit(0x01) // Verifica se o bit 0 esta ativo
    HAL.readBits(0xFF) // Le o estado atual de todos os bits
    HAL.writeBits(0xF0 , 0xA0) // Escreve 1010_0000 nos bits 4 a 7 (com mascara 0xF0)
    HAL.setBits(0x0F) // Ativa os primeiros 4 bits (bits 0 a 3)
    HAL.clrBits(0x05) // Limpa os bits 0 e 2 (mascara 0000_0101)
    println("Bit 1 ativo? ${HAL.isBit(0x02)}") // Exibe o estado final do bit 1
}