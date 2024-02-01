package jp.wataju.route

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import jp.wataju.model.Account
import jp.wataju.model.AccountService
import jp.wataju.model.User
import jp.wataju.model.UserService
import jp.wataju.route.Path.REGISTER
import jp.wataju.route.Path.SIGNING
import jp.wataju.route.Path.SIGNOUT
import jp.wataju.session.AccountSession
import jp.wataju.util.formatDateTime
import java.time.LocalDateTime
import java.util.*

fun Route.authentication(
    databaseConfig: ApplicationConfig
) {
    route(SIGNING) {
        get {
            val session = call.sessions.get() ?: AccountSession(null, null, null, null)
            if (session.id == null) {
                this::class.java.classLoader.getResource("$STATIC_RESOURCE_ROOT/authentication.html")?.let {
                    call.respondText(
                        it.readText(Charsets.UTF_8),
                        ContentType.Text.Html
                    )
                }
            } else {
                call.respondRedirect(SIGNOUT)
            }
        }
        post {
            val parameters = call.receiveParameters()
            val identifier = parameters["email"]!!
            val password = parameters["password"]!!

            val account = AccountService(databaseConfig).read(identifier)
            if (account != null) {
                if (account.password == password) {
                    val user = UserService(databaseConfig).read(account.id, UserService.USERS_ACCOUNT_ID)!!
                    call.sessions.set(
                        AccountSession(
                            id = account.id,
                            identifier = account.identifier,
                            admin = account.admin,
                            username = user.name
                        )
                    )
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.Unauthorized)
                }
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }
    }
    post(REGISTER) {
            val date = formatDateTime(LocalDateTime.now())

            val parameters = call.receiveParameters()
            val userName = parameters["username"]!!
            val identifier = parameters["email"]!!
            val password = parameters["password"]!!

            val account = AccountService(databaseConfig).read(identifier)
            if (account == null) {
                val idForAccounts = UUID.randomUUID()
                val accounts = Account(
                    id = idForAccounts,
                    identifier = identifier,
                    password = password,
                    admin = false,
                    createDate = date,
                    updateDate = date,
                    createAccount = idForAccounts,
                    updateAccount = idForAccounts,
                )

                val idForUsers = UUID.randomUUID()
                val users = User(
                    id = idForUsers,
                    accountId = idForAccounts,
                    name = userName,
                    nameKana = "",
                    phoneNumber = "",
                    emailAddress = "",
                    createDate = date,
                    updateDate = date,
                    createAccount = idForAccounts,
                    updateAccount = idForAccounts,
                )

                AccountService(databaseConfig).create(accounts)
                UserService(databaseConfig).create(users)

                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }
    get("/") {
        val session = call.sessions.get() ?: AccountSession(null, null, null, null)
        if (session.id == null) {
            call.respondRedirect(SIGNING)
        } else {
            call.respondRedirect(SIGNOUT)
        }
    }
    get(SIGNOUT) {
        call.sessions.set(AccountSession(null, null, null, null))
        call.respondRedirect(SIGNING)
    }
}
