package pdm.pratica04;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Date;
import java.util.Locale;

import com.google.android.gms.tasks.Task;

import pdm.pratica04.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int FINE_LOCATION_REQUEST_CODE = 4;

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private boolean fineLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        requestPermission();
    }

    private void requestPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        this.fineLocation = (permissionCheck == PackageManager.PERMISSION_GRANTED);
        if (this.fineLocation) return;

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_REQUEST_CODE);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng recife = new LatLng(-8.05, -34.9);
        LatLng caruaru = new LatLng(-8.27, -35.98);
        LatLng joaoPessoa = new LatLng(-7.12, -34.84);

        mMap = googleMap;
        mMap.addMarker(new MarkerOptions()
                .position(recife)
                .title("Recife")
                .icon(BitmapDescriptorFactory.defaultMarker(35)));
        mMap.addMarker(new MarkerOptions()
                .position(caruaru)
                .title("Caruaru")
                .icon(BitmapDescriptorFactory.defaultMarker(120)));
        mMap.addMarker(new MarkerOptions()
                .position(joaoPessoa)
                .title("João Pessoa")
                .icon(BitmapDescriptorFactory.defaultMarker(230)));

        mMap.setOnMarkerClickListener(marker -> {
            Toast.makeText(MapsActivity.this, "Você clicou em " + marker.getTitle(),
                    Toast.LENGTH_SHORT).show();
            return false; // Centraliza a visualizacao no marcador
        });

        mMap.setOnMapClickListener(latLng -> mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Adicionado em " + new Date())
                .icon(BitmapDescriptorFactory.defaultMarker(0))));

        mMap.setOnMyLocationButtonClickListener(() -> {
            Toast.makeText(MapsActivity.this, "Indo para a sua localização.",
                    Toast.LENGTH_SHORT).show();
            return false;
        });

        mMap.setOnMyLocationClickListener(
                location -> Toast.makeText(MapsActivity.this, "Você está aqui!",
                        Toast.LENGTH_SHORT).show()
        );

        try {
            mMap.setMyLocationEnabled(this.fineLocation);
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        findViewById(R.id.button_location).setEnabled(this.fineLocation);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean granted = (grantResults.length > 0)
                && (grantResults[0] == PackageManager.PERMISSION_GRANTED);
        this.fineLocation = (requestCode == FINE_LOCATION_REQUEST_CODE) && granted;

        if (mMap != null) {
            try {
                mMap.setMyLocationEnabled(this.fineLocation);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }

        findViewById(R.id.button_location).setEnabled(this.fineLocation);
    }

    public void currentLocation(View view) {
        FusedLocationProviderClient fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(this);

        Task<Location> task;
        try {
            task = fusedLocationProviderClient.getLastLocation();
            task.addOnSuccessListener(location -> {
                if (location != null) {
                    String text = String.format(Locale.forLanguageTag("pt-BR"),
                            "Localização Atual:\nLat: %f Long: %f",
                            location.getLatitude(), location.getLongitude());
                    Toast.makeText(MapsActivity.this, text, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
}
