package com.miclis.btlogger.View;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import com.miclis.btlogger.Model.BtService;
import com.miclis.btlogger.R;

import java.util.Objects;

public class RangeDialog extends AppCompatDialogFragment {

	private NumberPicker numberPicker;
	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
		final View view = inflater.inflate(R.layout.dialog_range,null);

		builder.setView(view).setTitle(R.string.dialog_title)
				.setNegativeButton(R.string.dialog_negative, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
					}
				})
				.setPositiveButton(R.string.dialog_positive, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						BtService.setMaxScanRange(Short.valueOf(String.valueOf(numberPicker.getValue()*-1)));
					}
				});

		numberPicker = view.findViewById(R.id.rangePicker);
		numberPicker.setMinValue(10);
		numberPicker.setMaxValue(100);
		numberPicker.setValue(BtService.getScanRange()*-1);

		return builder.create();
	}
}
