package com.deck.organize.activity

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.deck.organize.MovimentacaoRepository
import com.deck.organize.databinding.ActivityDespesaBinding
import com.deck.organize.model.Movimentacao
import com.deck.organize.retornaData


class DespesaActivity : AppCompatActivity() {
    private lateinit var binding:ActivityDespesaBinding
    private var movimentacao = Movimentacao()
    private lateinit var userId:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDespesaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPreferences = getSharedPreferences("organize", Context.MODE_PRIVATE)
        userId = sharedPreferences.getString("userId", null).toString()

        binding.editData.setText( retornaData() )

        binding.fab.setOnClickListener {
            if(validarCampos() == true) {
                salvaMovimentacao()
                clearAllCampos()
            }
        }
    }
    fun clearAllCampos(){
        binding.editCategoria.setText("")
        binding.editDescricao.setText("")
        binding.editValor.text.clear()
    }
    fun salvaMovimentacao(){

        movimentacao.apply {
            idUsuario = userId
            tipo = "d"
            data = binding.editData.text.toString()
            categoria = binding.editCategoria.text.toString()
            descricao = binding.editDescricao.text.toString()
            valor = binding.editValor.text.toString().toDoubleOrNull() ?: 0.0
        }

        var despesa = MovimentacaoRepository(movimentacao)
        despesa.salvar()

    }

    fun validarCampos(): Boolean? {
        var campoValor = binding.editValor.text.toString()
        var campoData = binding.editData.text.toString()
        var campoCateg = binding.editCategoria.text.toString()
        var campoDescricao = binding.editDescricao.text.toString()


        return if (!campoValor.isEmpty()) {
            if (!campoData.isEmpty()) {
                if (!campoCateg.isEmpty()) {
                    if (!campoDescricao.isEmpty()) {
                        true
                    } else {
                        Toast.makeText(
                            this,
                            "Descrição não foi preenchida!",
                            Toast.LENGTH_SHORT
                        ).show()
                        false
                    }
                } else {
                    Toast.makeText(
                        this,
                        "Categoria não foi preenchida!",
                        Toast.LENGTH_SHORT
                    ).show()
                    false
                }
            } else {
                Toast.makeText(
                    this,
                    "Data não foi preenchida!",
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
        } else {
            Toast.makeText(
                this,
                "Valor não foi preenchido!",
                Toast.LENGTH_SHORT
            ).show()
            false
        }
    }
}