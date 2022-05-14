package com.jamessaboia.prodmaisapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.util.PatternsCompat
import com.jamessaboia.prodmaisapp.databinding.ActivityRegisterBinding
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.btRegister.setOnClickListener {
            validate()
        }


    }

    private fun validate() {
        val result = arrayOf(validateName(), validateEmail(), validatePassword())

        if (false in result) {
            return
        }
        Toast.makeText(this, "Cadastro realizado com Sucesso!", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun validateName(): Boolean {
        val nome = binding.name.editText?.text.toString()
        return if (nome.isEmpty()) {
            binding.name.error = "Campo obrigatório"
            false
        } else {
            binding.name.error = null
            true
        }
    }

    private fun validateEmail(): Boolean {
        val email = binding.email.editText?.text.toString()
        return if (email.isEmpty()) {
            binding.email.error = "Campo obrigatório"
            false
        } else if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.email.error = "Email inválido"
            false
        } else {
            binding.email.error = null
            true
        }
    }


    private fun validatePassword(): Boolean {
        val senha = binding.senha.editText?.text.toString()

        return if (senha.isEmpty()) {
            binding.senha.error = "Campo obrigatório"
            false
        } else {
            binding.senha.error = null
            true
        }
    }
//    private fun validateTerms(): Boolean {
//        val terms = binding.cbTerms
//
//    }


    }