package com.example.thehackbotcontrol;

import androidx.appcompat.app.AppCompatActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SelectDeviceActivity extends AppCompatActivity {
    private Button mDiscoverBtn;
    private BluetoothAdapter mBTAdapter;
    private Set<BluetoothDevice> mPairedDevices;
    private ArrayAdapter<String> mBTArrayAdapter;
    private ArrayAdapter<String> mBTArrayAdapter2;
    private final static int REQUEST_ENABLE_BT = 1; // used to identify adding bluetooth names
    private final List<BluetoothDevice> tmpBtChecker = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_device); // set the layout to select_device.xml
        ImageButton BackButton = findViewById(R.id.BackSelectDevice);
        Button BluetoothOn = findViewById(R.id.BluetoothOn);
        Button BluetoothOff = findViewById(R.id.BluetoothOff);
        mDiscoverBtn = findViewById(R.id.DiscoverDevices);
        BackButton.setOnClickListener(v -> finish());
        BluetoothOn.setOnClickListener(v -> BluetoothOn());
        BluetoothOff.setOnClickListener(v -> BluetoothOff());
        mDiscoverBtn.setOnClickListener(v -> Discover());
        mBTArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        mBTArrayAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        mBTAdapter = BluetoothAdapter.getDefaultAdapter(); // get the default Bluetooth Adapter
        ListView mDevicesListView = findViewById(R.id.paired_devices_list_view);
        ListView mDevicesListView2 = findViewById(R.id.discovered_devices_list_view);
        mDevicesListView.setAdapter(mBTArrayAdapter);
        mDevicesListView2.setAdapter(mBTArrayAdapter2);
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

        mDevicesListView2.setOnItemClickListener((parent, view, position, id) -> {
            String info = ((TextView) view).getText().toString();
            final String address = info.substring(info.length() - 17);
            final String name = info.substring(0,info.length() - 17);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("deviceName", name);
            intent.putExtra("deviceAddress", address);
            startActivity(intent);
        });
    }

    private void Discover() {
        // Check if the device is already discovering
        if(mBTAdapter.isDiscovering()){
            mBTAdapter.cancelDiscovery();
            Log.e("STATUS", "Discovery Cancelled");
            mDiscoverBtn.setText("Start Discovery");
            tmpBtChecker.clear();
        }
        else{
            if(mBTAdapter.isEnabled()) {
                mBTArrayAdapter2.clear(); // clear items
                mBTAdapter.startDiscovery();
                Log.e("STATUS", "Discovery Started");
                mDiscoverBtn.setText("Stop Discovery");


                registerReceiver(blReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
            }
            else{
                Toast.makeText(getApplicationContext(), "Bluetooth isn't turned on", Toast.LENGTH_SHORT).show();
            }
        }
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

    final BroadcastReceiver blReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mBTArrayAdapter2.notifyDataSetChanged();
                if(!tmpBtChecker.contains(device)){
                    tmpBtChecker.add(device);
                    mBTArrayAdapter2.add(device.getName()+"\n"+device.getAddress()); //add the name and address to the list
                    Log.e("Stat", "found device" + device.getName() + device.getAddress()); //Log the name and address
                }
            }
        }
    };
}