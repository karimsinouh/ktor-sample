package com.example.routes.users.data

import com.example.routes.users.model.UserModel
import com.example.routes.users.model.UsersTable
import com.example.routes.users.model.toUser
import com.example.routes.users.model.toUserOrNull
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class UsersDAO {

    fun insertUser(user:UserModel):Int{
        return transaction {
            UsersTable.insert {
                it[name]=user.name
                it[phoneNumber]=user.phoneNumber
                it[email]=user.email?:""
                it[feedbackCollected]=user.feedbackCollected?:false
                it[note]=user.note?:""
            }[UsersTable.id]
        }
    }

    fun update(user: UserModel){
        return transaction {
            UsersTable.update({ UsersTable.id eq (user.id?:0) }) {
                it[name]=user.name
                it[phoneNumber]=user.phoneNumber
                it[email]=user.email?:""
                it[feedbackCollected]=user.feedbackCollected?:false
                it[note]=user.note?:""
            }
        }
    }

    fun getUserByPhoneNumber(phoneNumber:String):UserModel?{
        return transaction {
            UsersTable.selectAll()
                .where(UsersTable.phoneNumber eq phoneNumber)
                .singleOrNull()
                ?.toUserOrNull()
        }
    }

    fun getAllUsers():List<UserModel>{
        return transaction {
            UsersTable.selectAll()
                .orderBy(UsersTable.id to SortOrder.DESC)
                .limit(50)
                .map {
                    it.toUser()
                }
        }
    }

    fun deleteByPhoneNumber(phoneNumber: String){
        return transaction {
            UsersTable.deleteWhere {
                UsersTable.phoneNumber eq phoneNumber
            }
        }
    }

}