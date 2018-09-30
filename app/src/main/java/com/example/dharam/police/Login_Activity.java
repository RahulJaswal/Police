package com.example.dharam.police;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login_Activity extends AppCompatActivity
{
    EditText mail, pwd;
    Button login;
    ProgressBar progressBar;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        mail=findViewById(R.id.mail);
        pwd =findViewById(R.id.pwd);
        login=findViewById(R.id.login);
        progressBar=findViewById(R.id.loginProgress);

        login.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view)
            {
                //connected to internet or not
                if(new MyArsenal().connectivity( (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE)))
                {
                    //we are connected to a network

                        //validations from here
                        if(TextUtils.isEmpty(mail.getText().toString())| TextUtils.isEmpty(pwd.getText().toString()))
                        {
                            Toast.makeText(getApplicationContext(),"Fields are mandetory!",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            String email=mail.getText().toString().trim();
                            String password=pwd.getText().toString().trim();

                            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                            {
                                mail.setError("should be mail.");
                            }
                            else
                            {
                                progressBar.setVisibility(View.VISIBLE);
                                auth=FirebaseAuth.getInstance();
                                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task)
                                    {
                                        if(task.isSuccessful())
                                        {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            pwd.getText().clear();
                                            finish();
                                            startActivity(intent);
                                        }
                                        else
                                        {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            Toast.makeText(getApplicationContext(),"Wrong Password or Email",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        }


                }
                else
                {
                    //not connected to network
                    Snackbar.make(view, "No internet connection", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
        }
    });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth=FirebaseAuth.getInstance();

        if(auth.getCurrentUser()!=null)
        {
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(intent);
        }

    }
}
