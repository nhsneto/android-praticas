package pdm.pratica10parte2;

import static pdm.pratica10parte2.db.SchoolContract.Student.*;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import pdm.pratica10parte2.db.SchoolDbHelper;

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

        SchoolDbHelper dbHelper = new SchoolDbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_STUDENT_NAME, name);
        values.put(COLUMN_NAME_STUDENT_GRADE, grade);
        long newId = db.insert(TABLE_NAME, null, values);

        Toast toast = Toast.makeText(this,
                "Registro adicionado. ID = " + newId, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void buttonQueryClick(View view) {
        String name = this.nameEditText.getText().toString();

        SchoolDbHelper dbHelper = new SchoolDbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {_ID,
                COLUMN_NAME_STUDENT_NAME,
                COLUMN_NAME_STUDENT_GRADE};
        String selection = COLUMN_NAME_STUDENT_NAME + " LIKE ?";
        String[] selectionArgs = {name + "%"};
        String sortOrder = COLUMN_NAME_STUDENT_GRADE + " DESC";
        Cursor c = db.query(TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

        ArrayList<CharSequence> data = new ArrayList<CharSequence>();
        c.moveToFirst();
        while (!c.isAfterLast()) {
            String entry = c.getInt(c.getColumnIndexOrThrow(_ID)) + ": ";
            entry += c.getString(c.getColumnIndexOrThrow(COLUMN_NAME_STUDENT_NAME)) + " = ";
            entry += c.getDouble(c.getColumnIndexOrThrow(COLUMN_NAME_STUDENT_GRADE));
            data.add(entry);
            c.moveToNext();
        }
        c.close();

        Intent intent = new Intent(this, QueryResultActivity.class);
        intent.putCharSequenceArrayListExtra("data", data);
        startActivity(intent);
    }

    public void buttonUpdateClick(View view) {
        String name = this.nameEditText.getText().toString();
        String grade = this.gradeEditText.getText().toString();

        SchoolDbHelper dbHelper = new SchoolDbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_STUDENT_GRADE, grade);

        String whereClause = COLUMN_NAME_STUDENT_NAME + " LIKE ?";
        String[] whereArgs = {name + ""};
        int count = db.update(TABLE_NAME, values, whereClause, whereArgs);

        Toast toast = Toast.makeText(this,
                "Registros atualizados: " + count, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void buttonDeleteClick(View view) {
        String name = this.nameEditText.getText().toString();

        SchoolDbHelper dbHelper = new SchoolDbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = COLUMN_NAME_STUDENT_NAME + " LIKE ?";
        String[] selectionArgs = {name + ""};
        int count = db.delete(TABLE_NAME, selection, selectionArgs);

        Toast toast = Toast.makeText(this,
                "Registros deletados: " + count, Toast.LENGTH_SHORT);
        toast.show();
    }
}
