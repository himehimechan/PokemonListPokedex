package com.teguh.pokelist.ui.view.my_pokemon

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.teguh.pokelist.data.model.PokemonListLocal
import com.teguh.pokelist.databinding.ActivityMyPokemonBinding
import com.teguh.pokelist.ui.adapter.ItemMyPokemonAdapter
import com.teguh.pokelist.ui.view.detail_pokemon.DetailPokemonActivity

class MyPokemonActivity : AppCompatActivity(), ItemMyPokemonAdapter.ListItemListener {
    private lateinit var binding: ActivityMyPokemonBinding
    private lateinit var viewModel: MyPokemonViewModel
    private var pDialog: Dialog? = null
    private lateinit var adapter: ItemMyPokemonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPokemonBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(MyPokemonViewModel::class.java)
        setContentView(binding.root)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        init()
        listener()
        setupObservers()
    }

    private fun init(){
        adapter = ItemMyPokemonAdapter(this)
        binding.recyclerView.adapter = adapter
    }

    private fun listener(){

    }

    private fun setupObservers(){
        viewModel.myPokemonLocalList.observe(this){
            adapter.notifyDataSetChanged()
        }
    }

    override fun onItemClick(name: String) {
        val intent = Intent(this, DetailPokemonActivity::class.java)
        // You can also include some extra data.
        intent.putExtra("name", name)
        startActivity(intent)
    }

    override fun onDeleteItemClick(id: String) {
        viewModel.db.deleteMyPokemon(id)
        viewModel.loadMyPokemon()
    }

    override fun getEntries(): List<PokemonListLocal> {
        return viewModel.myPokemonLocalList.value?: ArrayList()
    }

}