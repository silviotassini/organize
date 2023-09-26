package com.deck.organize.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.deck.organize.config.ConfigFirebase
import com.deck.organize.databinding.ActivityEntradaBinding
import com.google.firebase.auth.FirebaseAuth

class entrada : AppCompatActivity() {
    private lateinit var binding: ActivityEntradaBinding
    private lateinit var auth:FirebaseAuth

    override fun onStart() {
        super.onStart()
        verificaUserLogado()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEntradaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var botaoCadastro = binding.btnCadastro
        var txtEntrada = binding.txtEntrada
        botaoCadastro.setOnClickListener {
            startActivity(Intent(this, CadastroActivity::class.java))
        }

        txtEntrada.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
    fun verificaUserLogado(){
        auth = ConfigFirebase.firebaseAuth
        //auth.signOut()
        if(auth.currentUser != null){
            val userId = auth.currentUser?.uid
            val sharedPreferences = getSharedPreferences("organize", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("userId", userId)
            editor.apply()
            startActivity(Intent(this, PrincipalActivity::class.java))
        }
    }
}