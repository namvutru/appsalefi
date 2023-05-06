package com.example.crudfirebase.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crudfirebase.R;
import com.example.crudfirebase.model.ItemCart;
import com.example.crudfirebase.model.Product;
import com.example.crudfirebase.model.User;
import com.example.crudfirebase.session.SessionManagement;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountActivity extends AppCompatActivity {
    TextView gmail;
    EditText editten,editdiachi,editsdt;
    Button update;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        anhxa();
        SessionManagement sessionManagement = new SessionManagement(getApplicationContext());
        String username = sessionManagement.getUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("User");
        DatabaseReference user1Ref = ref.child(username);

        user1Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                User user= snapshot.getValue(User.class);
                if(user==null) {
                    ref.child(username).setValue(new User(username + "@gmail.com", "", "", ""));
                }
                User user1 = snapshot.getValue(User.class);
                gmail.setText(user1.getGmail());
                editten.setText(user1.getNameuser());
                editsdt.setText(user1.getSdt());
                editdiachi.setText(user1.getDiachi());
            }
            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Lỗi: " + error.getMessage());
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference proRef = FirebaseDatabase.getInstance().getReference("User");
                proRef.child(username).setValue(new User(gmail.getText().toString(),editten.getText().toString(),editsdt.getText().toString(),editdiachi.getText().toString()));
                Toast.makeText(AccountActivity.this, "cập nhật thông tin toàn khoản thành công", Toast.LENGTH_SHORT).show();
            }
        });


    }
    private void anhxa(){
        gmail = findViewById(R.id.textgmail);
        editten= findViewById(R.id.edit_text_name);
        editdiachi= findViewById(R.id.edit_text_address);
        editsdt= findViewById(R.id.edit_text_phone_number);
        update=findViewById(R.id.button_update);
    }
}