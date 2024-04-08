package com.example.asm_ph42640;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.asm_ph42640.Api.ApiService;
import com.example.asm_ph42640.Modal.Response;
import com.example.asm_ph42640.Modal.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class Register extends AppCompatActivity {

    EditText txtemaildk,txtuserdk,txtpassdk,txtfullname;
    ImageView imgUser;
    Button btnRegister;
    private File file;

    private File createFileFormUri (Uri path, String name) {
        File _file = new File(Register.this.getCacheDir(), name + ".png");
        try {
            InputStream in = Register.this.getContentResolver().openInputStream(path);
            OutputStream out = new FileOutputStream(_file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) >0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
            Log.d("123123", "createFileFormUri: " +_file);
            return _file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.register), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getView();

        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void getView (){
        txtemaildk = findViewById(R.id.txtemaildk);
        txtfullname = findViewById(R.id.txtfullname);
        txtuserdk = findViewById(R.id.txtuserdk);
        txtpassdk = findViewById(R.id.txtpassdk);
        imgUser = findViewById(R.id.imgUser);
        btnRegister = findViewById(R.id.btnRegister);
    }

    private void register(){
        String email = txtemaildk.getText().toString().trim();
        String fullname = txtfullname.getText().toString().trim();
        String username = txtuserdk.getText().toString().trim();
        String password = txtpassdk.getText().toString().trim();
        if (email.isEmpty() || username.isEmpty() || password.isEmpty() || fullname.isEmpty()){
            Toast.makeText(this, "Không được để trống", Toast.LENGTH_SHORT).show();
        }else{
            RequestBody _email =RequestBody.create(MediaType.parse("multipart/form-data"),email);
            RequestBody _fullname =RequestBody.create(MediaType.parse("multipart/form-data"),fullname);
            RequestBody _username =RequestBody.create(MediaType.parse("multipart/form-data"),username);
            RequestBody _password =RequestBody.create(MediaType.parse("multipart/form-data"),password);
            MultipartBody.Part muPart;
            if (file != null){
                RequestBody _avatar = RequestBody.create(MediaType.parse("image/*"),file);
                muPart = MultipartBody.Part.createFormData("avatar",file.getName(),_avatar);
            }else{
                muPart = null;
            }

            Call<Response<User>> call = ApiService.apiService.registerUser(_username,_password,_email,_fullname,muPart);
            call.enqueue(new Callback<Response<User>>() {
                @Override
                public void onResponse(Call<Response<User>> call, retrofit2.Response<Response<User>> response) {
                    if (response.isSuccessful()){
                        if (response.body().getStatus() == 200){
                            Toast.makeText(Register.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                            User user = response.body().getData();
                            Log.d("Register","acc : "+ user.getUsername() + " - "+user.getPassword());
                            Intent intent = new Intent(Register.this,Login.class);
                            intent.putExtra("user",user.getUsername());
                            intent.putExtra("pass",user.getPassword());
                            startActivity(intent);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Response<User>> call, Throwable t) {
                    Toast.makeText(Register.this, "Đăng ký không thành công", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }



    private void chooseImage() {
//        if (ContextCompat.checkSelfPermission(RegisterActivity.this,
//                android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
        Log.d("123123", "chooseAvatar: " +123123);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        getImage.launch(intent);
//        }else {
//            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
//
//        }
    }
    ActivityResultLauncher<Intent> getImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if (o.getResultCode() == Activity.RESULT_OK) {
                        Intent data = o.getData();
                        Uri imageUri = data.getData();

                        Log.d("RegisterActivity", imageUri.toString());

                        Log.d("123123", "onActivityResult: "+data);

                        file = createFileFormUri(imageUri, "avatar");

                        //binding.avatar.setImageURI(imageUri);

                        Glide.with(imgUser)
                                .load(imageUri)
                                .centerCrop()
                                .circleCrop()
                                .into(imgUser);

//                        Glide.with(RegisterActivity.this)
//                                .load(file)
//                                .thumbnail(Glide.with(RegisterActivity.this).load(R.drawable.baseline_broken_image_24))
//                                .centerCrop()
//                                .circleCrop()
////                                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                                .skipMemoryCache(true)
//                                .into(binding.avatar);
                    }
                }
            });
}