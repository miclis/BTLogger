package com.miclis.btlogger.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.time.LocalDateTime;
import java.util.Date;

@Entity(tableName = "devices_table")
public class BtDevice {

	@PrimaryKey(autoGenerate = true)
	private int id;

	private String name;
	private String address;
	private int type;
	private Short rssi;
	@TypeConverters(TimeConverter.class)
	private Date timeIn;
	@TypeConverters(TimeConverter.class)
	private Date timeOut;

	public Date getTimeIn() {
		return timeIn;
	}

	public void setTimeIn(Date timeIn) {
		this.timeIn = timeIn;
	}

	public Date getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(Date timeOut) {
		this.timeOut = timeOut;
	}

	public BtDevice(String name, String address, int type, Short rssi) {
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