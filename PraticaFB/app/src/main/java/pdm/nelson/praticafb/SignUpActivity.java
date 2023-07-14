package pdm.nelson.praticafb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import pdm.nelson.praticafb.model.User;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth fbAuth;
    private FirebaseAuthListener authListener;
    private EditText edName;
    private EditText edEmail;
    private EditText edPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        this.edName = findViewById(R.id.edit_name);
        this.edEmail = findViewById(R.id.edit_email);
        this.edPassword = findViewById(R.id.edit_password);

        Button registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(view -> buttonSignUpClick(view));

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
        String name = edName.getText().toString();
        String email = edEmail.getText().toString();
        String password = edPassword.getText().toString();

        this.fbAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    String msg = task.isSuccessful() ? "SIGN UP OK!" : "SIGN UP ERROR!";
                    Toast.makeText(SignUpActivity.this, msg, Toast.LENGTH_SHORT).show();

                    if (task.isSuccessful()) {
                        User tempUser = new User(name, email);
                        DatabaseReference drUsers = FirebaseDatabase
                                .getInstance()
                                .getReference("users");
                        drUsers.child(fbAuth.getCurrentUser().getUid())
                                .setValue(tempUser);
                    }
                });
    }
}
