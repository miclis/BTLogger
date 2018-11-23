package com.miclis.btlogger.View;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;
import com.miclis.btlogger.Model.BtService;
import com.miclis.btlogger.R;

public class RangeDialog extends AppCompatDialogFragment {

	private NumberPicker numberPicker;
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		LayoutInflater inflater = getActivity().getLayoutInflater();
		final View view = inflater.inflate(R.layout.dialog_range, null);

		builder.setView(view).setTitle("Set the minimal signal strength:")
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {

					}
				})
				.setPositiveButton("Set", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						BtService.setMaxScanRange(Short.valueOf(String.valueOf(numberPicker.getValue()*-1)));
					}
				});

		numberPicker = view.findViewById(R.id.rangePicker);
		numberPicker.setMinValue(10);
		numberPicker.setMaxValue(90);

		return builder.create();
	}
}
