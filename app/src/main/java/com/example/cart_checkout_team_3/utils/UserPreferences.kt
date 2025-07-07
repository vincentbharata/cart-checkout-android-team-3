package com.example.cart_checkout_team_3.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.cart_checkout_team_3.data.models.User

class UserPreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USERNAME = "username"
        private const val KEY_EMAIL = "email"
        private const val KEY_FIRST_NAME = "first_name"
        private const val KEY_LAST_NAME = "last_name"
        private const val KEY_GENDER = "gender"
        private const val KEY_IMAGE = "image"
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }

    fun saveUser(user: User, accessToken: String) {
        prefs.edit().apply {
            putInt(KEY_USER_ID, user.id)
            putString(KEY_USERNAME, user.username)
            putString(KEY_EMAIL, user.email)
            putString(KEY_FIRST_NAME, user.firstName)
            putString(KEY_LAST_NAME, user.lastName)
            putString(KEY_GENDER, user.gender)
            putString(KEY_IMAGE, user.image)
            putString(KEY_ACCESS_TOKEN, accessToken)
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
    }

    fun getUser(): User? {
        return if (isLoggedIn()) {
            User(
                id = prefs.getInt(KEY_USER_ID, 0),
                username = prefs.getString(KEY_USERNAME, "") ?: "",
                email = prefs.getString(KEY_EMAIL, "") ?: "",
                firstName = prefs.getString(KEY_FIRST_NAME, "") ?: "",
                lastName = prefs.getString(KEY_LAST_NAME, "") ?: "",
                gender = prefs.getString(KEY_GENDER, "") ?: "",
                image = prefs.getString(KEY_IMAGE, "") ?: ""
            )
        } else null
    }

    fun getUserId(): Int = prefs.getInt(KEY_USER_ID, 0)

    fun getAccessToken(): String = prefs.getString(KEY_ACCESS_TOKEN, "") ?: ""

    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_IS_LOGGED_IN, false)

    fun logout() {
        prefs.edit().clear().apply()
    }
}
