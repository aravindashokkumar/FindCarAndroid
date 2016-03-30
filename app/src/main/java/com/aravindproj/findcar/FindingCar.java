package com.aravindproj.findcar;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FindingCar extends AppCompatActivity {

    private static final String TAG = "FindingCar";
    private BluetoothAdapter BTAdapter = BluetoothAdapter.getDefaultAdapter();
//    ListView lView;
//    List<MyDevice> devices;
//    SimpleAdapter adapter;
    Switch s1;
    int count=1;
    TextView tdist;
    short rssi;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finding_car);
        registerReceiver(receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));

        tdist=(TextView)findViewById(R.id.distanceText);
        if (!BTAdapter.isEnabled()) {
            // We need to enable the Bluetooth, so we ask the user
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            // REQUEST_ENABLE_BT es un valor entero que vale 1
            startActivityForResult(enableBtIntent, 1);
        }
        s1= (Switch) findViewById(R.id.switch1);

        s1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                   BTAdapter.cancelDiscovery();
                    unregisterReceiver(receiver);
                }
                else {
                    registerReceiver(receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
                    BTAdapter.startDiscovery();
                }
            }
        });
       try{ BTAdapter.startDiscovery();

           final Handler handler = new Handler();
           handler.postDelayed(new Runnable() {
               @Override
               public void run() {
                   try {
                       TextView Rname = (TextView) findViewById(R.id.name);
                       Rname.setText("ITAG");
                       if(count==1)
                           rssi = -87;
                       rssi += 5;
                       count++;
                       Log.v(TAG, "name=" + "ITAG" + " , rssi=" + rssi);
                       ((LinearLayout) findViewById(R.id.Sig9)).setBackgroundColor(0xff4d50f9);
                       ((LinearLayout) findViewById(R.id.Sig8)).setBackgroundColor(0xff4d50f9);
                       ((LinearLayout) findViewById(R.id.Sig7)).setBackgroundColor(0xff4d50f9);
                       ((LinearLayout) findViewById(R.id.Sig6)).setBackgroundColor(0xff4d50f9);
                       ((LinearLayout) findViewById(R.id.Sig5)).setBackgroundColor(0xff4d50f9);
                       ((LinearLayout) findViewById(R.id.Sig4)).setBackgroundColor(0xff4d50f9);
                       ((LinearLayout) findViewById(R.id.Sig3)).setBackgroundColor(0xff4d50f9);
                       ((LinearLayout) findViewById(R.id.Sig2)).setBackgroundColor(0xff4d50f9);
                       ((LinearLayout) findViewById(R.id.Sig1)).setBackgroundColor(0xff4d50f9);
                       ((LinearLayout) findViewById(R.id.Sig0)).setBackgroundColor(0xff4d50f9);
                       TextView rssiset = (TextView) findViewById(R.id.rssi);
                       rssiset.setText("" + rssi);
                       if (rssi < -37) {
                           ((LinearLayout) findViewById(R.id.Sig9)).setBackgroundColor(0xfff9f9f9);
                           tdist.setText("You are around " + Math.abs(rssi) + "  metres away");
                       }
                       if (rssi < -40) {
                           ((LinearLayout) findViewById(R.id.Sig8)).setBackgroundColor(0xfff9f9f9);
                           tdist.setText("You are around " + Math.abs(rssi) + "  metres away");
                       }
                       if (rssi < -45) {
                           ((LinearLayout) findViewById(R.id.Sig7)).setBackgroundColor(0xfff9f9f9);
                           tdist.setText("You are around " + Math.abs(rssi) + "  metres away");
                       }
                       if (rssi < -50) {
                           //rssi = -33;
                           ((LinearLayout) findViewById(R.id.Sig6)).setBackgroundColor(0xfff9f9f9);
                           tdist.setText("You are around " + Math.abs(rssi) + "  metres away");
                       }
                       if (rssi < -55) {
                           //rssi = -70;
                           ((LinearLayout) findViewById(R.id.Sig5)).setBackgroundColor(0xfff9f9f9);
                           tdist.setText("You are around " + Math.abs(rssi) + "  metres away");
                       }
                       if (rssi < -60) {
                           //rssi = -49;
                           ((LinearLayout) findViewById(R.id.Sig4)).setBackgroundColor(0xfff9f9f9);
                           tdist.setText("You are around " + Math.abs(rssi) + "  metres away");
                       }
                       if (rssi < -65) {
                           //rssi = -85;
                           ((LinearLayout) findViewById(R.id.Sig3)).setBackgroundColor(0xfff9f9f9);
                           tdist.setText("You are around " + Math.abs(rssi) + "  metres away");
                       }
                       if (rssi < -75) {
                           //rssi = -58;
                           ((LinearLayout) findViewById(R.id.Sig2)).setBackgroundColor(0xfff9f9f9);
                           tdist.setText("You are around " + Math.abs(rssi) + "  metres away");
                       }
                       if (rssi < -85) {
                           //rssi = -73;
                           ((LinearLayout) findViewById(R.id.Sig1)).setBackgroundColor(0xfff9f9f9);
                           tdist.setText("You are very far away from your car around " + Math.abs(rssi) + "  metres away");
                       }
                       if (rssi == -37) {
                           tdist.setText("You have reached near your car");
                           NotificationManager manager =
                                   (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                           Intent n = new Intent(FindingCar.this, FindingCar.class);
                           PendingIntent pending = PendingIntent.getActivity(FindingCar.this, 13, n, 0);
                           Notification notification =
                                   new Notification.Builder(FindingCar.this)
                                           .setTicker("Good news! You have reached near your car.")
                                           .setSmallIcon(R.mipmap.imap)
                                           .setAutoCancel(true)
                                           .setContentTitle("Destination")
                                           .setContentText("You have now reached near your car!")
                                           .setContentIntent(pending)
                                           .build();
                           manager.notify(13, notification);

                           handler.removeCallbacks(null);



                       }
                       handler.postDelayed(this,3000l);


                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               }
           }, 5000l);


       }
       catch (Exception e){
           Log.v("BTDiscovery", "Discovery not started");
       }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_finding_car, menu);
        return true;
    }


    private final BroadcastReceiver receiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, final Intent intent) {

            final String action = intent.getAction();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                        rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
                        String name = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
                        Log.v(TAG, "name=" + name + " , rssi=" + rssi);
                        try {
                            if (name.equals("ITAG")) {
                                TextView Rname = (TextView) findViewById(R.id.name);
                                Rname.setText(name);

                                ((LinearLayout) findViewById(R.id.Sig9)).setBackgroundColor(0xff4d50f9);
                                ((LinearLayout) findViewById(R.id.Sig8)).setBackgroundColor(0xff4d50f9);
                                ((LinearLayout) findViewById(R.id.Sig7)).setBackgroundColor(0xff4d50f9);
                                ((LinearLayout) findViewById(R.id.Sig6)).setBackgroundColor(0xff4d50f9);
                                ((LinearLayout) findViewById(R.id.Sig5)).setBackgroundColor(0xff4d50f9);
                                ((LinearLayout) findViewById(R.id.Sig4)).setBackgroundColor(0xff4d50f9);
                                ((LinearLayout) findViewById(R.id.Sig3)).setBackgroundColor(0xff4d50f9);
                                ((LinearLayout) findViewById(R.id.Sig2)).setBackgroundColor(0xff4d50f9);
                                ((LinearLayout) findViewById(R.id.Sig1)).setBackgroundColor(0xff4d50f9);
                                ((LinearLayout) findViewById(R.id.Sig0)).setBackgroundColor(0xff4d50f9);
                                TextView rssiset = (TextView) findViewById(R.id.rssi);
                                rssiset.setText("" + rssi);
                                if (rssi < -37) {
                                    //rssi = -37;
                                    ((LinearLayout) findViewById(R.id.Sig9)).setBackgroundColor(0xfff9f9f9);
                                }
                                if (rssi < -40) {
                                    ((LinearLayout) findViewById(R.id.Sig8)).setBackgroundColor(0xfff9f9f9);
                                }
                                if (rssi < -45) {
                                    ((LinearLayout) findViewById(R.id.Sig7)).setBackgroundColor(0xfff9f9f9);
                                }
                                if (rssi < -50) {
                                    //rssi = -33;
                                    ((LinearLayout) findViewById(R.id.Sig6)).setBackgroundColor(0xfff9f9f9);
                                }
                                if (rssi < -55) {
                                    //rssi = -70;
                                    ((LinearLayout) findViewById(R.id.Sig5)).setBackgroundColor(0xfff9f9f9);
                                }
                                if (rssi < -60) {
                                    //rssi = -49;
                                    ((LinearLayout) findViewById(R.id.Sig4)).setBackgroundColor(0xfff9f9f9);
                                }
                                if (rssi < -65) {
                                    //rssi = -85;
                                    ((LinearLayout) findViewById(R.id.Sig3)).setBackgroundColor(0xfff9f9f9);
                                }
                                if (rssi < -75) {
                                    //rssi = -58;
                                    ((LinearLayout) findViewById(R.id.Sig2)).setBackgroundColor(0xfff9f9f9);
                                }
                                if (rssi < -85) {
                                    //rssi = -73;
                                    ((LinearLayout) findViewById(R.id.Sig1)).setBackgroundColor(0xfff9f9f9);
                                }

                                //rssi_msg.setText(name + " => " + rssi + "dBm\n");    //rssi_msg.getText() +
                                if (rssi == -37) {
                                    Toast.makeText(FindingCar.this, "You have reached near your car!", Toast.LENGTH_SHORT).show();

                                    NotificationManager manager =
                                            (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                    Intent n = new Intent(FindingCar.this, FindingCar.class);
                                    PendingIntent pending = PendingIntent.getActivity(FindingCar.this, 13, n, 0);
                                    Notification notification =
                                            new Notification.Builder(FindingCar.this)
                                                    .setTicker("Good news! You have reached near your car.")
                                                    .setSmallIcon(R.mipmap.imap)
                                                    .setAutoCancel(true)
                                                    .setContentTitle("Destination")
                                                    .setContentText("You have now reached near your car!")
                                                    .setContentIntent(pending)
                                                    .build();
                                    manager.notify(13, notification);

                                    handler.removeCallbacks(this);


                                }
                            }

                        } catch (Exception e) {
//                            Log.e("FindCar Exception", e.getMessage());
                            handler.postDelayed(this,500);

                        }
                    } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

                        Log.v("DiscFinished", "Discover Finished loop");
                    //if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action))

                    }


                    //handler.postDelayed(this, 500);

                }

            }, 1000);
        }




    };


}

