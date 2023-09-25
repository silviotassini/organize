package com.deck.organize.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.deck.organize.databinding.ActivityReceitaBinding

class ReceitaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReceitaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReceitaBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}