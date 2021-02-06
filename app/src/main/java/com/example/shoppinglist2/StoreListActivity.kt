package com.example.shoppinglist2

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppinglist2.databinding.ActivityStoreListBinding
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_store_list.*
import com.google.android.gms.maps.GoogleMap as GoogleMap1

class StoreListActivity : AppCompatActivity() {

    private lateinit var mMap: GoogleMap1

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityStoreListBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val geoclient = LocationServices.getGeofencingClient(this)
        val rtdb = FirebaseDatabase.getInstance()
        val rtdbAuth = FirebaseAuth.getInstance()
        val user = rtdbAuth.getCurrentUser()

        val ref = rtdb.getReference("users/" + user!!.uid)
        val storeRef = ref.child("stores")
        val list = arrayListOf<Store>()
        val adapter = StoreAdapter(this, list, storeRef)


        //Layout
        binding.rv2.layoutManager = LinearLayoutManager(this)
        binding.rv2.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        binding.rv2.adapter = adapter


        //button
        binding.button.setOnClickListener {

            val id = storeRef.push().key!!
            var location = Location("gps")

            val perms = arrayOf(
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            )
            if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
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
                                .title(et_store_name.text.toString())

                        val geo = Geofence.Builder().setRequestId("Geo ${id}")
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
                        val id = storeRef.push().getKey()!!
                        val store = Store(
                                id = id,
                                name = binding.etStoreName.text.toString(),
                                description = binding.etDescription.text.toString(),
                                radius = binding.etRadius.text.toString(),
                                lat = (location.latitude).toString(),
                                lng = (location.longitude).toString(),
                        )
                        storeRef.child(id).setValue(store)
                    }
        }
    }
}