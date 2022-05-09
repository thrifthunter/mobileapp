package com.thrifthunter

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.core.provider.FontsContractCompat.Columns.RESULT_CODE
import com.thrifthunter.auth.RegistrationActivity
import com.thrifthunter.auth.UserModel
import com.thrifthunter.auth.UserPreference
import com.thrifthunter.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mUserPreference: UserPreference
    private lateinit var binding: ActivityProfileBinding

    private var isPreferenceEmpty = false
    private lateinit var userModel: UserModel

    private val resultLauncher = registerForActivityResult(
        StartActivityForResult()
    ) { result: ActivityResult ->

        if (result.data != null && result.resultCode == RegistrationActivity.RESULT_CODE) {
            userModel = result.data?.getParcelableExtra<UserModel>(RegistrationActivity.EXTRA_RESULT) as UserModel
            populateView(userModel)
            checkForm(userModel)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Profile"

        mUserPreference = UserPreference(this)

        showExistingPreference()

        binding.btnSave.setOnClickListener(this)

    }

    private fun showExistingPreference() {
        userModel = mUserPreference.getUser()
        populateView(userModel)
        checkForm(userModel)
    }

    private fun populateView(userModel: UserModel) {
        binding.tvName.text =
            if (userModel.name.toString().isEmpty()) "NULL" else userModel.name
        binding.tvEmail.text =
            if (userModel.email.toString().isEmpty()) "NULL" else userModel.email
        binding.tvPassword.text =
            if (userModel.password.toString().isEmpty()) "NULL" else userModel.password
    }

    private fun checkForm(userModel: UserModel) {
        when {
            userModel.name.toString().isNotEmpty() -> {
                binding.btnSave.text = getString(R.string.change)
                isPreferenceEmpty = false
            }
            else -> {
                binding.btnSave.text = getString(R.string.signup)
                isPreferenceEmpty = true
            }
        }
    }

    override fun onClick(view: View) {
        if (view.id == R.id.btn_save) {
            val intent = Intent(this@ProfileActivity, RegistrationActivity::class.java)
            when {
                isPreferenceEmpty -> {
                    intent.putExtra(
                        RegistrationActivity.EXTRA_TYPE_FORM,
                        RegistrationActivity.TYPE_ADD
                    )
                    intent.putExtra("USER", userModel)
                }
                else -> {
                    intent.putExtra(
                        RegistrationActivity.EXTRA_TYPE_FORM,
                        RegistrationActivity.TYPE_EDIT
                    )
                    intent.putExtra("USER", userModel)
                }
            }
            resultLauncher.launch(intent)
        }
    }
}