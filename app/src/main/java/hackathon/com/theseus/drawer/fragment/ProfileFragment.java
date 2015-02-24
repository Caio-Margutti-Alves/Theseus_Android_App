package hackathon.com.theseus.drawer.fragment;

import hackathon.com.theseus.user.User;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.widget.ProfilePictureView;
import hackathon.com.theseus.R;

import hackathon.com.theseus.drawer.utils.Constant;
import hackathon.com.theseus.drawer.utils.Menus;

public class ProfileFragment extends Fragment {

	private static String firstName;
	private static String lastName;
	private static String login;
	private static String password;
	private static String rePassword;
	private static String mobileToken;
	// private static Bitmap profilePicture;
	private static int gender;
	private static String email;
	private static String dob;

	private EditText edtFisrtName;
	private EditText edtLastName;
	private EditText edtAge;
	private EditText edtGender;
	private EditText edtPassword;
	private EditText edtEmail;
	private EditText edtLogin;
	private Button btnMale;
	private Button btnFemale;
	private ProfilePictureView profilePicture;

	Button.OnClickListener lstnMale = new Button.OnClickListener() {
		@Override
		public void onClick(View view) {
			view.setBackgroundColor(Color.rgb(177, 235, 000));
			btnFemale.setBackgroundColor(Color.rgb(243, 243, 243));
			gender = 1;
		}
	};

	Button.OnClickListener lstnFemale = new Button.OnClickListener() {
		@Override
		public void onClick(View view) {
			view.setBackgroundColor(Color.rgb(177, 235, 000));
			btnMale.setBackgroundColor(Color.rgb(243, 243, 243));
			gender = 2;
		}
	};

	public ProfileFragment newInstance(String text){
		ProfileFragment mFragment = new ProfileFragment();		
		Bundle mBundle = new Bundle();
		mBundle.putString(Constant.TEXT_FRAGMENT, text);
		mFragment.setArguments(mBundle);
		return mFragment;
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub		
		View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
		super.onCreate(savedInstanceState);

		edtFisrtName = (EditText) rootView.findViewById(R.id.edtFirstName);
		edtLastName = (EditText) rootView.findViewById(R.id.edtLastName);
		edtEmail = (EditText) rootView.findViewById(R.id.edtEmail);
		edtLogin = (EditText) rootView.findViewById(R.id.edtLogin);
		edtAge = (EditText) rootView.findViewById(R.id.edtAge);
		btnMale = (Button) rootView.findViewById(R.id.btnProfileMale);
		btnFemale = (Button) rootView.findViewById(R.id.btnProfileFemale);
		profilePicture = (ProfilePictureView) rootView.findViewById(R.id.imgvProfile);

		try{
			edtFisrtName.setText(User.getFirstName());
			edtLastName.setText(User.getLastName());
			edtAge.setText(String.valueOf(User.getAge()));
			edtEmail.setText(User.getEmail());
			edtLogin.setText(User.getLogin());
			profilePicture.setProfileId(User.getFacebookId());
	
			if (User.getGender() != 2)
				btnMale.setBackgroundColor(Color.rgb(177, 235, 000));
			else
				btnFemale.setBackgroundColor(Color.rgb(177, 235, 000));
			
		}catch(Exception ex){
			edtFisrtName.setText("Default");
			edtLastName.setText("Name");
			edtAge.setText("Default Age");
			edtEmail.setText("Default Email");
			edtLogin.setText("Default Login");
			//profilePicture.setProfileId(User.getFacebookId());
		}
		
		
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
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
