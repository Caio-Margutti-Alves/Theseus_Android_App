package hackathon.com.theseus.drawer.fragment;

import hackathon.com.theseus.viewUtils.RangeSeekBar;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import hackathon.com.theseus.R;

import hackathon.com.theseus.drawer.utils.Constant;

public class SettingsFragment extends Fragment {

	private int radiusStart;
    private int radiusEnd;

	private SeekBar skbAgeBetween;
	private RangeSeekBar<Integer> rskbRadiusBetween;
	private TextView txtvSearchRadiusResp;

	RangeSeekBar.OnRangeSeekBarChangeListener<Integer> rskbRadiusListener = new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
		@Override
		public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
			// handle changed range values
			radiusStart = minValue;
			radiusEnd = maxValue;
			txtvSearchRadiusResp.setText("From: " + radiusStart + " to " + radiusEnd);
		}
	};

	public SettingsFragment newInstance(String text){
		SettingsFragment mFragment = new SettingsFragment();		
		Bundle mBundle = new Bundle();
		mBundle.putString(Constant.TEXT_FRAGMENT, text);
		mFragment.setArguments(mBundle);
		return mFragment;
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub		
		View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
		super.onCreate(savedInstanceState);

		rskbRadiusBetween = new RangeSeekBar<Integer>(1, 50, this.getActivity());
		((ViewGroup) rootView.findViewById(R.id.SettingsForm)).addView(rskbRadiusBetween);
		txtvSearchRadiusResp = (TextView) rootView.findViewById(R.id.txtvRadiusBetween);


		rskbRadiusBetween.setOnRangeSeekBarChangeListener(rskbRadiusListener);

		// ageStart = skbAgeBetween.getProgress();
		// txtvSearchAgeResp.setText(ageEnd);

		// radius = skbRadius.getProgress();
		// txtvSearchAgeResp.setText(radius);
		
		rootView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT ));		
		return rootView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Inflate the menu items for use in the action bar
		super.onCreateOptionsMenu(menu, inflater);		
		inflater.inflate(R.menu.menu_main, menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		return super.onOptionsItemSelected(item);
	}
}
