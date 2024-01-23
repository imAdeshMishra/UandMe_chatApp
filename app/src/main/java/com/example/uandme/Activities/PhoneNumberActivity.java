package com.example.uandme.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.uandme.databinding.ActivityPhoneNumberBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PhoneNumberActivity extends AppCompatActivity {

    ActivityPhoneNumberBinding binding;
    FirebaseAuth auth;
    boolean userFound = false;

    FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhoneNumberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        database = FirebaseDatabase.getInstance();

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            if (getStringFromPreferences("uid").isEmpty()){
                Intent intent = new Intent(PhoneNumberActivity.this, SetUpProfileActivity.class);
                startActivity(intent);
                finish();
            }else {
                Intent intent = new Intent(PhoneNumberActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }


        binding.continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = binding.phoneBox.getText().toString();

                binding.phoneBox.requestFocus();

                Intent intent = new Intent(PhoneNumberActivity.this, OTPActivity.class);
                intent.putExtra("phoneNumber","+91"+ phoneNumber);
                startActivity(intent);
            }
        });


    }

    private void searchUserByUid(String uidToSearch, UserSearchCallback callback) {
        database.getReference().child("users").child(uidToSearch).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.d("YourActivity", "User found!");
                    callback.onUserFound();
                } else {
                    Log.d("YourActivity", "User not found!");
                    callback.onUserNotFound();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("YourActivity", "Error searching for user: " + databaseError.getMessage());
            }
        });
    }

    interface UserSearchCallback {
        void onUserFound();
        void onUserNotFound();
    }

    private String getStringFromPreferences(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences("SharedPreference", Context.MODE_PRIVATE);
        // Provide a default value (empty string in this case) if the key is not found
        return sharedPreferences.getString(key, "");
    }

}