package com.example.pertemuan13

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.pertemuan13.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var prefManager: PrefManager
    private val usernameData = "tes"
    private val passwordData = "123"
    private val channelId = "TEST_NOTIF"
    private val notifId = 90

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PrefManager.getInstance(this)
        checkLoginStatus()

        with(binding) {
            btnNotif.setOnClickListener {
                sendNotification()
            }

            btnLogin.setOnClickListener {
                val username = edtUsername.text.toString()
                val password = edtPassword.text.toString()
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this@MainActivity, "Mohon isi semua data", Toast.LENGTH_SHORT).show()
                } else if (username == usernameData && password == passwordData) {
                    prefManager.setLoggedIn(true)
                    prefManager.saveUsername(username)
                    checkLoginStatus()
                } else {
                    Toast.makeText(this@MainActivity, "Username atau password salah", Toast.LENGTH_SHORT).show()
                }
            }

            btnLogout.setOnClickListener {
                prefManager.setLoggedIn(false)
                checkLoginStatus()
            }

            btnClear.setOnClickListener {
                prefManager.clear()
                checkLoginStatus()
            }
        }
    }

    private fun sendNotification() {
        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE
        } else {
            0
        }

        // Intent untuk aksi baca
        val readIntent = Intent(this, NotifReceiver::class.java).putExtra("MESSAGE", "Baca selengkapnya...")
        val readPendingIntent = PendingIntent.getBroadcast(this, 0, readIntent, flag)

        // Intent untuk aksi logout
        val logoutIntent = Intent(this, NotifReceiver::class.java).apply {
            action = "ACTION_LOGOUT"
        }
        val logoutPendingIntent = PendingIntent.getBroadcast(this, 1, logoutIntent, flag)

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_bell)
            .setContentTitle("Notifku")
            .setContentText("Hello World")
            .setContentIntent(readPendingIntent)
            .setAutoCancel(true)
            .addAction(0, "Baca", readPendingIntent)
            .addAction(0, "Logout", logoutPendingIntent)

        val notifManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notifChannel = NotificationChannel(
                channelId,
                "notifku",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notifManager.createNotificationChannel(notifChannel)
        }

        notifManager.notify(notifId, builder.build())
    }

    private fun checkLoginStatus() {
        val isLoggedIn = prefManager.isLoggedIn()
        val username = prefManager.getusername()
        Log.d("MainActivity", "checkLoginStatus: isLoggedIn=$isLoggedIn, username=$username")

        if (isLoggedIn) {
            binding.llLogged.visibility = View.VISIBLE
            binding.llLogin.visibility = View.GONE
        } else {
            binding.llLogged.visibility = View.GONE
            binding.llLogin.visibility = View.VISIBLE
        }
    }
}
