package jp.wataju.model.data

enum class ShippingBox(
    val boxName: String,
    val price: Int
) {
    BOX_130("130円ケース",130),
    BOX_150("150円ケース", 150)
}
