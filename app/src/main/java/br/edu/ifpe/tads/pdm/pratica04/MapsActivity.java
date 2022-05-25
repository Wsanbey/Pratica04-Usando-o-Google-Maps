package br.edu.ifpe.tads.pdm.pratica04;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Date;

import br.edu.ifpe.tads.pdm.pratica04.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final Object FINE_LOCATION_REQUEST = 1;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private boolean fine_location;

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

    //Esse método verifica se já temos a permissão que queremos
    private void requestPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        this.fine_location = (permissionCheck == PackageManager.PERMISSION_GRANTED);

        if (this.fine_location) return;

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, (Integer) FINE_LOCATION_REQUEST);
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean granted = (grantResults.length > 0) &&
                (grantResults[0] == PackageManager.PERMISSION_GRANTED);
        this.fine_location = (requestCode == (Integer) FINE_LOCATION_REQUEST) && granted;

        if (mMap != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mMap.setMyLocationEnabled(this.fine_location);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng recife = new LatLng(-8.05, -34.9);
        LatLng caruaru = new LatLng(-8.27, -35.98);
        LatLng joaopessoa = new LatLng(-7.12, -34.84);
        mMap.addMarker(new MarkerOptions().
                position(recife).
                title("Recife").
                icon(BitmapDescriptorFactory.defaultMarker(35)));
        mMap.addMarker(new MarkerOptions().
                position(caruaru).
                title("Caruaru").
                icon(BitmapDescriptorFactory.defaultMarker(120)));
        mMap.addMarker(new MarkerOptions().
                position(joaopessoa).
                title("João Pessoa").
                icon(BitmapDescriptorFactory.defaultMarker(230)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(recife));
        //  trata o evento de click/toque nos marcadores:
        mMap.setOnMarkerClickListener(marker -> {
            Toast.makeText(MapsActivity.this,
                    "Você clicou em " + marker.getTitle(),
                    Toast.LENGTH_SHORT).show();
            //return;

            mMap.setOnMyLocationClickListener(
                    location -> Toast.makeText(MapsActivity.this,
                            "Você está aqui!", Toast.LENGTH_SHORT).show());

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

            }
            mMap.setMyLocationEnabled(this.fine_location);

            return false;
        });

        //  Adicionar marcadores com o toque do usuário
        mMap.setOnMapClickListener(latLng -> mMap.addMarker(new MarkerOptions().
                position(latLng).
                title("Adicionado em " + new Date()).
                icon(BitmapDescriptorFactory.defaultMarker(0))));

        mMap.setOnMyLocationButtonClickListener(
                () -> {
                    Toast.makeText(MapsActivity.this,
                            "Indo para a sua localização.", Toast.LENGTH_SHORT).show();
                    return false;
                });

    }
}