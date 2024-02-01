package jp.wataju.route

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.mustache.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import jp.wataju.model.Product
import jp.wataju.model.ProductService
import jp.wataju.route.Path.EDIT
import jp.wataju.route.Path.LIST
import jp.wataju.route.Path.PRODUCT
import jp.wataju.route.Path.REGISTER
import jp.wataju.route.Path.SIGNOUT
import jp.wataju.session.AccountSession
import jp.wataju.util.*
import java.time.LocalDateTime
import java.util.*

fun Route.product(
    databaseConfig: ApplicationConfig
) {
    route(PRODUCT) {
        route(REGISTER) {
            get {
                val session = call.sessions.get() ?: AccountSession(null, null, null, null)
                if (session.id != null) {
                    if (session.admin!!) {
                        val model = mapOf(
                            "admin" to session.admin,
                            "username" to session.username,
                            "edit" to false,
                        )
                        call.respond(MustacheContent("product/product-edit.hbs", model))
                    } else {
                        call.respondRedirect("$PRODUCT$LIST/1")
                    }
                } else {
                    call.respondRedirect(SIGNOUT)
                }
            }
            post {
                val session = call.sessions.get() ?: AccountSession(null, null, null, null)
                if (session.id != null) {
                    val date = formatDateTime(LocalDateTime.now())

                    val parameters = call.receiveParameters()
                    val name = parameters["product-name"]!!
                    val nameKana = parameters["product-name-kana"] ?: ""
                    val price = parameters["price"]!!
                    val condition = parameters["condition"] == "on"

                    try {
                        val product = Product(
                            id = null,
                            productName = name,
                            productNameKana = nameKana,
                            price = price.toInt(),
                            enabled = condition,
                            createDate = date,
                            updateDate = date,
                            createAccount = session.id,
                            updateAccount = session.id
                        )
                        ProductService(databaseConfig).create(product)

                        call.respond(HttpStatusCode.OK)
                    } catch (_: Exception) {
                        call.respond(HttpStatusCode.Forbidden)
                    }
                } else {
                    call.respondRedirect(SIGNOUT)
                }
            }
            post("/{id}") {
                val session = call.sessions.get() ?: AccountSession(null, null, null, null)
                if (session.id != null) {
                    try {
                        val id = UUID.fromString(call.parameters["id"])
                        val date = formatDateTime(LocalDateTime.now())

                        val parameters = call.receiveParameters()
                        val productName = parameters["product-name"]!!
                        val productNameKana = parameters["product-name-kana"] ?: ""
                        val price = parameters["price"]!!.toInt()
                        val enabled = parameters["condition"] == "on"

                        val product = Product(
                            id = null,
                            productName = productName,
                            productNameKana = productNameKana,
                            price = price,
                            enabled = enabled,
                            createDate = date,
                            updateDate = date,
                            createAccount = session.id,
                            updateAccount = session.id,
                            )
                        ProductService(databaseConfig).update(id,product)

                        call.respond(HttpStatusCode.OK)
                    } catch (_: Exception) {
                        call.respond(HttpStatusCode.Forbidden)
                    }
                } else {
                    call.respondRedirect(SIGNOUT)
                }
            }
        }
        route(EDIT) {
            get("/{id}") {
                val session = call.sessions.get() ?: AccountSession(null, null, null, null)
                if (session.id != null) {
                    if (session.admin!!) {
                        try {
                            val id = UUID.fromString(call.parameters["id"])
                            val product = ProductService(databaseConfig).read(id)
                            val model = mapOf(
                                "admin" to session.admin,
                                "username" to session.username,
                                "edit" to true,
                                "product" to product,
                            )
                            call.respond(MustacheContent("product/product-edit.hbs", model))
                        } catch (_: Exception) {
                            call.respondRedirect("$PRODUCT$LIST/1")
                        }
                    } else {
                        call.respondRedirect("$PRODUCT$LIST/1")
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
                        val product = ProductService(databaseConfig).read()
                        val max = queryParameters["max"]?.toInt() ?: 50
                        val group = call.parameters["group"]?.toInt() ?: 1
                        val divided = DivideRecords(
                            entities = product,
                            max = max,
                            group = group,
                        ).divided
                        val paging = Paging(
                            entities = product,
                            max = max,
                            group = group
                        )

                        if (paging.enabled) {
                            val model = mapOf(
                                "admin" to session.admin,
                                "username" to session.username,
                                "is_record" to product.isNotEmpty(),
                                "max" to max,
                                "products" to divided,
                                "paging" to paging,
                                "queryMax" to "?max=$max"
                            )
                            call.respond(MustacheContent("product/product-list.hbs", model))
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
                    val max = call.receiveParameters()["product-size"]!!.toInt()

                    call.respondRedirect("$PRODUCT$LIST/1?max=$max")
                } else {
                    call.respondRedirect(SIGNOUT)
                }
            }
        }
    }
}
