package com.example.smack.Controller

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.view.View
import android.widget.Toast
import com.example.smack.R
import com.example.smack.Services.AuthService
import com.example.smack.Services.UserDataService
import com.example.smack.Utilities.BROADCAST_USER_DATA_CHANGE
import kotlinx.android.synthetic.main.activity_create_user.*

class CreateUserActivity : AppCompatActivity() {

    var userAvatar = "profileDefault"
    var avatarColor = "[0.5, 0.5, 0.5, 1]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)

        createSpinner.visibility = View.INVISIBLE
    }

    fun generateUserAvatar (view: View) {

        var random = java.util.Random()
        val color = random.nextInt(2)
        val avatar = random.nextInt(28)

        if (color == 0) {
            userAvatar = "light$avatar"
        } else {
            userAvatar = "dark$avatar"
        }

        val resourceId = resources.getIdentifier(userAvatar, "drawable", packageName)
        createAvatarImage.setImageResource(resourceId)



    }

    fun generateColorOnClick (view: View) {
        val random = java.util.Random()
        val r = random.nextInt(255)
        val g = random.nextInt(255)
        val b = random.nextInt(255)

        createAvatarImage.setBackgroundColor(Color.rgb(r, g, b))

        val savedR = r.toDouble() / 255
        val savedG = g.toDouble() / 255
        val savedB = b.toDouble() / 255

        avatarColor = "[$savedR, $savedG, $savedB]"


    }

    fun generateNewUser(view: View) {

        enableSpinner(true)
        val userName = createUserNameTxt.text.toString()
        val password = createPasswordTxt.text.toString()
        val email = createEmailTxt.text.toString()

        if (userName.isNotEmpty() && email.isNotEmpty() && userName.isNotEmpty()) {
            AuthService.registerUser(this, email, password) { registerSuccess ->
                if (registerSuccess) {
                    AuthService.loginUser(this, email, password) { loginSuccess ->
                        if (loginSuccess) {
                            AuthService.createUser(this, userName, email, userAvatar, avatarColor) { createSuccess ->
                                if (createSuccess) {

                                    val userDataChange = Intent(BROADCAST_USER_DATA_CHANGE)
                                    LocalBroadcastManager.getInstance(this).sendBroadcast(userDataChange)
                                    enableSpinner(false)
                                    finish()
                                } else {
                                    errorToast()
                                }
                            }
                        } else {
                            errorToast()
                        }
                    }
                } else {
                    errorToast()
                }
            }
        } else {
            Toast.makeText(this, "Make sure username, email, and password are filled.", Toast.LENGTH_LONG).show()
            enableSpinner(false)
        }


    }

    fun errorToast () {
        Toast.makeText(this, "something went wrong", Toast.LENGTH_LONG).show()
        enableSpinner(false)
    }

    fun enableSpinner (enable: Boolean) {
        if (enable) {
            createSpinner.visibility = View.VISIBLE
            createUserBtn.isEnabled = false
            createAvatarImage.isEnabled = false
            backgroundColorBtn.isEnabled = false
            }
        else {
            createSpinner.visibility = View.INVISIBLE
            createUserBtn.isEnabled = true
            createAvatarImage.isEnabled = true
            backgroundColorBtn.isEnabled = true
        }

    }

}
