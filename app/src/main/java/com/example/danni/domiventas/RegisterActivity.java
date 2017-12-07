package com.example.danni.domiventas;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private EditText eEmail, ePassword, eConfirmPassword;
    private String Email, Password;

    //Autenticacion Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActionBar ab =getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        eEmail=(EditText)findViewById(R.id.eEmail);
        ePassword=(EditText)findViewById(R.id.ePass);
        eConfirmPassword=(EditText)findViewById(R.id.eConfirmPass);

    }

    public void bContinue(View view) {
        if(eEmail.getText().toString().isEmpty()){eEmail.setError("Complete this field");return;}
        if(ePassword.getText().toString().isEmpty()){ePassword.setError("Complete this field");return;}
        if(eConfirmPassword.getText().toString().isEmpty()){eConfirmPassword.setError("Complete this field");return;}
        if(!ePassword.getText().toString().equals(eConfirmPassword.getText().toString())){eConfirmPassword.setError("Passwords don't match");return;}

        if (!validarEmail(eEmail.getText().toString())){eEmail.setError("Email isn't valid");}
        Email=eEmail.getText().toString();
        Password=ePassword.getText().toString();
        mAuth.createUserWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Intent intent = new Intent(RegisterActivity.this,Register2Activity.class);
                            intent.putExtra("Email",Email);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(RegisterActivity.this, "Register failed.",Toast.LENGTH_SHORT).show();
                            return;
                        }

                    }
                });

    }
    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
