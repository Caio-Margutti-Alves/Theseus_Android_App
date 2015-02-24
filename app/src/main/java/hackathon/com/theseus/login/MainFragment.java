package hackathon.com.theseus.login;

import hackathon.com.theseus.user.User;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;

public class MainFragment extends Fragment {

	private String facebookId;
	private String firstName;
	private String lastName;
	private String mobileToken;
	private String gender;
	private String email;
	private String dob;

	private static final String TAG = "MainFragment";
	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback statusCallback = new Session.StatusCallback() {

		@Override
		public void call(final Session session, SessionState state,
				Exception exception) {
			if (session != null && session.isOpened()) {
				// If the session is open, make an API call to get user data
				// and define a new callback to handle the response
				Request request = Request.newMeRequest(session,
						new Request.GraphUserCallback() {
							@Override
							public void onCompleted(GraphUser user,
									Response response) {
								// If the response is successful
								if (session == Session.getActiveSession()) {
									if (user != null) {
										facebookId = user.getId();
										firstName = user.getFirstName();
										lastName = user.getLastName();
										email = user.getProperty("email").toString();
										gender = user.getProperty("gender").toString(); 
										dob = user.getBirthday();
										if(!User.getUserByFacebookId(facebookId)){
											newUser(facebookId, null, null, mobileToken, firstName, lastName, email, gender, dob);
										}
									}
								}
							}
						});
				Request.executeBatchAsync(request);
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(getActivity(), statusCallback);
		uiHelper.onCreate(savedInstanceState);
	}

	// Verificar mais tarde
	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (state.isOpened()) {
			Log.i(TAG, "Logged in...");

            Intent intent = new Intent(getActivity(),hackathon.com.theseus.drawer.navigation.NavigationMain.class);
            startActivity(intent);
		} else if (state.isClosed()) {
			Log.i(TAG, "Logged out...");
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		// For scenarios where the main activity is launched and user
		// session is not null, the session state change notification
		// may not be triggered. Trigger it if it's open/closed.
		Session session = Session.getActiveSession();
		if (session != null && (session.isOpened() || session.isClosed())) {
			onSessionStateChange(session, session.getState(), null);
		}

		uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	public void newUser(String facebookId, String login, String password, String mobileToken,
			String firstName, String lastName, String email, String gender,
			String dob) {

		if(User.newUser(facebookId, login, password, mobileToken, firstName, lastName, email,
				String.valueOf(gender), dob)){
		
			User.getUserByFacebookId(facebookId);
		}
	}

}
