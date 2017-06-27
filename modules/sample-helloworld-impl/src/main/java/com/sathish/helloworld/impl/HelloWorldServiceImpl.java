package com.sathish.helloworld.impl;

import com.sathish.helloworld.api.HelloWorldService;

public class HelloWorldServiceImpl implements HelloWorldService {

	public void activate() {
		System.out.println("Bundle activated");
		helloWorld();
	}
	
	public void deactivate() {
		System.out.println("Bundle deActivated");
	}
	
	public void helloWorld() {
		// TODO Auto-generated method stub
		System.out.println("Hello World Service");
	}

}
