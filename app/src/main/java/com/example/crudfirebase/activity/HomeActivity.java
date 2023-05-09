package com.example.crudfirebase.activity;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.crudfirebase.model.Product;
import com.example.crudfirebase.adapter.ProductRecycleHomeAdapter;
import com.example.crudfirebase.R;
import com.example.crudfirebase.model.User;
import com.example.crudfirebase.session.SessionManagement;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.paypal.android.sdk.ad;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    ArrayList<Product> productArrayList;
    ProductRecycleHomeAdapter adapter;

    ViewFlipper viewFlipper;
    ImageView imgcart;
    TextView textAccount,headergmail,headername;

    DrawerLayout drawerLayout ;
    Toolbar toolbar;
    NavigationView navigationView;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        anhxa();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        gso= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(this,gso);
        GoogleSignInAccount account =GoogleSignIn.getLastSignedInAccount(this);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(account!=null){
            String email = account.getEmail();
            SessionManagement sessionManagement = new SessionManagement(getApplicationContext());
            sessionManagement.setUser(email.substring(0, email.lastIndexOf("@")));
        }

        SessionManagement sessionManagement = new SessionManagement(getApplicationContext());
        textAccount.setText(sessionManagement.getUser());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("User");
        DatabaseReference user1Ref = ref.child(sessionManagement.getUser());

        user1Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                User user= snapshot.getValue(User.class);
                headergmail.setText(String.valueOf(user.getGmail()));
                headername.setText(String.valueOf(user.getNameuser()));

            }
            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Lỗi: " + error.getMessage());
            }
        });


        productArrayList= new ArrayList<>();


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case  R.id.home:
                    {
                        goHomeActivity();
                        break;
                    }
                    case  R.id.account:
                    {
                        goAccountActivity();
                        break;
                    }case  R.id.order:
                    {
                        goOrderActivity();
                        break;
                    }

                    case R.id.cart:
                    {
                        goCartActivity();
                        break;
                    }
                    case  R.id.chat:
                    {
                        break;

                    }
                    case  R.id.contact:
                    {
                        break;

                    }
                    case  R.id.logout:
                    {
                        logout(user,account);
                        break;
                    }
                }
                return false;
            }
        });

        actionToolBar();

        ActionViewFlipper();
        readData();
    }

    private void anhxa(){

        textAccount= findViewById(R.id.textAccount);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayouthome);
        toolbar=(Toolbar) findViewById(R.id.toolbarhome);
        navigationView =(NavigationView) findViewById(R.id.navigationView);
        View headerView = navigationView.getHeaderView(0);
        headername= headerView.findViewById(R.id.textUsernamemenu);
        headergmail =headerView.findViewById(R.id.textgmailmenu);
        viewFlipper = findViewById(R.id.viewflipper);


        recyclerView = findViewById(R.id.recyclerView);
    }

    private void actionToolBar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_action_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.toolbarsearch).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchProductByName(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchProductByName(s);

                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.toolbarcart) {
            goCartActivity();
            return true;
        }
        if(id== R.id.toolbarsearch){

            return true;

        }
        return super.onOptionsItemSelected(item);
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
                adapter =new ProductRecycleHomeAdapter(HomeActivity.this,productArrayList);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void searchProductByName(String productName) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Product");

        Query query = reference.orderByChild("ten").startAt(productName).endAt(productName + "\uf8ff");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ArrayList<Product> prolist = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    prolist.add(product);
                }
                adapter =new ProductRecycleHomeAdapter(HomeActivity.this,prolist);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void ActionViewFlipper() {

        ArrayList<Product> list = new ArrayList<>();
        DatabaseReference proRef = FirebaseDatabase.getInstance().getReference("Product");
        proRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> userSnapshots = snapshot.getChildren();
                for (DataSnapshot userSnapshot : userSnapshots) {
                    Product product = userSnapshot.getValue(Product.class);
                    ImageView imageView = new ImageView(getApplicationContext());
                    Glide.with(getApplicationContext()).load(product.getAnh()).into(imageView);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    viewFlipper.addView(imageView);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        Animation slide_in = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in_right);
        Animation slide_out = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out_right);
        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setOutAnimation(slide_out);

    }
    private void goHomeActivity(){
        finish();
        Intent intent =  new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
    }
    private void goOrderActivity(){
        Intent intent =  new Intent(HomeActivity.this, OrderActivity.class);
        startActivity(intent);
    }

    private void goCartActivity(){
        Intent intent =  new Intent(HomeActivity.this, CartActivity.class);
        startActivity(intent);
    }
    private void goAccountActivity(){
        Intent intent =  new Intent(getApplicationContext(), AccountActivity.class);
        startActivity(intent);
    }
    private void logout(FirebaseUser user, GoogleSignInAccount account){
        SessionManagement sessionManagement = new SessionManagement(getApplicationContext());
        sessionManagement.removeAll();
        if (user != null) {
            FirebaseAuth.getInstance().signOut();

        }
        if(account!=null){
            gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    finish();
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
