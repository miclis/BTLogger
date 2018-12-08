package com.miclis.btlogger.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity(tableName = "devices_table")
public class BtDevice {

	@PrimaryKey(autoGenerate = true)
	@SerializedName("id")
	private int id;

	@SerializedName("instance")
	private String instanceId;
	@SerializedName("name")
	private String name;
	@SerializedName("address")
	private String address;
	@SerializedName("type")
	private int type;
	@SerializedName("rssi")
	private Short rssi;
	@SerializedName("timeIn")
	@TypeConverters(TimeConverter.class)
	private Date timeIn;

	public BtDevice(String instanceId, String name, String address, int type, Short rssi) {
		this.instanceId = instanceId;
		this.name = name;
		this.address = address;
		this.type = type;
		this.rssi = rssi;
		this.timeIn = new Date();
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public String getInstanceId() {
		return instanceId;
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

	public Date getTimeIn() {
		return timeIn;
	}

	public void setTimeIn(Date timeIn) {    // Used in constructor
		this.timeIn = timeIn;
	}
}