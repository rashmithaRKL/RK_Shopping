package com.example.rk_shop

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class ManageAddressActivity : AppCompatActivity() {

    private lateinit var editTextAddress: EditText
    private lateinit var editTextCity: EditText
    private lateinit var editTextZipCode: EditText
    private lateinit var buttonSaveAddress: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_address)

        editTextAddress = findViewById(R.id.editTextAddress)
        editTextCity = findViewById(R.id.editTextCity)
        editTextZipCode = findViewById(R.id.editTextZipCode)
        buttonSaveAddress = findViewById(R.id.buttonSaveAddress)

        buttonSaveAddress.setOnClickListener {
            // Handle save address logic
        }
    }
}
