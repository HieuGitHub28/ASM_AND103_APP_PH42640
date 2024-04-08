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

public class DangNhap extends AppCompatActivity {
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
        setContentView(R.layout.activity_dang_nhap);

        EditText taikhoan = findViewById(R.id.txtuser);
        EditText matkhau = findViewById(R.id.txtpass);

        String email;
        String password;

        Intent intent = getIntent();
        Log.d("Monky D:", "Luffy:" + intent.getStringExtra("email"));
        if(intent != null){
            email = intent.getStringExtra("email");
            password = intent.getStringExtra("password");
            taikhoan.setText(email);
            matkhau.setText(password);
        }

        findViewById(R.id.btndangky).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DangNhap.this, MainActivity.class);
                startActivity((intent));
            }
        });

        findViewById(R.id.btndangnhapPhone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DangNhap.this, Phone.class));
            }
        });

        findViewById(R.id.btndangnhap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = taikhoan.getText().toString();
                String password = matkhau.getText().toString();

                if (email.trim().length() == 0 || password.trim().length() == 0 ){
                    Toast.makeText(DangNhap.this, "Không được để trống thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(DangNhap.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d("Main", "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    Toast.makeText(DangNhap.this, "Đăng nhập thành công", Toast.LENGTH_LONG).show();

                                    Intent intent1 = new Intent(DangNhap.this, Home.class);
                                    startActivity(intent1);
                                } else {
                                    Log.w("Main", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(DangNhap.this, "Đăng nhập thất bại",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}