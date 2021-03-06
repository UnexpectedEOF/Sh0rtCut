package com.sh0rtcut.api;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
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
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.DefaultedHttpParams;
import org.apache.http.params.HttpParams;


/**
 *
 * 
 * @author D1m3
 *
 */

public class MtnymobiStrategy implements EndpointStrategy {

	/* Fields */
	private ArrayList<String> requestUrls;
	private ArrayList<String> responseUrls;
	private String endpointUrl = "http://mtny.mobi/api/";
	private String requestString;
	
	private static DefaultHttpClient httpClient;
	private HttpGet httpGet;
	//private HttpMethodParams params;
	private HttpResponse response;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MtnymobiStrategy isgd = new MtnymobiStrategy();
		System.out.println(isgd.shorten("http://slashdot.org"));
		
        // When HttpClient instance is no longer needed, 
        // shut down the connection manager to ensure
        // immediate deallocation of all system resources
        httpClient.getConnectionManager().shutdown(); 

	}

	private MtnymobiStrategy(){}

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
		int worker;
		
		
		try {
			response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			
	        InputStream is = entity.getContent();
	        
	        StringBuffer sb = new StringBuffer();
	        
	        while((worker=is.read()) != -1){
	        	
	        	sb.append((char)worker);/*
	        	if((pointer = sb.lastIndexOf("onselect=")) != -1 ){
	        		tinyUrl = new String((String)(sb.subSequence(sb.lastIndexOf("value=")+7, pointer-2))); //BAAAD MAGIC NUMBERS!!
	        		break;
	        	}*/
	        }
	        
	        System.out.println(sb);
	        
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

	public void setRequestParams(Map<String, String> opsMap){
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		Iterator it = opsMap.entrySet().iterator();
		requestString = new String(endpointUrl+"?");
		String worker;

		while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        nameValuePairs.add(new BasicNameValuePair((String)pairs.getKey(), (String)pairs.getValue()));
	        worker = (new String((String)pairs.getKey()+"="+(String)pairs.getValue()+"&"));
	        requestString = requestString.concat(worker);
	    }

		System.out.println(requestString);
		
        try {
			httpGet.setURI(new URI(requestString));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		//throw new MethodNotSupportedException("This class doesn't support this method.");
		
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
	public String shorten(String url){
		
		HashMap ops = new HashMap<String, String>();
		ops.put("url", url);
		
		setRequestParams(ops);
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
	
	
	private static class MtnymobiHolder{
		private static final MtnymobiStrategy INSTANCE = new MtnymobiStrategy();
	}
	

	public static MtnymobiStrategy getInstance() {
		return MtnymobiHolder.INSTANCE;
	}

}
