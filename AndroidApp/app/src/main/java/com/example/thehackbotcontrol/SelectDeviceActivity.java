package com.example.thehackbotcontrol;

import androidx.appcompat.app.AppCompatActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Set;

public class SelectDeviceActivity extends AppCompatActivity {
    ListView listView;
    BluetoothAdapter myBluetoothAdapter;
    public static String DEVICE_ADDRESS;
    public static String DEVICE_NAME = "TheHackBot"; //temporarily way to set device name
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_device);
        ImageButton back = (ImageButton) findViewById(R.id.Back2);
        back.setOnClickListener(v -> finish());
        listView=(ListView) findViewById(R.id.DeviceListPaired);

        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Get List of Paired Bluetooth Device
        Set<BluetoothDevice> PairedDevices=myBluetoothAdapter.getBondedDevices();
        String[] strings=new String[PairedDevices.size()];
        int index=0;

        if (PairedDevices.size()>0){
            for(BluetoothDevice device:PairedDevices) {
                strings[index]=device.getAddress();
                index++;
            }
            ArrayAdapter<String> arrayAdapter= new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, strings);
            listView.setAdapter(arrayAdapter);

            listView.setOnItemClickListener((parent, view, position, id) -> {
                DEVICE_ADDRESS = strings[position];
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("deviceName", DEVICE_NAME);
                Log.e("STAT", "Send device name to Main Activity: " + DEVICE_NAME);
                intent.putExtra("deviceAddress", DEVICE_ADDRESS);
                Log.e("STAT", "Send device address to Main Activity: " + DEVICE_ADDRESS);
                startActivity(intent);
            });
        } else {
            Toast.makeText(getApplicationContext(), "Activate Bluetooth or pair a Bluetooth device", Toast.LENGTH_SHORT).show();
        }
    }
}
