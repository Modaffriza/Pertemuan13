package com.example.pertemuan13
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pertemuan13.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var prefManager: PrefManager
    private val usernameData = "tes"
    private val passwordData = "123"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefManager = PrefManager.getInstance(this)
        checkLoginStatus()
        with(binding) {
            btnLogin.setOnClickListener {
                val username = edtUsername.text.toString()
                val password = edtPassword.text.toString()
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(
                        this@MainActivity,
                        "Mohon isi semua data",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    if (username == usernameData && password == passwordData) {
                        prefManager.setLoggedIn(true)
                        prefManager.saveUsername(username)
                        checkLoginStatus()
                    } else {
                        Toast.makeText(this@MainActivity, "Username atau password salah", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            btnLogout.setOnClickListener {
                prefManager.setLoggedIn(false)
                checkLoginStatus()
            }
            btnClear.setOnClickListener{
                prefManager.clear()
                checkLoginStatus()
            }
        }
    }
    private fun checkLoginStatus() {
        val isLoggedIn = prefManager.isLoggedIn()
        if (isLoggedIn) {
            binding.llLogged.visibility = View.VISIBLE
            binding.llLogin.visibility = View.GONE
        }else{
            binding.llLogged.visibility = View.GONE
            binding.llLogin.visibility = View.VISIBLE
        }
    }
}