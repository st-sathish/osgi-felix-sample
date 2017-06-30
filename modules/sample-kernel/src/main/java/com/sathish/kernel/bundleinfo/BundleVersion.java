package com.sathish.kernel.bundleinfo;

/*import static com.sathish.util.EqualsUtil.eq;
import static com.sathish.util.EqualsUtil.hash;

import com.sathish.util.data.Option;*/

public final class BundleVersion {

	private final String bundleVersion;
	  //private final Option<String> buildNumber;

	public BundleVersion(String bundleVersion) {
		this.bundleVersion = bundleVersion;
	}
	
	  /*public BundleVersion(String bundleVersion, Option<String> buildNumber) {
	    this.bundleVersion = bundleVersion;
	    this.buildNumber = buildNumber;
	  }

	  public static BundleVersion version(String bundleVersion, Option<String> buildNumber) {
	    return new BundleVersion(bundleVersion, buildNumber);
	  }*/

	  /*public String getBundleVersion() {
	    return bundleVersion;
	  }*/

	  /*public Option<String> getBuildNumber() {
	    return buildNumber;
	  }*/

	  /*@Override public boolean equals(Object that) {
	    return (this == that) || (that instanceof BundleVersion && eqFields((BundleVersion) that));
	  }*/

	  /*private boolean eqFields(BundleVersion that) {
	    return eq(this.bundleVersion, that.bundleVersion) && eq(this.buildNumber, that.buildNumber);
	  }

	  @Override public int hashCode() {
	    return hash(bundleVersion, buildNumber);
	  }*/
}
