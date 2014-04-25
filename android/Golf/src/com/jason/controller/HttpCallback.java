package com.jason.controller;

public abstract class HttpCallback {
	
	public HttpCallback() {}
	public void finalWork() {};
	public void sucess(String res) { finalWork(); };
	public void timeout(String res) { finalWork(); };
	public void malformedURL(String res) { finalWork(); };
	public void ioError(String res) { finalWork(); };
	public void other(String res) {  finalWork(); };

}
