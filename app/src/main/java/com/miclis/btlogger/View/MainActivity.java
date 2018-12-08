package com.miclis.btlogger.View;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.bluetooth.BluetoothAdapter;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
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

import java.util.List;

public class MainActivity extends AppCompatActivity {

	private DeviceViewModel deviceViewModel;
	private long backPressedTime;
	private Toast exitToast;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		RecyclerView recyclerView = findViewById(R.id.recycler_view);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setHasFixedSize(true);

		final DeviceAdapter adapter = new DeviceAdapter();
		recyclerView.setAdapter(adapter);

		deviceViewModel = ViewModelProviders.of(this).get(DeviceViewModel.class);
		deviceViewModel.getAllDevices().observe(this, new Observer<List<BtDevice>>(){
			@Override
			public void onChanged(@Nullable List<BtDevice> btDevices) {
				// Update RecyclerView
				adapter.submitList(btDevices);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu_main, menu);

		final MenuItem scanSwitch = menu.findItem(R.id.app_bar_switch);
		final SwitchCompat actionView = (SwitchCompat) scanSwitch.getActionView();
		actionView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					actionView.setText(R.string.scan_on);
					deviceViewModel.startScanning();
				} else {
					actionView.setText(R.string.scan_off);
					deviceViewModel.stopScanning();
				}
			}
		});

		if(BluetoothAdapter.getDefaultAdapter() == null){
			scanSwitch.setVisible(false);
			Toast.makeText(this, "Unfortunately your device does not seem to support Bluetooth...",
					Toast.LENGTH_LONG).show();
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_settings:
				// Open scanner dialog
				openDialog();

				break;
			case R.id.action_delete_all:
				deviceViewModel.deleteAll();
				Toast.makeText(this, "All records deleted", Toast.LENGTH_SHORT).show();
				break;
			default:

		}
		return super.onOptionsItemSelected(item);
	}

	private void openDialog(){
		RangeDialog rangeDialog = new RangeDialog();
		rangeDialog.show(getSupportFragmentManager(), "Range dialog");
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
