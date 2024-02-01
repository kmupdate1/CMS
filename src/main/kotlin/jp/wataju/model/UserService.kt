package jp.wataju.model

import io.ktor.server.config.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*

class UserService(
    databaseConfig: ApplicationConfig
): Service(Users, databaseConfig) {

    object Users : Table("users") {
        val id = uuid("id").autoGenerate()
        val accountId = uuid("account_id") references AccountService.Accounts.id
        val name = text("name")
        val nameKana = text("name_kana")
        val phoneNumber = text("phone_number")
        val emailAddress = text("email_address")
        val createDate = varchar("create_date", 50)
        val updateDate = varchar("update_date", 50)
        val createAccount = uuid("create_account") references AccountService.Accounts.id
        val updateAccount = uuid("update_account") references AccountService.Accounts.id

        override val primaryKey = PrimaryKey(id)
    }

    companion object Where {
        const val USERS_ID = 0
        const val USERS_ACCOUNT_ID = 1
        const val USERS_CREATE_ACCOUNT = 10
        const val USERS_UPDATE_ACCOUNT = 11
    }

    suspend fun create(user: User): UUID = dbQuery {
        Users
            .insert {
                it[accountId] = user.accountId
                it[name] = user.name
                it[nameKana] = user.nameKana
                it[phoneNumber] = user.phoneNumber
                it[emailAddress] = user.emailAddress
                it[createDate] = user.createDate
                it[updateDate] = user.updateDate
                it[createAccount] = user.createAccount
                it[updateAccount] = user.updateAccount
            }[Users.id]
    }

    suspend fun read(): MutableList<User?> {
        val users = mutableListOf<User?>()
        dbQuery {
            Users.selectAll().orderBy(Users.nameKana)
                .forEach {
                    users.add(
                        User(
                            it[Users.id],
                            it[Users.accountId],
                            it[Users.name],
                            it[Users.nameKana],
                            it[Users.phoneNumber],
                            it[Users.emailAddress],
                            it[Users.createDate],
                            it[Users.updateDate],
                            it[Users.createAccount],
                            it[Users.updateAccount]
                        )
                    )
                }
        }
        return users
    }

    suspend fun read(uuid: UUID, where: Int): User? = dbQuery {
        Users.select {
            when (where) {
                USERS_ACCOUNT_ID -> {
                    Users.accountId eq uuid
                }

                USERS_CREATE_ACCOUNT -> {
                    Users.createAccount eq uuid
                }

                USERS_UPDATE_ACCOUNT -> {
                    Users.updateAccount eq uuid
                }

                else -> {
                    Users.id eq uuid
                }
            }
        }.map {
            User(
                it[Users.id],
                it[Users.accountId],
                it[Users.name],
                it[Users.nameKana],
                it[Users.phoneNumber],
                it[Users.emailAddress],
                it[Users.createDate],
                it[Users.updateDate],
                it[Users.createAccount],
                it[Users.updateAccount]
            )
        }.singleOrNull()
    }

    suspend fun readWithAccounts(): MutableList<UserWithAccount?> {
        val accountsAndUsers = mutableListOf<UserWithAccount?>()
        dbQuery {
            Users.join(AccountService.Accounts, JoinType.FULL) {
                Users.accountId eq AccountService.Accounts.id
            }.selectAll().orderBy(AccountService.Accounts.identifier)
                .forEach {
                    accountsAndUsers.add(
                        UserWithAccount(
                            it[AccountService.Accounts.id],
                            it[AccountService.Accounts.identifier],
                            it[AccountService.Accounts.password],
                            it[AccountService.Accounts.admin],
                            it[AccountService.Accounts.createDate],
                            it[AccountService.Accounts.updateDate],
                            it[AccountService.Accounts.updateAccount],
                            it[AccountService.Accounts.updateAccount],
                            it[Users.id],
                            it[Users.name],
                            it[Users.nameKana],
                            it[Users.phoneNumber],
                            it[Users.emailAddress],
                            it[Users.createDate],
                            it[Users.updateDate],
                            it[Users.createAccount],
                            it[Users.updateAccount]
                        )
                    )
                }
        }
        return accountsAndUsers
    }

    suspend fun readWithAccounts(uuid: UUID) = dbQuery {
        Users.join(AccountService.Accounts, JoinType.INNER) {
            Users.accountId eq AccountService.Accounts.id
        }.select { AccountService.Accounts.id eq uuid }
            .map {
                UserWithAccount(
                    it[AccountService.Accounts.id],
                    it[AccountService.Accounts.identifier],
                    it[AccountService.Accounts.password],
                    it[AccountService.Accounts.admin],
                    it[AccountService.Accounts.createDate],
                    it[AccountService.Accounts.updateDate],
                    it[AccountService.Accounts.updateAccount],
                    it[AccountService.Accounts.updateAccount],
                    it[Users.id],
                    it[Users.name],
                    it[Users.nameKana],
                    it[Users.phoneNumber],
                    it[Users.emailAddress],
                    it[Users.createDate],
                    it[Users.updateDate],
                    it[Users.createAccount],
                    it[Users.updateAccount]
                )
            }
            .singleOrNull()
    }

    suspend fun update(id: UUID, user: User, where: Int) {
        dbQuery {
            Users.update(
                 if (where == USERS_ID) {
                     { Users.id eq id }
                 } else {
                     {Users.accountId eq id }
                 }) {
                it[name] = user.name
                it[nameKana] = user.nameKana
                it[phoneNumber] = user.phoneNumber
                it[emailAddress] = user.emailAddress
                it[updateDate] = user.updateDate
                it[updateAccount] = user.updateAccount
            }
        }
    }

    suspend fun delete(uuid: UUID, where: Int) {
        dbQuery {
            Users.deleteWhere {
                when (where) {
                    USERS_ACCOUNT_ID -> { accountId eq uuid}
                    USERS_CREATE_ACCOUNT -> { createAccount eq uuid }
                    USERS_UPDATE_ACCOUNT -> { updateAccount eq uuid }
                    else -> { id eq uuid }
                }
            }
        }
    }
}

data class User(
    val id: UUID,
    val accountId: UUID,
    val name: String,
    val nameKana: String,
    val phoneNumber: String,
    val emailAddress: String,
    val createDate: String,
    val updateDate: String,
    val createAccount: UUID,
    val updateAccount: UUID
): Entity

data class UserWithAccount(
    val id: UUID,
    val identifier: String,
    val password: String,
    val admin: Boolean,
    val createDate: String,
    val updateDate: String,
    val createAccount: UUID,
    val updateAccount: UUID,
    val usersId: UUID,
    val usersName: String,
    val usersNameKana: String,
    val usersPhoneNumber: String,
    val usersEmailAddress: String,
    val usersCreateDate: String,
    val usersUpdateDate: String,
    val usersCreateAccount: UUID,
    val usersUpdateAccount: UUID
): Entity {
    val authority = if (admin) "管理者" else "一般"
    val checked = admin
}
