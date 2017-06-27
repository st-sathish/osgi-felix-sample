package com.sathish.helloworld;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.sathish.helloworld.api.HelloWorldService;
import com.sathish.helloworld.impl.HelloWorldServiceImpl;

public class Activator implements BundleActivator {

	public void start(BundleContext context) throws Exception {
		System.out.println("Hello World Activated");
		HelloWorldService helloWorld = new HelloWorldServiceImpl();
		helloWorld.helloWorld();
	}

	public void stop(BundleContext context) throws Exception {
		System.out.println("Hello World DeActivated");
	}
}
