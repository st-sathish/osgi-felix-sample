package com.sathish.kernel.bundleinfo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity(name = "Test")
@Table(name = "ch_test")
@NamedQueries({
        @NamedQuery(name = "Test.findAll", query = "select a from Test a")})
public class Test {

  @Id
  @GeneratedValue
  @Column(name = "test_id")
  protected int testId;

  @Column(name = "test_message", length = 128, nullable = false)
  protected String testMessage;

  public int getTestId() {
		return testId;
	}

	public void setTestId(int testId) {
		this.testId = testId;
	}

	public String getTestMessage() {
		return testMessage;
	}

	public void setTestMessage(String testMessage) {
		this.testMessage = testMessage;
	}
  
}
