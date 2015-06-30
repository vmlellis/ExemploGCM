package br.exemplogcm.util;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpConnectionUtil {
	/**
	 * @param connection object; note: before calling this function,
	 *   ensure that the connection is already be open, and any writes to
	 *   the connection's output stream should have already been completed.
	 * @return String containing the body of the connection response or
	 *   null if the input stream could not be read correctly
	 */
	private static String readInputStreamToString(HttpURLConnection connection) {
		String result = null;
		StringBuilder sb = new StringBuilder();
		InputStream is = null;

		try {
			is = new BufferedInputStream(connection.getInputStream());
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String inputLine = "";
			while ((inputLine = br.readLine()) != null) {
				sb.append(inputLine);
			}
			result = sb.toString();
		}
		catch (Exception e) {
			Log.i("readInputStreamToString", "Error reading InputStream");
			result = null;
		}
		finally {
			if (is != null) {
				try {
					is.close();
				}
				catch (IOException e) {
					Log.i("readInputStreamToString", "Error closing InputStream");
				}
			}
		}

		return result;
	}

	public static String sendRegistrationIdToBackend(String regId){
		String answer = "";
		HttpURLConnection conn = null;
		try {
			URL url = new URL("http://www.thiengo.com.br/doc/projects/android/gcm/ctrl/CtrlGcm.php");
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("method", "save-gcm-registration-id");
			conn.setRequestProperty("reg-id", regId);
			answer = readInputStreamToString(conn);
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (conn != null)
				conn.disconnect();
		}

		/*
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost("http://www.thiengo.com.br/doc/projects/android/gcm/ctrl/CtrlGcm.php");
		String answer = "";

		try{
			ArrayList<NameValuePair> valores = new ArrayList<NameValuePair>();
			valores.add(new BasicNameValuePair("method", "save-gcm-registration-id"));
			valores.add(new BasicNameValuePair("reg-id", regId));

			httpPost.setEntity(new UrlEncodedFormEntity(valores));
			HttpResponse resposta = httpClient.execute(httpPost);
			answer = EntityUtils.toString(resposta.getEntity());
		}
		catch(NumberFormatException e){ e.printStackTrace(); }
		catch(NullPointerException e){ e.printStackTrace(); }
		catch(ClientProtocolException e){ e.printStackTrace(); }
		catch(IOException e){ e.printStackTrace(); }*/

		return(answer);
	}
}
