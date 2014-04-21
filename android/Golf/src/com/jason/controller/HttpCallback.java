package com.jason.controller;

public abstract class HttpCallback {
	
	public HttpCallback() {}
	public void sucess(String res) {};
	public void timeout(String res) {};
	public void malformedURL(String res) {};
	public void ioError(String res) {};
	public void other(String res) {};

}
