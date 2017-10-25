package com.pixcomp.sisei.siseiapp;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pixcomp.sisei.siseiapp.data.UserManager;

public class MainActivity extends AppCompatActivity {

    private final String FIREBASE_DEBUG = "FIREBASE";
    //Containers
    private View loginContainer;
    private View messagesContainer;

    //Login Texts
    private EditText txtEmail;
    private EditText txtPassword;

    //Firebase Authentication
    private FirebaseAuth auth;

    private Dialog progressWait;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        loginContainer = (View)findViewById(R.id.login_container);
        loginContainer.setVisibility(View.VISIBLE);

        txtEmail = (EditText)findViewById(R.id.txtEmail);
        txtPassword = (EditText)findViewById(R.id.txtPassword);

        initDialog();

    }

    @Override
    public void onBackPressed() {
        backBehavior();
    }

    private void backBehavior(){
        moveTaskToBack(true);
    }

    private void initDialog(){
        progressWait = new Dialog(this);
        progressWait.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressWait.setContentView(R.layout.lay_progressdialog);
        progressWait.setCanceledOnTouchOutside(false);
        progressWait.setCancelable(false);
    }

    public void onClikLogin(View v){

        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();
        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(MainActivity.this, "Correo o password vacios",Toast.LENGTH_SHORT).show();
            return;
        }
        progressWait.show();
        Task<AuthResult> authResultTask = auth.signInWithEmailAndPassword(email, password);
        authResultTask.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(FIREBASE_DEBUG, task.toString());
                if(task.isSuccessful()){
                    Log.d(FIREBASE_DEBUG, "Login correcto");
                    FirebaseUser firebaseUser = task.getResult().getUser();
                    UserManager instance = UserManager.getInstance();
                    instance.createUser(firebaseUser, MainActivity.this);
                    progressWait.cancel();
                }else{
                    Log.w(FIREBASE_DEBUG, "signInWithEmail:failed", task.getException());
                    Toast.makeText(MainActivity.this, "Login incorecto: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    progressWait.cancel();
                }
            }
        });
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
