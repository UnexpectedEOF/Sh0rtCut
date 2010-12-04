package com.sh0rtcut.api;

import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.w3c.dom.Document;


/**
 * Bitly uses SOAP or something. Include in this class all of the stuff that needs 
 * to be done to make calls possible. Adhere to SourceEndpoint constraints.
 * 
 * @author D1m3
 *
 */
public class BitlyStrategy implements EndpointStrategy{

	
	
	
	private BitlyStrategy(){
		
	}
	

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	@Override
	public String getEndpointUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<String, String> getResponse() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getResponseUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getResponseUrls() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStatusCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendRequest() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEndpointUrl(String url) {
		// TODO Auto-generated method stub
		
	}

	public void setRequestParams(Map<String, String> opsMap) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRequestUrl(String url) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRequestUrls(List<String> urls) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String shorten(String url) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(HttpGet get, HttpPost post, List<String> reqUrls,
			List<String> respUrls) {
		// TODO Auto-generated method stub
		
	}

	
	private static class BitlyHolder{
		private static final BitlyStrategy INSTANCE = new BitlyStrategy();
	}
	
	@Override
	public EndpointStrategy getInstance() {
		return BitlyHolder.INSTANCE;
	}

}
