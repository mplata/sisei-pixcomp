package com.pixcomp.sisei.siseiapp.data;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pixcomp.sisei.siseiapp.MessagesActivity;
import com.pixcomp.sisei.siseiapp.data.dto.User;

/**
 * Created by Marcos Plata on 24/10/2017.
 */

public class UserManager {

    private static UserManager instance = null;
    private static User currentUser;

    private static String USERS_REF = "users";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference usersRef = database.getReference(USERS_REF);

    private UserManager(){

    }

    public static UserManager getInstance(){
        if(instance == null){
            instance = new UserManager();
        }
        return instance;
    }

    public User getCurrentUser(){
        return currentUser;
    }

    public void createUser(final FirebaseUser firebaseUser, final Context context){
        final String uid = firebaseUser.getUid();

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean userExists = dataSnapshot.hasChild(uid);
                if(!userExists){
                    User user = new User();
                    String email = firebaseUser.getEmail();
                    user.setEmail(email);
                    user.setNickname(email);
                    user.setFirebaseUid(uid);
                    currentUser = user;
                    DatabaseReference userChild = usersRef.child(uid);
                    Task<Void> task = userChild.setValue(user);
                    task.addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent intent = new Intent(context, MessagesActivity.class);
                            context.startActivity(intent);
                        }
                    });
                }else{
                    User user = new User();
                    String email = firebaseUser.getEmail();
                    user.setEmail(email);
                    user.setNickname(email);
                    user.setFirebaseUid(uid);
                    currentUser = user;
                    Intent intent = new Intent(context, MessagesActivity.class);
                    context.startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
