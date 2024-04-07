package com.team48.intelligentautomation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LAStatus extends AppCompatActivity {
    TextView stats;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lastatus);
        stats = findViewById(R.id.stats);
        imageView = findViewById(R.id.imageView);
        imageView.setVisibility(View.INVISIBLE);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userMail = currentUser.getEmail();
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

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("LeaveApplication").child(userMail);
//            ref.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    // Check if the dataSnapshot exists
//                    if (dataSnapshot.exists()) {
//                        // Fetch the status from dataSnapshot
//                        String status = dataSnapshot.child("status").getValue(String.class);
//                        // Update the TextView with the fetched status
//                        stats.setText("Leave Application Status: " + status);
//                        if(status.equals("Pending")){
//                            imageView.setImageResource(R.drawable.pending);
//                        } else {
//                            imageView.setImageResource(R.drawable.approved);
//                        }
//                    } else {
//                        // If dataSnapshot does not exist, show a message indicating no data found
//                        imageView.setImageResource(R.drawable.notfound);
//                        stats.setText("No leave application status found.");
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//                    // Handle any errors that occur
//                    stats.setText("Error fetching leave application status: " + databaseError.getMessage());
//                }
//            });
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Check if the dataSnapshot exists
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            // Fetch the status from each child
                            String status = childSnapshot.child("status").getValue(String.class);
                            // Update the TextView with the fetched status
                            stats.setText("Leave Application Status: " + status);
                            if(status != null && status.equals("Pending")){
                                imageView.setImageResource(R.drawable.pending);
                            } else {
                                imageView.setImageResource(R.drawable.approved);
                            }
                            // Assuming you only want to handle the status of the first child,
                            // you can break the loop after processing the first child
                            break;
                        }
                    } else {
                        // If dataSnapshot does not exist, show a message indicating no data found
                        imageView.setImageResource(R.drawable.notfound);
                        stats.setText("No leave application status found.");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle any errors that occur
                    stats.setText("Error fetching leave application status: " + databaseError.getMessage());
                }
            });


        }




    }
}
