package main.kotlin
import isel.leic.utils.Time

object CoinAcceptor {
    const val ACCEPT_MASK = 0x40
    const val COIN_MASK = 0x40
    const val COINID_MASK = 0x20
    private var lastCoinState: Boolean = false

    fun init () {
        HAL.clrBits(ACCEPT_MASK)
        lastCoinState = false
    }

    fun hasCoin () : Boolean {
        val currentCoinState = HAL.isBit(COIN_MASK)
        val coinDetected = !lastCoinState && currentCoinState
        lastCoinState = currentCoinState
        return coinDetected
    }

    fun getCoinValue (): Int {
        return if (HAL.isBit(COINID_MASK)) 4 else 2
    }

    fun acceptCoin () {
        HAL.setBits(ACCEPT_MASK)
        Time.sleep(200)
        HAL.clrBits(ACCEPT_MASK)
    }
}

fun main() {
    HAL.init()
    CoinAcceptor.init()

    while (true) {
        if (CoinAcceptor.hasCoin()) {
            CoinAcceptor.getCoinValue()
            CoinAcceptor.acceptCoin()
        }
        Time.sleep(100)
    }
}