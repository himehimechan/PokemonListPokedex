package com.teguh.pokelist.ui.view.list_pokemon

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teguh.pokelist.R
import com.teguh.pokelist.data.model.Results
import com.teguh.pokelist.databinding.ActivityListPokemonBinding
import com.teguh.pokelist.ui.adapter.ItemPokemonAdapter
import com.teguh.pokelist.ui.view.detail_pokemon.DetailPokemonActivity
import com.teguh.pokelist.ui.view.my_pokemon.MyPokemonActivity
import java.util.ArrayList

class ListPokemonActivity : AppCompatActivity(), ItemPokemonAdapter.ListItemListener {
    private lateinit var binding: ActivityListPokemonBinding
    private lateinit var viewModel: ListPokemonViewModel
    private var pDialog: Dialog? = null
    private lateinit var adapter: ItemPokemonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListPokemonBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(ListPokemonViewModel::class.java)
        setContentView(binding.root)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        init()
        listener()
        setupObservers()
    }

    private fun init(){
        adapter = ItemPokemonAdapter(this)
        binding.recyclerView.adapter = adapter
    }

    private fun listener(){
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!viewModel.isLoading.value!! && viewModel.nextOffset != "") {
                    val isAtBottom = (visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    val isNotAtBeginning = firstVisibleItemPosition >= 0
                    val isScrollingDownwards = dy > 0

                    if (isAtBottom && isNotAtBeginning && isScrollingDownwards) {
                        Log.e("listener", "endOfRecyleView")
                        viewModel.loadPokemonList(viewModel.nextOffset)
                    }
                }
            }
        })
        binding.buttonMyPokemon.setOnClickListener(){
            val intent = Intent(this, MyPokemonActivity::class.java)
            // You can also include some extra data.
            startActivity(intent)
        }
    }

    private fun addDataToRecyclerView(newDataList: List<Results>) {
        val startPosition = viewModel.listDataForView.size
        viewModel.listDataForView.addAll(newDataList)
        adapter.notifyItemRangeInserted(startPosition, newDataList.size)
    }

    private fun setupObservers(){
        viewModel.resultDataPokemon.observe(this) {
            addDataToRecyclerView(it)
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

    override fun onItemClick(name: String) {
        val intent = Intent(this, DetailPokemonActivity::class.java)
        // You can also include some extra data.
        intent.putExtra("name", name)
        startActivity(intent)
    }

    override fun getEntries(): List<Results> {
        return viewModel.listDataForView
    }

}