package com.deck.organize

import android.graphics.Color
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.deck.organize.databinding.RowMovimentacaoBinding
import com.deck.organize.model.Movimentacao
import java.text.NumberFormat
import java.util.Locale

class MovimentacaoViewHolder(private val itemBinding: RowMovimentacaoBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        private val formatador = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

        fun bindData(movimentacao: Movimentacao) {
            itemBinding.textValor.text = formatador.format(movimentacao.valor)
            itemBinding.textDescricao.text = movimentacao.descricao
            itemBinding.textCategoria.text = movimentacao.categoria
            if (movimentacao.tipo.equals("d")) {
                itemBinding.textValor.text = formatador.format(movimentacao.valor * (-1))
                itemBinding.textValor.setTextColor(Color.RED) // Define a cor do texto como vermelho
            } else {
                itemBinding.textValor.setTextColor(Color.BLUE) // Define a cor do texto como preto para outros casos
            }

        }
    }