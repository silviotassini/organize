package com.deck.organize.activity

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.deck.organize.AdapterMovimentacao
import com.deck.organize.R
import com.deck.organize.config.ConfigFirebase
import com.deck.organize.databinding.ActivityPrincipalBinding
import com.deck.organize.model.Movimentacao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import java.text.NumberFormat
import java.util.Locale


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
    private lateinit var recyclerView:RecyclerView
    private lateinit var adapter: AdapterMovimentacao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences("organize", Context.MODE_PRIVATE)
        userId = sharedPreferences.getString("userId", null).toString()
        userNome = sharedPreferences.getString("userNome", null).toString()
        binding = ActivityPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = "Organize"
        setSupportActionBar(binding.toolbar)

        adapter = AdapterMovimentacao()

        recyclerView = findViewById<RecyclerView>(R.id.recyclerMovimentos)
        swipe()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        recuperarMovimentacoes(adapter)

    }

    fun swipe() {
        val itemTouch: ItemTouchHelper.Callback = object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val dragFlags = ItemTouchHelper.ACTION_STATE_IDLE
                val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
                return makeMovementFlags(dragFlags, swipeFlags)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                excluirMovimentacao(viewHolder)
            }
        }
        ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerView)
    }
    fun excluirMovimentacao(viewHolder: RecyclerView.ViewHolder) {
        val alertDialog = AlertDialog.Builder(this)

        //Configura AlertDialog
        alertDialog.setTitle("Excluir Movimentação da Conta")
        alertDialog.setMessage("Você tem certeza que deseja realmente excluir essa movimentação de sua conta?")
        alertDialog.setCancelable(false)

        alertDialog.setPositiveButton("Confirmar"
        ) { dialog, which ->
            val position = viewHolder.adapterPosition
            movimentacao = movimentacoes[position]
            dbref = ConfigFirebase.firebaseDatabase
            var movimentacaoRef = dbref.child("Movimentacao")

            movimentacaoRef.child(movimentacao.id).removeValue()
            adapter.notifyItemRemoved(position)
            atualizarSaldo()
        }

        alertDialog.setNegativeButton("Cancelar"
        ) { dialog, which ->
            Toast.makeText(
                this@PrincipalActivity,
                "Cancelado",
                Toast.LENGTH_SHORT
            ).show()
            adapter.notifyDataSetChanged()
        }

        val alert: AlertDialog = alertDialog.create()
        alert.show()
    }

    fun atualizarSaldo(){
        saldototal = 0.0
        for (item in movimentacoes){
            if(item.tipo == "d")
                saldototal -= item.valor
            else
                saldototal += item.valor
        }
        recuperarResumo()
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
                    saldototal = 0.0
                    for (dados in dataSnapshot.children) {
                        val movimentacao = dados.getValue(Movimentacao::class.java)
                        if (movimentacao != null) {
                            if(movimentacao.idUsuario == userId) {
                                movimentacao.id = dados.key.toString()
                                movimentacoes.add(movimentacao)
                                if(movimentacao.tipo == "d")
                                    saldototal -= movimentacao.valor
                                else
                                    saldototal += movimentacao.valor

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
        val formatador = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        var textSaudacao = findViewById<TextView>(R.id.textSaudacao)
        var textSaldo = findViewById<TextView>(R.id.textSaldo)
        textSaudacao.text = "Bem vindo"
        textSaldo.text = formatador.format(saldototal)
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