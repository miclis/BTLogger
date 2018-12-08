package com.miclis.btlogger.Model;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = BtDevice.class, version = 5, exportSchema = false)
public abstract class BtDevicesDatabase extends RoomDatabase {

	private static BtDevicesDatabase instance;

	public abstract BtDeviceDao deviceDao();

	public static synchronized BtDevicesDatabase getInstance(Context context){
		if(instance == null){
			instance = Room.databaseBuilder(context.getApplicationContext(), BtDevicesDatabase.class,
					"devices_database")
					.fallbackToDestructiveMigration()
					.addCallback(callback)
					.build();
		}
		return instance;
	}

	private static RoomDatabase.Callback callback = new RoomDatabase.Callback(){
		@Override
		public void onCreate(@NonNull SupportSQLiteDatabase db) {
			super.onCreate(db);
			new PopulateDbAsyncTask(instance).execute();
		}
	};

	private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>{

		private BtDeviceDao deviceDao;

		private PopulateDbAsyncTask(BtDevicesDatabase db){
			deviceDao = db.deviceDao();
		}

		@Override
		protected Void doInBackground(Void... voids) {
			deviceDao.insert(new BtDevice("Honor 9","U800", "00:11:22:33:44:55",
					BluetoothDevice.DEVICE_TYPE_CLASSIC, new Short("-30")));
			deviceDao.insert(new BtDevice("Honor 9","U900", "02:11:72:33:44:55",
					BluetoothDevice.DEVICE_TYPE_CLASSIC, new Short("-37")));
			deviceDao.insert(new BtDevice("Honor 9","U910", "03:11:22:35:44:55",
					BluetoothDevice.DEVICE_TYPE_DUAL, new Short("-30")));
			deviceDao.insert(new BtDevice("Honor 9","U920", "01:11:22:33:44:55",
					BluetoothDevice.DEVICE_TYPE_UNKNOWN, new Short("-40")));
			return null;
		}
	}
}
