package com.reindrairawan.organisasimahasiswa.presentation.dashboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.reindrairawan.organisasimahasiswa.databinding.ActivityMainBinding
import com.reindrairawan.organisasimahasiswa.domain.dashboard.category.entity.CategoriesEntity
import com.reindrairawan.organisasimahasiswa.infra.utils.SharedPrefs
import com.reindrairawan.organisasimahasiswa.presentation.common.extension.*
import com.reindrairawan.organisasimahasiswa.presentation.main.IntroActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    //    private val binding get() = _binding
    private val viewModel: DashboardViewModel by viewModels()

    @Inject
    lateinit var prefs: SharedPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpRecyclerView()
        observe()
        viewModel.fetchCategories()


        binding.welcomeTextview.text = "Selamat Datang " + prefs.getUsername()
        showToast(prefs.getToken())

        addCategory()
    }

    private fun addCategory() {
        binding.cameraFloat.setOnClickListener {
            AwesomeDialogMessage(this, "Camera", "Gallery"){
                Log.d("TAG", "addCategory: "+it)
            }


        }

    }


    private fun observe() {
        observeState()
        observeCategories()
    }

    private fun observeCategories() {
        viewModel.mCategories.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { categories ->
                handleCategories(categories)
            }
            .launchIn(lifecycleScope)
    }

    private fun handleCategories(categories: List<CategoriesEntity>) {
        binding.categoriesRecyclerView.adapter?.let {
            if (it is DashboardAdapter) {
                viewModel.fetchCategories()
                it.setData(categories)

            }
        }
    }

    private fun observeState() {
        viewModel.mState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state ->
                handleState(state)
            }
            .launchIn(lifecycleScope)
    }

    private fun handleState(state: DashboardState) {
        when (state) {
            is DashboardState.IsLoading -> handleLoading(state.isLoading)
            is DashboardState.ShowToast -> showToast(state.message)
            is DashboardState.Init -> Unit
        }
    }


    private fun handleLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingProgressBar?.visible()

        } else {
            binding.loadingProgressBar?.gone()
        }
    }

    private fun setUpRecyclerView() {
        val mAdapter = DashboardAdapter(mutableListOf())
//        mAdapter.setItemTapListener(object : DashboardAdapter.OnItemTap {
//            override fun onTap(categories: CategoriesEntity) {
//                val b = bundleOf("id" to categories.id)
//
//            }
//        })
        binding.categoriesRecyclerView.apply {
            binding.categoriesRecyclerView.visible()
            layoutManager = LinearLayoutManager(context)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = mAdapter
        }
    }

    private fun checkIsLoggedIn() {
        if (prefs.getToken().isEmpty()) {
            goToLoginActivity()
        }
    }


    private fun goToLoginActivity() {
        startActivity(Intent(this, IntroActivity::class.java))
        finish()
    }

    override fun onStart() {
        super.onStart()
        checkIsLoggedIn()
    }


}