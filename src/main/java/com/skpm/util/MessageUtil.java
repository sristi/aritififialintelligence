package com.skpm.util;

import java.io.Serializable;

import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

/**
 * @author suresh
 *
 */
@Dependent
public class MessageUtil implements Serializable{
	public void showInfoMessage(String message) {
		FacesMessage facesMessage = createMessage(FacesMessage.SEVERITY_INFO, message);
		addMessageToContext(facesMessage);
	}
	public void showWarningMessage(String message) {
		FacesMessage facesMessage = createMessage(FacesMessage.SEVERITY_WARN, message);
		addMessageToContext(facesMessage);
	}
	
	public void showErrorMessage(String message) {
		FacesMessage facesMessage = createMessage(FacesMessage.SEVERITY_ERROR, message);
		addMessageToContext(facesMessage);
	}

	private FacesMessage createMessage(Severity severity, String mensagemErro) {
		return new FacesMessage(severity, mensagemErro, mensagemErro);
	}

	private void addMessageToContext(FacesMessage facesMessage) {
		FacesContext.getCurrentInstance().addMessage(null, facesMessage);
	}
	public void addMessageWithClientId(String id,String message)
	{
		FacesMessage facesMessage = createMessage(FacesMessage.SEVERITY_ERROR, message);
		FacesContext.getCurrentInstance().addMessage(id, facesMessage);
	}
	
	public void setValidationFlag()
	{
		FacesContext.getCurrentInstance().validationFailed();
		
	}
}
