package com.sociale.knowme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sociale.knowme.Adapters.MessageAdapter;
import com.sociale.knowme.Models.AllMethods;
import com.sociale.knowme.Models.Message;
import com.sociale.knowme.Models.User;

import java.util.ArrayList;
import java.util.List;

public class GroupeChat extends AppCompatActivity implements View.OnClickListener{
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference msgdb;
    MessageAdapter messageAdapter;
    User u;
    List<Message> messages;

    RecyclerView rvMsg;
    EditText etMsg;
    ImageButton imgButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupe_chat);
        init();
    }
    private void init(){
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        u = new User();
        rvMsg = findViewById(R.id.recchat);
        etMsg = findViewById(R.id.msg);
        imgButton = findViewById(R.id.send);
        imgButton.setOnClickListener(this);
        messages = new ArrayList<>();

    }
    @Override
    public void onClick(View v){
        if (!TextUtils.isEmpty(etMsg.getText().toString())){
            Message msg = new Message(etMsg.getText().toString(),u.getName());
            etMsg.setText("");
            msgdb.push().setValue(msg);
        }else{
            Toast.makeText(GroupeChat.this,"You cannot send blanck message",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==R.id.menuLogout){
            auth.signOut();
            finish();
            startActivity(new Intent(GroupeChat.this,Autentification.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        final FirebaseUser currentUser = auth.getCurrentUser();
        u.setUid(currentUser.getUid());
        u.setEmail(currentUser.getEmail());

        database.getReference("Users").child(currentUser.getUid()).addListenerForSingleValueEvent((new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                u = snapshot.getValue(User.class);
                u.setUid(currentUser.getUid());
                AllMethods.name= u.getName();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }));
        msgdb = database.getReference("messages");
        msgdb.addChildEventListener(new ChildEventListener() {
            /**
             * This method is triggered when a new child is added to the location to which this listener was
             * added.
             *
             * @param snapshot          An immutable snapshot of the data at the new child location
             * @param previousChildName The key name of sibling location ordered before the new child. This
             */
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message message = snapshot.getValue(Message.class);
                message.setKey(snapshot.getKey());
                messages.add(message);
                displayMessage(messages);
            }

            /**
             * This method is triggered when the data at a child location has changed.
             *
             * @param snapshot          An immutable snapshot of the data at the new data at the child location
             * @param previousChildName The key name of sibling location ordered before the child. This will
             */
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message msg = snapshot.getValue(Message.class);
                msg.setKey(snapshot.getKey());
                List<Message> newmsg = new ArrayList<Message>();
                for (Message m:messages){
                    if(m.getKey().equals(msg.getKey())){
                        newmsg.add(msg);
                    }else{
                        newmsg.add(m);
                    }
                }
                messages = newmsg;
                displayMessage(messages);
            }

            /**
             * This method is triggered when a child is removed from the location to which this listener was
             * added.
             *
             * @param snapshot An immutable snapshot of the data at the child that was removed.
             */
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Message msg = snapshot.getValue(Message.class);
                msg.setKey(snapshot.getKey());
                List<Message> newmsg = new ArrayList<Message>();
                for (Message m:messages){
                    if(!m.getKey().equals(msg.getKey())){
                        newmsg.add(m);

                    }
                }
                messages = newmsg;
                displayMessage(messages);


            }

            /**
             * This method is triggered when a child location's priority changes. See {@link
             * DatabaseReference#setPriority(Object)} and <a
             * href="https://firebase.google.com/docs/database/android/retrieve-data#data_order"
             * target="_blank">Ordered Data</a> for more information on priorities and ordering data.
             *
             * @param snapshot          An immutable snapshot of the data at the location that moved.
             * @param previousChildName The key name of the sibling location ordered before the child
             */
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            /**
             * This method will be triggered in the event that this listener either failed at the server, or
             * is removed as a result of the security and Firebase rules. For more information on securing
             * your data, see: <a href="https://firebase.google.com/docs/database/security/quickstart"
             * target="_blank"> Security Quickstart</a>
             *
             * @param error A description of the error that occurred
             */
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        messages = new ArrayList<>();
    }

    private void displayMessage(List<Message> messages) {
        rvMsg.setLayoutManager(new LinearLayoutManager(GroupeChat.this));
        messageAdapter = new MessageAdapter(GroupeChat.this,messages,msgdb);
        rvMsg.setAdapter(messageAdapter);

    }
}