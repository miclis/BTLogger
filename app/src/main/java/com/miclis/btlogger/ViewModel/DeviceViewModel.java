package com.miclis.btlogger.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import com.miclis.btlogger.Model.BtDevice;
import com.miclis.btlogger.Model.BtService;
import com.miclis.btlogger.Model.Repository;

import java.util.List;

public class DeviceViewModel extends AndroidViewModel {

	private Repository repository;
	private LiveData<List<BtDevice>> allDevices;

	public DeviceViewModel(@NonNull Application application){
		super(application);
		repository = new Repository(application);
		allDevices = repository.getAllDevices();
	}

	public void startScanning(){
		repository.startScanning(getApplication());
	}
	public void stopScanning(){
		repository.stopScanning(getApplication());
	}

	public void setMaxScanRange(int range){
		BtService.setMaxScanRange(Short.valueOf("-90"));
	}

	// Database API
	public void insert(BtDevice device){repository.insert(device);}

	public void update(BtDevice device){repository.update(device);}

	public void delete(BtDevice device){repository.delete(device);}

	public void deleteAll(){repository.deleteAllDevices();}

	public LiveData<List<BtDevice>> getAllDevices(){return allDevices;}
}
