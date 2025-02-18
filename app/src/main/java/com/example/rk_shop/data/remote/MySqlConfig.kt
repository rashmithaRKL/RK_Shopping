package com.example.rk_shop.data.remote

import java.sql.Connection
import java.sql.DriverManager

object MySqlConfig {
    private const val DATABASE_URL = "jdbc:mysql://your_mysql_host:3306/rk_shopping"
    private const val DATABASE_USER = "your_username"
    private const val DATABASE_PASSWORD = "your_password"

    fun getConnection(): Connection {
        Class.forName("com.mysql.jdbc.Driver")
        return DriverManager.getConnection(
            DATABASE_URL,
            DATABASE_USER,
            DATABASE_PASSWORD
        )
    }
}
