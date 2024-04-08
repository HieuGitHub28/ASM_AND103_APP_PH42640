package com.example.asm_ph42640;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.asm_ph42640.Api.ApiService;
import com.example.asm_ph42640.Modal.Response;
import com.example.asm_ph42640.Modal.User;

import retrofit2.Call;
import retrofit2.Callback;

public class Login extends AppCompatActivity {

    EditText txtuser,txtpass;
    Button btndangnhap;
    TextView btndangnhapPhone,btndangky;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getView();

        Intent intent = getIntent();
        if (intent != null){
            String user = intent.getStringExtra("user");
            String pass = intent.getStringExtra("pass");
            txtuser.setText(user);
            txtpass.setText(pass);
        }
        btndangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = txtuser.getText().toString();
                String password = txtpass.getText().toString();
                if (password.isEmpty() || username.isEmpty()){
                    Toast.makeText(Login.this, "Không được để trống", Toast.LENGTH_SHORT).show();
                }else {
                    User user = new User(username,password);
                    Call<Response<User>> call = ApiService.apiService.loginUser(user);
                    call.enqueue(new Callback<Response<User>>() {
                        @Override
                        public void onResponse(Call<Response<User>> call, retrofit2.Response<Response<User>> response) {
                            if(response.isSuccessful()){
                                if (response.body().getStatus() == 200){
                                    Toast.makeText(Login.this, "Login success", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Login.this,Home.class));
                                    Log.d("Login", "acc" + response.body().getData());
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Response<User>> call, Throwable t) {
                            Toast.makeText(Login.this, "Login fail", Toast.LENGTH_SHORT).show();
                            Log.d("Login", "err  : " + t);
                        }
                    });

                }
            }
        });

        btndangnhapPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Phone.class));
            }
        });

        btndangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });
    }
    private void getView (){
        txtuser = findViewById(R.id.txtuser);
        txtpass = findViewById(R.id.txtpass);
        btndangnhap = findViewById(R.id.btndangnhap);
        btndangnhapPhone = findViewById(R.id.btndangnhapPhone);
        btndangky = findViewById(R.id.btndangky);
    }
}