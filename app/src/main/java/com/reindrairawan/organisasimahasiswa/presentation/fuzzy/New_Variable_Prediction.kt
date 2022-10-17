package com.reindrairawan.organisasimahasiswa.presentation.fuzzy

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.reindrairawan.organisasimahasiswa.databinding.FragmentNewVariableBinding
import com.reindrairawan.organisasimahasiswa.data.fuzzy.FuzzyEntity
import com.reindrairawan.organisasimahasiswa.presentation.common.extension.showToast


class New_Variable_Prediction : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentNewVariableBinding
    private lateinit var prediksiViewModel: PrediksiViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity()
        prediksiViewModel = ViewModelProvider(activity).get(PrediksiViewModel::class.java)


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewVariableBinding.inflate(inflater, container, false)
        val data = arguments?.getParcelableArray("")
        val status = arguments?.getString("key")

        if (status.equals("variable")) {
            binding.nilaiNaikInput.visibility = View.GONE
            binding.nilaiTurunInput.visibility = View.GONE

            binding.saveVariableButton.setOnClickListener {
                saveAction()
            }
        } else if (status.equals("nilai")) {
            binding.nameInput.visibility = View.GONE
            binding.saveVariableButton.setOnClickListener {
                saveValue(data)
            }
        }
        return binding.root
    }

    private fun saveValue(data: Array<Parcelable>?) {
        prediksiViewModel.nilaitinggi.value = binding.nilaiNaikEdittext.text.toString()
        prediksiViewModel.nilaiturun.value = binding.nilaiTurunEdittext.text.toString()
        binding.nilaiTurunEdittext.setText("")
        binding.nilaiNaikEdittext.setText("")


        dismiss()
    }

    private fun saveAction() {
        prediksiViewModel.name.value = binding.name.text.toString()
        binding.name.setText("")
        dismiss()
    }
}