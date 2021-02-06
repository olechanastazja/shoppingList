package com.example.shoppinglist2

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices


import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.store_list_element.view.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        var id = 0
        val geoclient = LocationServices.getGeofencingClient(this)

        val rtdb = FirebaseDatabase.getInstance()
        val rtdbAuth = FirebaseAuth.getInstance()
        val user = rtdbAuth.getCurrentUser()
        val ref = rtdb.getReference("users/" + user!!.uid)
        val storeRef = ref.child("stores")

        val storeListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val storeList : MutableList<Store?> = mutableListOf()
                Log.i("StoresSnap", "${snapshot.children}")
                for (storeSnapshot in snapshot.children) {
                    Log.i("StoreSnap", "lat: ${storeSnapshot.child("id")}")
                    val store = Store(
                            id = storeSnapshot.child("id").value as String,
                            name = storeSnapshot.child("name").value as String,
                            description = storeSnapshot.child("description").value as String,
                            radius = storeSnapshot.child("radius").value as String,
                            lat = storeSnapshot.child("lat").value as String,
                            lng = storeSnapshot.child("lng").value as String
                    )
                    storeList.add(store)
                }

                for (store in storeList){
                    Log.i("Store", "lat: ${store?.lat}, ${store?.lng}")
                    val marker = LatLng(store?.lat!!.toDouble(), store.lng.toDouble())
                    mMap.addMarker(MarkerOptions().position(marker).title(store.name))
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Error", "loadPost:onCancelled", databaseError.toException())
            }
        }
        storeRef.addValueEventListener(storeListener)


            var location = Location("gps")
            Log.i("location", "${location}")
            val perms = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(perms, 0)
            }


            LocationServices.getFusedLocationProviderClient(this).lastLocation
                .addOnSuccessListener {
                    location = it
                    Log.i("location", "Location: ${it.latitude}, ${it.longitude}")

                    val latLng = LatLng(location.latitude, location.longitude)
                    val place = MarkerOptions()
                        .position(latLng)
                        .title(et_map.text.toString())
                    mMap.addMarker(place)
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))

                    val geo = Geofence.Builder().setRequestId("Geo${id++}")
                        .setCircularRegion(
                            latLng.latitude,
                            latLng.longitude,
                            100F
                        )
                        .setExpirationDuration(Geofence.NEVER_EXPIRE)
                        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                        .build()

                    val geoRequest = GeofencingRequest.Builder()
                        .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                        .addGeofence(geo)
                        .build()
                    val geoPendingIntent = PendingIntent.getBroadcast(
                        this,
                        0,
                        Intent(this, GeofenceReceiver::class.java),
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                    geoclient.addGeofences(geoRequest, geoPendingIntent)
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
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.isMyLocationEnabled = true
    }
}