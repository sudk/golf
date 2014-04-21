package com.jason.controller;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GThreadExecutor {

	private static ThreadPoolExecutor _pool;

	static {
		_pool = new ThreadPoolExecutor(3, 10, 5, TimeUnit.SECONDS,	new ArrayBlockingQueue<Runnable>(50));
	}
	
	public static void execute(Runnable r){
		
//		_pool.
		
		_pool.execute(r);
	}

}
