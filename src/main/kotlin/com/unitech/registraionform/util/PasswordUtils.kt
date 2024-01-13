package com.unitech.registraionform.util

import org.mindrot.jbcrypt.BCrypt

object PasswordUtils {

    // Hash a password using BCrypt
    fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    // Check if the entered password matches the stored hashed password
    fun checkPassword(inputPassword: String, hashedPassword: String): Boolean {
        return BCrypt.checkpw(inputPassword, hashedPassword)
    }
}