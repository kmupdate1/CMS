package jp.wataju.route

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.mustache.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import jp.wataju.model.OrderService
import jp.wataju.route.Path.MONTHLY
import jp.wataju.route.Path.RECEIPT
import jp.wataju.route.Path.SIGNOUT
import jp.wataju.route.Path.YEARLY
import jp.wataju.session.AccountSession
import jp.wataju.util.OrderPeriod
import java.time.LocalDateTime
import java.util.*

fun Route.receipt(
    databaseConfig: ApplicationConfig
) {
    route(RECEIPT) {
        route(YEARLY) {
            get {
                val session = call.sessions.get() ?: AccountSession(null, null, null, null)
                if (session.id != null) {
                    try {
                        val date = LocalDateTime.now().toString().split("-")

                        val query = call.request.queryParameters
                        val year = query["year"] ?: date[0]
                        val yearly = "${year}年%"

                        val customerId = query["customer_id"] ?: "all"
                        val orders = if (customerId == "all") {
                            OrderService(databaseConfig).readWithCustomer(yearly)
                        } else {
                            OrderService(databaseConfig).readWithCustomer(UUID.fromString(customerId), yearly)
                        }
                        val orderPeriod = OrderPeriod(
                            orders = orders,
                            databaseConfig = databaseConfig
                        )

                        val model = mapOf(
                            "admin" to session.admin,
                            "username" to session.username,
                            "condition" to "年間",
                            "yearly" to true,
                            "is_record" to orders.isNotEmpty(),
                            "concrete_data" to orderPeriod.concreteOrder(),
                            "abstract_data" to orderPeriod.abstractOrder()
                        )
                        call.respond(MustacheContent("receipt/receipt-list.hbs", model))
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
                    val year = call.receiveParameters()["year"]

                    call.respondRedirect("$RECEIPT$YEARLY?year=$year")
                } else {
                    call.respondRedirect(SIGNOUT)
                }
            }
        }
        route(MONTHLY) {
            get {
                val session = call.sessions.get() ?: AccountSession(null, null, null, null)
                if (session.id != null) {
                    try {
                        val date = LocalDateTime.now().toString().split("-")

                        val query = call.request.queryParameters
                        val month = query["month"] ?: "${date[0]}-${date[1]}"
                        val querySplit = month.split("-")
                        val monthly = "${querySplit[0]}年${querySplit[1]}月%"


                        val customerId = query["customer_id"] ?: "all"
                        val orders = if (customerId == "all") {
                            OrderService(databaseConfig).readWithCustomer(monthly)
                        } else {
                            OrderService(databaseConfig).readWithCustomer(UUID.fromString(customerId), monthly)
                        }
                        val orderPeriod = OrderPeriod(
                            orders = orders,
                            databaseConfig = databaseConfig
                        )

                        val model = mapOf(
                            "admin" to session.admin,
                            "username" to session.username,
                            "condition" to "月間",
                            "yearly" to false,
                            "is_record" to orders.isNotEmpty(),
                            "concrete_data" to orderPeriod.concreteOrder(),
                            "abstract_data" to orderPeriod.abstractOrder()
                        )
                        call.respond(MustacheContent("receipt/receipt-list.hbs", model))
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
                    val year = 2024
                    val month = call.receiveParameters()["month"]

                    call.respondRedirect("$RECEIPT$MONTHLY?month=$year-$month")
                } else {
                    call.respondRedirect(SIGNOUT)
                }
            }
        }
    }
}
