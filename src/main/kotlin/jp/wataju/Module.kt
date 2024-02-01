package jp.wataju

import com.github.mustachejava.DefaultMustacheFactory
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.mustache.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import jp.wataju.route.STATIC_RESOURCE_ROOT
import jp.wataju.session.AccountSession
import kotlin.collections.set

fun Application.module() {
    install(Sessions) {
        cookie<AccountSession>(SESSION_NAME, SessionStorageMemory()) {
            cookie.extensions["SameSite"] = "lax"
            cookie.path = "/"
        }
    }
    install(Mustache) {
        mustacheFactory = DefaultMustacheFactory("templates")
    }
    install(StatusPages) {
        status(HttpStatusCode.NotFound) { call, status ->
            this::class.java.classLoader.getResource("$STATIC_RESOURCE_ROOT/404-not-found.html")?.let {
                call.respondText(
                    it.readText(),
                    ContentType.Text.Html,
                    status
                )
            }
        }
    }

    jp.wataju.route.routing(
        application = this,
        applicationConfig = environment.config.config("user")
    )
}

const val SESSION_NAME = "ACCOUNT_SESSION"
