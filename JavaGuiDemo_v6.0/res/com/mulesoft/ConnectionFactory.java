package com.mulesoft;

import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;
import javax.net.ssl.X509TrustManager;



public class ConnectionFactory {

	/* Method to Implement Trust Manager */
	public static TrustManager[] implementTrstMngr ()
	{
		return  new TrustManager[]{
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
	}
	
	//
	public static TrustManager[] implementTrstMngr2 ()
	{
		
		
		return	 new TrustManager[]{
				    new X509ExtendedTrustManager() {
				    	@Override
				        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException 
				        {
				        	
				        }
				        
				    	@Override
				        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException 
				        {
				        	
				        }
				        
				    	@Override
				        public X509Certificate[] getAcceptedIssuers() {
				        return null;
				        }
				        
				    	@Override
				        public void checkClientTrusted(X509Certificate[] x509Certificates, String s, Socket socket) throws CertificateException 
				        {
				        	
				        }
				        
				    	@Override
				        public void checkServerTrusted(X509Certificate[] x509Certificates, String s, Socket socket) throws CertificateException
				        {
				        	
				        }
				        
				    	@Override
				        public void checkClientTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine) throws CertificateException 
				        {
				        	
				        }
				        
				    	@Override
				        public void checkServerTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine) throws CertificateException 
				        {
				        	
				        }
				    }
				};
		
	}
	
	/* Method to return a Connection Object for a URL */
	public static HttpURLConnection retrnConectnFrURL(URL url) {
		
		HttpURLConnection connectionHTTPS=null ;
		try {
			SSLContext sc = SSLContext.getInstance(Utility.SSLContext);
			sc.init(null, implementTrstMngr2(), new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			connectionHTTPS = (HttpURLConnection)url.openConnection();
			
			// Create all-trusting host name verifier
	        HostnameVerifier allHostsValid = new HostnameVerifier() {
	            public boolean verify(String hostname, SSLSession session) {
	                return true;
	            }
	        };
	        //Install the all-trusting host verifier
	        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
			//HttpURLConnection.setFollowRedirects(false);
			//connection.setConnectTimeout(500);
	        
	        connectionHTTPS.setRequestMethod(Utility.GETRequest);
	        connectionHTTPS.setRequestProperty("charset", "utf-8");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return connectionHTTPS;
	}
	
}
