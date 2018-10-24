package com.dlkustovmylocatorgps.dmitry.mygpsone;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {


    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private String m_TAG = "MyTag";
    private DatabaseReference mDatabase;

    Location mLastLocation;
    Double m_dLatitude = 0.0;
    Double m_dLongitude = 0.0;
    private static String m_phoneID = "";
    Intent m_intent;
    SupportMapFragment m_mapFragment = null;// Фрагмент карты!!!

    DrawerLayout m_drawer_layaout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        m_phoneID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        mDatabase.child("users").child("phoneID_" + m_phoneID).child("phoneID").setValue(m_phoneID);


        //mDatabase.child("users").child("2").child("phoneID").setValue("R-71");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
///////////////////////////////////////////////////////////
       // android.support.v4.app.Fragment IOSFragment = new IOSFragment();
       // this.setDefaultFragment(IOSFragment);
/////////////////////////////////////////////////////////////

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
/*                mapFragment.getView().setVisibility(View.VISIBLE);

                android.support.v4.app.Fragment iosFragment = new IOSFragment();
                HideFrame(iosFragment);
                android.support.v4.app.Fragment winFragment = new WindowsFragment();
                HideFrame(winFragment);*/
            }
        });

        m_drawer_layaout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, m_drawer_layaout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        m_drawer_layaout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        m_mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        m_mapFragment.getMapAsync(this);

        /*Button iosButton = (Button)findViewById(R.id.buttonToiOs);
        iosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.Fragment iosFragment = new IOSFragment();
                replaceFragment(iosFragment);
                //final FrameLayout mymap = (FrameLayout) view.findViewById(R.id.mapF);
                m_mapFragment.getView().setVisibility(View.GONE);
            }
        });
        Button winButton = (Button)findViewById(R.id.buttonToWindows);
        winButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.Fragment winFragment = new WindowsFragment();
                replaceFragment(winFragment);
               //final FrameLayout mymap = (FrameLayout) view.findViewById(R.id.mapF);
                m_mapFragment.getView().setVisibility(View.GONE);
            }
        });*/
    }
    // This method is used to set the default fragment that will be shown.
    private void setDefaultFragment(android.support.v4.app.Fragment defaultFragment)
    {
        this.replaceFragment(defaultFragment);
    }
    // Replace current Fragment with the destination Fragment.
    public void replaceFragment(android.support.v4.app.Fragment destFragment)
    {
        // First get FragmentManager object.
        FragmentManager fragmentManager = this.getSupportFragmentManager();

        // Begin Fragment transaction.
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the layout holder with the required Fragment object.
        fragmentTransaction.replace(R.id.dynamic_fragment_frame_layout, destFragment);

        // Commit the Fragment replace action.
        fragmentTransaction.commit();
    }
    /*public void HideFrame(android.support.v4.app.Fragment destFragment)
    {
        destFragment.getView().setVisibility(View.GONE);
       // mapFragment.getView().setVisibility(View.VISIBLE);
    }*/
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mMap.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener);
        mMap.setOnMyLocationClickListener(onMyLocationClickListener);
        enableMyLocationIfPermitted();

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMinZoomPreference(1);
        Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .add(
                        new LatLng(56.8823, 37.3843),
                        new LatLng(56.8702, 37.3723),
                        new LatLng(56.8719, 37.3585),
                        new LatLng(56.8912, 37.3625),
                        new LatLng(56.8900, 37.3609),
                        new LatLng(56.8904, 37.3500),
                        new LatLng(56.8899, 37.3531)));
    }
    private void enableMyLocationIfPermitted()
    {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
        else if (mMap != null)
        {
            mMap.setMyLocationEnabled(true);
            Log.i(m_TAG, "RealLocationMy!!!");


            //DatabaseReference ref = mDatabase.child("users");
            //Query phoneQuery = ref.orderByChild(phoneNo).equalTo("+923336091371");
            //mDatabase.child("users").child("2").addValueEventListener(new ValueEventListener() {
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    DataSnapshot usersSnapshot = dataSnapshot.child("users");
                    Iterable<DataSnapshot> contactChildren = usersSnapshot.getChildren();

                    for (DataSnapshot contact : contactChildren) {
                        User user = contact.getValue(User.class);

                        Log.i(m_TAG, "User phoneID: " + user.phoneID );
                        //Log.d("contact:: ", c.name + " " + c.phone);
                        //contacts.add(c);
                    }
                    // User user = dataSnapshot.getValue(User.class);

                    //Log.i(TAG, "User name: " + user.phoneID );
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.i(m_TAG, "Failed to read value.", error.toException());
                }
            });
        }
    }

    private void showDefaultLocation() {
        Toast.makeText(this, "Location permission not granted, " +
                        "showing default location",
                Toast.LENGTH_SHORT).show();
        LatLng redmond = new LatLng(47.6739881, -122.121512);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(redmond));
        //Log.i(TAG, "Yes!!!" );
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocationIfPermitted();
                    // Log.i(TAG, "Yes!!!" );
                } else {
                    showDefaultLocation();
                }
                return;
            }

        }
    }
    private GoogleMap.OnMyLocationButtonClickListener onMyLocationButtonClickListener =
            new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    Location myLocation = mMap.getMyLocation();
                    ///// Здесь будем обновлять свою позицию и смотреть другие и маркировать их на карте!!!
                    try {
                        m_dLatitude = myLocation.getLatitude();
                        m_dLongitude = myLocation.getLongitude();
                        Log.i("MygetLatitude = ", Double.toString(m_dLatitude));
                        Log.i("MygetLongitude = ", Double.toString(m_dLongitude));
// Запишем свои данные в firebase!!! Для данного устройства!!!
                        mDatabase.child("users").child("phoneID_" + m_phoneID).child("MyLatitude").setValue(Double.toString(m_dLatitude));
                        mDatabase.child("users").child("phoneID_" + m_phoneID).child("MyLongitude").setValue(Double.toString(m_dLongitude));
                    }
                    catch (Exception ex)
                    {

                    }


/// Здесь просмотрим еще и другие позиции других планшетов!!!!!!!
                    mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {


                            DataSnapshot usersSnapshot = dataSnapshot.child("users");
                            Iterable<DataSnapshot> contactChildren = usersSnapshot.getChildren();
                            // Здесь перебераем все устройства, свое по phoneID игнорируем, а по другим
                            // выставляем маркеры!!! Как то так))))
                            for (DataSnapshot contact : contactChildren) {
                                User user = contact.getValue(User.class);

                                Log.i(m_TAG, "!!!!!!!User phoneID: " + user.phoneID );
                                // Проверка если свой phoneID
                                if(user.phoneID.compareTo(m_phoneID) == 0)
                                {
                                    Log.i(m_TAG, "Это Я!!!" );
                                }
                                else
                                {
                                    Log.i(m_TAG, "А Это НЕ Я!!!" );
                                    String stFinishString = "";
                                    Double dLatTest = Double.parseDouble(user.MyLatitude);
                                    Double dLongTest = Double.parseDouble(user.MyLongitude);
                                    mMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(dLatTest, dLongTest))
                                            .title(user.phoneID + "\nЭто ВРАГ!!!"));
                                }


                                //Log.d("contact:: ", c.name + " " + c.phone);
                                //contacts.add(c);
                            }
                            // User user = dataSnapshot.getValue(User.class);

                            //Log.i(TAG, "User name: " + user.phoneID );
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.i(m_TAG, "Failed to read value.", error.toException());
                        }
                    });





                    //Geocoder geocoder = new Geocoder(this, Locale.ROOT.getDefault());
                    mMap.setMinZoomPreference(1);
                    return false;
                }
            };

    private GoogleMap.OnMyLocationClickListener onMyLocationClickListener =
            new GoogleMap.OnMyLocationClickListener() {
                @Override
                public void onMyLocationClick(@NonNull Location location) {
                    Log.i("MyTag", "ClickedRealLocationMy!!!");
                    mMap.setMinZoomPreference(12);

                    CircleOptions circleOptions = new CircleOptions();
                    circleOptions.center(new LatLng(location.getLatitude(),
                            location.getLongitude()));

                    circleOptions.radius(200);
                    circleOptions.fillColor(Color.RED);
                    circleOptions.strokeWidth(6);

                    mMap.addCircle(circleOptions);
                }
            };
    @Override
    public void onBackPressed()
    {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Выход")
                    .setMessage("Вы действительно хотите выйти?")
                    .setPositiveButton("Да", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            finish();
                        }

                    })
                    .setNegativeButton("Нет", null)
                    .show();
            //uper.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //drawer.closeDrawers();

        if (id == R.id.nav_go_map)
        {
            try// Удаляем вид сообщений!!!
            {
                View vTemp = findViewById(R.id.fragment_massages_main);
                ViewGroup myParent = (ViewGroup) vTemp.getParent();
                if(myParent != null)
                {
                    myParent.removeView(vTemp);
                    Log.i(m_TAG, "ID = " + vTemp.getId());
                    Log.i(m_TAG, "remove fragment !!!");
                }
            }
            catch (Exception ex)
            {
                Log.i(m_TAG, ex.getMessage());
            }
            try// Удаляем вид Управление!!!
            {
                View vTemp = findViewById(R.id.fragment_controls_main);
                ViewGroup myParent = (ViewGroup) vTemp.getParent();
                if(myParent != null)
                {
                    myParent.removeView(vTemp);
                    Log.i(m_TAG, "ID = " + vTemp.getId());
                    Log.i(m_TAG, "remove fragment !!!");
                }
            }
            catch (Exception ex)
            {
                Log.i(m_TAG, ex.getMessage());
            }

            if(!m_mapFragment.isVisible())
            {
                m_mapFragment.getView().setVisibility(View.VISIBLE);
                Log.i(m_TAG, "id == R.id.nav_go_map!!!");
            }
        }
        else if (id == R.id.nav_go_points)
        {
            android.support.v4.app.Fragment tempFragment = new PointsFragmentMain();
            replaceFragment(tempFragment);
            if(m_mapFragment.isVisible())
            {
                m_mapFragment.getView().setVisibility(View.GONE);
            }
            Log.i(m_TAG, "id == R.id.nav_go_points!!!");
        }
        else if (id == R.id.nav_go_tasks)
        {
            android.support.v4.app.Fragment tempFragment = new TaskFragmentMain();
            replaceFragment(tempFragment);
            if(m_mapFragment.isVisible())
            {
                m_mapFragment.getView().setVisibility(View.GONE);
            }
            Log.i(m_TAG, "id == R.id.nav_go_tasks!!!");
        }
        else if (id == R.id.nav_go_messages)
        {
            android.support.v4.app.Fragment tempFragment = new MessagesFragmentMain();
            replaceFragment(tempFragment);
            if(m_mapFragment.isVisible())
            {
                m_mapFragment.getView().setVisibility(View.GONE);
            }
            Log.i(m_TAG, "id == R.id.nav_go_messages!!!");
        }
        else if (id == R.id.nav_go_controls)
        {
            android.support.v4.app.Fragment tempFragment  = new ControlsFragmentMain();
            replaceFragment(tempFragment);
            if(m_mapFragment.isVisible())
            {
                m_mapFragment.getView().setVisibility(View.GONE);
            }
            Log.i(m_TAG, "id == R.id.nav_go_controls!!!");

        }
        else if (id == R.id.nav_go_controls_settings)
        {
            android.support.v4.app.Fragment tempFragment  = new ControlSettingsFragmentMain();
            replaceFragment(tempFragment);
            if(m_mapFragment.isVisible())
            {
                m_mapFragment.getView().setVisibility(View.GONE);
            }
            Log.i(m_TAG, "id == R.id.nav_go_controls_settings!!!");
        }
        else if (id == R.id.nav_go_exit)
        {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Выход")
                    .setMessage("Вы действительно хотите выйти?")
                    .setPositiveButton("Да", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            finish();
                        }

                    })
                    .setNegativeButton("Нет", null)
                    .show();
            Log.i(m_TAG, "id == R.id.nav_go_exit!!!");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
