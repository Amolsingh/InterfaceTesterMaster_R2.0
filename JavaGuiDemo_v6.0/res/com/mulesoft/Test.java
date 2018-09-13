package com.mulesoft;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Test {

	public static void main(String [] args) {
		
		try {
			
			TrustManager[] trustAllCerts = new TrustManager[]{
					new X509TrustManager() {

						public java.security.cert.X509Certificate[] getAcceptedIssuers()
						{
							return null;
						}
						public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
						{
							//No need to implement.
						}
						public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
						{
							//No need to implement.
						}
					}
			};
			String stURL = "https://dhdvmsgw01:8908/api/policy/fnol/safelite/v1?wsdl";
			int code =0;
			
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			URL url = new URL(stURL);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			//HttpURLConnection.setFollowRedirects(false);
			//connection.setConnectTimeout(10 * 1000);
			connection.setRequestMethod("GET");
			code = connection.getResponseCode();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
}
