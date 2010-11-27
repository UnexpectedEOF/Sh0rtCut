package com.sh0rtcut.api;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.text.html.HTMLDocument;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;


/**
 * Is.gd has no API, so it needs to be scraped. Hopefully, since all of these
 * API wrappers and scrapers both implement SourceEndpoint, business logic doesn't 
 * need to tell the difference between what is and isn't scraped/called. Just consider
 * everything that provides our stuff a URL an endpoint and let the classes do their
 * work.
 * 
 * @author D1m3
 *
 */

public class GooglWrapper implements SourceEndpoint {

	/* Fields */
	private ArrayList<String> requestUrls;
	private ArrayList<String> responseUrls;
	private String endpointUrl = "http://goo.gl/";
	
	private static DefaultHttpClient httpClient;
	private HttpPost httpPost;
	private HttpParams params;
	private HttpResponse response;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GooglWrapper googl = new GooglWrapper();
		System.out.println(googl.shorten("http://slashdot.org"));
		
        // When HttpClient instance is no longer needed, 
        // shut down the connection manager to ensure
        // immediate deallocation of all system resources
        httpClient.getConnectionManager().shutdown(); 

	}

	public GooglWrapper(){
		init();
	}
	
	@Override
	public void init() {
		httpClient = new DefaultHttpClient();
		requestUrls = new ArrayList<String>();
		responseUrls = new ArrayList<String>();
        httpPost = new HttpPost(endpointUrl);
        try {
			httpPost.setEntity(new UrlEncodedFormEntity(null));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
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
		
		byte[] byteArr = new byte[1024];
		int pointer;
		String tinyUrl = new String();
		String securityToken;
		int worker;
		
		
		try {
			response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			
	        InputStream is = entity.getContent();
	        
	        StringBuffer sb = new StringBuffer();
	        
	        while((worker=is.read()) != -1){
	        	
	        	sb.append((char)worker);
	        	if((pointer = sb.indexOf("value=")) != -1 ){
	        		securityToken = new String((String)(sb.subSequence(sb.lastIndexOf("value=")+7, sb.indexOf("\"", pointer+7) ))); //BAAAD MAGIC NUMBERS!!
	        		break;
	        	}
	        }
	        is.close();
	        
	        httpPost.
	        
	        response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			
	        InputStream is = entity.getContent();
	        
	        StringBuffer sb = new StringBuffer();
	        
	        while((worker=is.read()) != -1){
	        	
	        	sb.append((char)worker);
	        	if((pointer = sb.lastIndexOf("onselect=")) != -1 ){
	        		tinyUrl = new String((String)(sb.subSequence(sb.lastIndexOf("value=")+7, pointer-2))); //BAAAD MAGIC NUMBERS!!
	        		break;
	        	}
	        }
	        
			responseUrls.add(0, tinyUrl);
	        is.close();
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
	public void setRequestParams(Map<String, String> opsMap){

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		Iterator it = opsMap.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        nameValuePairs.add(new BasicNameValuePair((String)pairs.getKey(), (String)pairs.getValue()));
	        //System.out.println(pairs.getKey() + " = " + pairs.getValue());
	    }

        try {
			//httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        	httpPost.getEntity().
			//httpPost.setParams(new HttpParams());

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void setRequestUrl(String url) {
		requestUrls.add(0, new String(url));
	}

	@Override
	public void setRequestUrls(List<String> urls) {
		
		for (String url : urls)
		   requestUrls.add(new String(url));
	}

	@Override
	public String shorten(String url){
		setRequestParams(null);
		sendRequest();
		return getResponseUrl();
	}
	
}
