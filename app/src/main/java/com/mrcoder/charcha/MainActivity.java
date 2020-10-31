package com.mrcoder.charcha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mrcoder.charcha.activity.LoginActivity;
import com.mrcoder.charcha.activity.SettingsActivity;
import com.mrcoder.charcha.adapters.TabAccessorAdapter;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    ViewPager mViewPager;
    TabLayout mTabLayout;
    private TabAccessorAdapter mTabAccessorAdapter;
    FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
        currentUser = mAuth.getCurrentUser();
        mToolbar = findViewById(R.id.main_page_toolbar);
        mTabLayout = findViewById(R.id.main_tabs);
        mViewPager = findViewById(R.id.main_page_ViewPager);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Charcha");

        mTabAccessorAdapter = new TabAccessorAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mTabAccessorAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (currentUser == null) {
            sendUserToLogin();
        } else {
            virifyUserExistance();
        }
    }

    private void virifyUserExistance() {
        String currentUserId = mAuth.getCurrentUser().getUid();
        reference.child("Charcha_AllUsers").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("name").exists()) {
                    Toast.makeText(MainActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                } else {
                    sendUserToSettings();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendUserToLogin() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.search_friend:
                Toast.makeText(this, "Seach Friend", Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings:
                sendUserToSettings();
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.createGroup:
                createGroup();
                break;
            case R.id.logout:
                mAuth.signOut();
                sendUserToLogin();
                finish();
                break;
        }
        return true;
    }

    private void createGroup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.alertDialog);
        builder.setTitle("Create New Group");
        final EditText edtGroupName = new EditText(MainActivity.this);
        edtGroupName.setHint("group name should indicate purpose");
        builder.setView(edtGroupName);
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String groupName = edtGroupName.getText().toString();
                if (TextUtils.isEmpty(groupName)) {
                    //edtGroupName.setError("Enter valid group name");
                    Toast.makeText(MainActivity.this, "Enter valid group name", Toast.LENGTH_SHORT).show();
                } else {
                    createNewGoup(groupName);
                }
            }
        });
        builder.setNegativeButton("May be later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    private void createNewGoup(String groupName) {
        reference.child("Charcha_AllGroups").child(groupName).setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, groupName + "Group is ready", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendUserToSettings() {
        Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
        //settingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(settingsIntent);
//        finish();

    }
}