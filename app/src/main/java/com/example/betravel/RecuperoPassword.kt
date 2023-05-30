package com.example.betravel

import android.content.DialogInterface
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.betravel.databinding.ActivityRecuperoPasswordBinding
import com.example.betravel.databinding.ActivityRecuperoPasswordLandBinding
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class RecuperoPassword : AppCompatActivity() {

    private lateinit var binding: ActivityRecuperoPasswordBinding
    private lateinit var bindingLand: ActivityRecuperoPasswordLandBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecuperoPasswordBinding.inflate(layoutInflater)
        bindingLand = ActivityRecuperoPasswordLandBinding.inflate(layoutInflater)

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(bindingLand.root)
        } else if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(binding.root)
        }

        binding.recuperoPassword.setOnClickListener {
            val email = binding.editRecuperoPassword.text.toString()
            resetPassword(email)
            showMessage("Un'email con le istruzioni per il recupero password è stata inviata all'indirizzo fornito")
        }

        bindingLand.recuperoPassword.setOnClickListener {
            val email = bindingLand.editRecuperoPassword.text.toString()
            resetPassword(email)
            showMessage("Un'email con le istruzioni per il recupero password è stata inviata all'indirizzo fornito")
        }
    }

    private fun resetPassword(email: String) {
        val randomPassword = generateRandomPassword(8) // Genera una password casuale
        updatePasswordInDatabase(email, randomPassword) // Aggiorna la password nel database

        val recoveryLink = "https://your-website.com/reset-password?email=$email&password=$randomPassword"
        val message = "Ciao,\n\nAbbiamo ricevuto la tua richiesta di recupero password.\n\nClicca sul seguente link per reimpostare la tua password:\n\n$recoveryLink\n\nCordiali saluti,\nIl tuo team di supporto"

        sendEmail(email, "Recupero Password", message)

        val successMessage = "Un'email con le istruzioni per il recupero password è stata inviata all'indirizzo fornito."

        showMessage(successMessage)
    }

    private fun showMessage(message: String) {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Informazione")
            .setMessage(message)
            .setPositiveButton("OK") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
            .create()
        alertDialog.show()
    }

    private fun sendEmail(email: String, subject: String, body: String) {
        val properties = Properties().apply {
            put("mail.smtp.host", "smtp.gmail.com")
            put("mail.smtp.port", "587")
            put("mail.smtp.auth", "true")
            put("mail.smtp.starttls.enable", "true")
        }

        val session = Session.getInstance(properties, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication("gia.ferrara27@gmail.com", "FerraraGi27102001!")
            }
        })

        val message = MimeMessage(session).apply {
            setFrom(InternetAddress("gia.ferrara27@gmail.com"))
            setRecipients(Message.RecipientType.TO, InternetAddress.parse(email))
            setSubject(subject)
            setText(body)
        }

        try {
            Transport.send(message)
        } catch (e: MessagingException) {
            e.printStackTrace()
        }
    }

    private fun generateRandomPassword(length: Int): String {
        val characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        val random = Random()
        val password = StringBuilder()

        for (i in 0 until length) {
            val randomIndex = random.nextInt(characters.length)
            password.append(characters[randomIndex])
        }

        return password.toString()
    }

    private fun updatePasswordInDatabase(email: String, newPassword: String) {
        // Logica per aggiornare la password nel database
        // Implementa la tua logica per accedere al database esterno e aggiornare la password associata all'email specificata
    }
}
