package jp.wataju.util

import io.ktor.http.*
import jp.wataju.model.Product
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class OrderCondition(
    private val receiveParameters: Parameters,
    private val products: MutableList<Product?>
) {
    var size = 0
    fun serialized(): String {
        val conditions = mutableListOf<Condition>()
        products.forEach { product ->
            if (product != null) {
                val strID = product.id.toString()
                val strAmount = receiveParameters[strID] ?: ""
                val amount =  if (strAmount != "") strAmount.toInt() else 0
                conditions.add(
                    Condition(
                        productId = product.id.toString(),
                        amount = amount
                    )
                )
                this.size += amount
            }
        }
        return Json.encodeToString(conditions)
    }
}

@Serializable
data class Condition(
    @SerialName("product_id") val productId: String,
    @SerialName("amount") val amount: Int,
)
