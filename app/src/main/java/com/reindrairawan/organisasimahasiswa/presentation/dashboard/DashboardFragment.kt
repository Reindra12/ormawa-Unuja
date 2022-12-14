package com.reindrairawan.organisasimahasiswa.presentation.dashboard

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.snapshots.Snapshot.Companion.observe
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager

import com.reindrairawan.organisasimahasiswa.R
import com.reindrairawan.organisasimahasiswa.databinding.FragmentDashboardBinding
import com.reindrairawan.organisasimahasiswa.domain.dashboard.category.entity.CategoriesEntity
import com.reindrairawan.organisasimahasiswa.infra.utils.SharedPrefs
import com.reindrairawan.organisasimahasiswa.presentation.common.extension.AwesomeDialogMessage
import com.reindrairawan.organisasimahasiswa.presentation.common.extension.gone
import com.reindrairawan.organisasimahasiswa.presentation.common.extension.showToast
import com.reindrairawan.organisasimahasiswa.presentation.common.extension.visible
import com.reindrairawan.organisasimahasiswa.presentation.dashboard.jenisKegiatan.ShowImageActivity
import com.reindrairawan.organisasimahasiswa.presentation.dashboard.search.SearchActivity
import com.reindrairawan.organisasimahasiswa.presentation.main.IntroActivity
import com.reindrairawan.organisasimahasiswa.utils.cameraX.CameraActivity
import com.reindrairawan.organisasimahasiswa.utils.cameraX.reduceFileImage
import com.reindrairawan.organisasimahasiswa.utils.cameraX.uriToFile
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private lateinit var binding: FragmentDashboardBinding
    private val viewModel: DashboardViewModel by viewModels()
    private var getFile: File? = null

    @Inject
    lateinit var prefs: SharedPrefs

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    context, "Tidak mendapatkan permission.", Toast.LENGTH_SHORT
                ).show()
                activity?.finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        context?.let { it1 ->
            ContextCompat.checkSelfPermission(
                it1,
                it
            )
        } == PackageManager.PERMISSION_GRANTED
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        observe()
        viewModel.fetchCategories()

        binding.welcomeTextview.text = "Selamat Datang " + prefs.getUsername()
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
        addCategory()
        searchData()
    }

    private fun searchData() {
      binding.searchImageview.setOnClickListener {
          startActivity(Intent(requireContext(), SearchActivity::class.java))
      }
    }

    private fun addCategory() {
        binding.cameraFloat.setOnClickListener {
            requireActivity().AwesomeDialogMessage(requireActivity(), "Camera", "Gallery") {
                if (it.equals("Camera")) {
                    startCameraX()
                } else {
                    startGallery()
                }
            }
        }

    }

    private fun startCameraX() {
        val intent = Intent(requireContext(), CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            getFile = reduceFileImage(myFile)
            toShowImage(getFile!!, isBackCamera, "camera")
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, requireContext())
            getFile = myFile
            toShowImage(getFile!!, true, "gallery")
        }
    }


    private fun toShowImage(result: File, isBackCamera: Boolean, status: String) {
        val intent = Intent(context, ShowImageActivity::class.java)
        intent.putExtra("isBackCamera", isBackCamera)
        intent.putExtra("status", status)
        intent.putExtra("Bitmap", result)
        startActivity(intent)
    }

    private fun observe() {
        observeState()
        observeCategories()
    }

    private fun observeCategories() {
        viewModel.mCategories.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { categories ->
                handleCategories(categories)
            }.launchIn(lifecycleScope)
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
        viewModel.mState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).onEach { state ->
            handleState(state)
        }.launchIn(lifecycleScope)
    }

    private fun handleState(state: DashboardState) {
        when (state) {
            is DashboardState.IsLoading -> handleLoading(state.isLoading)
            is DashboardState.ShowToast -> requireContext().showToast(state.message)
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
        startActivity(Intent(requireContext(), IntroActivity::class.java))
        activity?.finish()
    }

    override fun onStart() {
        super.onStart()
        checkIsLoggedIn()
    }

}