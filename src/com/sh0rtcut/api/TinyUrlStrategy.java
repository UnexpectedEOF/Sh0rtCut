package com.sh0rtcut.api;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

public class TinyurlStrategy implements EndpointStrategy{

	
	/* Fields */
	private ArrayList<String> requestUrls;
	private ArrayList<String> responseUrls;
	private String endpointUrl = "http://tinyurl.com/api-create.php?url=";
	
	private static DefaultHttpClient httpClient;
	private HttpGet httpGet;
	private HttpParams params;
	private HttpResponse response;
	
	public static void main(String[] args) throws ClientProtocolException, IOException{
	     
	}
	
	private TinyurlStrategy(){}
	
	@Override
	public String getEndpointUrl() {
		return new String(endpointUrl);
	}

	@Override
	public HashMap<String, String> getResponse() throws MethodNotSupportedException {

		throw new MethodNotSupportedException("This class doesn't support this method.");
	}

	@Override
	public String getResponseUrl() {
		return responseUrls.get(0);
	}

	@Override
	public List<String> getResponseUrls() {
		
		ArrayList<String> responseRet = new ArrayList<String>();
		for (String url : responseUrls)
			   responseRet.add(new String(url));
		
		return responseRet;
	}

	@Override
	public String getStatusCode() {
		return String.valueOf(response.getStatusLine().getStatusCode());
	}

	@Override
	public void sendRequest() {
		httpGet = new HttpGet(endpointUrl.concat(requestUrls.get(0)));
		try {
			response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
	        
	        InputStream is = entity.getContent();
			StringBuffer sb = new StringBuffer();
			for( int i = is.read(); i != -1; i = is.read() ) {
				sb.append( (char) i );
			}
			String tinyUrl = sb.toString();
			is.close();
			responseUrls.add(0, tinyUrl);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void setEndpointUrl(String url) {
		endpointUrl = new String(url);
	}

	@Override
	public void setRequestParams(Map<String, String> opsMap) throws MethodNotSupportedException {
		params = httpClient.getParams();
		throw new MethodNotSupportedException("This class doesn't support this method.");
		
	}

	@Override
	public void setRequestUrl(String url) {
		requestUrls.add(0, new String(url));
	}

	@Override
	public void setRequestUrls(List<String> urls) {

		requestUrls = (ArrayList<String>) urls;
	}

	@Override
	public String shorten(String url) {
		setRequestUrl(url);
		sendRequest();
		return getResponseUrl();
	}

	@Override
	public void init(HttpClient client, HttpGet get, HttpPost post, List<String> reqUrls,
			List<String> respUrls) {
		httpClient = (DefaultHttpClient) client;
		requestUrls = (ArrayList<String>) reqUrls;
		responseUrls = (ArrayList<String>) respUrls;
		httpGet = get;
		post = null;
	}

	private static class TinyurlHolder{
		private static final TinyurlStrategy INSTANCE = new TinyurlStrategy();
	}
	

	public static EndpointStrategy getInstance() {
		return TinyurlHolder.INSTANCE;
	}
}
