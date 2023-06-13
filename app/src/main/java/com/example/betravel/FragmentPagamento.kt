package com.example.betravel

import android.Manifest
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
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.betravel.databinding.FragmentPagamentoBinding
import java.io.File
import java.io.FileWriter

class FragmentPagamento : Fragment() {

    private lateinit var binding: FragmentPagamentoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return super.onCreateView(inflater, container, savedInstanceState)
        } else {
            binding = FragmentPagamentoBinding.inflate(inflater, container, false)
            val view = binding.root

            val data = arguments?.getString(ARG_DATA)
            if (data != null) {
                binding.textViewDettagli.text = data
            }

            binding.numero.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // Non è necessario implementare questa funzione
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // Non è necessario implementare questa funzione
                }

                override fun afterTextChanged(s: Editable?) {
                    val maxLength = 16
                    if ((s?.length ?: 0) > maxLength) {
                        s?.delete(maxLength, s.length)
                    }
                }
            })

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

                if(nomeCarta.isNotEmpty() && numeroCarta.isNotEmpty() && cvv.isNotEmpty()){
                    // Controlla se il permesso è già stato concesso
                    if (isWriteExternalStoragePermissionGranted()) {
                        // Il permesso è già stato concesso, salva il file
                        saveTextAsFile()
                    } else {
                        // Il permesso non è stato ancora concesso, richiedilo all'utente
                        requestWriteExternalStoragePermission()
                    }
                }else{
                    showMessage("Compilare tutti i campi")
                }


            }

            return view
        }
    }

    private fun getPrintableText(): String {
        val data = arguments?.getString(ARG_DATA) ?: ""
        val text = "Resoconto : $data"
        return text
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
                PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE -> {
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

    companion object {
        private const val ARG_DATA = "data"
        private const val PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 1

        fun newPagamentoInstance(data: String): FragmentPagamento {
            val fragment = FragmentPagamento()
            val bundle = Bundle()
            bundle.putString(ARG_DATA, data)
            fragment.arguments = bundle
            return fragment
        }

        fun newPagamentoInstanceSoggiorno(data: String): FragmentPagamento {
            val fragment = FragmentPagamento()
            val bundle = Bundle()
            bundle.putString(ARG_DATA, data)
            fragment.arguments = bundle
            return fragment
        }
    }
}
