package com.sh0rtcut.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;

import com.sh0rtcut.service.RuleEngine;

/**
 * This class figures out, for the business logic that calls it, how to get URLs from 
 * all of the EndpointSources (APIs, scraped pages, etc.). EndpointManager 
 * discriminates between the scraped and non-scraped resource types, gets actual URLs 
 * from them, and then spits them to the service.
 * 
 * @author D1m3
 *
 */
public class EndpointManager{
	
	/* Fields */
	private EndpointStrategy strategy;
	private ArrayList<String> requestUrls;
	private ArrayList<String> responseUrls;
	private HashMap<String, String> paramMap;
	
	/* Apache HttpClient stuff */
	private static DefaultHttpClient httpClient;
	private HttpPost httpPost;
	private HttpGet httpGet;
	private HttpParams httpParams;
	private HttpResponse httpResponse;
	
	/* Moar connection stuff */
	private int httpTimeout;			//Request timeout in milliseconds
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		EndpointManager mgr = new EndpointManager();
		mgr.setStrategy(TinyurlStrategy.getInstance());
		mgr.getStrategy().setRequestUrls(new ArrayList<String>());
		mgr.setRequestUrl("http://npr.org");
		mgr.getStrategy().sendRequest();
		System.out.println(mgr.getStrategy().getResponseUrl());
		System.out.println(mgr.shorten("http://msn.com"));
	}
	
	
	public EndpointManager(){
		httpClient = new DefaultHttpClient();
		requestUrls = new ArrayList<String>();
		responseUrls = new ArrayList<String>();
		httpGet = new HttpGet();
		httpPost = new HttpPost();
	}
	
	public void setStrategy(EndpointStrategy strat){
		strategy = strat;
		httpPost = new HttpPost(strategy.getEndpointUrl());
		httpGet = new HttpGet(strategy.getEndpointUrl());
		strategy.init(httpClient, httpGet, httpPost, requestUrls, responseUrls);
	}
	
	public EndpointStrategy getStrategy(){ 
		return strategy;
	}
	
	public String getEndpointUrl() {
		return strategy.getResponseUrl();
	}

	public HashMap<String, String> getResponse()
			throws MethodNotSupportedException {
		return strategy.getResponse();
	}

	public String getResponseUrl() {
		return responseUrls.get(0);
	}

	public List<String> getResponseUrls() {
		return strategy.getResponseUrls();
	}

	public String getStatusCode() {
		return String.valueOf(httpResponse.getStatusLine().getStatusCode());
	}

	public void sendRequest() {
		strategy.sendRequest();
	}

	public void setEndpointUrl(String url) {
		strategy.setEndpointUrl(url);
	}

	public void setRequestParams(Map<String, String> opsMap)
			throws MethodNotSupportedException {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		Iterator it = opsMap.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        paramMap.put( new String((String)pairs.getKey()), new String((String)pairs.getValue()));
	        nameValuePairs.add(new BasicNameValuePair((String)pairs.getKey(), (String)pairs.getValue()));
	    }
	}

	public void setRequestUrl(String url) {
		strategy.setRequestUrl(url);
	}

	public void setRequestUrls(List<String> urls) {
		for (String url : urls)
			requestUrls.add(new String(url));
	}

	public String shorten(String url) {
		return strategy.shorten(url);
	}


	public void init(HttpClient client, HttpGet get, HttpPost post, List<String> reqUrls,
			List<String> respUrls) {
		strategy.init(client, get, post, reqUrls, respUrls);
	}

	public void init(){
		strategy.init(httpClient, httpGet, httpPost, requestUrls, responseUrls);
	}
	
}
