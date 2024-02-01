package jp.wataju.context

class NumberContext {
    var number: Number

    fun setNumber(size: Int) {
        number.checkNumber(this, size)
    }
    fun changeNumber(number: Number) {
        this.number = number
    }

    init {
        number = Dos
    }
}

interface Number {
    fun checkNumber(
        numberContext: NumberContext,
        number: Int
    )
}

object Dos: Number {
    override fun checkNumber(
        numberContext: NumberContext,
        number: Int
    ) {
        when (number) {
            in 3..4 -> numberContext.changeNumber(Quatro)
            in 5..8 -> numberContext.changeNumber(Ocho)
            in 9..15 -> numberContext.changeNumber(Quince)
        }
    }
}

object Quatro: Number {
    override fun checkNumber(numberContext: NumberContext, number: Int) {
        when (number) {
            in 1..2 -> numberContext.changeNumber(Dos)
            in 5..8 -> numberContext.changeNumber(Ocho)
            in 9..15 -> numberContext.changeNumber(Quince)
        }
    }
}

object Ocho: Number {
    override fun checkNumber(
        numberContext: NumberContext,
        number: Int
    ) {
        when (number) {
            in 1..2 -> numberContext.changeNumber(Dos)
            in 3..4 -> numberContext.changeNumber(Quatro)
            in 9..15 -> numberContext.changeNumber(Quince)
        }
    }
}

object Quince: Number {
    override fun checkNumber(
        numberContext: NumberContext,
        number: Int
    ) {
        when (number) {
            in 1..2 -> numberContext.changeNumber(Dos)
            in 3..4 -> numberContext.changeNumber(Quatro)
            in 5..8 -> numberContext.changeNumber(Ocho)
        }
    }
}
