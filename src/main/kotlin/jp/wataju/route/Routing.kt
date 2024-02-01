package jp.wataju.route

import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*

object Path {
    const val SIGNING = "/signing"
    const val SIGNOUT = "/signout"
    const val VERIFICATION = "/verification"
    const val HOME = "/home"
    const val CUSTOMER = "/customer"
    const val PRODUCT = "/product"
    const val ORDER = "/order"
    const val RECEIPT = "/receipt"
    const val SETTING = "/setting"
    const val MYPAGE = "/mypage"
    const val ACCOUNT = "/account"
    const val LIST = "/list"
    const val DETAILS = "/details"
    const val REGISTER = "/register"
    const val FILE = "/file"
    const val EDIT = "/edit"
    const val MONTHLY = "/monthly"
    const val YEARLY = "/yearly"
    const val SEARCH = "/search"
    const val NAME = "/name"
    const val PREFECTURE = "/prefecture"
    const val DELETE = "/delete"
    const val DATE = "/date"
}

fun routing(
    application: Application,
    applicationConfig: ApplicationConfig
) {
    val databaseConfig = applicationConfig.config("database")
    val emailConfig = applicationConfig.config("email")

    application.routing {
        static("/$STATIC_RESOURCE_ROOT") {
            resources(STATIC_RESOURCE_ROOT)
        }

        authentication(databaseConfig)
        verification(databaseConfig, emailConfig)
        home(databaseConfig)
        customer(databaseConfig)
        product(databaseConfig)
        order(databaseConfig)
        receipt(databaseConfig)
        setting(databaseConfig)
    }
}

const val STATIC_RESOURCE_ROOT= "contents"
