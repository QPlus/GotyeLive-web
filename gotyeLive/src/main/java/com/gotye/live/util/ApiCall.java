package com.gotye.live.util;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class ApiCall {

	private final static class HttpMethod {
		public final static String GET = "GET";
		public final static String DELETE = "DELETE";
		public final static String POST = "POST";
		public final static String PUT = "PUT";
	}

	public static String delete(String url) {
		return send(url, null, HttpMethod.DELETE, null);
	}

	public static String post(String url, String param,
			Map<String, String> headers) {
		try {
			return send(url, param.getBytes("utf-8"), HttpMethod.POST, headers);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String post(String url, byte[] param,
			Map<String, String> headers) {
		return send(url, param, HttpMethod.POST, headers);
	}

	public static String get(String url, Map<String, String> headers) {
		return send(url, null, HttpMethod.GET, headers);
	}

	public static String put(String url) {
		return send(url, null, HttpMethod.PUT, null);
	}

	public static String send(String url, byte[] param, String method,
			Map<String, String> headers) {
		String result = null;
		try {
			if (url.startsWith("https")) {
				TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
					public java.security.cert.X509Certificate[] getAcceptedIssuers() {
						return null;
					}

					public void checkClientTrusted(
							java.security.cert.X509Certificate[] certs,
							String authType) {
					}

					public void checkServerTrusted(
							java.security.cert.X509Certificate[] certs,
							String authType) {
					}
				} };

				SSLContext sc = SSLContext.getInstance("SSL");
				sc.init(null, trustAllCerts, new java.security.SecureRandom());
				HttpsURLConnection.setDefaultSSLSocketFactory(sc
						.getSocketFactory());
				HostnameVerifier allHostsValid = new HostnameVerifier() {
					public boolean verify(String hostname, SSLSession session) {
						return true;
					}
				};

				// Install the all-trusting host verifier
				HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
			}
			HttpURLConnection.setFollowRedirects(true);
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			HttpURLConnection conn = (HttpURLConnection) realUrl
					.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("connection", "Keep-Alive");
			
			if (headers != null) {
				for (String key : headers.keySet()) {
					conn.setRequestProperty(key, headers.get(key));
				}
			}
			// Map<String, List<String>> map = conn.getHeaderFields();
			// //遍历所有的响应头字段
			// for (String key : map.keySet()) {
			// System.out.println(key + "--->" + map.get(key));
			// }

			conn.setRequestMethod(method);
			conn.setReadTimeout(300000);

			if (method.equalsIgnoreCase(HttpMethod.GET)
					|| method.equalsIgnoreCase(HttpMethod.DELETE)) {
				// 建立实际的连接
				conn.connect();
			} else {
				conn.setDoOutput(true);
				conn.setDoInput(true);
				OutputStream out = null;
				try {
					out = conn.getOutputStream();
					out.write(param);
					out.flush();
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					if(out!=null)
						out.close();
				}
				
			}
		
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			int len;
			byte[] buffer = new byte[1024];
			while ((len = conn.getInputStream().read(buffer)) != -1) {
				bout.write(buffer, 0, len);
			}
			result = new String(bout.toByteArray(),"utf-8");
			
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
		} finally {
		}
		return result;
	}

	public static void main(String[] args) {
	}
}
