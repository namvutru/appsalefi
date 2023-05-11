package com.example.crudfirebase.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.crudfirebase.R;
import com.example.crudfirebase.activity.DetailActivity;
import com.example.crudfirebase.model.ItemCart;
import com.example.crudfirebase.model.Product;
import com.example.crudfirebase.session.SessionManagement;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ItemCartRecycleAdapter extends RecyclerView.Adapter<ItemCartRecycleAdapter.ViewHolder>
{
    Context context;
    ArrayList<ItemCart> cartlist;

    public ItemCartRecycleAdapter(Context context, ArrayList<ItemCart> cartlist) {
        this.context = context;
        this.cartlist = cartlist;

    }

    @NonNull
    @Override

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.cart_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemCart itemCart = cartlist.get(position);
        holder.textSoluong.setText(String.valueOf(itemCart.getSoluong()));
        DatabaseReference Ref = FirebaseDatabase.getInstance().getReference("Product");
        Product pro=new Product();
        Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    Product product = userSnapshot.getValue(Product.class);
                    if(itemCart.getIdproduct().equals(product.getId())){
                        pro.setId(product.getId());
                        pro.setDes(product.getDes());
                        pro.setSoluong(product.getSoluong());
                        pro.setAnh(product.getAnh());
                        pro.setLoai(product.getLoai());
                        pro.setTen(product.getTen());
                        pro.setGia(product.getGia());
                        holder.textTen.setText(product.getTen());
                        holder.textGia.setText("Giá: "+String.valueOf(product.getGia())+"$");
                        Glide.with(context).load(product.getAnh()).into(holder.imganh);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi ở đây
            }
        });
        holder.imganh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("myProduct",pro);
                context.startActivity(intent);
            }
        });
        holder.imgcong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionManagement sessionManagement = new SessionManagement(context);
                String username = sessionManagement.getUser();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference("Cart");

                DatabaseReference user1Ref = ref.child(username+pro.getId());


                user1Ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        ItemCart  itemCart= snapshot.getValue(ItemCart.class);
                        if(itemCart!=null){
                            int soluong = itemCart.getSoluong();
                            ref.child(username+pro.getId()).setValue(new ItemCart(username+pro.getId(),username,pro.getId(),soluong+1,pro.getGia()));
                        }else{
                            ref.child(username+pro.getId()).setValue(new ItemCart(username+pro.getId(),username,pro.getId(),1,pro.getGia()));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        System.out.println("Lỗi: " + error.getMessage());
                    }
                });
            }
        });
        holder.imgtru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionManagement sessionManagement = new SessionManagement(context);
                String username = sessionManagement.getUser();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference("Cart");

                DatabaseReference user1Ref = ref.child(username+pro.getId());


                user1Ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        ItemCart  itemCart= snapshot.getValue(ItemCart.class);

                            int soluong = itemCart.getSoluong();
                            if(soluong==1){
                                ref.child(username+pro.getId()).removeValue();
                            }else {
                                ref.child(username+pro.getId()).setValue(new ItemCart(username+pro.getId(),username,pro.getId(),soluong-1,pro.getGia()));
                            }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        System.out.println("Lỗi: " + error.getMessage());
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return cartlist.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView textTen;
        TextView textGia;
        TextView textSoluong;
        ImageView imganh,imgcong,imgtru;

        public ViewHolder(@NonNull View proview){
            super(proview);
            imgcong = proview.findViewById(R.id.imgcartcong);
            imgtru = proview.findViewById(R.id.imgcarttru);
            textTen=proview.findViewById(R.id.textTencart);
            textGia= proview.findViewById(R.id.textGiacart);
            textSoluong= proview.findViewById(R.id.textSoluongcart);
            imganh = proview.findViewById(R.id.imgViewProductcart);


        }
    }


}
