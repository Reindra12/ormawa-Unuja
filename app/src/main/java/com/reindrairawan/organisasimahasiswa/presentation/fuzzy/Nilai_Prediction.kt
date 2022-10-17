package com.reindrairawan.organisasimahasiswa.presentation.fuzzy

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.reindrairawan.organisasimahasiswa.data.fuzzy.FuzzyEntity
import com.reindrairawan.organisasimahasiswa.databinding.FragmentNewVariableBinding


class Nilai_Prediction : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentNewVariableBinding
    private lateinit var prediksiViewModel: PrediksiViewModel
    var listener: ((String) -> Unit)? = null


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
        val data = arguments?.getString("key")
        listener?.invoke("")
        if (data.equals("variable")) {
            binding.nilaiNaikInput.visibility = View.GONE
            binding.nilaiTurunInput.visibility = View.GONE

            binding.saveVariableButton.setOnClickListener {
                saveAction()
            }
        } else if (data.equals("nilai")) {
            binding.nameInput.visibility = View.GONE
            binding.saveVariableButton.setOnClickListener {

                saveValue()
            }
        }
        return binding.root
    }

    private fun saveValue() {
        prediksiViewModel.nilaitinggi.value = binding.nilaiNaikEdittext.text.toString()
        prediksiViewModel.nilaiturun.value = binding.nilaiTurunEdittext.text.toString()
        listener?.invoke("nilai")


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
