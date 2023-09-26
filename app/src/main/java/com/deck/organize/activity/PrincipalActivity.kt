package com.deck.organize.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deck.organize.AdapterMovimentacao
import com.deck.organize.MovimentacaoRepository
import com.deck.organize.R
import com.deck.organize.config.ConfigFirebase
import com.deck.organize.databinding.ActivityPrincipalBinding
import com.deck.organize.databinding.ContentPrincipalBinding
import com.deck.organize.model.Movimentacao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener


class PrincipalActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityPrincipalBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var dbref: DatabaseReference
    private var movimentacao = Movimentacao()
    private var movimentacoes: MutableList<Movimentacao> = arrayListOf()
    private lateinit var userId:String
    private lateinit var userNome:String
    private lateinit var valueEventListenerMovimentacoes:ValueEventListener
    private var saldototal = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("organize", Context.MODE_PRIVATE)
        userId = sharedPreferences.getString("userId", null).toString()
        userNome = sharedPreferences.getString("userNome", null).toString()
        binding = ActivityPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = "Organize"
        setSupportActionBar(binding.toolbar)

        val adapter = AdapterMovimentacao()

        var recyclerView = findViewById<RecyclerView>(R.id.recyclerMovimentos)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        recuperarMovimentacoes(adapter)

    }
    fun adicionarReceita(view: View){
        startActivity(Intent(this, ReceitaActivity::class.java))
    }
    fun adicionarDespesa(view:View){
        startActivity(Intent(this, DespesaActivity::class.java))
    }

    fun recuperarMovimentacoes(adapter: AdapterMovimentacao) {
        dbref = ConfigFirebase.firebaseDatabase
        var listagem = dbref.child("Movimentacao")
        valueEventListenerMovimentacoes =
            listagem.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    movimentacoes.clear()
                    for (dados in dataSnapshot.children) {
                        val movimentacao = dados.getValue(Movimentacao::class.java)
                        if (movimentacao != null) {
                            if(movimentacao.idUsuario == userId) {
                                movimentacoes.add(movimentacao)
                                if(movimentacao.tipo == "d")
                                    saldototal -= movimentacao.valor.toDouble()
                                else
                                    saldototal += movimentacao.valor.toDouble()

                            }
                        }
                    }
                    adapter.updateAdapter(movimentacoes)
                    recuperarResumo()
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }
    fun recuperarResumo(){
        var textSaudacao = findViewById<TextView>(R.id.textSaudacao)
        var textSaldo = findViewById<TextView>(R.id.textSaldo)
        textSaudacao.text = "Bem vindo"
        textSaldo.text = "R$ " + saldototal.toString()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menuSair){
            auth = ConfigFirebase.firebaseAuth
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}