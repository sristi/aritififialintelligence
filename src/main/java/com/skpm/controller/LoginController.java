package com.skpm.controller;


import java.io.Serializable;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.skpm.facade.UserFacade;
import com.skpm.model.User;
import com.skpm.util.MessageUtil;

/**
 * @author suresh
 *
 */
@Named("loginController")
@ViewScoped
public class LoginController implements Serializable{
	@Inject private UserFacade userFacade;
	@Inject private MessageUtil messageUtil;
	@Inject private SessionHolder sessionHolder;

	private String userName;
	private String passCode;
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the passCode
	 */
	public String getPassCode() {
		return passCode;
	}
	/**
	 * @param passCode the passCode to set
	 */
	public void setPassCode(String passCode) {
		this.passCode = passCode;
	}
	public String performLogin()
	{
		try {
			
			User currentUser = userFacade.findUserByUserNameAndPassword(userName, passCode);
			if(null != currentUser && currentUser.getId()>0)
			{
				FacesContext.getCurrentInstance().getAttributes().put("user", currentUser);
				sessionHolder.setCurrentSessionUser(currentUser);
				messageUtil.showInfoMessage("Login Successfull");
				return "/pages/welcome.xhtml?faces-redirect=true";
			}
			else
			{
				messageUtil.showWarningMessage("Credentials do not match!");
				//return "/index.xhtml?faces-redirect=true";
				return "";
			}
		} catch (Exception e) {
			messageUtil.showErrorMessage("Credential is invalid.");
			return "";
		}
	}
	public String logout()
	{
		FacesContext.getCurrentInstance().getAttributes().put("user", null);
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		sessionHolder.setCurrentSessionUser(null);
		return "/index.xhtml?faces-redirect=true";
	}
	public void redirectToLoginPage()
	{
		User loggedInUser = sessionHolder.getCurrentSessionUser();
		if(null ==loggedInUser)
		{
			FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
			FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null,"/index.xhtml?faces-redirect=true");
		}
	}

}
