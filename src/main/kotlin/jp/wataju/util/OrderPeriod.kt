package jp.wataju.util

import io.ktor.server.config.*
import jp.wataju.model.OrderWithCustomer
import jp.wataju.model.ProductService
import jp.wataju.util.Value.TAX_108
import jp.wataju.util.Value.TAX_110
import kotlinx.serialization.json.Json
import java.util.*

class OrderPeriod(
    val orders: MutableList<OrderWithCustomer?>,
    val databaseConfig: ApplicationConfig
) {
    var totalAmounts = 0
    var totalEarning = 0
    var totalBoxCharge = 0
    var totalDeliveryCharge = 0
    suspend fun concreteOrder(): MutableList<OrderConcrete> {
        val concrete = mutableListOf<OrderConcrete>()

        orders.forEach {
            var amounts = 0
            var totals = 0
            var boxCharge = 0
            var deliveryCharge = 0
            Json.decodeFromString<List<Condition>>(it!!.ordersCondition)
                .filter { filter -> filter.amount != 0 }
                .forEach { condition ->
                    val product = ProductService(databaseConfig).read(UUID.fromString(condition.productId))!!
                    amounts += condition.amount
                    totals += product.price * condition.amount
                }

            concrete.add(
                OrderConcrete(
                    customerId = it.ordersCustomerID,
                    orderId = it.ordersId,
                    name = it.name,
                    nameKana = it.nameKana,
                    prefecture = it.prefecture,
                    ordersOrderDate = it.ordersOrderDate,
                    amounts = amounts,
                    totals = totals,
                    shippingBoxCharge = it.ordersShippingBoxCharge,
                    deliveryCharge = it.ordersDeliveryCharge
                )
            )
            this.totalAmounts += amounts
            this.totalEarning += totals
            this.totalBoxCharge += it.ordersShippingBoxCharge
            this.totalDeliveryCharge += it.ordersDeliveryCharge
        }

        return concrete
    }

    fun abstractOrder(): OrderAbstract =
        OrderAbstract(
            amounts = this.totalAmounts,
            totals = this.totalEarning,
            totalsBoxCharge = this.totalBoxCharge,
            totalsDeliveryCharge = this.totalDeliveryCharge
        )
}

data class OrderConcrete(
    val customerId: UUID,
    val orderId: UUID,
    val name: String,
    val nameKana: String,
    val prefecture: String,
    val ordersOrderDate: String,
    val amounts: Int,
    val totals: Int,
    val shippingBoxCharge: Int,
    val deliveryCharge: Int
) {
    val totalsFormatted = "%,d".format(totals)
    val totalsTaxFormatted = "%,d".format(Math.round(
        (totals * TAX_108) + (shippingBoxCharge * TAX_110) + deliveryCharge
    ))
}

data class OrderAbstract(
    val amounts: Int,
    val totals: Int,
    val totalsBoxCharge: Int,
    val totalsDeliveryCharge: Int
) {
    val totalsFormatted = "%,d".format(totals)
    val totalsTaxFormatted = "%,d".format(Math.round(
        (totals * TAX_108) + (totalsBoxCharge * TAX_110) + totalsDeliveryCharge
    ))
}
