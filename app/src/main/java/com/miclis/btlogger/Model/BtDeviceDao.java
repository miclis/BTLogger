package com.miclis.btlogger.Model;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.*;

import java.util.List;

@Dao
public interface BtDeviceDao {

	@Insert
	void insert(BtDevice device);
	@Update
	void update(BtDevice device);
	@Delete
	void delete(BtDevice device);

	@Query("DELETE FROM devices_table")
	void deleteAllDevices();

	@Query("SELECT * FROM devices_table ORDER BY rssi DESC")
	LiveData<List<BtDevice>> getAllDevices();
}
