package daewhan.example.com.mp_final_project;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class AllMapsActivity extends FragmentActivity implements OnMapReadyCallback {
    //database 객체들
    SQLiteDatabase db;
    String dbName = "idList.db";
    String tableName = "idListTable";
    int dbMode = Context.MODE_PRIVATE;

    //MAP
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_maps);

        //Database 생성 및 열기
        db = openOrCreateDatabase(dbName, dbMode, null);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        final int cnt = getSize(); // 총 등록된 데이터 개수

        //MyLocation.java의 추상클래스 구현
        MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
            @Override
            public void gotLocation(Location location) {
                for(int i =0; i < cnt; i++){
                drawMarker(location, i);
                }
            }
        };
        MyLocation myLocation = new MyLocation();
        myLocation.getLocation(getApplicationContext(), locationResult);

    }


    private void drawMarker(Location location, int i) {
        //mMap.clear();//기존 마커 지우기

        double mlat = Double.parseDouble(selectLat(i));
        double mlng = Double.parseDouble(selectLng(i));

        LatLng savedPosition = new LatLng(mlat, mlng);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(savedPosition, 17));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);

        //마커 추가
        mMap.addMarker(new MarkerOptions()
                .position(savedPosition)
                .snippet("Lat:" + mlat + "Lng:" + mlng)
                        //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title("저장된위치"));
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

        // Add a marker in 서울 and move the camera
        LatLng SEOUL = new LatLng( 37.56, 126.97);
        mMap.addMarker(new MarkerOptions().position(SEOUL).title("Marker in Seoul"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
    }

    //Data개수 반환하는 함수
    public int getSize() {
        String sql = "SELECT  * FROM " + tableName;
        Cursor result = db.rawQuery(sql, null);
        int a = result.getCount();
        return a;
    }

    // DataBase 안의 Latitude 읽기(꺼내오기)
    public String selectLat(int index) {
        String sql = "select * from " + tableName + " where id = " + (index+1) + ";";
        Cursor result = db.rawQuery(sql, null);


        // result(Cursor 객체)가 비어 있으면 false 리턴
        if (result.moveToFirst()) {
            int id = result.getInt(0);
            String name = result.getString(2);
//            Toast.makeText(this, "index= " + id + " name=" + name, Toast.LENGTH_LONG).show();
            Log.d("lab_sqlite", "\"index= \" + id + \" name=\" + name ");

            return name;
        }
        result.close();
        return null;
    }

    //DataBase 안의 Longitude 읽기(꺼내오기)
    public String selectLng(int index) {
        String sql = "select * from " + tableName + " where id = " + (index+1) + ";";
        Cursor result = db.rawQuery(sql, null);


        // result(Cursor 객체)가 비어 있으면 false 리턴
        if (result.moveToFirst()) {
            int id = result.getInt(0);
            String name = result.getString(3);
//            Toast.makeText(this, "index= " + id + " name=" + name, Toast.LENGTH_LONG).show();
            Log.d("lab_sqlite", "\"index= \" + id + \" name=\" + name ");

            return name;
        }
        result.close();
        return null;
    }
}
