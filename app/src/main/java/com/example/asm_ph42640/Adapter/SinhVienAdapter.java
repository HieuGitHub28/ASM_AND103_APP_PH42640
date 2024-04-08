package com.example.asm_ph42640.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.asm_ph42640.Api.ApiService;
import com.example.asm_ph42640.Home;
import com.example.asm_ph42640.Modal.Response;
import com.example.asm_ph42640.Modal.SinhVien;
import com.example.asm_ph42640.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class SinhVienAdapter extends RecyclerView.Adapter<SinhVienAdapter.viewHolder> {
    private final Context context;
    private List<SinhVien> list;
    Home home;


    public SinhVienAdapter(Context context, List<SinhVien> list) {
        this.context = context;
        this.list = list;
        home = (Home) context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_sinhvien, parent, false);
        return new viewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        if (position >= 0 && position <= list.size()) {
            SinhVien sv = list.get(position);

            holder.txtMaSV.setText("Mã SV : " + sv.getMasv());
            holder.txtNameSV.setText("Họ tên : " + sv.getName());
            holder.txtDiemTB.setText("Điểm TB : " + sv.getPoint());

            Glide.with(holder.itemView.getContext())
                    .load(sv.getAvatar())
                    .into(holder.imgAvatar);

            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String idStudent = sv.get_id();

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Cảnh báo");
                    builder.setMessage("Bạn có muốn xóa không?");
                    builder.setNegativeButton("No", null);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Call<Response<SinhVien>> call = ApiService.apiService.deleteStudent(sv.get_id());
                            call.enqueue(new Callback<Response<SinhVien>>() {
                                @Override
                                public void onResponse(Call<Response<SinhVien>> call, retrofit2.Response<Response<SinhVien>> response) {
                                    if (response.isSuccessful()){
                                        if (response.body().getStatus() == 200){
                                            list.remove(position);
                                            notifyDataSetChanged();
                                            Toast.makeText(context, response.body().getMessenger(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<Response<SinhVien>> call, Throwable t) {
                                    Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    builder.show();
                }
            });

            holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    home.showDialog(context, sv, 0, list);
                }
            });


        }
    }


    @Override
    public int getItemCount() {
        if (list.size() > 0) {
            return list.size();
        }
        return 0;
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        TextView txtNameSV, txtMaSV, txtDiemTB;
        ImageView imgAvatar;
        ImageButton btnUpdate, btnDelete;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            txtNameSV = itemView.findViewById(R.id.txtNameSV);
            txtMaSV = itemView.findViewById(R.id.txtMaSV);
            txtDiemTB = itemView.findViewById(R.id.txtDiemTB);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }


}
