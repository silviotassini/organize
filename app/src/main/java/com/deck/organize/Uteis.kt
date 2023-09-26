package com.deck.organize

import android.content.Context
import android.widget.Toast
import java.text.SimpleDateFormat

fun mensagemErro(context: Context, campo:String){
    Toast
        .makeText(context,"Campo $campo é obrigatório", Toast.LENGTH_SHORT)
        .show()
}

fun retornaData(): String? {
    var sdf = SimpleDateFormat("dd/M/yyyy")
    var data = System.currentTimeMillis()
    var dataAtual = sdf.format(data)
    return dataAtual
}

