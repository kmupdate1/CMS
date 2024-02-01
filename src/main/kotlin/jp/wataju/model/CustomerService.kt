package jp.wataju.model

import io.ktor.server.config.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*

class CustomerService(
    databaseConfig: ApplicationConfig
): Service(Customers, databaseConfig) {

    object Customers: Table("customers") {
        val id = uuid("id").autoGenerate()
        val name = text("name")
        val nameKana = text("name_kana")
        val zipcode = varchar("zipcode", 20)
        val prefecture = varchar("prefecture", 50)
        val address1 = text("address_1")
        val address2 = text("address_2")
        val address3 = text("address_3")
        val phoneNumber = text("phone_number")
        val emailAddress = text("email_address")
        val createDate = varchar("create_date", 50)
        val updateDate = varchar("update_date", 50)
        val createAccount = uuid("create_account") references AccountService.Accounts.id
        val updateAccount = uuid("update_account") references AccountService.Accounts.id

        override val primaryKey = PrimaryKey(id)
    }

    suspend fun create(customer: Customer): UUID = dbQuery {
        Customers.insert {
            it[name] = customer.name
            it[nameKana] = customer.nameKana
            it[zipcode] = customer.zipcode
            it[prefecture] = customer.prefecture
            it[address1] = customer.address1
            it[address2] = customer.address2
            it[address3] = customer.address3
            it[phoneNumber] = customer.phoneNumber
            it[emailAddress] = customer.emailAddress
            it[createDate] = customer.createDate
            it[updateDate] = customer.updateDate
            it[createAccount] = customer.createAccount
            it[updateAccount] = customer.updateAccount
        }[Customers.id]
    }

    suspend fun read(): MutableList<Customer?> {
        val customer = mutableListOf<Customer?>()
        dbQuery {
            Customers.selectAll().orderBy(Customers.nameKana)
                .forEach {
                    customer.add(
                        Customer(
                            it[Customers.id],
                            it[Customers.name],
                            it[Customers.nameKana],
                            it[Customers.zipcode],
                            it[Customers.prefecture],
                            it[Customers.address1],
                            it[Customers.address2],
                            it[Customers.address3],
                            it[Customers.phoneNumber],
                            it[Customers.emailAddress],
                            it[Customers.createDate],
                            it[Customers.updateDate],
                            it[Customers.createAccount],
                            it[Customers.updateAccount]
                        )
                    )
                }
        }
        return customer
    }

    suspend fun read(prefecture: String): MutableList<Customer?> {
        val customer = mutableListOf<Customer?>()
        dbQuery {
            Customers.select{ Customers.prefecture eq prefecture }
                .orderBy(Customers.nameKana)
                .forEach {
                    customer.add(
                        Customer(
                            it[Customers.id],
                            it[Customers.name],
                            it[Customers.nameKana],
                            it[Customers.zipcode],
                            it[Customers.prefecture],
                            it[Customers.address1],
                            it[Customers.address2],
                            it[Customers.address3],
                            it[Customers.phoneNumber],
                            it[Customers.emailAddress],
                            it[Customers.createDate],
                            it[Customers.updateDate],
                            it[Customers.createAccount],
                            it[Customers.updateAccount]
                        )
                    )
                }
        }
        return customer
    }

    suspend fun read(id: UUID): Customer? = dbQuery {
        Customers.select{ Customers.id eq id }
            .map {
                Customer(
                    it[Customers.id],
                    it[Customers.name],
                    it[Customers.nameKana],
                    it[Customers.zipcode],
                    it[Customers.prefecture],
                    it[Customers.address1],
                    it[Customers.address2],
                    it[Customers.address3],
                    it[Customers.phoneNumber],
                    it[Customers.emailAddress],
                    it[Customers.createDate],
                    it[Customers.updateDate],
                    it[Customers.createAccount],
                    it[Customers.updateAccount]
                )
            }
            .singleOrNull()
    }

    suspend fun update(id: UUID, customer: Customer) {
        dbQuery {
            Customers.update({ Customers.id eq id }) {
                it[name] = customer.name
                it[nameKana] = customer.nameKana
                it[zipcode] = customer.zipcode
                it[prefecture] = customer.prefecture
                it[address1] = customer.address1
                it[address2] = customer.address2
                it[address3] = customer.address3
                it[phoneNumber] = customer.phoneNumber
                it[emailAddress] = customer.emailAddress
                it[updateDate] = customer.updateDate
                it[updateAccount] = customer.updateAccount
            }
        }
    }

    suspend fun delete(id: UUID) {
        dbQuery {
            Customers.deleteWhere { Customers.id eq id }
        }
    }

}

data class Customer(
    val id: UUID?,
    val name: String,
    val nameKana: String,
    val zipcode: String,
    val prefecture: String,
    val address1: String,
    val address2: String,
    val address3: String,
    val phoneNumber: String,
    val emailAddress: String,
    val createDate: String,
    val updateDate: String,
    val createAccount: UUID,
    val updateAccount: UUID
): Entity
