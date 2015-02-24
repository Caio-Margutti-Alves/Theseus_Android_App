package hackathon.com.theseus.user;

import java.util.Calendar;
import java.util.Date;

import android.graphics.Bitmap;

public abstract class User {

	private static String id;
	private static String login;
	private static String facebookId = "100002560657734"; //Tirar
	private static String mobileToken;
	private static Bitmap profilePicture;
	private static String firstName;
	private static String lastName;
	private static int gender;
	private static String email;
	private static Date dob;
	private static int age;
	private static String lastActivity;
	private static String lastLocation;
	private static String dateAccCreation;

	public User() {

	}
	
	public static boolean newUser(String facebookId, String login, String password,
			String mobileToken, String firstName, String lastName,
			String email, String gender, String dob) {

		return JsonUser.newUser(facebookId, login, password, mobileToken, firstName,
				lastName, email, gender, dob);
		
	}
	
	//SOMENTE PARA TESTES - RETIRAR NO FINAL
	public static boolean getUserById(String id) {
		return JsonUser.getUserById(id);
	}
	
	public static boolean getUserByFacebookId(String facebookId) {
		return JsonUser.getUserByFacebookId(facebookId);
	}

	public static boolean getUserByLoginPassword(String login, String password) {
		return JsonUser.getUserByLoginPassword(login, password);
		
	}
	
	public static String getFacebookId() {
		return facebookId;
	}

	public static void setFacebookId(String facebookId) {
		User.facebookId = facebookId;
	}

	public static String getMobileToken() {
		return mobileToken;
	}

	public static void setMobileToken(String mobileToken) {
		User.mobileToken = mobileToken;
	}

	public static String getEmail() {
		return email;
	}

	public static void setEmail(String email) {
		User.email = email;
	}

	public static Date getDob() {
		return dob;
	}

	public static void setDob(String dob) {
		User.dob = new Date(dob);
		calculateAge();
	}

	public static String getLastActivity() {
		return lastActivity;
	}

	public static void setLastActivity(String lastActivity) {
		User.lastActivity = lastActivity;
	}

	public static String getLastLocation() {
		return lastLocation;
	}

	public static void setLastLocation(String lastLocation) {
		User.lastLocation = lastLocation;
	}

	public static String getDateAccCreation() {
		return dateAccCreation;
	}

	public static void setDateAccCreation(String dateAccCreation) {
		User.dateAccCreation = dateAccCreation;
	}

	public static String getLogin() {
		return login;
	}

	public static void setLogin(String login) {
		User.login = login;
	}
	
	public static String getId() {
		return id;
	}

	public static void setId(String id) {
		User.id = id;
	}

	public static int getGender() {
		return gender;
	}

	public static void setGender(String gender) {
		if(gender.equals("Male")){
			User.gender = 1;
		}else if (gender.equals("Female")){
			User.gender = 2;
		}else{
			User.gender = Integer.parseInt(gender);
		}
	}

	public static Bitmap getProfilePicture() {
		return profilePicture;
	}

	public static void setProfilePicture(Bitmap profilePicture) {
		User.profilePicture = profilePicture;
	}

	public static String getFirstName() {
		return firstName;
	}

	public static void setFirstName(String firstName) {
		User.firstName = firstName;
	}

	public static String getLastName() {
		return lastName;
	}

	public static void setLastName(String lastName) {
		User.lastName = lastName;
	}

	public static int getAge() {
		return age;
	}

	public static void setAge(int age) {
		User.age = age;
	}

	public static void calculateAge() {

		Calendar today = Calendar.getInstance();
		Calendar birthDate = Calendar.getInstance();

		int age = 0;

		birthDate.setTime(getDob());
		if (birthDate.after(today)) {
			throw new IllegalArgumentException("Can't be born in the future");
		}

		age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);

		// If birth date is greater than todays date (after 2 days adjustment of
		// leap year) then decrement age one year
		if ((birthDate.get(Calendar.DAY_OF_YEAR)
				- today.get(Calendar.DAY_OF_YEAR) > 3)
				|| (birthDate.get(Calendar.MONTH) > today.get(Calendar.MONTH))) {
			age--;

			// If birth date and todays date are of same month and birth day of
			// month is greater than todays day of month then decrement age
		} else if ((birthDate.get(Calendar.MONTH) == today.get(Calendar.MONTH))
				&& (birthDate.get(Calendar.DAY_OF_MONTH) > today
						.get(Calendar.DAY_OF_MONTH))) {
			age--;
		}

		setAge(age);
	}
}
