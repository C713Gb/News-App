package com.application.newsapp.ui.activities

import android.app.KeyguardManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.application.newsapp.R
import com.application.newsapp.data.models.Articles
import com.application.newsapp.repository.MainRepository
import com.application.newsapp.ui.MainViewModel
import com.application.newsapp.ui.MainViewModelFactory
import com.application.newsapp.ui.RVAdapter
import com.application.newsapp.utils.Constants

class MainActivity : AppCompatActivity() {

    private val mainRepository = MainRepository()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RVAdapter

    private var list: List<Articles> = ArrayList()

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

        val mainViewModel = ViewModelProvider(this, mainViewModelFactory)[MainViewModel::class.java]

        recyclerView = findViewById(R.id.recycler)
        recyclerView.setHasFixedSize(true)

        mainViewModel.getData(sources = "bbc-news", apiKey = Constants.API_KEY)

        mainViewModel.fetchData!!.observe(this){ it ->
            list = it.articles
            list.sortedByDescending { it.publishedAt }
            adapter = RVAdapter(list, this@MainActivity) {
                val intent = Intent(this@MainActivity, NewsDetailsActivity::class.java)
                intent.putExtra("headline", it.title)
                intent.putExtra("author", it.author)
                intent.putExtra("published", it.publishedAt)
                intent.putExtra("description", it.description)
                intent.putExtra("content", it.content)
                intent.putExtra("image", it.urlToImage)
                startActivity(intent)
            }
            recyclerView.adapter = adapter
        }

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

    private fun getCancellationSignal(): CancellationSignal {
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            toast("Authentication was cancelled by the user")
        }

        return cancellationSignal as CancellationSignal
    }

    private fun checkBiometricSupport(): Boolean {

        val keyguardManager : KeyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

        if (!keyguardManager.isKeyguardSecure){
            toast("Fingerprint Authentication has not been enabled in Settings")
            return false
        }

        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.USE_BIOMETRIC) != PackageManager.PERMISSION_GRANTED) {
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