package com.example.betravel

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.betravel.databinding.FragmentPagamentoPacchettoBinding
import com.example.betravel.databinding.FragmentPagamentoPacchettoOrizzontaleBinding
import java.io.File
import java.io.FileWriter

class FragmentPagamentoPacchetto : Fragment() {

    private lateinit var binding: FragmentPagamentoPacchettoBinding
    private lateinit var bindingOrizzontale : FragmentPagamentoPacchettoOrizzontaleBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            bindingOrizzontale = FragmentPagamentoPacchettoOrizzontaleBinding.inflate(inflater,container,false)
            val view = bindingOrizzontale.root

            val data = arguments?.getString(FragmentPagamentoPacchetto.ARG_DATA)
            if (data != null) {
                bindingOrizzontale.textViewDettagli.text = data
            }

            bindingOrizzontale.cvv.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // Non è necessario implementare questa funzione
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // Non è necessario implementare questa funzione
                }

                override fun afterTextChanged(s: Editable?) {
                    val maxLength = 3
                    if ((s?.length ?: 0) > maxLength) {
                        s?.delete(maxLength, s.length)
                    }
                }
            })

            bindingOrizzontale.pagamento.setOnClickListener {
                val nomeCarta = bindingOrizzontale.nome.text.toString()
                val numeroCarta = bindingOrizzontale.numero.text.toString()
                val cvv = bindingOrizzontale.cvv.text.toString()

                if (nomeCarta.isNotEmpty() && numeroCarta.isNotEmpty() && cvv.isNotEmpty()) {
                    showMessageConfirm("Procedere al pagamento?") { button ->
                        if (button) {
                            if (isWriteExternalStoragePermissionGranted()) {
                                saveTextAsFile()
                            } else {
                                requestWriteExternalStoragePermission()
                            }
                            showMessage("Pagamento effettuato con successo")

                            // Apri la MainActivity
                            val intent = Intent(context, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            // non fai niente
                        }
                    }
                } else {
                    showMessage("Compilare tutti i campi")
                }



            }

            return view
        }else{
            binding = FragmentPagamentoPacchettoBinding.inflate(inflater,container,false)
            val view = binding.root

            val data = arguments?.getString(FragmentPagamentoPacchetto.ARG_DATA)
            if (data != null) {
                binding.textViewDettagli.text = data
            }

            binding.cvv.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // Non è necessario implementare questa funzione
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // Non è necessario implementare questa funzione
                }

                override fun afterTextChanged(s: Editable?) {
                    val maxLength = 3
                    if ((s?.length ?: 0) > maxLength) {
                        s?.delete(maxLength, s.length)
                    }
                }
            })

            binding.pagamento.setOnClickListener {
                val nomeCarta = binding.nome.text.toString()
                val numeroCarta = binding.numero.text.toString()
                val cvv = binding.cvv.text.toString()

                if (nomeCarta.isNotEmpty() && numeroCarta.isNotEmpty() && cvv.isNotEmpty()) {
                    showMessageConfirm("Procedere al pagamento?") { button ->
                        if (button) {
                            if (isWriteExternalStoragePermissionGranted()) {
                                saveTextAsFile()
                            } else {
                                requestWriteExternalStoragePermission()
                            }
                            showMessage("Pagamento effettuato con successo")

                            // Apri la MainActivity
                            val intent = Intent(context, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            // non fai niente
                        }
                    }
                } else {
                    showMessage("Compilare tutti i campi")
                }



            }

            return view
        }
    }

    private fun getPrintableText(): String {
        val data = arguments?.getString(ARG_DATA)
        val text = "Resoconto : $data"
        return text
    }

    private fun showMessageConfirm(message: String, callback: (Boolean) -> Unit) {
        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("Attenzione")
            .setMessage(message)
            .setPositiveButton("No") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
                callback(false)
            }
            .setNegativeButton("Si") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
                callback(true)
            }
            .create()
        alertDialog.show()
    }

    private fun saveTextAsFile() {
        val text = getPrintableText()

        val downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(downloadsDirectory, "resoconto.txt")
        file.createNewFile()

        val writer = FileWriter(file)
        writer.append(text)
        writer.flush()
        writer.close()

        MediaScannerConnection.scanFile(
            requireContext(),
            arrayOf(file.absolutePath),
            null,
            null
        )

        showMessage("File di testo salvato nella directory Download")
    }


    private fun isWriteExternalStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Environment.isExternalStorageLegacy()
        } else {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestWriteExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Non è necessario richiedere il permesso esplicitamente per Android 10 (API livello 29) e versioni successive
            saveTextAsFile()
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                FragmentPagamentoPacchetto.PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            FragmentPagamentoPacchetto.PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Il permesso è stato concesso, salva il file
                    saveTextAsFile()
                } else {
                    // Il permesso non è stato concesso, mostra un messaggio all'utente o gestisci il caso di mancato permesso
                    showMessage("Permesso di accesso alla memoria esterna negato")
                }
            }
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object{
        private const val PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 1
        private const val ARG_DATA = "data"

        fun newPagamentoInstance(data: ArrayList<String>): FragmentPagamentoPacchetto{
            val fragment = FragmentPagamentoPacchetto()
            val bundle = Bundle()
            bundle.putStringArrayList(FragmentPagamentoPacchetto.ARG_DATA, data)
            fragment.arguments = bundle
            return fragment
        }
    }
}