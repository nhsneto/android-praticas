package pdm.nelson.praticafb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth fbAuth;
    private FirebaseAuthListener authListener;
    private EditText edEmail;
    private EditText edPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.edEmail = findViewById(R.id.edit_email);
        this.edPassword = findViewById(R.id.edit_password);
        findViewById(R.id.register_button).setOnClickListener(view -> buttonSignUpClick(view));
        this.fbAuth = FirebaseAuth.getInstance();
        this.authListener = new FirebaseAuthListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        fbAuth.addAuthStateListener(authListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        fbAuth.removeAuthStateListener(authListener);
    }

    public void buttonSignUpClick(View view) {
        String email = edEmail.getText().toString();
        String password = edPassword.getText().toString();
        this.fbAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    String msg = task.isSuccessful() ? "SIGN UP OK!" :
                            "SIGN UP ERROR!";
                    Toast.makeText(SignUpActivity.this, msg,
                            Toast.LENGTH_SHORT).show();
                });
    }
}