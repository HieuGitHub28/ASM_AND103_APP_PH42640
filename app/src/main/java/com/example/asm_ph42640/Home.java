package com.example.asm_ph42640;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.asm_ph42640.Adapter.SinhVienAdapter;
import com.example.asm_ph42640.Api.ApiService;
import com.example.asm_ph42640.Modal.SinhVien;
import com.example.asm_ph42640.Modal.Response;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;


public class Home extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 10;
    List<SinhVien> list = new ArrayList<>();
    FloatingActionButton fltadd;
    ImageView imagePiker;
    private File file;

    public RecyclerView rcvSV ;
    public SinhVienAdapter adapter;

    Button btnUp, btnDown, btnSearch;
    EditText txtsearch;


    private File createFileFormUri (Uri path, String name) {
        File _file = new File(Home.this.getCacheDir(), name + ".png");
        try {
            InputStream in = Home.this.getContentResolver().openInputStream(path);
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
        setContentView(R.layout.activity_home);
        fltadd = findViewById(R.id.fltadd);
        rcvSV = findViewById(R.id.rcvSV);

        loadData();
        getView();

        fltadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(Home.this,new SinhVien(),1,list);
            }
        });

        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortStudent(-1);
            }
        });
        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortStudent(1);
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = txtsearch.getText().toString().trim();
                searchStudent(key);
            }
        });
    }

    private void getView(){
        btnDown = findViewById(R.id.btnDown);
        btnUp = findViewById(R.id.btnUp);
        btnSearch = findViewById(R.id.btnSearch);
        txtsearch = findViewById(R.id.txtsearch);
    }


    public void showDialog (Context context, SinhVien sinhVien, Integer type, List<SinhVien> list){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        LayoutInflater inflater= ((Activity) context).getLayoutInflater();
        View view=inflater.inflate(R.layout.dialog_add_sinhvien,null);
        builder.setView(view);
        Dialog dialog=builder.create();
        dialog.show();

        EditText edtMaSV = view.findViewById(R.id.edtMaSV);
        EditText edtNameSV = view.findViewById(R.id.edtNameSV);
        EditText edtDiemTB = view.findViewById(R.id.edtDiemTB);
        EditText edtAvatar = view.findViewById(R.id.edtAvatar);
        imagePiker = view.findViewById(R.id.imgAvatarSV);
        Button btnChonAnh =view.findViewById(R.id.btnChonAnh);
        Button btnSave =view.findViewById(R.id.btnSave);

        if (type == 0){
            edtMaSV.setText(sinhVien.getMasv());
            edtNameSV.setText(sinhVien.getName());
            edtDiemTB.setText(sinhVien.getPoint()+"");
            edtAvatar.setText(sinhVien.getAvatar());
            Glide.with(view).load(sinhVien.getAvatar()).into(imagePiker);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String masv = edtMaSV.getText().toString().trim();
                String name = edtNameSV.getText().toString().trim();
                String diemTB = edtDiemTB.getText().toString();
                String avatar = edtAvatar.getText().toString().trim();
                if (masv.isEmpty() || name.isEmpty()|| diemTB.isEmpty()){
                    Toast.makeText(context, "Không được bỏ trống", Toast.LENGTH_SHORT).show();
                } else if (!isDouble(diemTB)) {
                    Toast.makeText(context, "Điểm trung bình phải là số", Toast.LENGTH_SHORT).show();
                } else {
                    Double point = Double.parseDouble(diemTB);
                    if (point < 0 || point > 10){
                        Toast.makeText(context, "Điểm phải từ 0-10", Toast.LENGTH_SHORT).show();
                    }else {

                        RequestBody rMasv = RequestBody.create(MediaType.parse("multipart/form-data"), masv);
                        RequestBody rName = RequestBody.create(MediaType.parse("multipart/form-data"), name);
                        RequestBody rPoint = RequestBody.create(MediaType.parse("multipart/form-data"), diemTB);
                        MultipartBody.Part muPart;
                        if (file != null){
                            RequestBody rAvatar = RequestBody.create(MediaType.parse("image/*"),file);
                            muPart = MultipartBody.Part.createFormData("avatar",file.getName(),rAvatar);
                        }else {
                            muPart = null;
                        }

                        Call<Response<SinhVien>> call = ApiService.apiService.addStudent(rMasv,rName,rPoint,muPart);
                        if(type == 0){
                            call = ApiService.apiService.updateStudent(sinhVien.get_id(),rMasv,rName,rPoint,muPart);
                        }
                        call.enqueue(new Callback<Response<SinhVien>>() {
                            @Override
                            public void onResponse(Call<Response<SinhVien>> call, retrofit2.Response<Response<SinhVien>> response) {
                                if (response.isSuccessful()){
                                    if(response.body().getStatus() == 200){
                                        Toast.makeText(Home.this,"Success", Toast.LENGTH_SHORT).show();
                                        loadData();
                                        dialog.dismiss();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<Response<SinhVien>> call, Throwable t) {
                                Toast.makeText(Home.this, "Fail", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                    }
                }
            }
        });

        btnChonAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });
    }

    private boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void loadData (){

        Call<Response<List<SinhVien>>> call = ApiService.apiService.getData();

        call.enqueue(new Callback<Response<List<SinhVien>>>() {
            @Override
            public void onResponse(Call<Response<List<SinhVien>>> call, retrofit2.Response<Response<List<SinhVien>>> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus() == 200){
                        List<SinhVien> ds = response.body().getData();
                        Log.d("List","a : "+response.body().getData());
                        getData(ds);
                    }
                }
            }

            @Override
            public void onFailure(Call<Response<List<SinhVien>>> call, Throwable t) {
                Log.d("List","a : "+ t);
            }
        });
    }

    public void getData (List<SinhVien> list){
        adapter = new SinhVienAdapter(Home.this, list);
        rcvSV.setLayoutManager(new LinearLayoutManager(Home.this));
        rcvSV.setAdapter(adapter);
    }

    public void searchStudent(String key){
        Call<Response<List<SinhVien>>> call = ApiService.apiService.searchStudent(key);
        call.enqueue(new Callback<Response<List<SinhVien>>>() {
            @Override
            public void onResponse(Call<Response<List<SinhVien>>> call, retrofit2.Response<Response<List<SinhVien>>> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus() == 200){
                        List<SinhVien> ds = response.body().getData();
                        getData(ds);
                    }
                }
            }

            @Override
            public void onFailure(Call<Response<List<SinhVien>>> call, Throwable t) {
                Log.d("Search","Lỗi : "+ t);
            }
        });
    }

    public void sortStudent(Integer type){
        Call<Response<List<SinhVien>>> call = ApiService.apiService.sortStudent(type);
        call.enqueue(new Callback<Response<List<SinhVien>>>() {
            @Override
            public void onResponse(Call<Response<List<SinhVien>>> call, retrofit2.Response<Response<List<SinhVien>>> response) {
                if (response.isSuccessful()){
                    if (response.body().getStatus() == 200){
                        List<SinhVien> ds = response.body().getData();
                        getData(ds);
                    }
                }
            }

            @Override
            public void onFailure(Call<Response<List<SinhVien>>> call, Throwable t) {

            }
        });
    }

    private void chooseImage() {
        Log.d("123123", "chooseAvatar: " +123123);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        getImage.launch(intent);
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

                        Glide.with(imagePiker)
                                .load(imageUri)
                                .centerCrop()
                                .into(imagePiker);

                    }
                }
            });

}