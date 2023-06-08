package pdm.nelson.praticafb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth fbAuth;
    private FirebaseAuthListener authListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findViewById(R.id.logout_button).setOnClickListener(view -> buttonSignOutClick(view));
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

    public void buttonSignOutClick(View view) {
        FirebaseUser user = this.fbAuth.getCurrentUser();
        if (user != null) {
            this.fbAuth.signOut();
        } else {
            Toast.makeText(HomeActivity.this, "Erro!", Toast.LENGTH_SHORT).show();
        }
    }
}