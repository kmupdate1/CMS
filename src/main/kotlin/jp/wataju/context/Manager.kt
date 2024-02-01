package jp.wataju.context

import jp.wataju.model.data.DeliveryCharge

class Manager(
    private val numberContext: NumberContext,
    private val regionContext: RegionContext
) {
    fun deliveryCharge(
        size: Int,
        prefecture: String
    ): Int {
        numberContext.setNumber(size)
        regionContext.setRegion(prefecture)

        return when (numberContext.number) {
            Dos -> when (regionContext.region) {
                AreaA -> DeliveryCharge.A_2.charge
                AreaB -> DeliveryCharge.B_2.charge
                AreaC -> DeliveryCharge.C_2.charge
                AreaD -> DeliveryCharge.D_2.charge
                AreaE -> DeliveryCharge.E_2.charge
                else -> 0
            }

            Quatro -> when (regionContext.region) {
                AreaA -> DeliveryCharge.A_4.charge
                AreaB -> DeliveryCharge.B_4.charge
                AreaC -> DeliveryCharge.C_4.charge
                AreaD -> DeliveryCharge.D_4.charge
                AreaE -> DeliveryCharge.E_4.charge
                else -> 0
            }

            Ocho -> when (regionContext.region) {
                AreaA -> DeliveryCharge.A_8.charge
                AreaB -> DeliveryCharge.B_8.charge
                AreaC -> DeliveryCharge.C_8.charge
                AreaD -> DeliveryCharge.D_8.charge
                AreaE -> DeliveryCharge.E_8.charge
                else -> 0
            }

            else -> when (regionContext.region) {
                AreaA -> DeliveryCharge.A_15.charge
                AreaB -> DeliveryCharge.B_15.charge
                AreaC -> DeliveryCharge.C_15.charge
                AreaD -> DeliveryCharge.D_15.charge
                AreaE -> DeliveryCharge.E_15.charge
                else -> 0
            }
        }
    }
}
