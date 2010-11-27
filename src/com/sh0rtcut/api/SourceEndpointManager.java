package com.sh0rtcut.api;

/**
 * This class figures out, for the business logic that calls it, how to get URLs from 
 * all of the EndpointSources (APIs, scraped pages, etc.). SourceEndpointManager 
 * discriminates between the scraped and non-scraped resource types, gets actual URLs 
 * from them, and then spits them to the service.
 * 
 * @author D1m3
 *
 */
public class SourceEndpointManager {
	
	
	private Class selectSourceEndpoint(String url, String source){
		return null;
	}
	
	private void checkRules(){	}
	
	private void setScraper(){}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
