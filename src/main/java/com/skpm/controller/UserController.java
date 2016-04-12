/**
 * 
 */
package com.skpm.controller;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.skpm.facade.AbstractFacade;
import com.skpm.facade.UserFacade;
import com.skpm.model.User;
import com.skpm.util.LazyLodingDataModel;

/**
 * @author suresh
 *
 */
@Named("userController")
@ViewScoped
public class UserController extends AbstractViewController<User, UserFacade>{
	@Inject private UserFacade userFacade;
	
	private LazyLodingDataModel<User> lazyModel;
	
	private User selected;
	
	public User getSelected() {
		return selected;
	}
	public void setSelected(User selected) {
		this.selected = selected;
	}
	public void addUser()
	{
		userFacade.create(selected);
	}
	@Override
	public void setButtonState(boolean buttonState) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public AbstractFacade<User> getFacade() {
		// TODO Auto-generated method stub
		return userFacade;
	}
	
	@PostConstruct
	private  void init()
	{
		lazyModel= new LazyLodingDataModel(userFacade);
	}
	
	public LazyLodingDataModel<User> getLazyModel() {
		return lazyModel;
	}
	public void setLazyModel(LazyLodingDataModel<User> lazyModel) {
		this.lazyModel = lazyModel;
	}
}
