package pdm.pratica10parte2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class QueryResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_result);

        LinearLayout layout = findViewById(R.id.query_result);

        Intent intent = getIntent();
        ArrayList<CharSequence> data = intent.getCharSequenceArrayListExtra("data");
        for (CharSequence entry : data) {
            TextView text = new TextView(this);
            text.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            text.setPadding(56, 24, 0, 0);
            text.setText(entry);
            text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
            text.setTypeface(text.getTypeface(), Typeface.BOLD);

            layout.addView(text);
        }
    }
}
