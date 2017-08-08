package com.rkhadka.hw2017mobiledev.lab7;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PingSource {

    public interface PingListener {
        void onPingsReceived(List<Ping> pingList);
    }

    private static PingSource sNewsSource;

    private Context mContext;

    public static PingSource get(Context context) {
        if (sNewsSource == null) {
            sNewsSource = new PingSource(context);
        }
        return sNewsSource;
    }

    private PingSource(Context context) {
        mContext = context;
    }

    // Firebase methods for you to implement.

    public void getPings(final PingListener pingListener) {
        final DatabaseReference pingsRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference articleRef = pingsRef.child("pings");
        Query last50PingsQuery = articleRef.limitToLast(50);
        final List<Ping> pingList = new ArrayList<Ping>();
        last50PingsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> pingSnaphots = dataSnapshot.getChildren();
                for(DataSnapshot datas : pingSnaphots){
                    Ping ping = new Ping(datas);
                    pingList.add(ping);
                }
                pingListener.onPingsReceived(pingList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getPingsForUserId(String userId, final PingListener pingListener) {
        final DatabaseReference pingsRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference articleRef = pingsRef.child("pings");
        Query userQuery = articleRef.orderByChild("userId").equalTo(userId).limitToLast(50);
        final List<Ping> pingList = new ArrayList<Ping>();
        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> pingSnaphots = dataSnapshot.getChildren();
                for(DataSnapshot datas : pingSnaphots){
                    Ping ping = new Ping(datas);
                    pingList.add(ping);
                }
                pingListener.onPingsReceived(pingList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void sendPing(Ping ping) {
        final DatabaseReference pingsRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference articleRef = pingsRef.child("pings");
        DatabaseReference newPingRef = articleRef.push();
        Map<String, Object> pingValMap = new HashMap<String, Object>();
        pingValMap.put("userId", ping.getUserId());
        pingValMap.put("userName", ping.getUserName());
        pingValMap.put("timestamp", ServerValue.TIMESTAMP);
        newPingRef.setValue(pingValMap);
    }
}
