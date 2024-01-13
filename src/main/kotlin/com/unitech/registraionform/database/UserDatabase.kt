package com.unitech.registraionform.database

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class UserDatabase {

    fun connectionDb(): Connection? {
        try {
            Class.forName("org.postgresql.Driver")
            return DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/userdata",
                "postgres", "12345")
        } catch (ex: SQLException) {
            ex.printStackTrace()
        }
        println("Connected to database")
        return null
    }
}