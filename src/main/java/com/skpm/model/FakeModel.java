package com.skpm.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author suresh
 *
 */
@XmlRootElement(name="FakeModel")
public class FakeModel implements Serializable{
	private int sn;
	private String name;
	private String registrationcode;
	private String collegecode;
	private String contact;
	private String address;
	private String level;
	/**
	 * @return the sn
	 */
	@XmlElement(name="SN")
	public int getSn() {
		return sn;
	}
	/**
	 * @param sn the sn to set
	 */
	public void setSn(int sn) {
		this.sn = sn;
	}
	/**
	 * @return the name
	 */
	@XmlElement(name="Name")
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the registrationcode
	 */
	public String getRegistrationcode() {
		return registrationcode;
	}
	/**
	 * @param registrationcode the registrationcode to set
	 */
	public void setRegistrationcode(String registrationcode) {
		this.registrationcode = registrationcode;
	}
	/**
	 * @return the collegecode
	 */
	public String getCollegecode() {
		return collegecode;
	}
	/**
	 * @param collegecode the collegecode to set
	 */
	public void setCollegecode(String collegecode) {
		this.collegecode = collegecode;
	}
	/**
	 * @return the contact
	 */
	public String getContact() {
		return contact;
	}
	/**
	 * @param contact the contact to set
	 */
	public void setContact(String contact) {
		this.contact = contact;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return the level
	 */
	public String getLevel() {
		return level;
	}
	/**
	 * @param level the level to set
	 */
	public void setLevel(String level) {
		this.level = level;
	}
	
}
