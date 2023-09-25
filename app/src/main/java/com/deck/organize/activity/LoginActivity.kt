package com.deck.organize.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.deck.organize.config.ConfigFirebase
import com.deck.organize.databinding.ActivityLoginBinding
import com.deck.organize.mensagemErro
import com.deck.organize.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private var usuario: Usuario = Usuario()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val btnLogin = binding.btnLogin

        btnLogin.setOnClickListener {
            val campoEmail = binding.txtEmail.text.toString()
            val campoSenha = binding.txtSenha.text.toString()
            validaCampos(campoEmail, campoSenha)
        }
    }
    private fun loginUsuario(){
        auth = ConfigFirebase.firebaseAuth
        auth.signInWithEmailAndPassword(usuario.email, usuario.senha)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    val userId = auth.currentUser?.uid
                    val sharedPreferences = getSharedPreferences("organize", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("userId", userId)
                    editor.apply()
                    startActivity(Intent(this, PrincipalActivity::class.java))
                    finish()
                }
                else {
                    var excecao = ""
                    try {
                        throw it.getException()!!
                    } catch (e: FirebaseAuthInvalidUserException) {
                        excecao = "Usuário não cadastrado!"
                    } catch (e: FirebaseAuthInvalidUserException) {
                        excecao = "Por favor, digite um e-mail válido"
                    }  catch (e: Exception) {
                        excecao = "Erro ao logar usuário: " + e.message
                        e.printStackTrace()
                    }

                    Toast.makeText(
                        this@LoginActivity,
                        excecao,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
    private fun validaCampos(campoEmail: String, campoSenha: String) {

            if(!campoEmail.isEmpty()){
                if(!campoSenha.isEmpty()){
                    usuario.apply {
                        email = campoEmail
                        senha = campoSenha
                    }
                    loginUsuario()
                }
                else{
                    mensagemErro( this,"Senha")
                }
            }
            else{
                mensagemErro(this, "Email")
            }
        }

    }

