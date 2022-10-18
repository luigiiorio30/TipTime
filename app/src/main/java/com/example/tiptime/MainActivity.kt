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
        val costDouble = costToDouble(binding.costOfServiceEditText.text.toString())
        val personInt = personToInt(binding.splitCostEditText.text.toString())

        binding.moneyPerson.isEnabled = personToInt(binding.splitCostEditText.text.toString()) != 0

        val tipPercentage = getPercentageById(binding.tipOptions.checkedRadioButtonId)

        //calculate tip
        val tip = calculateTip(costDouble, tipPercentage)

        //formatting and showing tip
        val formattedTip = NumberFormat.getCurrencyInstance().format(tip)
        binding.tipResult.text = getString(R.string.tip_amount, formattedTip)

        //total bill
        val totalBill = calculateTotal(tip, costDouble)
        val formattedTotal = NumberFormat.getCurrencyInstance().format(totalBill)
        binding.totalBill.text = getString(R.string.total, formattedTotal)

        //total bill for person
        val totalBillForPerson = calculateSplitTotal(costDouble, personInt)
        val formattedTotalBillForPerson =
            NumberFormat.getCurrencyInstance().format(totalBillForPerson)
        binding.moneyPerson.text = getString(R.string.money_person, formattedTotalBillForPerson)
    }


    //PRIMARY FACTIONS
    private fun calculateTip(cost: Double, tipPercentage: Double): Double {
        return tipPercentage * cost
    }

    private fun calculateTotal(tip: Double, cost: Double): Double {
        return tip + cost
    }

    private fun calculateSplitTotal(cost: Double, person: Int): Double {
        return cost / person
    }

    //UTILS FUNCTIONS
    private fun getPercentageById(id: Int): Double {
        return when (id) {
            R.id.option_twenty_percent -> 0.20
            R.id.option_eighteen_percent -> 0.18
            else -> 0.15
        }
    }

    //USER COST CONVERTED TO DOUBLE
    private fun costToDouble(cost: String): Double {
        return if (cost.isNotEmpty()) {
            cost.toDouble()
        } else {
            0.0
        }
    }

    //PERSON CONVERTED TO INT
    private fun personToInt(cost: String): Int {
        return if (cost.isNotEmpty()) {
            cost.toInt()
        } else {
            0
        }
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
