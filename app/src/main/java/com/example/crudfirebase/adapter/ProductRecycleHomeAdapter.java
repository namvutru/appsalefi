package com.example.crudfirebase.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.crudfirebase.R;
import com.example.crudfirebase.activity.DetailActivity;
import com.example.crudfirebase.model.Product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductRecycleHomeAdapter extends RecyclerView.Adapter<ProductRecycleHomeAdapter.ViewHolder> {
    Context context;
    ArrayList<Product> productlist,productOldlist;
    String username ;




    public ProductRecycleHomeAdapter(Context context, ArrayList<Product> productlist) {
        this.context = context;
        this.productlist = productlist;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.home_product_item,parent,false);
        return new ProductRecycleHomeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductRecycleHomeAdapter.ViewHolder holder, int position) {
        Product product = productlist.get(position);
       holder.textGia.setText("Giá: "+String.valueOf(product.getGia())+"$");
        holder.textLoai.setText("Loại: "+product.getLoai());
        holder.textSoluong.setText("Số lượng: "+String.valueOf(product.getSoluong()));
        holder.textTen.setText(product.getTen());
        String imageUrl = product.getAnh();
        Glide.with(context).load(imageUrl).into(holder.imgAnh);
        holder.imgAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                Product pro = new Product(product.getId(), product.getTen(), product.getAnh(), product.getDes(), product.getGia(), product.getLoai(), product.getSoluong());
                intent.putExtra("myProduct",pro);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(productlist!=null) {
            return productlist.size();
        }
        return 0;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textTen,textGia,textLoai,textSoluong;
        ImageView imgAnh;
        public ViewHolder(@NonNull View proview){
            super(proview);
            textTen=proview.findViewById(R.id.textTen);
            imgAnh=proview.findViewById(R.id.imgViewProduct);
            textGia=proview.findViewById(R.id.textGia);
            textLoai=proview.findViewById(R.id.textLoai);
            textSoluong= proview.findViewById(R.id.textSoluong);
        }
    }
//    public class ViewDialogDetail{
//
//        public void showDialog(Context context , String id ,String ten ,String anh , String des ,String username){
//            final Dialog dialog = new Dialog(context);
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            dialog.setCancelable(false);
//            dialog.setContentView(R.layout.detail_product);
//            TextView textTen = dialog.findViewById(R.id.textTenchitiet);
//            ImageView imgAnh = dialog.findViewById(R.id.imgAnhchitiet);
//            TextView textDes = dialog.findViewById(R.id.textMotachitiet);
//
//            textTen.setText(ten);
//            textDes.setText(des);
//            Glide.with(context).load(anh).into(imgAnh);
//
//            Button buttonAddtoCard = dialog.findViewById(R.id.buttonAddtocart);
//            Button buttonCancel = dialog.findViewById(R.id.buttonCancelchitiet);
//
//
//
//            buttonCancel.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    dialog.dismiss();
//                }
//            });
//            buttonAddtoCard.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(context,username, Toast.LENGTH_SHORT).show();
//                }
//            });
//
//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            dialog.show();
//        }
//
//    }

}
