package com.sathish.kernel.bundleinfo;

public class BundleInfoDbException extends RuntimeException {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BundleInfoDbException(String s) {
		super(s);
	}

	public BundleInfoDbException(String s, Throwable throwable) {
		super(s, throwable);
	}

	public BundleInfoDbException(Throwable throwable) {
		super(throwable);
	}
}
