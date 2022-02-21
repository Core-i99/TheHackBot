/*
 * HC05 - Bluetooth module - MainActivity
 * Copyright (C) 2022 Stijn Rombouts
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.example.thehackbotcontrol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    private String deviceName = null;
    public static Handler handler;
    public static BluetoothSocket mmSocket;
    public static ConnectedThread connectedThread;
    public static CreateConnectThread createConnectThread;
    private int Connected = 0;
    private final static int CONNECTING_STATUS = 1; // used in bluetooth handler to identify message status
    private final static int MESSAGE_READ = 2; // used in bluetooth handler to identify message update

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton about = (ImageButton) findViewById(R.id.About);
        ImageButton settings  = (ImageButton) findViewById(R.id.settings);
        about.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        });
        settings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        // UI Initialization
        final Button buttonConnect = findViewById(R.id.buttonConnect);
        final ImageButton UpButton = findViewById(R.id.UpButton);
        UpButton.setEnabled(true);
        final ImageButton StopButton = findViewById(R.id.Stop);
        StopButton.setEnabled(false);
        final ImageButton LeftButton = findViewById(R.id.Left);
        LeftButton.setEnabled(false);
        final ImageButton RightButton = findViewById(R.id.Right);
        RightButton.setEnabled(false);
        final ImageButton DownButton = findViewById(R.id.Down);
        DownButton.setEnabled(false);

        final Toolbar toolbar = findViewById(R.id.actionBar);

        //set the toolbar subtitle
        toolbar.setSubtitle("Not Connected");

        final ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        final TextView textViewInfo = findViewById(R.id.textViewInfo);

        // If a bluetooth device has been selected from SelectDeviceActivity
        deviceName = getIntent().getStringExtra("deviceName");
        if (deviceName != null){
            // Get the device address to make BT Connection
            String deviceAddress = getIntent().getStringExtra("deviceAddress");
            // Show connection status + change StopButton visibility + change progressBar visibility + disable connect button
            toolbar.setSubtitle("Connecting to " + deviceName);
            StopButton.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            buttonConnect.setEnabled(false);

            // When "deviceName" is found call the thread to create a bluetooth connection (to the selected device)
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            createConnectThread = new CreateConnectThread(bluetoothAdapter, deviceAddress);
            createConnectThread.start();
        }

        handler = new Handler(Looper.getMainLooper()) {

            @SuppressLint("SetTextI18n")
            @Override
            public void handleMessage(Message msg){
                switch (msg.what){
                    case CONNECTING_STATUS:
                        StopButton.setVisibility(View.VISIBLE);
                        switch(msg.arg1){
                            case 1:
                                toolbar.setSubtitle("Connected to " + deviceName);
                                Connected = 1;
                                progressBar.setVisibility(View.GONE);
                                buttonConnect.setEnabled(true);
                                UpButton.setEnabled(true);
                                StopButton.setEnabled(true);
                                LeftButton.setEnabled(true);
                                RightButton.setEnabled(true);
                                DownButton.setEnabled(true);
                                break;
                            case -1:
                                toolbar.setSubtitle("Device fails to connect");
                                progressBar.setVisibility(View.GONE);
                                buttonConnect.setEnabled(true);
                                break;
                        }
                        break;

                    case MESSAGE_READ:
                        String arduinoMsg = msg.obj.toString(); // Read message from Arduino
                        switch (arduinoMsg){
                            case "FORWARD":
                                StopButton.setBackgroundColor(getResources().getColor(R.color.colorOff));
                                UpButton.setBackgroundColor(getResources().getColor(R.color.colorOn));
                                textViewInfo.setText("Arduino Message : " + arduinoMsg);
                                Log.e("STATUS", "FOO");
                                break;
                            case "LEFT":
                                StopButton.setBackgroundColor(getResources().getColor(R.color.colorOff));
                                LeftButton.setBackgroundColor(getResources().getColor(R.color.colorOn));
                                textViewInfo.setText("Arduino Message : " + arduinoMsg);
                                break;
                            case "RIGHT":
                                StopButton.setBackgroundColor(getResources().getColor(R.color.colorOff));
                                RightButton.setBackgroundColor(getResources().getColor(R.color.colorOn));
                                textViewInfo.setText("Arduino Message : " + arduinoMsg);
                                break;
                            case "BACKWARDS":
                                StopButton.setBackgroundColor(getResources().getColor(R.color.colorOff));
                                DownButton.setBackgroundColor(getResources().getColor(R.color.colorOn));
                                textViewInfo.setText("Arduino Message : " + arduinoMsg);
                                break;
                            case "STOP":
                                StopButton.setBackgroundColor(getResources().getColor(R.color.colorOn));
                                UpButton.setBackgroundColor(getResources().getColor(R.color.colorOff));
                                LeftButton.setBackgroundColor(getResources().getColor(R.color.colorOff));
                                RightButton.setBackgroundColor(getResources().getColor(R.color.colorOff));
                                DownButton.setBackgroundColor(getResources().getColor(R.color.colorOff));
                                textViewInfo.setText("Arduino Message : " + arduinoMsg);
                                break;
                        }
                        break;
                }
            }
        };
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        UpButton.setOnClickListener(view -> {
            if (Connected == 1){
                connectedThread.write(prefs.getString("Up Button","1"));
                Log.e("Status", "Ticked up");
            }
            else{ NotYetConnected(); }
        });

        StopButton.setOnClickListener(view -> {
            if (Connected == 1){
                connectedThread.write(prefs.getString("Stop Button","5"));
                Log.e("Status", "Ticked stop");
            }
            else{ NotYetConnected(); }
        });

        LeftButton.setOnClickListener(view -> {
            if (Connected == 1){
                connectedThread.write(prefs.getString("Left Button","2"));
                Log.e("Status", "Ticked left");
            }
            else{ NotYetConnected(); }
        });

        RightButton.setOnClickListener(view -> {
            if (Connected == 1){
                connectedThread.write(prefs.getString("Right Button","3"));
                Log.e("Status", "Ticked right");
            }
            else{ NotYetConnected(); }
        });

        DownButton.setOnClickListener(view -> {
            if (Connected == 1){
                connectedThread.write(prefs.getString("Down Button","4"));
                Log.e("Status", "Ticked down");
            }
            else{ NotYetConnected(); }
        });

        // Select Bluetooth Device
        buttonConnect.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SelectDeviceActivity.class);
            startActivity(intent);
        });
    }

    private void NotYetConnected() {
        Toast.makeText(getApplicationContext(),"First connect to a device", Toast.LENGTH_SHORT).show();
    }

    /* ============================ Thread to Create Bluetooth Connection =================================== */
    public static class CreateConnectThread extends Thread {

        @SuppressLint("MissingPermission")
        public CreateConnectThread(BluetoothAdapter bluetoothAdapter, String address) {
            /*
            Use a temporary object that is later assigned to mmSocket
            because mmSocket is final.
             */
            BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(address);
            BluetoothSocket tmp = null;
            @SuppressLint("MissingPermission") UUID uuid = bluetoothDevice.getUuids()[0].getUuid();

            try {
                /*
                Get a BluetoothSocket to connect with the given BluetoothDevice.
                Due to Android device varieties,the method below may not work fo different devices.
                You should try using other methods i.e. :
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
                 */
                tmp = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(uuid);

            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed", e);
            }
            mmSocket = tmp;
        }

        @SuppressLint("MissingPermission")
        public void run() {
            // Cancel discovery because it otherwise slows down the connection.
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            bluetoothAdapter.cancelDiscovery();
            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                mmSocket.connect();
                Log.e("Status", "Device connected");
                handler.obtainMessage(CONNECTING_STATUS, 1, -1).sendToTarget();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and return.
                try {
                    mmSocket.close();
                    Log.e("Status", "Cannot connect to device");
                    handler.obtainMessage(CONNECTING_STATUS, -1, -1).sendToTarget();
                } catch (IOException closeException) {
                    Log.e(TAG, "Could not close the client socket", closeException);
                }
                return;
            }

            // The connection attempt succeeded. Perform work associated with the connection in a separate thread.
            connectedThread = new ConnectedThread(mmSocket);
            connectedThread.run();
        }

        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the client socket", e);
            }
        }
    }

    /* =============================== Thread for Data Transfer =========================================== */
    public static class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException ignored) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes = 0; // bytes returned from read()
            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream from Arduino until termination character is reached. Then send the whole String message to GUI Handler.

                    buffer[bytes] = (byte) mmInStream.read();
                    String readMessage;
                    if (buffer[bytes] == '\n'){
                        readMessage = new String(buffer,0,bytes);
                        Log.e("Arduino Message",readMessage);
                        handler.obtainMessage(MESSAGE_READ,readMessage).sendToTarget();
                        bytes = 0;
                    } else {
                        bytes++;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(String input) {
            byte[] bytes = input.getBytes(); //converts entered String into bytes
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                Log.e("Send Error","Unable to send message",e);
            }
        }

    }

    /* ============================ Terminate Connection at BackPress ====================== */
    @Override
    public void onBackPressed() {
        // Terminate Bluetooth Connection and close app
        if (createConnectThread != null){
            createConnectThread.cancel();
        }
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}