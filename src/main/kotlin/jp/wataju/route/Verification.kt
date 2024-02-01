package jp.wataju.route

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import jp.wataju.mail.assembleEmail
import jp.wataju.model.AccountService
import jp.wataju.model.UserService
import jp.wataju.route.Path.SIGNOUT
import jp.wataju.route.Path.VERIFICATION
import jp.wataju.session.AccountSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun Route.verification(
    databaseConfig: ApplicationConfig,
    mailConfig: ApplicationConfig
) {
    route(VERIFICATION) {
        get {
            val session = call.sessions.get() ?: AccountSession(null, null, null, null)
            if (session.id == null) {
                this::class.java.classLoader.getResource("$STATIC_RESOURCE_ROOT/verification.html")?.let {
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
            val identifier = call.receiveParameters()["email"]!!
            val account = AccountService(databaseConfig).read(identifier)
            if (account != null) {
                launch(Dispatchers.IO) {
                    /* send e-mail */
                    val user = UserService(databaseConfig).read(account.id, UserService.USERS_ACCOUNT_ID)!!
                    assembleEmail(
                        account.identifier,
                        user.name,
                        mailConfig
                    )
                }
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}
