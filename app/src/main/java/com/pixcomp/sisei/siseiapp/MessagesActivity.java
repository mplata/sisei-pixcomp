package com.pixcomp.sisei.siseiapp;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pixcomp.sisei.siseiapp.adapters.MessageAdapter;
import com.pixcomp.sisei.siseiapp.data.UserManager;
import com.pixcomp.sisei.siseiapp.data.dto.Message;
import com.pixcomp.sisei.siseiapp.data.dto.User;

import java.util.ArrayList;
import java.util.Date;

public class MessagesActivity extends AppCompatActivity implements ChildEventListener {

    private EditText txtNickname;
    private EditText txtMessage;

    private static String USERS_REF = "users";
    private static String MESSAGES_REF = "messages";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference usersRef = database.getReference(USERS_REF);
    DatabaseReference userChild;
    DatabaseReference messagesChild;

    User currentUser;

    private MessageAdapter adapter;
    private ListView messageListView;
    private ArrayList<Message> messages;
    private Button btnEnviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        overridePendingTransition(R.anim.anim_slide_rightin, R.anim.anim_slide_leftout);
        currentUser = UserManager.getInstance().getCurrentUser();

        txtNickname = (EditText)findViewById(R.id.txtNickname);
        txtMessage = (EditText)findViewById(R.id.txtMessage);
        messageListView = (ListView)findViewById(R.id.message_list);
        btnEnviar = (Button) findViewById(R.id.btnSendMessage);
        txtNickname.clearFocus();

        String firebaseUid = currentUser.getFirebaseUid();
        userChild = usersRef.child(firebaseUid);

        messagesChild = database.getReference(MESSAGES_REF);

        userChild.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(User.class);
                txtNickname.setText(currentUser.getNickname());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        txtNickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                currentUser.setNickname(charSequence.toString());
                userChild.setValue(currentUser);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        messagesChild.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messages = new ArrayList<Message>();
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    Message message = messageSnapshot.getValue(Message.class);
                    messages.add(message);
                }
                adapter = new MessageAdapter(MessagesActivity.this,R.layout.message_layout,messages);
                messageListView.setAdapter(adapter);
                messageListView.setSelection(messages.size()-1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        messagesChild.addChildEventListener(this);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_slide_leftin, R.anim.anim_slide_rightout);
    }

    @Override
    public void onBackPressed() {
        backBehavior();
    }

    private void backBehavior(){
        showAlert();
    }

    private void showAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.alert_message))
                .setTitle(getString(R.string.alert_title));
        builder.setPositiveButton(getString(R.string.alert_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        builder.setNegativeButton(getString(R.string.alert_notok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                moveTaskToBack(true);
            }
        });
        AlertDialog dialog = builder.create();
        //dialog.setCancelable(false);
        //dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void addMessage(View v){

        String message = txtMessage.getText().toString();
        if(message.isEmpty()){
            Toast.makeText(MessagesActivity.this, "Mensaje vacio",Toast.LENGTH_SHORT).show();
            return;
        }
        Message messageDto = new Message();
        messageDto.setDate(new Date().getTime());
        messageDto.setMessage(message);
        messageDto.setUserUid(currentUser.getFirebaseUid());
        messageDto.setUserNickname(currentUser.getNickname());

        DatabaseReference newMessage = messagesChild.push();
        Task<Void> task = newMessage.setValue(messageDto);
        task.addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(MessagesActivity.this, "Mensaje enviado",Toast.LENGTH_SHORT).show();
                txtMessage.setText("");
                txtMessage.clearFocus();
            }
        });
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
