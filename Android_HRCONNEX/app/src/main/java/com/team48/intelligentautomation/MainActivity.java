package com.team48.intelligentautomation;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    final double YOUR_OVERTIME_LIMIT = 4; // Overtime limit set to 8 hours

    Button wfh,leaveApplication,signout,leaveApplicationStatus;


    String userMail ="";
    boolean isCheckedIn = false;
    DatabaseReference ref;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private WifiManager wifiManager;
    private TextView statusTextView; // TextView to display user's status
    private BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // Handle the case where permission is not granted
                    return;
                }
                // Get the current Wi-Fi connection information
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                if (wifiInfo != null) {
                    String connectedSSID = wifiInfo.getSSID();
                    // Check if connected to the specific Wi-Fi network
                    if (connectedSSID != null && connectedSSID.equals("\"red\"")) {
                        // If connected, mark attendance
                        if(isCheckedIn){
                            updateStatusText("Inside Wi-Fi Range");
                        }else{
                            updateStatusText("Inside Wi-Fi Range");
                            isCheckedIn = true;
                            checkin();
                            Toast.makeText(MainActivity.this, "Attendance Marked (User entered Wi-Fi range)", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        // If not connected, unmark attendance
                        if(isCheckedIn){
                            checkout();
                        }
                        updateStatusText("Outside Wi-Fi Range");
                        //Checked Out Time
                        Toast.makeText(MainActivity.this, "Attendance Unmarked (User exited Wi-Fi range)", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    };

    private void checkout() {
        Date currentDate = new Date();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String formattedTime = timeFormat.format(currentDate);
        ref.child("checkout").setValue(formattedTime);

        // Calculate working hours
        ref.child("checkedin").get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                String checkinTimeStr = snapshot.getValue(String.class);
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                    Date checkinTime = dateFormat.parse(checkinTimeStr);
                    Date checkoutTime = dateFormat.parse(formattedTime);


                    long diffMillis = checkoutTime.getTime() - checkinTime.getTime();
                    // Convert milliseconds to hours
                    long workingHours = diffMillis / (60 * 60 * 1000);
                    ref.child("workingHour").setValue(workingHours);
                    // Check if working hours exceed the limit
                    if (workingHours > YOUR_OVERTIME_LIMIT) {
                        // Calculate overtime
                        double overtimeHours = workingHours - YOUR_OVERTIME_LIMIT;
                        double workingHours1 = YOUR_OVERTIME_LIMIT;
                        ref.child("overtime").setValue(overtimeHours);
                        ref.child("workingHour").setValue(workingHours1);

                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }).addOnFailureListener(e -> {
            // Handle failure
        });
    }


    private void checkin() {
        Date currentDate = new Date();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String formattedTime = timeFormat.format(currentDate);
        ref.child("checkedin").setValue(formattedTime);
        wfh.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        wfh = findViewById(R.id.wfh);
        signout = findViewById(R.id.signout);
        leaveApplication = findViewById(R.id.leaveApplication);
        leaveApplicationStatus = findViewById(R.id.leaveApplicationStatus);


        leaveApplicationStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,LAStatus.class);
                startActivity(i);
            }
        });
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });
        leaveApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,LeaveApplication.class);
                startActivity(i);
            }
        });
        wfh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ref.child("Working").setValue("Working From Home");
            }
        });

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        // Initialize statusTextView
        statusTextView = findViewById(R.id.statusTextView);

        // Check and request permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.CHANGE_WIFI_STATE
            }, PERMISSION_REQUEST_CODE);
        } else {
            startWifiScan();
        }
    }

    private void startWifiScan() {
        // Check if Wi-Fi is enabled before starting the scan
        if (wifiManager.isWifiEnabled()) {
            registerReceiver(wifiReceiver, new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION));
        } else {
            // Handle the case where Wi-Fi is disabled
            Toast.makeText(this, "Wi-Fi is disabled. Please enable Wi-Fi.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startWifiScan();
            } else {
                Toast.makeText(this, "Permission denied. App cannot detect Wi-Fi networks.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(wifiReceiver);
    }

    private void updateStatusText(String status) {
        statusTextView.setText("User Status: " + status);
    }
    private String getIntentString() {
        // Check if there is an intent and if it contains the desired data
        if (getIntent() != null && getIntent().hasExtra("Eid")) {
            // Retrieve the string data from the Intent
            String eid = getIntent().getStringExtra("Eid");
            // Return the retrieved string
            return eid;
        } else {
            // If no data found in the Intent, return null or handle it accordingly
            return null;
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        } else {
            Date currentDate = new Date();
            userMail = mAuth.getCurrentUser().getEmail();

            // Extract only digits after "_" and before "@" in userMail
            int underscoreIndex = userMail.indexOf("_");
            int atIndex = userMail.indexOf("@");
            if (underscoreIndex != -1 && atIndex != -1 && underscoreIndex < atIndex) {
                // Extract the substring between "_" and "@"
                userMail = userMail.substring(underscoreIndex + 1, atIndex);
            } else {
                // Handle the case where "_" or "@" is not found
                // You can either set userMail to an empty string or handle it as per your requirement
                userMail = "404";
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            String formattedDate = dateFormat.format(currentDate);
            ref = FirebaseDatabase.getInstance().getReference("Attendance").child(userMail).child(formattedDate);
        }
    }

}
