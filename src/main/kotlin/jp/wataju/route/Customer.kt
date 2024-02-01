package jp.wataju.route

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.mustache.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.utils.io.core.*
import jp.wataju.model.Customer
import jp.wataju.model.CustomerService
import jp.wataju.route.Path.CUSTOMER
import jp.wataju.route.Path.EDIT
import jp.wataju.route.Path.FILE
import jp.wataju.route.Path.LIST
import jp.wataju.route.Path.REGISTER
import jp.wataju.route.Path.SIGNOUT
import jp.wataju.session.AccountSession
import jp.wataju.util.*
import java.time.LocalDateTime
import java.util.*

fun Route.customer(
    databaseConfig: ApplicationConfig
) {
    route(CUSTOMER) {
        route(REGISTER) {
            get {
                val session = call.sessions.get() ?: AccountSession(null, null, null, null)
                if (session.id != null) {
                    val model = mapOf(
                        "admin" to session.admin,
                        "username" to session.username,
                    )
                    call.respond(MustacheContent("customer/customer-edit.hbs", model))
                } else {
                    call.respondRedirect(SIGNOUT)
                }
            }
            post {
                val session = call.sessions.get() ?: AccountSession(null, null, null, null)
                if (session.id != null) {
                    val date = formatDateTime(LocalDateTime.now())

                    val parameters = call.receiveParameters()
                    val name = parameters["name"]!!
                    val nameKana = parameters["name-kana"] ?: ""
                    val zipcode = parameters["zipcode"] ?: ""
                    val prefecture = parameters["prefecture"] ?: ""
                    val address1 = parameters["address-1"] ?: ""
                    val address2 = parameters["address-2"] ?: ""
                    val address3 = parameters["address-3"] ?: ""
                    val phoneNumber = parameters["phone-number"] ?: ""
                    val emailAddress = parameters["email-address"] ?: ""

                    try {
                        val customer = Customer(
                            id = null,
                            name = name,
                            nameKana = nameKana,
                            zipcode = zipcode,
                            prefecture = prefecture,
                            address1 = address1,
                            address2 = address2,
                            address3 = address3,
                            phoneNumber = phoneNumber,
                            emailAddress = emailAddress,
                            createDate = date,
                            updateDate = date,
                            createAccount = session.id,
                            updateAccount = session.id,
                        )
                        CustomerService(databaseConfig).create(customer)

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
                    val id = UUID.fromString(call.parameters["id"])
                    val date = formatDateTime(LocalDateTime.now())

                    val parameters = call.receiveParameters()
                    val name = parameters["name"]!!
                    val nameKana = parameters["name-kana"] ?: ""
                    val zipcode = parameters["zipcode"] ?: ""
                    val prefecture = parameters["prefecture"] ?: ""
                    val address1 = parameters["address-1"] ?: ""
                    val address2 = parameters["address-2"] ?: ""
                    val address3 = parameters["address-3"] ?: ""
                    val phoneNumber = parameters["phone-number"] ?: ""
                    val emailAddress = parameters["email-address"] ?: ""

                    val customer = Customer(
                        id = null,
                        name = name,
                        nameKana = nameKana,
                        zipcode = zipcode,
                        prefecture = prefecture,
                        address1 = address1,
                        address2 = address2,
                        address3 = address3,
                        phoneNumber = phoneNumber,
                        emailAddress = emailAddress,
                        createDate = "",
                        updateDate = date,
                        createAccount = UUID.randomUUID(),
                        updateAccount = session.id
                    )
                    call.respond(HttpStatusCode.OK)
                    try {
                        CustomerService(databaseConfig).update(id, customer)
                    } catch (_: Exception) {
                        call.respond(HttpStatusCode.Forbidden)
                    }
                } else {
                    call.respondRedirect(SIGNOUT)
                }
            }
            post(FILE) {
                val session = call.sessions.get() ?: AccountSession(null, null, null, null)
                if (session.id != null) {
                    call.receiveMultipart().forEachPart { part ->
                        when (part) {
                            is PartData.FileItem -> {
                                csvReader().readAllWithHeader(part.provider().readText(Charsets.UTF_8))
                                    .forEach { map ->
                                        val entryIterator = map.iterator()
                                        while (entryIterator.hasNext()) {
                                            val date = formatDateTime(LocalDateTime.now())
                                            val customer = Customer(
                                                id = null,
                                                name = entryIterator.next().value,
                                                nameKana = entryIterator.next().value,
                                                zipcode = entryIterator.next().value,
                                                prefecture = entryIterator.next().value,
                                                address1 = entryIterator.next().value,
                                                address2 = entryIterator.next().value,
                                                address3 = entryIterator.next().value,
                                                phoneNumber = entryIterator.next().value,
                                                emailAddress = entryIterator.next().value,
                                                createDate = date,
                                                updateDate = date,
                                                createAccount = session.id,
                                                updateAccount = session.id,
                                            )
                                            CustomerService(databaseConfig).create(customer)
                                        }
                                    }
                                call.respond(HttpStatusCode.OK)
                            }
                            else -> {
                                call.respond(HttpStatusCode.Forbidden)
                            }
                        }
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
                        val customer = CustomerService(databaseConfig).read()
                        val max = queryParameters["max"]?.toInt() ?: 50
                        val group = call.parameters["group"]?.toInt() ?: 1
                        val divided = DivideRecords(
                            entities = customer,
                            max = max,
                            group = group,
                        ).divided
                        val paging = Paging(
                            entities = customer,
                            max = max,
                            group = group
                        )

                        if (paging.enabled) {
                            val model = mapOf(
                                "admin" to session.admin,
                                "username" to session.username,
                                "is_record" to customer.isNotEmpty(),
                                "max" to max,
                                "customers" to divided,
                                "paging" to paging,
                                "queryMax" to "?max=$max"
                            )
                            call.respond(MustacheContent("customer/customer-list.hbs", model))
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
                    val max = call.receiveParameters()["customer-size"]!!.toInt()

                    call.respondRedirect("$CUSTOMER$LIST/1?max=$max")
                } else {
                    call.respondRedirect(SIGNOUT)
                }
            }
        }
        route(EDIT) {
            get("/{id}") {
                val session = call.sessions.get() ?: AccountSession(null, null, null, null)
                if (session.id != null) {
                    try {
                        val id = UUID.fromString(call.parameters["id"]!!)
                        val customer = CustomerService(databaseConfig).read(id)
                        if (customer != null) {
                            val model = mapOf(
                                "admin" to session.admin,
                                "username" to session.username,
                                "edit" to true,
                                "customer" to customer,
                            )
                            call.respond(MustacheContent("customer/customer-edit.hbs", model))
                        } else {
                            call.respondRedirect("$CUSTOMER$LIST/0")
                        }
                    } catch (_: Exception) {
                        call.respondRedirect("$CUSTOMER$LIST/0")
                    }
                } else {
                    call.respondRedirect(SIGNOUT)
                }
            }
        }
/*
        route(SEARCH) {
            post(NAME) {
                val session = call.sessions.get() ?: AccountSession(null, null, null, null)
                if (session.id != null) {

                } else {
                    call.respondRedirect(SIGNOUT)
                }
            }
            post(PREFECTURE) {
                val session = call.sessions.get() ?: AccountSession(null, null, null, null)
                if (session.id != null) {
                    val prefecture = call.receiveParameters()["prefecture"]

                    call.respondRedirect("$CUSTOMER$LIST/1?max=50&prefecture=$prefecture")
                } else {
                    call.respondRedirect(SIGNOUT)
                }
            }
        }
*/
    }
}
