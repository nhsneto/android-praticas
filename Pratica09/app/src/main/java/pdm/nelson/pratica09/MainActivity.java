package pdm.nelson.pratica09;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {

    private RequestQueue queue;
    private static final City[] cities = {
            new City("Recife"),
            new City("João Pessoa"),
            new City("Natal"),
            new City("Fortaleza"),
            new City("Salvador"),
            new City("São Luiz"),
            new City("Teresina"),
            new City("Rio de Janeiro"),
            new City("São Paulo"),
            new City("Vitória"),
            new City("Belo Horizonte"),
            new City("Florianópolis"),
            new City("Curitiba"),
            new City("Porto Alegre"),
            new City("Macapá"),
            new City("Porto Velho"),
            new City("Palmas"),
            new City("Boa Vista"),
            new City("Belém"),
            new City("Rio Branco"),
            new City("Manaus"),
            new City("Goiania"),
            new City("Cuiabá"),
            new City("Campo Grande")
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.queue = Volley.newRequestQueue(this);

        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(new CityAdapter(this, R.layout.city_listitem, cities, queue));
        listView.setOnItemClickListener((parent, view, position, id) ->
                Toast.makeText(parent.getContext(), "Cidade selecionada: "
                        + cities[position].getName(), Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onStop() {
        super.onStop();
        queue.cancelAll(this);
    }
}
