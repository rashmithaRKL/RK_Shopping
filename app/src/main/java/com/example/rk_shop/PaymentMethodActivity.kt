package com.example.rk_shop

import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity

class PaymentMethodActivity : AppCompatActivity() {

    private lateinit var radioGroupPaymentMethods: RadioGroup
    private lateinit var buttonSavePaymentMethod: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_method)

        radioGroupPaymentMethods = findViewById(R.id.radioGroupPaymentMethods)
        buttonSavePaymentMethod = findViewById(R.id.buttonSavePaymentMethod)

        buttonSavePaymentMethod.setOnClickListener {
            // Handle save payment method logic
        }
    }
}
