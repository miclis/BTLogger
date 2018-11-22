package com.miclis.btlogger.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import com.miclis.btlogger.Model.BtDevice;
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

	// Database API
	public void insert(BtDevice device){repository.insert(device);}

	public void update(BtDevice device){repository.update(device);}

	public void delete(BtDevice device){repository.delete(device);}

	public LiveData<List<BtDevice>> getAllDevices(){return allDevices;}
}
