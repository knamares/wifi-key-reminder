package com.kna.wifikeyreminder.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.kna.wifikeyreminder.R;
import com.kna.wifikeyreminder.WifiKeyReminder;
import com.kna.wifikeyreminder.listener.CallBackEditWifiListener;
import com.kna.wifikeyreminder.listener.ChangeActionMenuListener;
import com.kna.wifikeyreminder.listener.SaveDataToCloudListener;

public class EditWifiFragment extends Fragment {

	public final static String ARG_POSITION = "position";
	int mCurrentPosition = -1;

	public static String POSITION = "position";

	private EditText editTextBssid;
	private EditText editTextKey;
	private Button buttonCancel;
	private Button buttonAccept;

	private CallBackEditWifiListener callBackEditWifiListener;
	private ChangeActionMenuListener changeActionMenuListener;
	private SaveDataToCloudListener saveDataToCloudListener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		callBackEditWifiListener = (CallBackEditWifiListener) activity;
		changeActionMenuListener = (ChangeActionMenuListener) activity;
		saveDataToCloudListener = (SaveDataToCloudListener) activity;
	}

	@Override
	public void onResume() {
		super.onResume();
		changeActionMenuListener.changeActionMenuOptions("EDIT");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_edit_wifi, container, false);
	}

	@Override
	public void onStart() {
		super.onStart();

		editTextBssid = (EditText) getView().findViewById(R.id.editTextBssid);
		editTextKey = (EditText) getView().findViewById(R.id.editTextKey);
		buttonCancel = (Button) getView().findViewById(R.id.buttonCancel);
		buttonAccept = (Button) getView().findViewById(R.id.buttonAccept);

		buttonCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Back
				callBackEditWifiListener.doCallBack(EditWifiFragment.this);
			}
		});

		buttonAccept.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				saveDataToCloudListener.save(editTextBssid.getText().toString(), editTextKey.getText().toString());
			}
		});

		// During startup, check if there are arguments passed to the fragment.
		// onStart is a good place to do this because the layout has already
		// been
		// applied to the fragment at this point so we can safely call the
		// method
		// below that sets the article text.
		Bundle args = getArguments();
		if (args != null) {
			// Set article based on argument passed in
			updateView(args.getInt(ARG_POSITION));
		}
	}

	public void updateView(int position) {
		editTextBssid.setText(WifiKeyReminder.wifis.get(position).getBssid());
		editTextKey.setText(WifiKeyReminder.wifis.get(position).getKey());
	}

}
