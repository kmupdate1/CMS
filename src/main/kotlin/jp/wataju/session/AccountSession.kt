package jp.wataju.session

import io.ktor.server.auth.*
import java.util.*

data class AccountSession(
    val id: UUID?,
    val identifier: String?,
    val admin: Boolean?,
    val username: String?
): Principal
