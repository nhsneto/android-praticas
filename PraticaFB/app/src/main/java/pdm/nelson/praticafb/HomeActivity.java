package pdm.nelson.praticafb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import pdm.nelson.praticafb.model.Message;
import pdm.nelson.praticafb.model.User;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth fbAuth;
    private FirebaseAuthListener authListener;
    private TextView txWelcome;
    private ViewGroup vgChat;
    private User user;
    private DatabaseReference drUser;
    private DatabaseReference drChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fbAuth = FirebaseAuth.getInstance();
        authListener = new FirebaseAuthListener(this);
        txWelcome = findViewById(R.id.text_welcome);
        vgChat = findViewById(R.id.chat_area);
        EditText edMessage = findViewById(R.id.edit_message);

        Button sendMessageButton = findViewById(R.id.send_message);
        sendMessageButton.setOnClickListener(view -> {
            String message = edMessage.getText().toString();
            edMessage.setText("");
            drChat.push().setValue(new Message(user.getName(), message));
        });

        Button logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(view -> buttonSignOutClick(view));

        FirebaseDatabase fbDB = FirebaseDatabase.getInstance();
        FirebaseUser fbUser = fbAuth.getCurrentUser();
        drUser = fbDB.getReference("users/" + fbUser.getUid());
        drChat = fbDB.getReference("chat");

        drUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User tempUser = dataSnapshot.getValue(User.class);
                if (tempUser != null) {
                    HomeActivity.this.user = tempUser;
                    txWelcome.setText("Welcome " + tempUser.getName() + "!");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        drChat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message message = dataSnapshot.getValue(Message.class);
                showMessage(message);
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
        });
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

    public void showMessage(Message message) {
        TextView tvMessage = new TextView(this);
        tvMessage.setText(message.getName() + ": " + message.getText());
        vgChat.addView(tvMessage);
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
