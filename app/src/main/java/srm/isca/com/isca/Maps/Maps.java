package srm.isca.com.isca.Maps;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import srm.isca.com.isca.R;

/**
 * Created by Anshul on 11-05-2016.
 */
public class Maps extends AppCompatActivity {

    private GoogleMap map;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent loc = getIntent();
        Double lt = loc.getDoubleExtra("Lat", 12);
        Double lng = loc.getDoubleExtra("Lng", 80);

        LatLng TP = new LatLng(lt,lng);

        map = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();

        Marker one = map.addMarker(new MarkerOptions().position(TP)
                .title("Event").snippet("This event is good.")) ;

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(TP, 15));
        map.animateCamera(CameraUpdateFactory.zoomTo(18),2000,null);
    }
}
