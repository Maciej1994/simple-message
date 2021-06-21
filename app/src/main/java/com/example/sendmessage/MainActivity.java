package com.example.sendmessage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button login;
    private Switch active;

    private Button send;
    private EditText EnterMessage;

    public FloatingActionButton floatingActionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        active = findViewById(R.id.active);
        send = findViewById(R.id.send);
        floatingActionButton = findViewById(R.id.fab);




        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("login").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String input1 = username.getText().toString();
                        String input2 = password.getText().toString();

                        if (snapshot.child(input1).exists()) {
                            if (snapshot.child(input1).child("password").getValue(String.class).equals(input2)) {
                                if (active.isChecked()) {


                                    if (snapshot.child(input1).child("as").getValue(String.class).equals("admin")) {
                                        preferences.setDataLogin(MainActivity.this, true);
                                        preferences.setDataAs(MainActivity.this, "admin");
                                        startActivity(new Intent(MainActivity.this, AdminActivity.class));

                                    } else if(snapshot.child(input1).child("as").getValue(String.class).equals("user")) {
                                        preferences.setDataLogin(MainActivity.this, true);
                                        preferences.setDataAs(MainActivity.this, "user");
                                        startActivity(new Intent(MainActivity.this, UserActivity.class));

                                    }

                                } else {
                                    if (snapshot.child(input1).child("as").getValue(String.class).equals("admin")) {
                                        preferences.setDataLogin(MainActivity.this, false);
                                        startActivity(new Intent(MainActivity.this, AdminActivity.class));

                                    } else if(snapshot.child(input1).child("as").getValue(String.class).equals("user")) {
                                        preferences.setDataLogin(MainActivity.this, false);
                                        startActivity(new Intent(MainActivity.this, UserActivity.class));

                                    }
                                }

                            } else {
                                Toast.makeText(MainActivity.this, "password error", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(MainActivity.this, "username error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(preferences.getDataLogin(this)){
            if(preferences.getDataAs(this).equals("admin")){
                startActivity(new Intent(MainActivity.this, AdminActivity.class));
                finish();

            } else if(preferences.getDataAs(this).equals("user")){
                startActivity(new Intent(MainActivity.this, UserActivity.class));
                finish();

            }
        }

    }
}