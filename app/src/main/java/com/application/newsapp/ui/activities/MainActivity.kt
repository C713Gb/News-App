package com.application.newsapp.ui.activities

import android.app.KeyguardManager
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.application.newsapp.R
import com.application.newsapp.repository.MainRepository
import com.application.newsapp.ui.viewmodel.MainViewModel
import com.application.newsapp.ui.viewmodel.MainViewModelFactory
import com.application.newsapp.utils.Constants

class MainActivity : AppCompatActivity() {

    private val mainRepository = MainRepository()
    private lateinit var navController: NavController

    lateinit var mainViewModel: MainViewModel

    private var cancellationSignal: CancellationSignal? = null
    private val authenticationCallback: BiometricPrompt.AuthenticationCallback
        get() =
            @RequiresApi(Build.VERSION_CODES.P)
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                    super.onAuthenticationError(errorCode, errString)
                    toast("Authentication Error: $errString")
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                    super.onAuthenticationSucceeded(result)
                    toast("Authentication Success!")
                }
            }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_main)

        val mainViewModelFactory: MainViewModelFactory by lazy {
            MainViewModelFactory(mainRepository)
        }

        mainViewModel = ViewModelProvider(this, mainViewModelFactory)[MainViewModel::class.java]

        val sharedPreferences = getSharedPreferences(Constants.NEWS_SOURCE, Context.MODE_PRIVATE)
        if (sharedPreferences.getString(Constants.NEWS_TITLE, "") == "") {
            val editor = sharedPreferences.edit()
            editor.putString(Constants.NEWS_ID, "bbc-news")
            editor.putString(Constants.NEWS_TITLE, "BBC News")
            editor.apply()
            editor.commit()
            mainViewModel.getTopHeadlinesFromSource(sources = "bbc-news", apiKey = Constants.API_KEY)
        } else {
            val source = sharedPreferences.getString(Constants.NEWS_ID, "")
            if (source != null) {
                mainViewModel.getTopHeadlinesFromSource(sources = source, apiKey = Constants.API_KEY)
            }
        }


        mainViewModel.getSources(apiKey = Constants.API_KEY)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController

        setupActionBarWithNavController(navController)

        if (checkBiometricSupport()) {
            val biometricPrompt = BiometricPrompt.Builder(this@MainActivity)
                .setTitle("Title of Prompt")
                .setSubtitle("Authentication is required")
                .setDescription("This app uses Fingerprint protection to keep your data secure")
                .setNegativeButton("Cancel", this.mainExecutor) { dialog, which ->
                    toast("Authentication cancelled")
                }.build()

            biometricPrompt.authenticate(
                getCancellationSignal(),
                mainExecutor,
                authenticationCallback
            )
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun getCancellationSignal(): CancellationSignal {
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            toast("Authentication was cancelled by the user")
        }

        return cancellationSignal as CancellationSignal
    }

    private fun checkBiometricSupport(): Boolean {

        val keyguardManager: KeyguardManager =
            getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

        if (!keyguardManager.isKeyguardSecure) {
            toast("Fingerprint Authentication has not been enabled in Settings")
            return false
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.USE_BIOMETRIC
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            toast("Fingerprint Authentication Permission is not been enabled")
            return false
        }

        return if (packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
            true
        } else true
    }

    private fun toast(s: String) {
        Toast.makeText(this@MainActivity, s, Toast.LENGTH_SHORT).show()
    }

}