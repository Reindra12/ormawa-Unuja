package com.reindrairawan.organisasimahasiswa.presentation.fuzzy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.reindrairawan.organisasimahasiswa.data.fuzzy.FuzzyEntity
import com.reindrairawan.organisasimahasiswa.databinding.ActivityFuzzyBinding

class FuzzyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFuzzyBinding
    private lateinit var nilaiPrediction: Nilai_Prediction
    lateinit var fuzzyEntity: FuzzyEntity

    private lateinit var list: ArrayList<FuzzyEntity>
    private lateinit var prediksiViewModel: PrediksiViewModel
    lateinit var adapter: FuzzyAdapter
    var nilaiturun: String = ""
    var nilaitinggi: String = ""
    var statusnilai: Boolean = false

    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFuzzyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prediksiViewModel = ViewModelProvider(this).get(PrediksiViewModel::class.java)

        nilaiPrediction = Nilai_Prediction()
        list = ArrayList()
        adapter = FuzzyAdapter(this, list)

        linearLayoutManager = LinearLayoutManager(this)
        binding.fuzzyRv.layoutManager = linearLayoutManager

        binding.fuzzyRv.adapter = adapter

        binding.variableButton.setOnClickListener {
            openBottomSheetDialogVariable("variable")
        }


        prediksiViewModel.name.observe(this) {
            list.add(FuzzyEntity("$it", "0", "0"))
            adapter.notifyDataSetChanged()
        }

        adapter.onItemClick = {
            openBottomSheetDialogNilai(it)
        }


    }


    private fun openBottomSheetDialogNilai(fuzzyEntity: FuzzyEntity): Boolean {

        val nilaiPrediction = Nilai_Prediction()
        val bundle = Bundle()
        bundle.putString("key", "nilai")
        nilaiPrediction.arguments = bundle
        nilaiPrediction.show(supportFragmentManager, "variable")

        prediksiViewModel.nilaitinggi.observe(this) {
            nilaitinggi = it

        }

        prediksiViewModel.nilaiturun.observe(this) {
            nilaiturun = it


        }
        nilaiPrediction.listener = {
            if (it.equals("")) {

            } else {

                fuzzyEntity.nilaiTinggi = nilaitinggi
                fuzzyEntity.nilaiTurun = nilaiturun
                adapter.notifyDataSetChanged()
            }

        }

        return true
    }

    private fun openBottomSheetDialogVariable(s: String) {
        val newVariablePrediction = New_Variable_Prediction()
        val bundle = Bundle()
        bundle.putString("key", s)
        newVariablePrediction.arguments = bundle
        newVariablePrediction.show(supportFragmentManager, "variable")


    }

}