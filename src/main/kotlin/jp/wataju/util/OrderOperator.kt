package jp.wataju.util

import io.ktor.server.config.*
import jp.wataju.model.Order
import jp.wataju.model.ProductService
import jp.wataju.util.Value.TAX_108
import jp.wataju.util.Value.TAX_110
import kotlinx.serialization.json.Json
import java.util.*

class OrderOperator(
    val order: Order,
    val databaseConfig: ApplicationConfig
) {
    private var amounts = 0
    private var totals = 0
    suspend fun concreteData(): MutableList<Concrete> {
        val purchaseData = mutableListOf<Concrete>()
        Json.decodeFromString<List<Condition>>(order.condition)
            .filter {
                it.amount != 0
            }
            .forEach {
                val productId = UUID.fromString(it.productId)
                val product = ProductService(databaseConfig).read(productId)!!

                val amount = it.amount
                val total = product.price * amount
                purchaseData.add(
                    Concrete(
                        productName = product.productName,
                        price = product.price,
                        amount = amount,
                        total = total,
                    )
                )

                amounts += amount
                totals  += total
            }
        return purchaseData
    }

    fun abstractData(): Abstract {
        return Abstract(
            amount = this.amounts,
            total = this.totals,
            boxCharge = order.shippingBoxCharge,
            deliveryCharge = order.deliveryCharge
        )
    }
}

data class Concrete(
    val productName: String,
    val price: Int,
    val amount: Int,
    val total: Int
) {
    val price_formatted = "%,d".format(price)
    val total_formatted = "%,d".format(total)
    val total_tax_formatted = "%,d".format(Math.round(total * TAX_108))
}

data class Abstract(
    val amount: Int,
    val total: Int,
    val boxCharge: Int,
    val deliveryCharge: Int
) {
    val total_tax = total * TAX_108
    val boxCharge_tax = boxCharge * TAX_110

    val total_formatted = "%,d".format(total)
    val boxCharge_tax_formatted = "%,d".format(Math.round(boxCharge_tax))
    val deliveryCharge_formatted = "%,d".format(deliveryCharge)
    val totals_sum_formatted = "%,d".format(Math.round(
        total_tax + boxCharge_tax + deliveryCharge.toDouble()
    ))
}
