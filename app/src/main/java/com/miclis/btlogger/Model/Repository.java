package com.miclis.btlogger.Model;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.List;

public class Repository {

	private BtDeviceDao deviceDao;
	private LiveData<List<BtDevice>> allDevices;
	private Intent BtServiceIntent;

	public Repository(Application application){
		BtDevicesDatabase database = BtDevicesDatabase.getInstance(application);
		deviceDao = database.deviceDao();
		allDevices = deviceDao.getAllDevices();
	}

	// BtService management
	public void startScanning(Application application){
		BtServiceIntent = new Intent(application, BtService.class);
		application.startService(BtServiceIntent);
	}

	public void stopScanning(Application application){
		application.stopService(BtServiceIntent);
	}

	// Api
	public void insert(BtDevice device){
		new InsertDeviceAsyncTask(deviceDao).execute(device);
	}

	public void update(BtDevice device){
		new UpdateDeviceAsyncTask(deviceDao).execute(device);
	}

	public void delete(BtDevice device){
		new DeleteDeviceAsyncTask(deviceDao).execute(device);
	}

	public void deleteAllDevices(){
		new DeleteAllDevicesAsyncTask(deviceDao).execute();
	}

	public LiveData<List<BtDevice>> getAllDevices(){
		return allDevices;
	}
	///////////////////////////////////////////////////

	private static class InsertDeviceAsyncTask extends AsyncTask<BtDevice, Void, Void> {

		private BtDeviceDao deviceDao;

		private InsertDeviceAsyncTask(BtDeviceDao deviceDao){
			this.deviceDao = deviceDao;
		}

		@Override
		protected Void doInBackground(BtDevice... devices) {
			deviceDao.insert(devices[0]);
			return null;
		}
	}

	private static class UpdateDeviceAsyncTask extends AsyncTask<BtDevice, Void, Void> {

		private BtDeviceDao deviceDao;

		private UpdateDeviceAsyncTask(BtDeviceDao deviceDao){
			this.deviceDao = deviceDao;
		}

		@Override
		protected Void doInBackground(BtDevice... devices) {
			deviceDao.update(devices[0]);
			return null;
		}
	}

	private static class DeleteDeviceAsyncTask extends AsyncTask<BtDevice, Void, Void> {

		private BtDeviceDao deviceDao;

		private DeleteDeviceAsyncTask(BtDeviceDao deviceDao){
			this.deviceDao = deviceDao;
		}

		@Override
		protected Void doInBackground(BtDevice... devices) {
			deviceDao.delete(devices[0]);
			return null;
		}
	}

	private static class DeleteAllDevicesAsyncTask extends AsyncTask<Void, Void, Void> {

		private BtDeviceDao deviceDao;

		private DeleteAllDevicesAsyncTask(BtDeviceDao deviceDao){
			this.deviceDao = deviceDao;
		}

		@Override
		protected Void doInBackground(Void... voids) {
			deviceDao.deleteAllDevices();
			return null;
		}
	}


}
