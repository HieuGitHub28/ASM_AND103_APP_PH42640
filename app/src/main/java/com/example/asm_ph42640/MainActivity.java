package com.example.asm_ph42640;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.checkerframework.checker.nullness.qual.NonNull;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        EditText taikhoan = findViewById(R.id.txtuserdk);
        EditText matkhau = findViewById(R.id.txtpassdk);

        findViewById(R.id.btndangnhapdk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = taikhoan.getText().toString();
                String password = matkhau.getText().toString();

                if (email.trim().length() == 0 || password.trim().length() == 0 ){
                    Toast.makeText(MainActivity.this, "Không được để trống thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    Intent intent = new Intent(MainActivity.this, DangNhap.class);
                                    intent.putExtra("email", email);
                                    intent.putExtra("password", password);
                                    startActivity(intent);

                                    Toast.makeText(MainActivity.this, "Đăng ký thành công", Toast.LENGTH_LONG).show();

                                } else {
                                    Log.w("Main", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(MainActivity.this, "Đăng ký thất bại",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });



    }
}