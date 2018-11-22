package com.miclis.btlogger.View;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.miclis.btlogger.Model.BtDevice;
import com.miclis.btlogger.R;
import com.miclis.btlogger.ViewModel.DeviceViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

	private DeviceViewModel deviceViewModel;

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
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_settings:
				// Open settings activity
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
