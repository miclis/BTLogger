package com.miclis.btlogger.View;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.miclis.btlogger.Model.BtDevice;
import com.miclis.btlogger.R;

import java.text.SimpleDateFormat;


public class DeviceAdapter extends ListAdapter<BtDevice, DeviceAdapter.DeviceHolder> {

	public DeviceAdapter(){
		super(DIFF_CALLBACK);
	}

	private static final SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd     HH:mm:ss");

	private static final DiffUtil.ItemCallback<BtDevice> DIFF_CALLBACK = new DiffUtil.ItemCallback<BtDevice>() {
		@Override
		public boolean areItemsTheSame(BtDevice oldItem, BtDevice newItem) {
			return oldItem.getId() == newItem.getId();
		}

		@Override
		public boolean areContentsTheSame(BtDevice oldItem, BtDevice newItem) {
			return oldItem.getAddress().equals(newItem.getAddress()) &&
					oldItem.getRssi().equals(newItem.getRssi()) &&
					oldItem.getTimeIn().equals(newItem.getTimeIn());
		}
	};

	@NonNull
	@Override
	public DeviceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.device_item, parent, false);
		return new DeviceHolder(itemView);
	}

	@Override
	public void onBindViewHolder(@NonNull DeviceHolder holder, int position) {
		BtDevice currentDevice = getItem(position);
		holder.textViewName.setText(currentDevice.getName());
		holder.textViewAddress.setText(currentDevice.getAddress());
		switch (currentDevice.getType()){
			case BluetoothDevice.DEVICE_TYPE_CLASSIC:
				holder.textViewType.setText(R.string.device_classic);
				break;
			case BluetoothDevice.DEVICE_TYPE_DUAL:
				holder.textViewType.setText(R.string.device_dual);
				break;
			case BluetoothDevice.DEVICE_TYPE_LE:
				holder.textViewType.setText(R.string.device_le);
				break;
				default:
					holder.textViewType.setText(R.string.device_unknown);
		}
		holder.textViewRssi.setText(String.valueOf(currentDevice.getRssi()));
		holder.textViewTime.setText(df.format(currentDevice.getTimeIn()));
		//holder.textViewTime.setText(currentDevice.getTimeIn().toString());
	}

	class DeviceHolder extends RecyclerView.ViewHolder{
		private TextView textViewName;
		private TextView textViewAddress;
		private TextView textViewType;
		private TextView textViewRssi;
		private TextView textViewTime;

		public DeviceHolder(View itemView) {
			super(itemView);
			textViewName = itemView.findViewById(R.id.text_view_name);
			textViewAddress = itemView.findViewById(R.id.text_view_address);
			textViewType = itemView.findViewById(R.id.text_view_type);
			textViewRssi = itemView.findViewById(R.id.text_view_rssi);
			textViewTime = itemView.findViewById(R.id.text_view_time);
		}
	}
}
