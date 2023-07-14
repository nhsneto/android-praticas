package pdm.nelson.pratica08;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ForecastActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        LinearLayout layout = findViewById(R.id.layout_forecast);

        Intent intent = getIntent();
        ArrayList<CharSequence> data = intent.getCharSequenceArrayListExtra("data");
        for (CharSequence entry : data) {
            TextView text = new TextView(this);
            text.setText(entry);
            text.setTextSize(20);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(40, 30, 0, 0);
            text.setLayoutParams(params);

            layout.addView(text);
        }
    }
}
