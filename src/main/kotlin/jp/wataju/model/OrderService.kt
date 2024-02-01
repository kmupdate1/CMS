package jp.wataju.model

import io.ktor.server.config.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import java.util.*

class OrderService(
    databaseConfig: ApplicationConfig
): Service(Orders, databaseConfig) {

    object Orders: Table("orders") {
        val id = uuid("id").autoGenerate()
        val customerId = uuid("customer_id") references CustomerService.Customers.id
        val condition = largeText("condition")
        val shippingBoxCharge = integer("shipping_box_charge")
        val deliveryCharge = integer("delivery_charge")
        val orderDate = varchar("order_date", 50)
        val createDate = varchar("create_date", 50)
        val updateDate = varchar("update_date", 50)
        val createAccount = uuid("create_account") references AccountService.Accounts.id
        val updateAccount = uuid("update_account") references AccountService.Accounts.id

        override val primaryKey = PrimaryKey(id)
    }

    companion object Where {
        const val ORDERS_ID = 0
        const val ORDERS_CUSTOMER_ID = 1
    }

    suspend fun create(order: Order): UUID = dbQuery {
        Orders.insert {
            it[customerId] = order.customerId
            it[condition] = order.condition
            it[shippingBoxCharge] = order.shippingBoxCharge
            it[deliveryCharge] = order.deliveryCharge
            it[orderDate] = order.orderDate
            it[createDate] = order.createDate
            it[updateDate] = order.updateDate
            it[createAccount] = order.createAccount
            it[updateAccount] = order.updateAccount
        }[Orders.id]
    }

    suspend fun read(): MutableList<Order?> {
        val orders = mutableListOf<Order?>()
        dbQuery {
            Orders.selectAll().orderBy(Orders.orderDate)
                .forEach {
                    orders.add(
                        Order(
                            it[Orders.id],
                            it[Orders.customerId],
                            it[Orders.condition],
                            it[Orders.shippingBoxCharge],
                            it[Orders.deliveryCharge],
                            it[Orders.orderDate],
                            it[Orders.createDate],
                            it[Orders.updateDate],
                            it[Orders.createAccount],
                            it[Orders.updateAccount]
                        )
                    )
                }
        }
        return orders
    }

    suspend fun read(id: UUID, where: Int): MutableList<Order?> {
        val orders = mutableListOf<Order?>()
        dbQuery {
            Orders.select {
                when (where) {
                    ORDERS_CUSTOMER_ID -> { Orders.customerId eq id}
                    else -> { Orders.id eq id}
                }
            }.orderBy(Orders.orderDate)
                .forEach {
                    orders.add(
                        Order(
                            it[Orders.id],
                            it[Orders.customerId],
                            it[Orders.condition],
                            it[Orders.shippingBoxCharge],
                            it[Orders.deliveryCharge],
                            it[Orders.orderDate],
                            it[Orders.createDate],
                            it[Orders.updateDate],
                            it[Orders.createAccount],
                            it[Orders.updateAccount]
                        )
                    )
                }
        }
        return orders
    }

    suspend fun readWithCustomer(): MutableList<OrderWithCustomer?> {
        val orderWithCustomer = mutableListOf<OrderWithCustomer?>()
        dbQuery {
            Orders.join(CustomerService.Customers, JoinType.INNER) {
                Orders.customerId eq CustomerService.Customers.id
            }.selectAll().orderBy(Orders.orderDate)
                .forEach {
                    orderWithCustomer.add(
                        OrderWithCustomer(
                            it[CustomerService.Customers.id],
                            it[CustomerService.Customers.name],
                            it[CustomerService.Customers.nameKana],
                            it[CustomerService.Customers.zipcode],
                            it[CustomerService.Customers.prefecture],
                            it[CustomerService.Customers.address1],
                            it[CustomerService.Customers.address2],
                            it[CustomerService.Customers.address3],
                            it[CustomerService.Customers.phoneNumber],
                            it[CustomerService.Customers.emailAddress],
                            it[CustomerService.Customers.createDate],
                            it[CustomerService.Customers.updateDate],
                            it[CustomerService.Customers.createAccount],
                            it[CustomerService.Customers.updateAccount],
                            it[Orders.id],
                            it[Orders.customerId],
                            it[Orders.condition],
                            it[Orders.shippingBoxCharge],
                            it[Orders.deliveryCharge],
                            it[Orders.orderDate],
                            it[Orders.createDate],
                            it[Orders.updateDate],
                            it[Orders.createAccount],
                            it[Orders.updateAccount],
                        )
                    )
                }

        }
        return orderWithCustomer
    }

    suspend fun readWithCustomer(like: String): MutableList<OrderWithCustomer?> {
        val orderWithCustomer = mutableListOf<OrderWithCustomer?>()
        dbQuery {
            Orders.join(CustomerService.Customers, JoinType.INNER) {
                Orders.customerId eq CustomerService.Customers.id
            }.select(Orders.orderDate like like).orderBy(Orders.orderDate)
                .forEach {
                    orderWithCustomer.add(
                        OrderWithCustomer(
                            it[CustomerService.Customers.id],
                            it[CustomerService.Customers.name],
                            it[CustomerService.Customers.nameKana],
                            it[CustomerService.Customers.zipcode],
                            it[CustomerService.Customers.prefecture],
                            it[CustomerService.Customers.address1],
                            it[CustomerService.Customers.address2],
                            it[CustomerService.Customers.address3],
                            it[CustomerService.Customers.phoneNumber],
                            it[CustomerService.Customers.emailAddress],
                            it[CustomerService.Customers.createDate],
                            it[CustomerService.Customers.updateDate],
                            it[CustomerService.Customers.createAccount],
                            it[CustomerService.Customers.updateAccount],
                            it[Orders.id],
                            it[Orders.customerId],
                            it[Orders.condition],
                            it[Orders.shippingBoxCharge],
                            it[Orders.deliveryCharge],
                            it[Orders.orderDate],
                            it[Orders.createDate],
                            it[Orders.updateDate],
                            it[Orders.createAccount],
                            it[Orders.updateAccount],
                        )
                    )
                }

        }
        return orderWithCustomer
    }

    suspend fun readWithCustomer(uuid: UUID, like: String): MutableList<OrderWithCustomer?> {
        val orderWithCustomer = mutableListOf<OrderWithCustomer?>()
        dbQuery {
            Orders.join(CustomerService.Customers, JoinType.INNER) {
                Orders.customerId eq CustomerService.Customers.id
            }.select{
                Orders.customerId eq uuid
                Orders.orderDate like like
            }.orderBy(Orders.orderDate)
                .forEach {
                    orderWithCustomer.add(
                        OrderWithCustomer(
                            it[CustomerService.Customers.id],
                            it[CustomerService.Customers.name],
                            it[CustomerService.Customers.nameKana],
                            it[CustomerService.Customers.zipcode],
                            it[CustomerService.Customers.prefecture],
                            it[CustomerService.Customers.address1],
                            it[CustomerService.Customers.address2],
                            it[CustomerService.Customers.address3],
                            it[CustomerService.Customers.phoneNumber],
                            it[CustomerService.Customers.emailAddress],
                            it[CustomerService.Customers.createDate],
                            it[CustomerService.Customers.updateDate],
                            it[CustomerService.Customers.createAccount],
                            it[CustomerService.Customers.updateAccount],
                            it[Orders.id],
                            it[Orders.customerId],
                            it[Orders.condition],
                            it[Orders.shippingBoxCharge],
                            it[Orders.deliveryCharge],
                            it[Orders.orderDate],
                            it[Orders.createDate],
                            it[Orders.updateDate],
                            it[Orders.createAccount],
                            it[Orders.updateAccount],
                        )
                    )
                }

        }
        return orderWithCustomer
    }

    suspend fun read(id: UUID): Order? = dbQuery {
        Orders.select{ Orders.id eq id }
        .map {
            Order(
                it[Orders.id],
                it[Orders.customerId],
                it[Orders.condition],
                it[Orders.shippingBoxCharge],
                it[Orders.deliveryCharge],
                it[Orders.orderDate],
                it[Orders.createDate],
                it[Orders.updateDate],
                it[Orders.createAccount],
                it[Orders.updateAccount]
            )
        }
        .singleOrNull()
    }

    suspend fun readWithCustomer(customerId: UUID): MutableList<OrderWithCustomer?> {
        val orderJoinedCustomer = mutableListOf<OrderWithCustomer?>()
        dbQuery {
            Orders.join(CustomerService.Customers, JoinType.INNER) {
                Orders.customerId eq CustomerService.Customers.id
            }.select { Orders.customerId eq customerId }
                .forEach {
                    orderJoinedCustomer.add(
                        OrderWithCustomer(
                            it[CustomerService.Customers.id],
                            it[CustomerService.Customers.name],
                            it[CustomerService.Customers.nameKana],
                            it[CustomerService.Customers.zipcode],
                            it[CustomerService.Customers.prefecture],
                            it[CustomerService.Customers.address1],
                            it[CustomerService.Customers.address2],
                            it[CustomerService.Customers.address3],
                            it[CustomerService.Customers.phoneNumber],
                            it[CustomerService.Customers.emailAddress],
                            it[CustomerService.Customers.createDate],
                            it[CustomerService.Customers.updateDate],
                            it[CustomerService.Customers.createAccount],
                            it[CustomerService.Customers.updateAccount],
                            it[Orders.id],
                            it[Orders.customerId],
                            it[Orders.condition],
                            it[Orders.shippingBoxCharge],
                            it[Orders.deliveryCharge],
                            it[Orders.orderDate],
                            it[Orders.createDate],
                            it[Orders.updateDate],
                            it[Orders.createAccount],
                            it[Orders.updateAccount],
                        )
                    )
                }
        }
        return orderJoinedCustomer
    }

    suspend fun update(id: UUID, order: Order) {
        dbQuery {
            Orders.update({ Orders.id eq id }) {
                it[customerId] = order.customerId
                it[condition] = order.condition
                it[shippingBoxCharge] = order.shippingBoxCharge
                it[deliveryCharge] = order.deliveryCharge
                it[orderDate] = order.orderDate
                it[updateDate] = order.updateDate
                it[updateAccount] = order.updateAccount
            }
        }
    }

    suspend fun delete(id: UUID) {
        dbQuery {
            Orders.deleteWhere { Orders.id eq id }
        }
    }
}

data class Order(
    val id: UUID?,
    val customerId: UUID,
    val condition: String,
    val shippingBoxCharge: Int,
    val deliveryCharge: Int,
    val orderDate: String,
    val createDate: String,
    val updateDate: String,
    val createAccount: UUID,
    val updateAccount: UUID
): Entity

data class OrderWithCustomer(
    val id: UUID,
    val name: String,
    val nameKana :String,
    val zipcode :String,
    val prefecture :String,
    val address1 :String,
    val address2 :String,
    val address3 :String,
    val phoneNumber :String,
    val emailAddress :String,
    val createDate :String,
    val updateDate :String,
    val createAccount :UUID,
    val updateAccount :UUID,
    val ordersId: UUID,
    val ordersCustomerID: UUID,
    val ordersCondition: String,
    val ordersShippingBoxCharge: Int,
    val ordersDeliveryCharge: Int,
    val ordersOrderDate: String,
    val ordersCreateDate: String,
    val ordersUpdateDate :String,
    val ordersCreateAccount :UUID,
    val ordersUpdateAccount :UUID
): Entity
