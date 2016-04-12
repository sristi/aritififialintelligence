/**
 * 
 */
package com.skpm.facade;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.skpm.model.User;
import com.skpm.model.User_;

/**
 * @author suresh
 *
 */
@Stateless
public class UserFacade extends AbstractFacade<User> {
	@PersistenceContext(unitName="mypu")
	EntityManager em;
	
	public UserFacade() {
		super(User.class);
	}
	
	public void create(User user)
	{
		em.persist(user);
	}
	public void update(User user)
	{
		em.merge(user);
	}
	public void remove(Long id)
	{
		em.remove(id);
	}
	public User find(Long id)
	{
		return em.find(User.class, id);
	}
	@SuppressWarnings("unchecked")
	public List<User> findAll()
	{
		return em.createQuery("SELECT u from User u").getResultList();
	}
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	public User findUserByUserNameAndPassword(String userName, String password) throws Exception
	{
		return ((UserFacade)this.startQuery()).filterByUserNameAndPassword(userName,password).distinct().getSingleResult();//super.find(id);
	}
	private  UserFacade filterByUserNameAndPassword(String userName, String password){
		addCriteria(getCriteriaBuilder().and(getCriteriaBuilder().equal(getRoot().get(User_.userName), userName)
				,getCriteriaBuilder().equal(getRoot().get(User_.password), password)));
		return this;
	}
	
}
