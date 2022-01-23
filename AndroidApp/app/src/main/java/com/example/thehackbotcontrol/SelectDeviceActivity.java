package com.example.thehackbotcontrol;

import androidx.appcompat.app.AppCompatActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Set;

public class SelectDeviceActivity extends AppCompatActivity {
    private BluetoothAdapter mBTAdapter;
    private Set<BluetoothDevice> mPairedDevices;
    private ArrayAdapter<String> mBTArrayAdapter;
    private final static int REQUEST_ENABLE_BT = 1; // used to identify adding bluetooth names

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_device); // set the layout to select_device.xml
        ImageButton BackButton = findViewById(R.id.BackSelectDevice);
        Button BluetoothOn = findViewById(R.id.BluetoothOn);
        Button BluetoothOff = findViewById(R.id.BluetoothOff);
        BackButton.setOnClickListener(v -> finish());
        BluetoothOn.setOnClickListener(v -> BluetoothOn());
        BluetoothOff.setOnClickListener(v -> BluetoothOff());
        mBTArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        mBTAdapter = BluetoothAdapter.getDefaultAdapter(); // get the default Bluetooth Adapter
        ListView mDevicesListView = (ListView) findViewById(R.id.paired_devices_list_view);
        mDevicesListView.setAdapter(mBTArrayAdapter);
        mPairedDevices = mBTAdapter.getBondedDevices(); // Get list of Paired Bluetooth Devices

        listPairedDevices();

        mDevicesListView.setOnItemClickListener((parent, view, position, id) -> {
            String info = ((TextView) view).getText().toString();
            final String address = info.substring(info.length() - 17);
            final String name = info.substring(0,info.length() - 17);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("deviceName", name);
            intent.putExtra("deviceAddress", address);
            startActivity(intent);
        });
    }

    private void BluetoothOff() {
        mBTAdapter.disable();
    }

    private void BluetoothOn() {
        if (!mBTAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        else{
            Toast.makeText(getApplicationContext(),"Bluetooth is already enabled", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent Data) { // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, Data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) { // Make sure the request was successful
                Log.e("STAT", "Enabled Bluetooth");
                listPairedDevices(); //List the paired devices after enabling Bluetooth
            } else
                Log.e("STAT", "Bluetooth enabling failed");
        }
    }

    private void listPairedDevices() {
        mBTArrayAdapter.clear();
        mPairedDevices = mBTAdapter.getBondedDevices();
        if(mBTAdapter.isEnabled()) {
            for (BluetoothDevice device : mPairedDevices)
                mBTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
        }
        else
            Toast.makeText(getApplicationContext(), "Bluetooth isn't turned on", Toast.LENGTH_SHORT).show();
    }
}