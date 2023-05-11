package com.example.crudfirebase.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.crudfirebase.R;
import com.example.crudfirebase.adapter.ItemCartRecycleAdapter;
import com.example.crudfirebase.adapter.ProductRecycleHomeAdapter;
import com.example.crudfirebase.config.Config;
import com.example.crudfirebase.model.DonHang;
import com.example.crudfirebase.model.ItemCart;
import com.example.crudfirebase.model.Product;
import com.example.crudfirebase.session.SessionManagement;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CartActivity extends AppCompatActivity {
    RecyclerView  recyclerViewcart;
    Button btThanhtoan;
    TextView texttongtien;
    ArrayList<ItemCart> listcart;
    ItemCartRecycleAdapter adapter;
    RadioGroup radioGroup;
    float tongtien=0;

    public static final int PAYPAL_REQUEST_CODE= 7171;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //Start paypal service
        Intent intent = new Intent(this,PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);




        recyclerViewcart = findViewById(R.id.recyclerViewcart);
        btThanhtoan = findViewById(R.id.buttonThanhtoan);
        texttongtien=findViewById(R.id.texttongiten);
        radioGroup = findViewById(R.id.radiobutton);
        recyclerViewcart.setHasFixedSize(true);
        recyclerViewcart.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference proRef = FirebaseDatabase.getInstance().getReference("Cart");

        listcart= new ArrayList<>();

        readData();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton checkedRadioButton = findViewById(i);
                String selectedText = checkedRadioButton.getText().toString();
                Toast.makeText(getApplicationContext(), "Đã chọn: " + selectedText, Toast.LENGTH_SHORT).show();
                if(selectedText.equals("Thanh toán khi nhận hàng")){
                    btThanhtoan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            goOrderActivity();
                            themDonhangmoi();
                        }
                    });

                }

                if(selectedText.equals("Paypal")){
                    btThanhtoan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            processPayment();
                            themDonhangmoi();
                        }
                    });

                }

            }
        });




    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this,PayPalService.class));
        super.onDestroy();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation con = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (con != null) {
                    try {
                        String paymentDetails = con.toJSONObject().toString(4);
                        startActivity(new Intent(this, PaymentDetails.class)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount", String.valueOf(tongtien)));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED)
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Toast.makeText(this, "Invaild", Toast.LENGTH_SHORT).show();
        }
    }

    private void readData() {
        SessionManagement sessionManagement = new SessionManagement(getApplicationContext());
        String username = sessionManagement.getUser();

        DatabaseReference proRef = FirebaseDatabase.getInstance().getReference("Cart");
        proRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                listcart.clear();
                Iterable<DataSnapshot> userSnapshots = snapshot.getChildren();
                for (DataSnapshot userSnapshot : userSnapshots) {
                    ItemCart itemCart = userSnapshot.getValue(ItemCart.class);
                    if(username.equals(itemCart.getUsername())){
                        tongtien+=itemCart.getSoluong()*itemCart.getGiatien();
                        System.out.println(itemCart.getGiatien());
                        listcart.add(itemCart);
                    }
                }
                texttongtien.setText("Tổng tiền: "+String.valueOf(tongtien)+"$");
                adapter = new ItemCartRecycleAdapter(CartActivity.this,listcart);
                recyclerViewcart.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void processPayment(){
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(tongtien)),"USD","thanh toán giỏ hàng",PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this,PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(intent,PAYPAL_REQUEST_CODE);
    }
    private void themDonhangmoi(){
        SessionManagement sessionManagement = new SessionManagement(getApplicationContext());
        String username = sessionManagement.getUser();

        DatabaseReference proRef = FirebaseDatabase.getInstance().getReference("Cart");
        proRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                Iterable<DataSnapshot> userSnapshots = snapshot.getChildren();
                for (DataSnapshot userSnapshot : userSnapshots) {
                    ItemCart itemCart = userSnapshot.getValue(ItemCart.class);
                    if(username.equals(itemCart.getUsername())){
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String tTime = formatter.format(new Date());

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference ref = database.getReference("Order");
                        DonHang donHang= new DonHang(username+itemCart.getIdproduct()+tTime,username,itemCart.getIdproduct(),itemCart.getSoluong(),tTime,"0");
                        ref.child(username+itemCart.getIdproduct()+tTime).setValue(donHang);

                        DatabaseReference proRef = FirebaseDatabase.getInstance().getReference("Cart");
                        proRef.child(itemCart.getIdcart()).removeValue();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void goOrderActivity(){
        Intent intent =  new Intent(CartActivity.this, OrderActivity.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
