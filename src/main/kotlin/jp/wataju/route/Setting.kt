package jp.wataju.route

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.mustache.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import jp.wataju.model.Account
import jp.wataju.model.AccountService
import jp.wataju.model.User
import jp.wataju.model.UserService
import jp.wataju.model.UserService.Where.USERS_ACCOUNT_ID
import jp.wataju.route.Path.ACCOUNT
import jp.wataju.route.Path.DETAILS
import jp.wataju.route.Path.EDIT
import jp.wataju.route.Path.LIST
import jp.wataju.route.Path.MYPAGE
import jp.wataju.route.Path.REGISTER
import jp.wataju.route.Path.SETTING
import jp.wataju.route.Path.SIGNOUT
import jp.wataju.session.AccountSession
import jp.wataju.util.*
import java.time.LocalDateTime
import java.util.*

fun Route.setting(
    databaseConfig: ApplicationConfig
) {
    route(SETTING) {
        route(MYPAGE) {
            route(EDIT) {
                get("/{id}") {
                    val session = call.sessions.get() ?: AccountSession(null, null, null, null)
                    if (session.id != null) {
                        val id = UUID.fromString(call.parameters["id"])
                        val user = UserService(databaseConfig).read(id, USERS_ACCOUNT_ID)
                        val model = mapOf(
                            "admin" to session.admin,
                            "username" to session.username,
                            "user" to user,
                        )
                        call.respond(MustacheContent("setting/mypage-edit.hbs", model))
                    } else {
                        call.respondRedirect(SIGNOUT)
                    }

                }
                post("/{id}") {
                    val session = call.sessions.get() ?: AccountSession(null, null, null, null)
                    if (session.id != null) {
                        val id = UUID.fromString(call.parameters["id"])
                        val parameters = call.receiveParameters()
                        val name = parameters["name"] ?: ""
                        val nameKana = parameters["name-kana"] ?: ""
                        val phoneNumber = parameters["phone-number"] ?: ""
                        val emailAddress = parameters["email-address"] ?: ""
                        val uuid = UUID.randomUUID()
                        val date = formatDateTime(LocalDateTime.now())

                        try {
                            val user = User(
                                id = uuid,
                                accountId = uuid,
                                name = name,
                                nameKana = nameKana,
                                phoneNumber = phoneNumber,
                                emailAddress = emailAddress,
                                createDate = "",
                                updateDate = date,
                                createAccount = uuid,
                                updateAccount = session.id
                            )
                            UserService(databaseConfig).update(id, user, USERS_ACCOUNT_ID)
                            call.respond(HttpStatusCode.OK)
                        } catch (_: Exception) {
                            call.respond(HttpStatusCode.Forbidden)
                        }
                    } else {
                        call.respondRedirect(SIGNOUT)
                    }
                }
            }
            get {
                val session = call.sessions.get() ?: AccountSession(null, null, null, null)
                if (session.id != null) {
                    val userAndAccount = UserService(databaseConfig).readWithAccounts(session.id)
                    val model = mapOf(
                        "admin" to session.admin,
                        "username" to session.username,
                        "user" to userAndAccount,
                    )
                    call.respond(MustacheContent("setting/mypage.hbs", model))
                } else {
                    call.respondRedirect(SIGNOUT)
                }
            }
        }
        route(ACCOUNT) {
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
                            call.respond(MustacheContent("setting/account-edit.hbs", model))
                        } else {
                            call.respondRedirect("$SETTING$MYPAGE")
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
                        val identifier = parameters["identifier"]!!
                        val password = parameters["password"]!!
                        val name = parameters["name"]!!
                        val nameKana = parameters["name-kana"] ?: ""
                        val phoneNumber = parameters["phone-number"] ?: ""
                        val emailAddress = parameters["email-address"] ?: ""
                        val admin = parameters["admin"] == "on"

                        val account = AccountService(databaseConfig).read(identifier)
                        if (account == null) {
                            try {
                                val idForAccounts = UUID.randomUUID()
                                val accounts = Account(
                                    id = idForAccounts,
                                    identifier = identifier,
                                    password = password,
                                    admin = admin,
                                    createDate = date,
                                    updateDate = date,
                                    createAccount = session.id,
                                    updateAccount = session.id
                                )

                                val idForUsers = UUID.randomUUID()
                                val users = User(
                                    id = idForUsers,
                                    accountId = idForAccounts,
                                    name = name,
                                    nameKana = nameKana,
                                    phoneNumber = phoneNumber,
                                    emailAddress = emailAddress,
                                    createDate = date,
                                    updateDate = date,
                                    createAccount = session.id,
                                    updateAccount = session.id,
                                )
                                AccountService(databaseConfig).create(accounts)
                                UserService(databaseConfig).create(users)

                                call.respond(HttpStatusCode.OK)
                            } catch (_: Exception) {
                                call.respond(HttpStatusCode.Forbidden)
                            }
                        } else {
                            call.respond(HttpStatusCode.Unauthorized)
                        }
                    } else {
                        call.respondRedirect(SIGNOUT)
                    }
                }
            }
            route(EDIT) {
                route(ACCOUNT) {
                    get("/{id}") {
                        val session = call.sessions.get() ?: AccountSession(null, null, null, null)
                        if (session.id != null) {
                            if (session.admin!!) {
                                val id = UUID.fromString(call.parameters["id"])
                                val usersJoinAccounts = UserService(databaseConfig).readWithAccounts(id)

                                val model = mapOf(
                                    "admin" to session.admin,
                                    "username" to session.username,
                                    "edit" to true,
                                    "info" to usersJoinAccounts,
                                )
                                call.respond(MustacheContent("setting/account-edit.hbs", model))
                            } else {
                                call.respondRedirect("$SETTING$MYPAGE")
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
                            val identifier = parameters["identifier"]!!
                            val password = parameters["password"]!!
                            val name = parameters["name"]!!
                            val nameKana = parameters["name-kana"] ?: ""
                            val phoneNumber = parameters["phone-number"] ?: ""
                            val emailAddress = parameters["email-address"] ?: ""
                            val admin = parameters["admin"] == "on"

                            try {
                                val idForAccounts = UUID.randomUUID()
                                val accounts = Account(
                                    id = idForAccounts,
                                    identifier = identifier,
                                    password = password,
                                    admin = admin,
                                    createDate = date,
                                    updateDate = date,
                                    createAccount = session.id,
                                    updateAccount = session.id
                                )

                                val idForUsers = UUID.randomUUID()
                                val users = User(
                                    id = idForUsers,
                                    accountId = idForAccounts,
                                    name = name,
                                    nameKana = nameKana,
                                    phoneNumber = phoneNumber,
                                    emailAddress = emailAddress,
                                    createDate = date,
                                    updateDate = date,
                                    createAccount = session.id,
                                    updateAccount = session.id,
                                )
                                AccountService(databaseConfig).update(id, accounts)
                                UserService(databaseConfig).update(id, users, USERS_ACCOUNT_ID)

                                call.respond(HttpStatusCode.OK)
                            } catch (_: Exception) {
                                call.respond(HttpStatusCode.Forbidden)
                            }
                        } else {
                            call.respondRedirect(SIGNOUT)
                        }
                    }
                }
            }
            route(LIST) {
                get("/{group}") {
                    val session = call.sessions.get() ?: AccountSession(null, null, null, null)
                    if (session.id != null) {
                        if (session.admin!!) {
                            try {
                                val queryParameters = call.request.queryParameters
                                val usersJoinAccounts = UserService(databaseConfig).readWithAccounts()
                                val max = queryParameters["max"]?.toInt() ?: 50
                                val group = call.parameters["group"]?.toInt() ?: 1
                                val divided = DivideRecords(
                                    entities = usersJoinAccounts,
                                    max = max,
                                    group = group,
                                ).divided
                                val paging = Paging(
                                    entities = usersJoinAccounts,
                                    max = max,
                                    group = group
                                )

                                val model = mapOf(
                                    "admin" to session.admin,
                                    "username" to session.username,
                                    "max" to max,
                                    "users" to divided,
                                    "paging" to paging,
                                    "queryMax" to "?max=$max"
                                )
                                call.respond(MustacheContent("setting/account-list.hbs", model))
                            } catch (_: Exception) {
                                call.respond(HttpStatusCode.NotFound)
                            }
                        } else {
                            call.respondRedirect("$SETTING$MYPAGE")
                        }
                    } else {
                        call.respondRedirect(SIGNOUT)
                    }
                }
                post {
                    val session = call.sessions.get() ?: AccountSession(null, null, null, null)
                    if (session.id != null) {
                        if (session.admin!!) {
                            val max = call.receiveParameters()["user-size"]!!.toInt()

                            call.respondRedirect("$SETTING$ACCOUNT$LIST/1?max=$max")
                        } else {
                            call.respondRedirect("$SETTING$MYPAGE")
                        }
                    } else {
                        call.respondRedirect(SIGNOUT)
                    }
                }
            }
            route(DETAILS) {
                get("/{id}") {
                    val session = call.sessions.get() ?: AccountSession(null, null, null, null)
                    if (session.id != null) {
                        if (session.admin!!) {
                            try {
                                val id = UUID.fromString(call.parameters["id"]!!)
                                val info = UserService(databaseConfig).readWithAccounts(id)
                                if (info != null) {
                                    val model = mapOf(
                                        "admin" to session.admin,
                                        "username" to session.username,
                                        "info" to info,
                                        "this" to (id == session.id),
                                    )
                                    call.respond(MustacheContent("setting/account-details.hbs", model))
                                } else {
                                    call.respondRedirect("$SETTING$ACCOUNT$LIST/0")
                                }
                            } catch (_: Exception) {
                                call.respondRedirect("$SETTING$ACCOUNT$LIST/0")
                            }
                        } else {
                            call.respondRedirect("$SETTING$MYPAGE")
                        }
                    } else {
                        call.respondRedirect(SIGNOUT)
                    }
                }
            }
        }
    }
}
