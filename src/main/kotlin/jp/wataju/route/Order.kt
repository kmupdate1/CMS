package jp.wataju.route

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.mustache.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import jp.wataju.context.Manager
import jp.wataju.context.NumberContext
import jp.wataju.context.RegionContext
import jp.wataju.model.*
import jp.wataju.model.data.ShippingBox
import jp.wataju.route.Path.DETAILS
import jp.wataju.route.Path.LIST
import jp.wataju.route.Path.ORDER
import jp.wataju.route.Path.REGISTER
import jp.wataju.route.Path.SIGNOUT
import jp.wataju.session.AccountSession
import jp.wataju.util.*
import java.time.LocalDateTime
import java.util.UUID

fun Route.order(
    databaseConfig: ApplicationConfig
) {
    route(ORDER) {
        route(REGISTER) {
            get("/{customer_id}") {
                val session = call.sessions.get() ?: AccountSession(null, null, null, null)
                if (session.id != null) {
                    try {
                        val customerId = UUID.fromString(call.parameters["customer_id"])
                        val customer = CustomerService(databaseConfig).read(customerId)
                        val products = ProductService(databaseConfig).read().filter { it!!.enabled }
                        val shippingBoxes = listOf(
                            ShippingBox.BOX_130,
                            ShippingBox.BOX_150
                        )
                        val model = mapOf(
                            "admin" to session.admin,
                            "username" to session.username,
                            "customer" to customer,
                            "products" to products,
                            "shippingBox" to shippingBoxes
                        )
                        call.respond(MustacheContent("order/order-register.hbs", model))
                    } catch (_: Exception) {
                        call.respond(HttpStatusCode.NotFound)
                    }
                } else {
                    call.respondRedirect(SIGNOUT)
                }
            }
            post("/{customer_id}") {
                val session = call.sessions.get() ?: AccountSession(null, null, null, null)
                if (session.id != null) {
                    val customerId = UUID.fromString(call.parameters["customer_id"])
                    val parameters = call.receiveParameters()
                    val prefecture = CustomerService(databaseConfig).read(customerId)!!.prefecture
                    val deliveryChargeState = parameters["delivery_charge"] ?: ""
                    val shippingBox = parameters["shipping_box"]!!.toInt()
                    val dateTime = parameters["datetime"]!!
                    val products = ProductService(databaseConfig).read()
                    val condition = OrderCondition(parameters, products)
                    val serialized = condition.serialized()

                    val manager = Manager(
                        numberContext = NumberContext(),
                        regionContext = RegionContext()
                    )
                    val deliveryCharge =
                        if (deliveryChargeState == "on") manager.deliveryCharge(condition.size, prefecture) else 0

                    try {
                        val date = formatDateTime(LocalDateTime.now())
                        val order = Order(
                            id = null,
                            customerId = customerId,
                            condition = serialized,
                            shippingBoxCharge = shippingBox,
                            deliveryCharge = deliveryCharge,
                            orderDate = formatDateTime(dateTime),
                            createDate = date,
                            updateDate = date,
                            createAccount = session.id,
                            updateAccount = session.id,
                        )
                        OrderService(databaseConfig).create(order)
                        call.respond(HttpStatusCode.OK)
                    } catch (_: Exception) {
                        call.respond(HttpStatusCode.Forbidden)
                    }
                } else {
                    call.respondRedirect(SIGNOUT)
                }

            }
        }
        route(LIST) {
            get("/{group}") {
                val session = call.sessions.get() ?: AccountSession(null, null, null, null)
                if (session.id != null) {
                    try {
                        val queryParameters = call.request.queryParameters
                        val max = queryParameters["max"]?.toInt() ?: 50
                        val customerId = queryParameters["customer_id"] ?: "all"
                        val orders = if (customerId == "all") {
                            OrderService(databaseConfig).readWithCustomer()
                        } else {
                            OrderService(databaseConfig).readWithCustomer(UUID.fromString(customerId))
                        }
                        val group = call.parameters["group"]?.toInt() ?: 1
                        val divided = DivideRecords(
                            entities = orders,
                            max = max,
                            group = group,
                        ).divided
                        val paging = Paging(
                            entities = orders,
                            max = max,
                            group = group
                        )
                        val customer = if (customerId != "all") {
                            CustomerService(databaseConfig).read(UUID.fromString(customerId))
                        } else {
                            null
                        }

                        if (paging.enabled) {
                            val model = mapOf(
                                "admin" to session.admin,
                                "username" to session.username,
                                "individual" to false,
                                "customer" to customer,
                                "is_record" to orders.isNotEmpty(),
                                "max" to max,
                                "orders" to divided,
                                "paging" to paging,
                                "queryMax" to "?max=$max",
                                "queryCustomer" to "&customer_id=$customerId"
                            )
                            call.respond(MustacheContent("order/order-list.hbs", model))
                        } else {
                            call.respond(HttpStatusCode.NotFound)
                        }
                    } catch (_: Exception) {
                        call.respond(HttpStatusCode.NotFound)
                    }
                } else {
                    call.respondRedirect(SIGNOUT)
                }
            }
            post {
                val session = call.sessions.get() ?: AccountSession(null, null, null, null)
                if (session.id != null) {
                    val receiveParameters = call.receiveParameters()
                    val max = receiveParameters["order-size"]!!.toInt()
                    val customerId = receiveParameters["customer_id"] ?: ""

                    var queryParameters = "all"
                    if (customerId != "") {
                        queryParameters = customerId
                    }

                    call.respondRedirect("$ORDER$LIST/1?max=$max&customer_id=$queryParameters")
                } else {
                    call.respondRedirect(SIGNOUT)
                }
            }
        }
        route(DETAILS) {
            get("/{customer_id}/{order_id}") {
                val session = call.sessions.get() ?: AccountSession(null, null, null, null)
                if (session.id != null) {
                    try {
                        val customerId = UUID.fromString(call.parameters["customer_id"])
                        val orderId = UUID.fromString(call.parameters["order_id"])
                        val customer = CustomerService(databaseConfig).read(customerId)!!
                        val order = OrderService(databaseConfig).read(orderId)!!

                        val orderOperator = OrderOperator(order, databaseConfig)
                        val concreteData = orderOperator.concreteData()
                        val abstractData = orderOperator.abstractData()

                        val model = mapOf(
                            "admin" to session.admin,
                            "username" to session.username,
                            "customer" to customer,
                            "order" to order,
                            "concrete_data" to concreteData,
                            "abstract_data" to abstractData,
                        )
                        call.respond(MustacheContent("order/order-details.hbs", model))
                    } catch (_: Exception) {
                        call.respond(HttpStatusCode.NotFound)
                    }
                } else {
                    call.respondRedirect(SIGNOUT)
                }
            }
        }
    }
}
