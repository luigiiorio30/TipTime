package com.example.tiptime

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tiptime.databinding.ActivityMainBinding
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.calculateButton.setOnClickListener { calculateTip() }

        binding.clear.setOnClickListener { cleanTextView() }

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

        when (personToInt(binding.splitCostEditText.text.toString())) {
            0 -> binding.moneyPerson.visibility = TextView.INVISIBLE
            else -> binding.moneyPerson.visibility = TextView.VISIBLE
        }

        val tipPercentage = getPercentageById(binding.tipOptions.checkedRadioButtonId)
        //calculate tip
        val tip = calculateTip(costDouble, tipPercentage, binding.roundUpSwitch.isChecked)
        //formatting and showing tip
        val formattedTip = NumberFormat.getCurrencyInstance().format(tip)
        binding.tipResult.text = getString(R.string.tip_amount, formattedTip)

        //total bill
        val totalBill = calculateTotal(tip, costDouble)
        val formattedTotal = NumberFormat.getCurrencyInstance().format(totalBill)
        binding.totalBill.text = getString(R.string.total, formattedTotal)

        //total bill for person
        val totalBillForPerson = calculateSplitTotal(totalBill, personInt)
        val formattedTotalBillForPerson =
            NumberFormat.getCurrencyInstance().format(totalBillForPerson)
        binding.moneyPerson.text = getString(R.string.money_person, formattedTotalBillForPerson)
    }


    //PRIMARY FACTIONS
    private fun calculateTip(cost: Double, tipPercentage: Double, roundUp: Boolean): Double {
        return if (roundUp) {
            kotlin.math.ceil(tipPercentage * cost)
        } else {
            tipPercentage * cost
        }
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

    //CLEAN TEXT VIEW
    private fun cleanTextView() {
        binding.tipResult.text = ""
        binding.totalBill.text = ""
        binding.moneyPerson.text = ""
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
