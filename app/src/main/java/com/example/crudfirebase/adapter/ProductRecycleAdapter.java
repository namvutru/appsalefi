package com.example.crudfirebase.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crudfirebase.R;
import com.example.crudfirebase.model.Product;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ProductRecycleAdapter extends RecyclerView.Adapter<ProductRecycleAdapter.ViewHolder>
{
    Context context;
    ArrayList<Product> productlist;

    DatabaseReference databaseReference ;

    public ProductRecycleAdapter(Context context, ArrayList<Product> productlist) {
        this.context = context;
        this.productlist = productlist;
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.list_product,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productlist.get(position);

        holder.textId.setText(product.getId());
        holder.textAnh.setText(product.getAnh());
        holder.textTen.setText(product.getTen());
        holder.textDes.setText(product.getDes());
        holder.textGia.setText(String.valueOf(product.getGia()));
        holder.textLoai.setText(product.getLoai());
        holder.textSoluong.setText(String.valueOf(product.getSoluong()));
        holder.buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewDialogUpdate viewDialogUpdate = new ViewDialogUpdate();
                viewDialogUpdate.showDialog(context,product.getId(), product.getTen(), product.getAnh(), product.getDes(),product.getGia(), product.getLoai(), product.getSoluong());
            }
        });
        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewDialogDelete viewDialogDelete = new ViewDialogDelete();
                viewDialogDelete.showDialog(context, product.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return productlist.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textId;
        TextView textTen;
        TextView textAnh;
        TextView textDes;
        TextView textLoai;
        TextView textGia;
        TextView textSoluong;



        Button buttonUpdate;
        Button buttonDelete;


        public ViewHolder(@NonNull View proview){
            super(proview);
            textId=proview.findViewById(R.id.textId);
            textTen=proview.findViewById(R.id.textTen);
            textAnh=proview.findViewById(R.id.textAnh);
            textDes=proview.findViewById(R.id.textDes);
            textLoai=proview.findViewById(R.id.textLoai);
            textGia= proview.findViewById(R.id.textGia);
            textSoluong= proview.findViewById(R.id.textSoluong);
           buttonDelete =proview.findViewById(R.id.buttonDelete);
           buttonUpdate = proview.findViewById(R.id.buttonUpdate);


        }
    }
    public class ViewDialogUpdate{
        public void showDialog(Context context,String id ,String ten ,String anh , String des,float gia,String loai ,int soluong){
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.alert_dialog_addproduct);
            EditText textId =  dialog.findViewById(R.id.textId);
            EditText textTen =  dialog.findViewById(R.id.textTen);
            EditText textAnh =  dialog.findViewById(R.id.textAnh);
            EditText textDes =  dialog.findViewById(R.id.textDes);
            EditText textLoai =  dialog.findViewById(R.id.textLoai);
            EditText textGia =  dialog.findViewById(R.id.textGia);
            EditText textSoLuong =  dialog.findViewById(R.id.textSoluong);

            textId.setText(id);
            textTen.setText(ten);
            textAnh.setText(anh);
            textDes.setText(des);
            textLoai.setText(loai);
            String chuoigia = String.valueOf(gia);
            String chuoisoluong = String.valueOf(soluong);
            textGia.setText(chuoigia);
            textSoLuong.setText(chuoisoluong);

            Button buttonUpdate =  dialog.findViewById(R.id.buttonAdd2);
            Button buttonCancle =  dialog.findViewById(R.id.buttonCancel);
            buttonUpdate.setText("updateproduct");
            buttonCancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            buttonUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String newid = textId.getText().toString();
                    String newten =textTen.getText().toString();
                    String newanh = textAnh.getText().toString();
                    String newdes =  textDes.getText().toString();
                    String newloai =  textLoai.getText().toString();
                    float newgia =  Float.parseFloat(textGia.getText().toString());
                    int newsoluong = Integer.parseInt(textSoLuong.getText().toString());


                    if( id.isEmpty()||ten.isEmpty()||anh.isEmpty()||des.isEmpty()){
                        Toast.makeText(context, "nhap full cac truong", Toast.LENGTH_SHORT).show();
                    }else{
                        DatabaseReference proRef = FirebaseDatabase.getInstance().getReference("Product");
                        proRef.child(id).setValue(new Product(newid,newten,newanh,newdes,newgia,newloai,newsoluong));
                        Toast.makeText(context, "sua san pham thanh cong", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
            });
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        }

    }
    public class ViewDialogDelete{
        public void showDialog(Context context,String id){
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.view_dialog_confirm_delete);


            Button buttonDelete =  dialog.findViewById(R.id.buttonDelete);
            Button buttonCancle =  dialog.findViewById(R.id.buttonCancel);

            buttonCancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseReference proRef = FirebaseDatabase.getInstance().getReference("Product");

                    proRef.child(id).removeValue();
                        Toast.makeText(context, "xoa san pham thanh cong", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                }
            });
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        }
     }
}
