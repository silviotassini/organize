package com.deck.organize.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.deck.organize.UsuarioRepository
import com.deck.organize.config.ConfigFirebase
import com.deck.organize.databinding.ActivityCadastroBinding
import com.deck.organize.mensagemErro
import com.deck.organize.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class CadastroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCadastroBinding
    private lateinit var auth: FirebaseAuth
    private var usuario:Usuario = Usuario()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val btnCadastro = binding.btnCadastrar

        btnCadastro.setOnClickListener {
            val campoNome = binding.txtNome.text.toString()
            val campoEmail = binding.txtEmail.text.toString()
            val campoSenha = binding.txtSenha.text.toString()
            validaCampos(campoNome, campoEmail, campoSenha)
        }

    }
    private fun cadastrarUsuario(){
        auth = ConfigFirebase.firebaseAuth
        var usuarioRepository = UsuarioRepository(usuario)
        auth.createUserWithEmailAndPassword(usuario.email,usuario.senha)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    usuarioRepository.salvar()
                    Toast
                        .makeText(this,"Sucesso ao cadastrar usuario",Toast.LENGTH_SHORT)
                        .show()
                    startActivity(Intent(this, PrincipalActivity::class.java))
                    finish()
                }
                else {
                    var excecao = ""
                    try {
                        throw it.getException()!!
                    } catch (e: FirebaseAuthWeakPasswordException) {
                        excecao = "Digite uma senha mais forte!"
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        excecao = "Por favor, digite um e-mail válido"
                    } catch (e: FirebaseAuthUserCollisionException) {
                        excecao = "Este conta já foi cadastrada"
                    } catch (e: Exception) {
                        excecao = "Erro ao cadastrar usuário: " + e.message
                        e.printStackTrace()
                    }

                    Toast.makeText(
                        this@CadastroActivity,
                        excecao,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
    private fun validaCampos(campoNome: String, campoEmail: String, campoSenha: String) {
            if(!campoNome.isEmpty()){
                if(!campoEmail.isEmpty()){
                    if(!campoSenha.isEmpty()){
                        usuario.apply {
                            nome = campoNome
                            email = campoEmail
                            senha = campoSenha
                        }
                        cadastrarUsuario()
                    }
                    else{
                        mensagemErro(this, "Senha")
                    }
                }
                else{
                    mensagemErro(this, "Email")
                }
            }
            else{
                mensagemErro(this, "Nome")
            }
    }

}