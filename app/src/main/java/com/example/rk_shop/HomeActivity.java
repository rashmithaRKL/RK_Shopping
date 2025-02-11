package com.example.rk_shop;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rk_shop.adapter.ShopItemAdapter;
import com.example.rk_shop.model.ShopItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HomeActivity extends AppCompatActivity {
    private TextView welcomeText;
    private RecyclerView recyclerView;
    private ShopItemAdapter adapter;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("RKShopPrefs", MODE_PRIVATE);

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize views
        welcomeText = findViewById(R.id.welcomeText);
        recyclerView = findViewById(R.id.recyclerView);
        FloatingActionButton fab = findViewById(R.id.fab);

        // Get logged in user's email
        String userEmail = getIntent().getStringExtra("USER_EMAIL");
        if (userEmail != null && !userEmail.isEmpty()) {
            welcomeText.setText("Welcome, " + userEmail);
        }

        // Set up RecyclerView
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new ShopItemAdapter(getSampleItems(), item -> {
            // Handle item click
            Toast.makeText(this, "Selected: " + item.getName(), Toast.LENGTH_SHORT).show();
        });
        recyclerView.setAdapter(adapter);

        // Set up FAB
        fab.setOnClickListener(view -> {
            Toast.makeText(this, "Add new item clicked", Toast.LENGTH_SHORT).show();
            // TODO: Implement add new item functionality
        });
    }

    private List<ShopItem> getSampleItems() {
        List<ShopItem> items = new ArrayList<>();
        items.add(new ShopItem(
            UUID.randomUUID().toString(),
            "Smartphone",
            "Latest model with high-end features",
            999.99,
            "https://example.com/smartphone.jpg"
        ));
        items.add(new ShopItem(
            UUID.randomUUID().toString(),
            "Laptop",
            "Powerful laptop for professionals",
            1499.99,
            "https://example.com/laptop.jpg"
        ));
        items.add(new ShopItem(
            UUID.randomUUID().toString(),
            "Headphones",
            "Wireless noise-canceling headphones",
            299.99,
            "https://example.com/headphones.jpg"
        ));
        items.add(new ShopItem(
            UUID.randomUUID().toString(),
            "Smartwatch",
            "Fitness tracking and notifications",
            199.99,
            "https://example.com/smartwatch.jpg"
        ));
        return items;
    }
}
