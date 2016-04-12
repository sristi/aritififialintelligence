/**
 * 
 */
package com.skpm.controller;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import com.skpm.model.User;

/**
 * @author suresh
 *
 */
@Named("sessionController")
@SessionScoped
public class SessionHolder implements Serializable{

	private User currentSessionUser;

	public User getCurrentSessionUser() {
		return currentSessionUser;
	}

	public void setCurrentSessionUser(User currentSessionUser) {
		this.currentSessionUser = currentSessionUser;
	}
	
}
