package com.miclis.btlogger.Model;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;


public class BtService extends Service {

	public static final int SCAN_THRESHOLD = 16000; // Time between starts of scan (millis)

	private Repository repository;

	private boolean mIsScanningOn;

	private BluetoothAdapter mBluetoothAdapter;
	private BroadcastReceiver mBluetoothReceiver;

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Stop looking for BT devices
		stopScanning();
		unregisterReceiver(mBluetoothReceiver);

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		repository = new Repository(getApplication());

		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		//  Device does support BT
		if(mBluetoothAdapter.getState() != BluetoothAdapter.STATE_ON) mBluetoothAdapter.enable(); // Enables BT
		/* Broadcast receiver registration for case, when BT device is discovered */
		IntentFilter filter = new IntentFilter();
		filter.addAction(BluetoothDevice.ACTION_FOUND);
		mBluetoothReceiver = new BroadcastReceiver(){
			@Override
			public void onReceive(Context context, Intent intent) {
				if(BluetoothDevice.ACTION_FOUND.equals(intent.getAction())){
					BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					BtDevice foundDevice = new BtDevice(
							device.getName(),
							device.getAddress(),
							device.getType(),
							intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE));
					repository.insert(foundDevice);
				}
			}
		};
		registerReceiver(this.mBluetoothReceiver, filter);

		if(!mIsScanningOn){
			mIsScanningOn = true;
			new Thread(new Runnable() {
				@Override
				public void run() {
					performScan();
				}
			}).start();
		}
		return START_STICKY;
	}

	/** BT scan logic here * */
	private void performScan(){
		while(mIsScanningOn){
			try{
				if(mIsScanningOn && mBluetoothAdapter != null){
					mBluetoothAdapter.startDiscovery(); // Is performed asynchronously
				}
				Thread.sleep(SCAN_THRESHOLD);
				if(mBluetoothAdapter != null) mBluetoothAdapter.cancelDiscovery();
			} catch (InterruptedException e){
				Log.i("BT Logger", "Service Thread interrupted");
			}
		}
	}

	private void stopScanning(){
		mIsScanningOn = false;
	}
}
