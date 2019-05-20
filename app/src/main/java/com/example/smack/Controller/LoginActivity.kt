package com.example.smack.Controller

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.smack.R
import com.example.smack.Services.AuthService
import kotlinx.android.synthetic.main.activity_create_user.*
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginProgressBar.visibility = View.INVISIBLE



    }

    fun loginCreateUserBtnOnClick(view: View) {

        val intent = Intent(this, CreateUserActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun loginLoginBtnOnClick(view: View) {
        enableSpinner(true)
        val email = loginEmailTxt.text.toString()
        val password = loginPassTxt.text.toString()
        hideKeyboard()

        if(email.isNotEmpty() && password.isNotEmpty()) {
            AuthService.loginUser(this, email, password) { loginSuccess ->
                if (loginSuccess) {
                    AuthService.findUserByEmail(this) { findSuccess ->
                        if (findSuccess) {
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
            Toast.makeText(this, "Please fill in both email and password", Toast.LENGTH_LONG).show()
        }


    }

    fun errorToast() {
        Toast.makeText(this, "something went wrong", Toast.LENGTH_LONG).show()
        enableSpinner(false)
    }

    fun enableSpinner(enable: Boolean) {
        if (enable) {
            loginProgressBar.visibility = View.VISIBLE
        } else {
            loginLoginBtn.isEnabled = !enable
            loginCreateUserBtn.isEnabled = !enable
        }

    }

    fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if(inputManager.isAcceptingText) {
            inputManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }
    }

}
