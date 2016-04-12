package com.skpm.controller;

import java.util.ResourceBundle;

import javax.persistence.PersistenceException;

import com.skpm.facade.AbstractFacade;
import com.skpm.model.AbstractLongPKEntity;


/**
 *
 * @author uttam
 * @edited by suresh
 */
public abstract class AbstractViewController<T extends AbstractLongPKEntity,C extends AbstractFacade<T>> extends AbstractController<T, C> {

    public AbstractViewController() {
    }

    public String prepareList() {
	return "List?faces-redirect=true";
    }

    private AbstractLongPKEntity abstractEntity;

    public String prepareView(T selected) {
	setSelected(selected);
	return "View?faces-redirect=true&id=" + selected.getId();
    }

    public String gotoListPage() {

	return "View?faces-redirect=true";
    }

    public void prepareCreate() {
	Class<T> entityClass = getFacade().getEntityClass();
	try {
	    // current = entityClass.newInstance();
	    setButtonState(true);
	    setSelected(entityClass.newInstance());

	} catch (InstantiationException ex) {
	    //  log.log(Level.SEVERE, ex.getMessage());
	    messageUtil.showErrorMessage(ResourceBundle.getBundle("/Bundle").getString("instantiation_exception"));
	} catch (IllegalAccessException ex) {
	    //  log.log(Level.SEVERE, ex.getMessage());
	}
    }
    public String prepareView() {
	return "View?faces-redirect=true";
    }

    public String create() {

	try {
	    getFacade().create(getSelected());
	    messageUtil.showInfoMessage(ResourceBundle.getBundle("/Bundle").getString("Created_Successfully"));
	    return gotoListPage();

	} /*catch (Exception e) {
	    handllerForConstraintViolation();
	    return null;
	}*/
	catch (PersistenceException e) {
	    messageUtil.showErrorMessage(ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
	    return null;
	}
    }

    public String createAndView() {
	try {
	    getFacade().create(getSelected());
	    messageUtil.showInfoMessage(ResourceBundle.getBundle("/Bundle").getString("Created_Successfully"));
	    return prepareView();
	} catch (Exception e) {
	    messageUtil.showErrorMessage(ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
	    return null;
	}
    }

    public String destroy(T selected) {

	try {
	    getFacade().remove(selected);
	    messageUtil.showInfoMessage(ResourceBundle.getBundle("/Bundle").getString("Deleted_Sucessfully"));
	} catch (Exception e) {
	    messageUtil.showErrorMessage(ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
	}
	return "List?faces-redirect=true";
    }

    public String destroyAndView(T selected) {
	try {
	    getFacade().remove(selected);
	    messageUtil.showInfoMessage(ResourceBundle.getBundle("/Bundle").getString("Deleted_Sucessfully"));
	    return "List?faces-redirect=true";
	} catch (Exception e) {
	    messageUtil.showErrorMessage(ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
	    return "View?faces-redirect=true&id=" + selected.getId();
	}
    }
    private Long idViewNavigate = 0l;

    public Long getIdViewNavigate() {
	return idViewNavigate;
    }

    public void setIdViewNavigate(Long idViewNavigate) {
	this.idViewNavigate = idViewNavigate;
    }

    public void initToGetViewData() {
	try {
	    setSelected(getFacade().find(idViewNavigate));
	} catch (Exception ex) {
	    //  Logger.getLogger(StoresController.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    public String prepareEdit(AbstractLongPKEntity selected) {
	setSelected((T) selected);
	return "Edit?faces-redirect=true&id=" + selected.getId();
    }

    public String update(AbstractLongPKEntity selected) {
	try {
	    getFacade().edit((T) selected);
	    messageUtil.showInfoMessage(ResourceBundle.getBundle("/Bundle").getString("Updated_Sucessfully"));
	    return "View?faces-redirect=true&id=" + selected.getId();
	}/* catch (DbInterigityConstraintViolation e) {
	    handllerForConstraintViolation();
	    return null;
	}*/
	catch (PersistenceException e) {
	    messageUtil.showWarningMessage(ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
	    return null;
	}
    }

    // for dialog box
    public void prepareEditForDialog(AbstractLongPKEntity selected) {
	setSelected((T) selected);
    }

    public void prepareViewForDialog(AbstractLongPKEntity selected) {
	setSelected((T) selected);
    }



    /** By:Anil Acharya
     * This method is handller Controller than may be implemented by any viewScopeBean to provide custom handller of interigity Constriant violation Exception
     * 
     */
    public void handllerForConstraintViolation()
    {
	messageUtil.setValidationFlag();
	messageUtil.showWarningMessage(ResourceBundle.getBundle("/Bundle").getString("genericIntirigityViolation"));
    }
    public abstract void setButtonState(boolean buttonState); //for disabling when row is clicked on each view
}
