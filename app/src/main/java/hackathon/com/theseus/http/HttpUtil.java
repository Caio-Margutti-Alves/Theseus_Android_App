package hackathon.com.theseus.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

public class HttpUtil {

	private static String url = "http://rex-pet-server.herokuapp.com";
	private static String urlGetUserById = "/usuario-com-id";
	private static String urlGetUserByLoginPassword = "/usuario-com-login-password";
	private static String urlGetUserByFacebookId = "/usuario-com-facebook-id";
	private static String urlNewUser = "/novo-usuario";
	private static String urlGetAnimalByUserId = "/avaliacoes-por-ids";
	private static String urlGetFamilyAnimals = "/animais-da-familia";
	private static String urlNewAnimal = "/novo-animal";
	private static String urlGetAnimalIdReviews = "/avaliacoes-por-ids";
	private static String urlGetUserFamilyAnimals = "/animais-da-familia-para-usuario";

	private ArrayList<NameValuePair> params;
	private ArrayList<NameValuePair> headers;

	private int responseCode;
	private String message;

	private String response;
	
	public HttpUtil() {
		super();
		this.params = new ArrayList<NameValuePair>();
		this.headers =  new ArrayList<NameValuePair>();
	}

	public String getResponse() {
		return response;
	}

	public String getErrorMessage() {
		return message;
	}

	public int getResponseCode() {
		return responseCode;
	}
	
	public static String getUrlGetUserId() {
		return url + urlGetUserById;
	}

	public static void setUrlGetUserId(String urlGetUserID) {
		HttpUtil.urlGetUserById = urlGetUserID;
	}

	public static String getUrlGetFamilyAnimals() {
		return url + urlGetFamilyAnimals;
	}

	public static void setUrlGetFamilyAnimals(String urlGetFamilyAnimals) {
		HttpUtil.urlGetFamilyAnimals = urlGetFamilyAnimals;
	}

	public static String getUrlGetAnimalIdReviews() {
		return url + urlGetAnimalIdReviews;
	}

	public static void setUrlGetAnimalIdReviews(String urlGetAnimalIdReviews) {
		HttpUtil.urlGetAnimalIdReviews = urlGetAnimalIdReviews;
	}

	public static String getUrlGetUserFamilyAnimals() {
		return url + urlGetUserFamilyAnimals;
	}

	public static void setUrlGetUserFamilyAnimals(String urlGetUserFamilyAnimals) {
		HttpUtil.urlGetUserFamilyAnimals = urlGetUserFamilyAnimals;
	}

	public static String getUrlGetAnimalByUserId() {
		return url + urlGetAnimalByUserId;
	}

	public static void setUrlGetAnimalByUserId(String urlGetAnimalUserId) {
		HttpUtil.urlGetAnimalByUserId = urlGetAnimalUserId;
	}

	public static String getUrlGetUserById() {
		return url + urlGetUserById;
	}

	public static void setUrlGetUserById(String urlGetUserById) {
		HttpUtil.urlGetUserById = urlGetUserById;
	}

	public static String getUrlGetUserByLoginPassword() {
		return url + urlGetUserByLoginPassword;
	}

	public static void setUrlGetUserByLoginPassword(String urlGetUserByLoginPassword) {
		HttpUtil.urlGetUserByLoginPassword = urlGetUserByLoginPassword;
	}

	public static String getUrlGetUserByFacebookId() {
		return url +  urlGetUserByFacebookId;
	}

	public static void setUrlGetUserByFacebookId(String urlGetUserByFacebookId) {
		HttpUtil.urlGetUserByFacebookId = urlGetUserByFacebookId;
	}

	public static String getUrlNewUser() {
		return url +  urlNewUser;
	}

	public static void setUrlNewUser(String urlNewUser) {
		HttpUtil.urlNewUser = urlNewUser;
	}

	public static String getUrlNewAnimal() {
		return url + urlNewAnimal;
	}

	public static void setUrlNewAnimal(String urlNewAnimal) {
		HttpUtil.urlNewAnimal = urlNewAnimal;
	}

	public static String getUrl() {
		return url;
	}

	public static void setUrl(String url) {
		HttpUtil.url = url;
	}
	

	public void AddParam(String name, String value) {
		params.add(new BasicNameValuePair(name, value));
	}

	public void AddHeader(String name, String value) {
		headers.add(new BasicNameValuePair(name, value));
	}

	public void Execute(RequestMethod method, String url) throws Exception {
		switch (method) {
		case GET: {
			// add parameters
			String combinedParams = "";
			if (!params.isEmpty()) {
				combinedParams += "?";
				for (NameValuePair p : params) {
					String paramString = p.getName() + "="
							+ URLEncoder.encode(p.getValue(), "UTF-8");
					if (combinedParams.length() > 1) {
						combinedParams += "&" + paramString;
					} else {
						combinedParams += paramString;
					}
				}
			}

			HttpGet request = new HttpGet(url + combinedParams);

			// add headers
			for (NameValuePair h : headers) {
				request.addHeader(h.getName(), h.getValue());
			}

			executeRequest(request, url);
			break;
		}
		case POST: {
			HttpPost request = new HttpPost(url);

			// add headers
			for (NameValuePair h : headers) {
				request.addHeader(h.getName(), h.getValue());
			}

			if (!params.isEmpty()) {
				request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			}

			executeRequest(request, url);
			break;
		}
		}
	}

	private void executeRequest(HttpUriRequest request, String url) {
		HttpClient client = new DefaultHttpClient();

		HttpResponse httpResponse;

		try {
			httpResponse = client.execute(request);
			responseCode = httpResponse.getStatusLine().getStatusCode();
			message = httpResponse.getStatusLine().getReasonPhrase();

			HttpEntity entity = httpResponse.getEntity();

			if (entity != null) {

				InputStream instream = entity.getContent();
				response = convertStreamToString(instream);

				// Closing the input stream will trigger connection release
				instream.close();
			}

		} catch (ClientProtocolException e) {
			client.getConnectionManager().shutdown();
			e.printStackTrace();
		} catch (IOException e) {
			client.getConnectionManager().shutdown();
			e.printStackTrace();
		}
	}

	private static String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public enum RequestMethod {
		GET, POST
	}
	
}
