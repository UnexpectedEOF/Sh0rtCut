package com.sh0rtcut.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.MethodNotSupportedException;

/**
 * This interface represents the functionality that every URL shortener endpoint should
 * have. It's a simple, general interface that allows any API to get called or any page
 * to be scraped with override options provided BL or default options from the DB or 
 * config files.
 * 
 * All classes that implement this will have a kind of Facade pattern built-in; these
 * methods allow for anything from arbitrarily complex API calls to simple 
 * "url-in-url-out" interactions from classes higher up in the chain.
 * 
 * TODO: Figure out how the SEManager will make calls. Most complex methods by default?
 * 
 * @author D1m3
 *
 */
public interface SourceEndpoint {

	public void init();
	public void setEndpointUrl(String url);
	public String getEndpointUrl();
	public String shorten(String url);		//Simplest case
	public void setRequestUrl(String url); 	//Simple case
	public String getResponseUrl(); 		//Simple case
	public String getStatusCode(); 			//Probably best to make our own status code class?
	public void setRequestParams(Map<String, String> opsMap) throws MethodNotSupportedException; //If it's a service that exposes an API, but must work even in scraper case.
	public void sendRequest();				//All cases
	public HashMap<String, String> getResponse() throws MethodNotSupportedException;
	public List<String> getResponseUrls(); 
	public void setRequestUrls(List<String> urls);
	//public another simple one?
	
}
