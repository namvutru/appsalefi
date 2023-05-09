package com.example.crudfirebase.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.crudfirebase.R;
import com.example.crudfirebase.model.DonHang;
import com.example.crudfirebase.model.ItemCart;
import com.example.crudfirebase.model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ItemOrderRecycleAdapter extends RecyclerView.Adapter<ItemOrderRecycleAdapter.ViewHolder>{
    Context context;
    ArrayList<DonHang> listdonhang;

    public ItemOrderRecycleAdapter(Context context, ArrayList<DonHang> listdonhang) {
        this.context = context;
        this.listdonhang = listdonhang;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.order_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemOrderRecycleAdapter.ViewHolder holder, int position) {
        DonHang donHang = listdonhang.get(position);
        holder.textSoluongcart.setText(String.valueOf(donHang.getSoluong()));
        holder.textThoigian.setText(donHang.getThoigian());
        DatabaseReference Ref = FirebaseDatabase.getInstance().getReference("Product");
        Product pro=new Product();
        Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    Product product = userSnapshot.getValue(Product.class);
                    if(donHang.getIdSanpham().equals(product.getId())){
                        pro.setId(product.getId());
                        pro.setDes(product.getDes());
                        pro.setSoluong(product.getSoluong());
                        pro.setAnh(product.getAnh());
                        pro.setLoai(product.getLoai());
                        pro.setTen(product.getTen());
                        pro.setGia(product.getGia());
                        holder.textTenorder.setText(product.getTen());
                        holder.textGiaorder.setText(String.valueOf(product.getGia()));
                        Glide.with(context).load(product.getAnh()).into(holder.imgproduct);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi ở đây
            }
        });
        holder.chitiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return listdonhang.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgproduct;
        TextView textThoigian,textTenorder,textGiaorder,textSoluongcart,chitiet;
        public ViewHolder(View view) {
            super(view);
            imgproduct= view.findViewById(R.id.imgViewProduct);
            textThoigian= view.findViewById(R.id.textThoigian);
            textTenorder = view .findViewById(R.id.textTenorder);
            textGiaorder = view.findViewById(R.id.textGiaorder);
            textSoluongcart= view.findViewById(R.id.textSoluongcart);
            chitiet = view.findViewById(R.id.chitiet);
        }
    }
}
