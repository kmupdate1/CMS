package jp.wataju.route

import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.mustache.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import jp.wataju.route.Path.HOME
import jp.wataju.route.Path.SIGNOUT
import jp.wataju.session.AccountSession

fun Route.home(databaseConfig: ApplicationConfig) {
    get(HOME) {
        val session = call.sessions.get() ?: AccountSession(null, null, null, null)
        if (session.id != null) {
            val model = mapOf(
                "admin" to session.admin,
                "username" to session.username,
            )
            call.respond(MustacheContent("home.hbs", model))
        } else {
            call.respondRedirect(SIGNOUT)
        }
    }
}
