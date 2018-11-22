package com.miclis.btlogger.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "devices_table")
public class BtDevice {

	@PrimaryKey(autoGenerate = true)
	private int id;

	private String name;
	private String address;
	private int type;
	private Short rssi;

	public BtDevice(String name, String address, int type, Short rssi) {
		this.name = name;
		this.address = address;
		this.type = type;
		this.rssi = rssi;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public int getType() {
		return type;
	}

	public Short getRssi() {
		return rssi;
	}
}