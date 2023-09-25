package com.deck.organize

import com.deck.organize.config.ConfigFirebase
import com.deck.organize.model.Usuario

class UsuarioRepository(private val usuario: Usuario) {
    private var dbref = ConfigFirebase.firebaseDatabase

    fun salvar() {
        dbref.child("Usuarios").push().setValue(usuario)
    }
}


