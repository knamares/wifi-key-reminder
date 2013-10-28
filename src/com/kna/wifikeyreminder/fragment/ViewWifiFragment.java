package com.kna.wifikeyreminder.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kna.wifikeyreminder.R;
import com.kna.wifikeyreminder.WifiKeyReminder;
import com.kna.wifikeyreminder.listener.ChangeActionMenuListener;

public class ViewWifiFragment extends Fragment {

    public final static String ARG_POSITION = "position";
    int mCurrentPosition = -1;
    
	public static String POSITION = "position";

	private TextView textViewEditBssid;
	private TextView textViewEditKey;
	
	private ChangeActionMenuListener changeActionMenuListener;
	
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		changeActionMenuListener = (ChangeActionMenuListener) activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_view_wifi, container, false);
	}

    @Override
    public void onStart() {
        super.onStart();
        
        textViewEditBssid = (TextView) getView().findViewById(R.id.textViewEditBssid);
		textViewEditKey = (TextView) getView().findViewById(R.id.textViewEditKey);

        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
        Bundle args = getArguments();
        if (args != null) {
            // Set article based on argument passed in
            updateView(args.getInt(ARG_POSITION));
        } else if (mCurrentPosition != -1) {
            // Set article based on saved instance state defined during onCreateView
            updateView(mCurrentPosition);
        }
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	
    	changeActionMenuListener.changeActionMenuOptions("VIEW");
    	
    }

    public void updateView(int position) {
        mCurrentPosition = position;
        
		textViewEditBssid.setText(WifiKeyReminder.wifis.get(position).getBssid());
		textViewEditKey.setText(WifiKeyReminder.wifis.get(position).getKey());        
        
    }	

}
