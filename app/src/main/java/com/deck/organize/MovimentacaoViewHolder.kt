package com.deck.organize

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.deck.organize.databinding.RowMovimentacaoBinding
import com.deck.organize.model.Movimentacao

class MovimentacaoViewHolder(private val itemBinding: RowMovimentacaoBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bindData(movimentacao: Movimentacao) {
            itemBinding.textValor.text = movimentacao.valor.toString()
            itemBinding.textDescricao.text = movimentacao.descricao
            itemBinding.textCategoria.text = movimentacao.categoria
            if (movimentacao.tipo.equals("d")) {
                itemBinding.textValor.text = "-${movimentacao.valor}"
                itemBinding.textValor.setTextColor(Color.RED) // Define a cor do texto como vermelho
            } else {
                itemBinding.textValor.setTextColor(Color.BLUE) // Define a cor do texto como preto para outros casos
            }
        }
    }