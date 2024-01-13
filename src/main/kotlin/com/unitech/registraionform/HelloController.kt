package com.unitech.registraionform

import com.unitech.registraionform.database.UserDatabase
import com.unitech.registraionform.model.User
import com.unitech.registraionform.util.PasswordUtils
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.input.KeyCode
import javafx.stage.Stage
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class HelloController {

    @FXML
    private lateinit var signIn: Label

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


    @FXML
    fun initialize() {
        // Set event handler for the username field
        usernameField.onKeyPressed = EventHandler { event ->
            if (event.code == KeyCode.TAB) {
                passwordField.requestFocus()
                event.consume() // Consume the event to prevent default behavior
            }
        }

        // Set event handler for the password field
        passwordField.onKeyPressed = EventHandler { event ->
            if (event.code == KeyCode.TAB) {
                // Move focus to the next field or perform any other desired action
                // For example, you can add logic to move to the next form element.
                event.consume() // Consume the event to prevent default behavior
            }
        }
    }
    @FXML
    fun registerButtonClicked() {
        val username = usernameField.text
        val originalPassword = passwordField.text
        val hashedPassword = PasswordUtils.hashPassword(originalPassword)

        connection = connectToUserDatabase.connectionDb()!!
        try {
            if (username.isEmpty() || originalPassword.isEmpty()) {

                // Alert Box
                alert = Alert(Alert.AlertType.ERROR)
                alert.title = "Empty Fields"
                alert.headerText = null
                alert.contentText = "Please fill required Fields"
                alert.showAndWait()
            } else {
                // Prevent Addition of the same School Type
                val userCheck = "SELECT username FROM users WHERE username ='${username}'"
                preparedStatement = connection.prepareStatement(userCheck)
                resultSet = preparedStatement.executeQuery()
                if (resultSet.next()) {

                    alert = Alert(Alert.AlertType.ERROR)
                    alert.title = "Existed Username"
                    alert.headerText = null
                    alert.contentText = "Username ${usernameField.text} already Exists"
                    alert.showAndWait()
                } else {
                    val user = User(username, hashedPassword)
                    saveUserWithHashedPassword(user)
                }
            }
        } catch (sqlException: SQLException) {
            sqlException.printStackTrace()
        }
    }

    private fun saveUserWithHashedPassword(user: User) {

        val saveUser = "INSERT INTO users(" +
                "username, password) VALUES(?, ?)"

        connection = connectToUserDatabase.connectionDb()!!
        preparedStatement = connection.prepareStatement(saveUser)
        preparedStatement.setString(1, user.username)
        preparedStatement.setString(2, user.password)

        preparedStatement.executeUpdate()

        alert = Alert(Alert.AlertType.INFORMATION)
        alert.title = "User Info"
        alert.headerText = null
        alert.contentText = "School Type Successfully Added"
        alert.showAndWait()

        navigateToLogin()

    }

    @FXML
    private fun navigateToLogin() {
        try {
            val root: Parent = FXMLLoader.load(javaClass.getResource("login.fxml"))
            val loginStage = Stage()

            loginStage.title = "Login"
            loginStage.scene = Scene(root)
            loginStage.isResizable = false
            loginStage.show()

            // Close the login window if needed
            val registerStage = usernameField.scene.window as Stage
            registerStage.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}