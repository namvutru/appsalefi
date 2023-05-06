package com.example.crudfirebase.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.crudfirebase.model.Product;
import com.example.crudfirebase.adapter.ProductRecycleAdapter;
import com.example.crudfirebase.R;
import com.example.crudfirebase.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DatabaseReference databaseReference;


    RecyclerView recyclerView;
    ArrayList<Product> productArrayList;
    ProductRecycleAdapter adapter;

    Button buttonAdd;



    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        databaseReference = FirebaseDatabase.getInstance().getReference();




        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        productArrayList= new ArrayList<>();
        buttonAdd = findViewById(R.id.buttonAdd1);

        gso= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(this,gso);
        GoogleSignInAccount account =GoogleSignIn.getLastSignedInAccount(this);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String mail = user.getEmail();
            String username = mail.substring(0, mail.lastIndexOf("@"));

        }
        if(account!=null){
            String mail = account.getEmail();
            String username = mail.substring(0, mail.lastIndexOf("@"));

        }

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewDialogAdd viewDialogAdd = new ViewDialogAdd();
                Toast.makeText(MainActivity.this, "namvutru", Toast.LENGTH_SHORT).show();
                viewDialogAdd.showDialog(MainActivity.this);
            }
        });



        readData();
    }
    private void logout(FirebaseUser user,GoogleSignInAccount account){
        if (user != null) {
            FirebaseAuth.getInstance().signOut();

        }
        if(account!=null){
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                finish();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
        }

    }
    private void readData() {
        DatabaseReference proRef = FirebaseDatabase.getInstance().getReference("Product");

        proRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productArrayList.clear();

                Iterable<DataSnapshot> userSnapshots = snapshot.getChildren();


                for (DataSnapshot userSnapshot : userSnapshots) {
                    Product product = userSnapshot.getValue(Product.class);
                    productArrayList.add(product);
                }

                adapter =new ProductRecycleAdapter(MainActivity.this,productArrayList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void goMainActivity(){
        finish();
        Intent intent =  new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
    }



    public class ViewDialogAdd{
        public void showDialog(Context context){
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.alert_dialog_addproduct);
            EditText textId = dialog.findViewById(R.id.textId);
            EditText textTen = dialog.findViewById(R.id.textTen);
            EditText textAnh = dialog.findViewById(R.id.textAnh);
            EditText textDes = dialog.findViewById(R.id.textDes);
            EditText textGia = dialog.findViewById(R.id.textGia);
            EditText textLoai = dialog.findViewById(R.id.textLoai);
            EditText textSoluong = dialog.findViewById(R.id.textSoluong);

            Button buttonAdd = dialog.findViewById(R.id.buttonAdd2);
            Button buttonCancel = dialog.findViewById(R.id.buttonCancel);
            buttonAdd.setText("add product");
            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            buttonAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = textId.getText().toString();
                    String ten =textTen.getText().toString();
                    String anh = textAnh.getText().toString();
                    String des =  textDes.getText().toString();
                    float gia =  Float.parseFloat(textGia.getText().toString());
                    String loai =  textLoai.getText().toString();
                    int soluong = Integer.parseInt(textSoluong.getText().toString());
                    if( id.isEmpty()||ten.isEmpty()||anh.isEmpty()||des.isEmpty()){
                        Toast.makeText(context, "nhap full cac truong", Toast.LENGTH_SHORT).show();
                    }else{
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference ref = database.getReference("Product");
                        Product pro = new Product(id,ten,anh,des,gia,loai,soluong);
                        ref.child(id).setValue(pro);
                        Toast.makeText(context, "them san pham thanh cong", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
            });
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        }

    }
}