package com.teguh.pokelist.ui.view.detail_pokemon

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.teguh.pokelist.R
import com.teguh.pokelist.data.db_helper.DataBaseHandler
import com.teguh.pokelist.data.model.Stats
import com.teguh.pokelist.data.model.Types
import com.teguh.pokelist.databinding.ActivityDetailPokemonBinding
import com.teguh.pokelist.ui.adapter.ItemRibbonAdapter
import com.teguh.pokelist.ui.adapter.ItemStatusAdapter
import com.teguh.pokelist.utils.Functions
import com.teguh.pokelist.utils.MyContext
import java.lang.Exception

class DetailPokemonActivity : AppCompatActivity(), ItemRibbonAdapter.ListItemListener, ItemStatusAdapter.ListItemListener {
    private lateinit var binding: ActivityDetailPokemonBinding
    private lateinit var viewModel: DetailPokemonViewModel
    private var pDialog: Dialog? = null
    private lateinit var adapterRibbon: ItemRibbonAdapter
    private lateinit var adapterStatus: ItemStatusAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPokemonBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[DetailPokemonViewModel::class.java]
        setContentView(binding.root)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        init()
        listener()
        setupObservers()
    }

    private fun init(){
        viewModel.nameFromIntent = intent.getStringExtra("name")?:""
        viewModel.loadPokemonList(viewModel.nameFromIntent)
        adapterStatus = ItemStatusAdapter(this)
        adapterRibbon = ItemRibbonAdapter(this)
        binding.ribbonRecyclerView.adapter = adapterRibbon
        binding.baseStatusRecyclerView.adapter = adapterStatus
    }

    private fun listener(){
        binding.arrow.setOnClickListener(){
            finish()
        }
        binding.addMyPokemon.setOnClickListener(){
            if(viewModel.isFound) {
                viewModel.db.deleteMyPokemon(viewModel.resultDataDetailPokemon.value?.id.toString())
                viewModel.loadPokemonList(viewModel.nameFromIntent)
            } else {
                val edtNickName: EditText
                val btnSimpan: Button
                val myDialog = Dialog(this)
                myDialog.setContentView(R.layout.pop_up_catch_pokemon)
                edtNickName = myDialog.findViewById(R.id.edt_nick_name)
                btnSimpan = myDialog.findViewById(R.id.btn_save) as Button
                btnSimpan.setOnClickListener(View.OnClickListener {
                    viewModel.db.insertDataMyPokemon(viewModel.resultDataDetailPokemon.value?.id.toString(),
                        viewModel.resultDataDetailPokemon.value?.name.toString(), edtNickName.text.toString())
                    myDialog.dismiss()
                    viewModel.loadPokemonList(viewModel.nameFromIntent)
                })
                myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                myDialog.show()
            }
        }
    }

    private fun setupObservers(){
        viewModel.resultDataDetailPokemon.observe(this){
            adapterRibbon.notifyDataSetChanged()
            adapterStatus.notifyDataSetChanged()
            val index = it.id
            if(viewModel.isFound) {
                binding.addMyPokemon.text = "Release Pokemon"
            } else {
                binding.addMyPokemon.text = "Catch My Pokemon"
            }
            val url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$index.png"


            Picasso.get()
                .load(url)
                .into(binding.image, object : Callback {
                    override fun onSuccess() {

                    }

                    override fun onError(e: Exception?) {

                    }

                })
            Functions().getColorPaletteFromUrl(url) { palette ->
                // Handle the extracted palette
                if (palette != null) {
                    val dominantSwatch = palette.dominantSwatch

                    val dominantColor = dominantSwatch?.rgb ?: Color.TRANSPARENT

                    // Use the colors as needed in your application
                    // For example, set the background color of a view
                    binding.header.setBackgroundColor(dominantColor)
                    binding.addMyPokemon.setBackgroundColor(dominantColor)
                } else {
                    // Error handling when palette extraction fails
                }
            }
        }
        viewModel.isLoading.observe(this
        ) {
            if(it){
                showProgressDialog()
            } else {
                dismissProgressDialog()
            }
        }
        viewModel.alertInfo.observe(this){
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    fun showProgressDialog() {
        if (pDialog == null) {
            pDialog = Dialog(this)
            pDialog?.setContentView(R.layout.pop_up_loading)
            pDialog?.setCancelable(false)
        }
        pDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        pDialog?.show()
    }

    fun dismissProgressDialog() {
        if (pDialog != null && pDialog!!.isShowing) {
            pDialog?.dismiss()
        }
    }

    override fun onDestroy() {
        dismissProgressDialog()
        super.onDestroy()
    }

    override fun getEntriesRibbon(): List<Types> {
        return viewModel.resultDataDetailPokemon.value?.types?: ArrayList()
    }

    override fun getEntries(): List<Stats> {
        return viewModel.resultDataDetailPokemon.value?.stats?: ArrayList()

    }
}