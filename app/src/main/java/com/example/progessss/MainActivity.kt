package com.example.progessss

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.progessss.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Properties
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val TAG = "EmailSender" // Tag for logging
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Assuming you have a button in your layout to send the email
        binding.sendEmailButton.setOnClickListener {
            val recipient = "hms7605428@gmail.com" // recipient's email
            val subject = "Test Subject" // Change the subject as needed
            val body = "This is a test email from my app." // Change the email body as needed

            sendEmail(subject, body, recipient)
        }
    }

    private fun sendEmail(subject: String, body: String, recipient: String) {
        val properties = Properties().apply {
            put("mail.smtp.auth", "true")
            put("mail.smtp.starttls.enable", "true")
            put("mail.smtp.host", "smtp.gmail.com") // Use your email provider's SMTP server
            put("mail.smtp.port", "587")
        }

        val session = Session.getInstance(properties, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication("hafsamaryam7847@gmail.com", "ihfp suuu hixt luug") // Replace with your email password
            }
        })

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val message = MimeMessage(session).apply {
                    setFrom(InternetAddress("hafsamaryam7847@gmail.com")) // Your email
                    setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient))
                    setSubject(subject)
                    setText(body)
                }
                Transport.send(message)

                // Log successful email send
                Log.d(TAG, "Email sent successfully to $recipient")
            } catch (e: Exception) {
                e.printStackTrace()

                // Switch to the main thread to update the UI
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Failed to send email: ${e.message}", Toast.LENGTH_LONG).show()
                    binding.tvTest.text = e.message // Set error message to tvTest
                }
                // Log error message
                Log.e(TAG, "Failed to send email: ${e.message}")
            }
        }
    }
}
