package com.deck.organize

import com.deck.organize.config.ConfigFirebase
import com.deck.organize.model.Movimentacao

class MovimentacaoRepository(private val movimentacao: Movimentacao) {
    private var dbref = ConfigFirebase.firebaseDatabase

    fun salvar() {
        dbref.child("Movimentacao").push().setValue(movimentacao)
    }

    fun resumo(userId: String) {

    }

    fun lista(userId: String) {
        dbref.child("Movimentacao")
    }


}


