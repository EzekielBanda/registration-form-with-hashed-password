package com.unitech.registraionform

import com.unitech.registraionform.database.UserDatabase
import com.unitech.registraionform.model.User
import com.unitech.registraionform.util.PasswordUtils
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import javafx.stage.Stage
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

class LoginController {

    @FXML
    private lateinit var passwordField: PasswordField

    @FXML
    private lateinit var usernameField: TextField


    // Alert Box
    private lateinit var alert: Alert

    private var connectToUserDatabase = UserDatabase()

    // Database Tools
    private lateinit var connection: Connection
    private lateinit var preparedStatement: PreparedStatement
    private lateinit var resultSet: ResultSet

    fun loginButtonClicked() {
        val enteredUsername = usernameField.text
        val enteredPassword = passwordField.text

        if (enteredPassword.isEmpty() || enteredPassword.isEmpty()) {
            // Alert Box
            alert = Alert(Alert.AlertType.ERROR)
            alert.title = "Empty Fields"
            alert.headerText = null
            alert.contentText = "Please fill required Fields"
            alert.showAndWait()

        } else {
            // Retrieve the user data from the database based on the entered username
            val storedUserData = getUserByName(enteredUsername)

            if (storedUserData != null && PasswordUtils.checkPassword(enteredPassword, storedUserData.password)) {
                println("Login successful")
                alert = Alert(Alert.AlertType.INFORMATION)
                alert.title = "Login Successful"
                alert.headerText = null
                alert.contentText = "Successfully Logged in as $enteredUsername"
                alert.showAndWait()
                navigateToDashboard()
            } else {

                // Alert Box
                alert = Alert(Alert.AlertType.ERROR)
                alert.title = "Login Failed"
                alert.headerText = null
                alert.contentText = "Login failed. Incorrect username or password."
                alert.showAndWait()
                usernameField.clear()
                passwordField.clear()
                println("Login failed. Incorrect username or password.")
            }
        }

    }

    private fun getUserByName(username: String): User? {
        connection = connectToUserDatabase.connectionDb()!!

        try {
            val selectStatement = connection
                .prepareStatement("SELECT * FROM users WHERE username = ?")
            selectStatement.setString(1, username)

            resultSet = selectStatement.executeQuery()

            if (resultSet.next()) {
                val storedUsername = resultSet.getString("username")
                val storedPassword = resultSet.getString("password")

                return User(storedUsername, storedPassword)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    private fun navigateToDashboard() {
        try {
            val root: Parent = FXMLLoader.load(javaClass.getResource("dashboard.fxml"))
            val loginStage = Stage()

            loginStage.title = "Dashboard"
            loginStage.scene = Scene(root)
            loginStage.show()

            // Close the login window if needed
            val registerStage = usernameField.scene.window as Stage
            registerStage.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}