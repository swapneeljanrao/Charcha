package com.mrcoder.charcha.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mrcoder.charcha.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class GroupChatActivity extends AppCompatActivity {

    TextView groupChat_textDisplayMessage;
    EditText groupChat_editTypeMessage;
    Toolbar appBarLayout;
    ImageButton groupChat_btnSendMessage;
    ScrollView groupChat_ScrollView;
    String setCurrentGroupName, currentUserId, currentUsername, currentDate, currentTime;
    private FirebaseAuth mAuth;
    private DatabaseReference reference, groupNameReference, groupMessageKeyReference;

    public GroupChatActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        setCurrentGroupName = getIntent().getExtras().get("groupName").toString();

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("Charcha_AllUsers");
        groupNameReference = FirebaseDatabase.getInstance().getReference().child("Charcha_AllGroups").child(setCurrentGroupName);


        init();

        getCurrentUserInfo();

        groupChat_btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessagetoDatabase();

                groupChat_editTypeMessage.setText("");
                groupChat_ScrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        groupNameReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {

                if (dataSnapshot.exists()) {
                    DisplayMessages(dataSnapshot);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void init() {

        groupChat_textDisplayMessage = findViewById(R.id.groupChat_textDisplayMessage);
        groupChat_editTypeMessage = findViewById(R.id.groupChat_TypeMessage);
        appBarLayout = findViewById(R.id.group_chat_appBar);
        groupChat_btnSendMessage = findViewById(R.id.groupChat_btnSend);
        groupChat_ScrollView = findViewById(R.id.ScrollView_groupChat);
        setSupportActionBar(appBarLayout);
        getSupportActionBar().setTitle(setCurrentGroupName);
    }

    private void getCurrentUserInfo() {
        reference.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    currentUsername = dataSnapshot.child("name").getValue().toString();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendMessagetoDatabase() {
        String message = groupChat_editTypeMessage.getText().toString();
        String messageKey = groupNameReference.push().getKey();

        if (TextUtils.isEmpty(message)) {
            groupChat_btnSendMessage.setEnabled(false);
        } else {
            Calendar Date = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            currentDate = simpleDateFormat.format(Date.getTime());

            Calendar Time = Calendar.getInstance();
            SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm:ss a");
            currentTime = simpleTimeFormat.format(Time.getTime());

            HashMap<String, Object> groupMessageKey = new HashMap<>();
            groupNameReference.updateChildren(groupMessageKey);

            groupMessageKeyReference = groupNameReference.child(messageKey);

            HashMap<String, Object> messageInfoMap = new HashMap<>();
            messageInfoMap.put("name", currentUsername);
            messageInfoMap.put("message", message);
            messageInfoMap.put("date", currentDate);
            messageInfoMap.put("time", currentTime);

            groupMessageKeyReference.updateChildren(messageInfoMap);

        }
    }

    private void DisplayMessages(DataSnapshot dataSnapshot) {
        Iterator iterator = dataSnapshot.getChildren().iterator();
        while (iterator.hasNext()) {
            String chatDate = (String) ((DataSnapshot) iterator.next()).getValue();
            String chatTime = (String) ((DataSnapshot) iterator.next()).getValue();
            String chatMessage = (String) ((DataSnapshot) iterator.next()).getValue();
            String chatName = (String) ((DataSnapshot) iterator.next()).getValue();

            groupChat_textDisplayMessage.append(chatName + ":\n" + chatMessage + "\n" +chatTime + "   " + chatDate + "\n\n\n");
            groupChat_ScrollView.fullScroll(ScrollView.FOCUS_DOWN);
        }
    }
}