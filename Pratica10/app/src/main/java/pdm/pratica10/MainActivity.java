package pdm.pratica10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText gradeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.nameEditText = findViewById(R.id.edit_name);
        this.gradeEditText = findViewById(R.id.edit_grade);

        Button buttonSave = findViewById(R.id.button_save);
        buttonSave.setOnClickListener(this::buttonInsertClick);

        Button buttonQuery = findViewById(R.id.button_query);
        buttonQuery.setOnClickListener(this::buttonQueryClick);

        Button buttonUpdate = findViewById(R.id.button_update);
        buttonUpdate.setOnClickListener(this::buttonUpdateClick);

        Button buttonDelete = findViewById(R.id.button_delete);
        buttonDelete.setOnClickListener(this::buttonDeleteClick);
    }

    public void buttonInsertClick(View view) {
        String name = this.nameEditText.getText().toString();
        String grade = this.gradeEditText.getText().toString();

        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(name, grade);
        editor.apply();

        Toast.makeText(this, "Salvo: " + name, Toast.LENGTH_SHORT).show();
    }

    public void buttonQueryClick(View view) {
        String name = this.nameEditText.getText().toString();
        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        String grade = prefs.getString(name, "[Não encontrado]");
        this.gradeEditText.setText(grade);
    }

    public void buttonUpdateClick(View view) {
        String name = this.nameEditText.getText().toString();
        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);

        String grade = prefs.getString(name, "[Não encontrado]");
        if (grade.equals("[Não encontrado]")) {
            Toast.makeText(this, "Erro! O nome informado não existe",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String newGrade = this.gradeEditText.getText().toString();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(name, newGrade);
        editor.apply();

        Toast.makeText(this, "Atualizado com sucesso", Toast.LENGTH_SHORT).show();
    }

    public void buttonDeleteClick(View view) {
        String name = this.nameEditText.getText().toString();
        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);

        String grade = prefs.getString(name, "[Não encontrado]");
        if (grade.equals("[Não encontrado]")) {
            Toast.makeText(this, "Erro! O nome informado não existe",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(name);
        editor.apply();

        Toast.makeText(this, "Removido com sucesso", Toast.LENGTH_SHORT).show();
    }
}
