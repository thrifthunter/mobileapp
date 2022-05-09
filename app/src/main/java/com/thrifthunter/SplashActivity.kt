package com.thrifthunter

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.thrifthunter.auth.LoginActivity
import com.thrifthunter.auth.UserPreference

class SplashActivity : AppCompatActivity() {
    private lateinit var mUserPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        mUserPreference = UserPreference(this)
        val userModel = mUserPreference.getUser()

        Handler(Looper.getMainLooper()).postDelayed({
            if (userModel.token != "") {
                startActivity(Intent(this@SplashActivity, ListStoryActivity::class.java))
                finish()
            }
            else {
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                finish()
            }
        },1000)
    }
}