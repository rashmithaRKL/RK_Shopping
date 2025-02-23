package com.example.rk_shop.data.remote

import java.sql.Connection
import java.sql.DriverManager

object MySqlConfig {
    private const val DATABASE_URL = "jdbc:mysql://root:3306/rk_shopping"
    private const val DATABASE_USER = "root"
    private const val DATABASE_PASSWORD = ""

    fun getConnection(): Connection {
        Class.forName("com.mysql.jdbc.Driver")
        return DriverManager.getConnection(
            DATABASE_URL,
            DATABASE_USER,
            DATABASE_PASSWORD
        )
    }
}
