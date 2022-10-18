package com.example.tiptime

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.example.tiptime.databinding.ActivityMainBinding
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("shake", "aaa222a")
        binding.calculateButton.setOnClickListener { calculateTip() }

        binding.costOfServiceEditText.setOnKeyListener { view, keyCode, _ ->
            handleKeyEvent(
                view,
                keyCode
            )
        }

    }

    private fun calculateTip() {
        Log.d("shake", "aaa222a")

        if (binding.costOfServiceEditText.text.toString()
                .isEmpty() || binding.costOfServiceEditText.text.toString().isEmpty()
        ) {
            displayTip(0.0)
            displayTotal(0.0)
            divideCost(0.0)
            return
        }
        if (binding.costOfServiceEditText.text.toString()
                .isNotEmpty() || binding.costOfServiceEditText.text.toString().isEmpty()
        ) {

            val stringInTextField = binding.costOfServiceEditText.text.toString()
            val cost = stringInTextField.toDouble()

            val totalPersonBill = binding.splitCostEditText.text.toString()
            val totalPerson = totalPersonBill.toInt()


            val tipPercentage = when (binding.tipOptions.checkedRadioButtonId) {
                R.id.option_twenty_percent -> 0.20
                R.id.option_eighteen_percent -> 0.18
                else -> 0.15
            }
            var tip = tipPercentage * cost
            val roundUp = binding.roundUpSwitch.isChecked
            if (roundUp) {
                tip = kotlin.math.ceil(tip)
            }
            displayTotal(cost + tip)
            displayTip(tip)
            if (totalPerson < 1) {
                divideCost(cost / totalPerson)
            }
        }
    }

    private fun displayTip(tip: Double) {
        val formattedTip = NumberFormat.getCurrencyInstance().format(tip)
        binding.tipResult.text = getString(R.string.tip_amount, formattedTip)
    }

    private fun displayTotal(total: Double) {
        val totalBill = NumberFormat.getCurrencyInstance().format(total)
        binding.totalBill.text = getString(R.string.total, totalBill)
    }

    private fun divideCost(divide: Double) {
        val formatDivide = NumberFormat.getCurrencyInstance().format(divide)
        binding.moneyPerson.text = getString(R.string.divide, formatDivide)
    }

    private fun handleKeyEvent(view: View, keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            return true
        }
        return false
    }
}