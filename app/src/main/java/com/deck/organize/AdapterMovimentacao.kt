package com.deck.organize

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.deck.organize.databinding.RowMovimentacaoBinding
import com.deck.organize.model.Movimentacao


class AdapterMovimentacao : RecyclerView.Adapter<MovimentacaoViewHolder>() {

    private var lista: List<Movimentacao> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            MovimentacaoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = RowMovimentacaoBinding
            .inflate(inflater, parent, false)
        return MovimentacaoViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: MovimentacaoViewHolder, position: Int) {
        holder.bindData(lista[position])
    }

    override fun getItemCount(): Int {
        Log.i("MEULOG1", "${lista.count().toString()}")
        return lista.count()
    }

    fun updateAdapter(list: List<Movimentacao>) {
        lista = list
        notifyDataSetChanged()
    }

}