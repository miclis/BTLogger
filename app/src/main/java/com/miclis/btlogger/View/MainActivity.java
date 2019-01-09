package com.miclis.btlogger.View;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Toast;
import com.miclis.btlogger.Model.BtDevice;
import com.miclis.btlogger.R;
import com.miclis.btlogger.ViewModel.DeviceViewModel;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import java.util.List;

public class MainActivity extends AppCompatActivity implements  EasyPermissions.PermissionCallbacks{

	private DeviceViewModel deviceViewModel;
	private long backPressedTime;
	private Toast exitToast;

	private SwitchCompat actionView;
	private BroadcastReceiver mBluetoothStateReceiver;

	private final String[] perms = {Manifest.permission.ACCESS_COARSE_LOCATION};
	private static final int PERMISSION_CODE = 111;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final RecyclerView recyclerView = findViewById(R.id.recycler_view);
		recyclerView.setLayoutManager(new SlowLinearLayoutManager(this));
		recyclerView.setHasFixedSize(true);

		final DeviceAdapter adapter = new DeviceAdapter();
		recyclerView.setAdapter(adapter);

		deviceViewModel = ViewModelProviders.of(this).get(DeviceViewModel.class);
		deviceViewModel.getAllDevices().observe(this, new Observer<List<BtDevice>>(){
			@Override
			public void onChanged(@Nullable List<BtDevice> btDevices) {
				// Update RecyclerView
				adapter.submitList(btDevices);
				if(recyclerView.computeVerticalScrollOffset() <= 20)
				{
				recyclerView.postDelayed(new Runnable() {
					@Override
					public void run() {

						// Call smooth scroll
						recyclerView.smoothScrollToPosition(0);
					}
				},500);
			}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu_main, menu);

		final MenuItem scanSwitch = menu.findItem(R.id.app_bar_switch);
		actionView = (SwitchCompat) scanSwitch.getActionView();

		// Broadcast receiver for event when scanning is on and someone turns the BT off
		mBluetoothStateReceiver = new BroadcastReceiver(){
			@Override
			public void onReceive(Context context, Intent intent) {
				if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(intent.getAction())){
					if(intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1) == BluetoothAdapter.STATE_OFF){
						actionView.setChecked(false);
						Log.i("BT Logger", "Bluetooth turned off - scanning stops.");
					}
				}
			}
		};

		actionView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					if(EasyPermissions.hasPermissions(getApplicationContext(), perms)) {
						actionView.setText(R.string.scan_on);
						deviceViewModel.startScanning();
						registerReceiver(mBluetoothStateReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
					} else {
						EasyPermissions.requestPermissions(MainActivity.this, getString(R.string.permission_message),
								PERMISSION_CODE, perms);
					}
				} else {
					actionView.setText(R.string.scan_off);
					deviceViewModel.stopScanning();
					try {
						unregisterReceiver(mBluetoothStateReceiver);
					} catch (IllegalArgumentException e){
						Log.i("BT Logger", "No need to unregister the BT broadcast receiver.");
					}
				}
			}
		});

		if(BluetoothAdapter.getDefaultAdapter() == null){
			scanSwitch.setVisible(false);
			Toast.makeText(this, getString(R.string.no_bt),
					Toast.LENGTH_LONG).show();
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_settings:
				// Open scanner dialog
				openRangeDialog();

				break;
			case R.id.action_delete_all:
				deviceViewModel.deleteAll();
				Toast.makeText(this, getString(R.string.clear), Toast.LENGTH_SHORT).show();
				break;
			default:

		}
		return super.onOptionsItemSelected(item);
	}

	private void openRangeDialog(){
		RangeDialog rangeDialog = new RangeDialog();
		rangeDialog.show(getSupportFragmentManager(), "Range dialog");
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
	}

	@Override
	public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
		actionView.setText(R.string.scan_on);
		deviceViewModel.startScanning();
		registerReceiver(mBluetoothStateReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
	}

	@Override
	public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
		actionView.setChecked(false);
		if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)){
			new AppSettingsDialog.Builder(this).build().show();
		}
	}

	@Override
	public void onBackPressed() {
		if(backPressedTime + 2000 > System.currentTimeMillis()){
			exitToast.cancel();
			try {
				deviceViewModel.stopScanning();
			} catch (NullPointerException e) {
				Log.i("BT Logger", "Service was not running...");
			}
			super.onBackPressed();
			return;
		} else {
			exitToast = Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT);
			exitToast.show();
		}
		backPressedTime = System.currentTimeMillis();
	}
}
