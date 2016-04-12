/*
 *
 *To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.skpm.facade;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.primefaces.model.SortOrder;

import com.skpm.model.User;

/**
 *
 * @author User
 */
public abstract class AbstractFacade<T> {

	private Class<T> entityClass;
	private CriteriaQuery<T> criteriaQuery;
	private CriteriaBuilder criteriaBuilder;
	private Root<T> from;
	private List<Predicate> predicates;
	private Map<Object, Object> globalFilters;

	public Class<T> getEntityClass() {
		return entityClass;
	}

	public AbstractFacade(Class<T> entityClass) {
		this.entityClass = entityClass;
		this.globalFilters = new HashMap<Object, Object>();

	}

	protected abstract EntityManager getEntityManager();

	public void create(T entity) throws PersistenceException {
			getEntityManager().persist(entity);
			getEntityManager().flush();
	}

	public void edit(T entity) throws PersistenceException {
			getEntityManager().merge(entity);
			getEntityManager().flush();
	}
	public void remove(T entity) {
		getEntityManager().remove(getEntityManager().merge(entity));
		getEntityManager().flush();
	}

	//@TransactionAttribute(TransactionAttributeType.NEVER) to allow calling from inside transaction
	public T find(Object id) throws javax.persistence.NoResultException {
		return getEntityManager().find(entityClass, id);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<T> findAll() {
		javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
		cq.select(cq.from(entityClass));
		Query q = getEntityManager().createQuery(cq);

		List<T> results = q.getResultList();
		return results;
	}
	public CriteriaBuilder getCriteriaBuilder()
	{
		return criteriaBuilder;
	}
	protected CriteriaQuery<T> getCriteriaQuery()
	{
		return criteriaQuery;
	}

	public List<T> findRange(int[] range) {
		javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
		cq.select(cq.from(entityClass));
		javax.persistence.Query q = getEntityManager().createQuery(cq);
		q.setMaxResults(range[1] - range[0]);
		q.setFirstResult(range[0]);

		List<T> results = q.getResultList();
		return results;

	}

	protected int count() {
		javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
		javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
		cq.select(getEntityManager().getCriteriaBuilder().count(rt));
		javax.persistence.Query q = getEntityManager().createQuery(cq);
		return ((Long) q.getSingleResult()).intValue();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<T> load(int first, int pageSize, final String sortField, final SortOrder sortOrder, Map<String, Object> filters,User currentUser) throws NoSuchFieldException {
		return this.startQuery().addJoinForLoad().addSorting(sortField, sortOrder).addFilter(filters).imposeFilter(currentUser).distinct().getResultList(first, pageSize); //addJoin Deleted so that users whose Bank are not assigned can be view into user Management Section
	}
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public int getRowSize(Map<String, Object> filters,User currentUser) throws NoSuchFieldException {
		this.startQuery();
		javax.persistence.criteria.CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
		from = countQuery.distinct(true).from(entityClass);
		this.addFilter(filters).imposeFilter(currentUser);
		countQuery.select(criteriaBuilder.countDistinct(from));
		countQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));
		int rowCount = getEntityManager().createQuery(countQuery).getSingleResult().intValue();
		return rowCount;
	}
	public AbstractFacade<T> startQuery() {
		this._startQuery();
		return this;
	}
	protected void _startQuery() {
		this.criteriaBuilder = getEntityManager().getCriteriaBuilder();
		this.criteriaQuery = this.criteriaBuilder.createQuery(entityClass);
		from = this.criteriaQuery.from(entityClass);
		predicates = new ArrayList<Predicate>();
	}
	@PostConstruct
	public void _startQueryInitilisation() {
		this.criteriaBuilder = getEntityManager().getCriteriaBuilder();
		this.criteriaQuery = this.criteriaBuilder.createQuery(entityClass);
		from = this.criteriaQuery.from(entityClass);
		predicates = new ArrayList<Predicate>();
	}

	protected Path getTransitivePath(String pathString) {
		String result[];
		result = pathString.split("\\.");
		Path p = null;
		Boolean firstTime = true;
		for (String filterPropty : result) {
			if (firstTime) {
				firstTime = false;
				p = from.get(filterPropty);
			} else {
				p = p.get(filterPropty);
			}
		}
		return p;
	}
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	protected List<T> getResultList() {
		criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));
		return getEntityManager().createQuery(criteriaQuery).getResultList();
	}
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	protected List<T> getResultList(int first, int pageSize) {
		criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));
		// paginate
		List<T> data = getEntityManager().createQuery(criteriaQuery).setFirstResult(first).setMaxResults(pageSize).getResultList();
		return data;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public T getSingleResult() throws  javax.persistence.NoResultException {

		criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));
		return getEntityManager().createQuery(criteriaQuery).getSingleResult();
	}

	public AbstractFacade<T> addSorting(String sortField, final SortOrder sortOrder) {
		if (sortField != null) {
			Path pSort = getTransitivePath(sortField);
			return this.addSorting(pSort, sortOrder);
		}
		else
		{
			addorderBy();
		}
		return this;
	}

	public AbstractFacade<T> addSorting(Path p, final SortOrder sortOrder) {
		if (sortOrder == SortOrder.ASCENDING) {
			criteriaQuery.orderBy(criteriaBuilder.asc(p));
		} else {
			criteriaQuery.orderBy(criteriaBuilder.desc(p));
		}	
		return this;
	}

	@SuppressWarnings("unchecked")
	public void commonAddFilter(Path p, Object filterValue) {

		if (p.getJavaType().isAssignableFrom(String.class)) {
			predicates.add(criteriaBuilder.like(p, "%" + filterValue.toString() + "%"));
		} else if (p.getJavaType().isAssignableFrom(Boolean.class)) {
			if (filterValue.getClass().equals(String.class)) {
				/*
                 This in case of lazy datatable filter cause it always gives String
				 */
				Boolean b = Boolean.valueOf((String) filterValue);
				predicates.add(criteriaBuilder.equal(p, b));
			} else {
				predicates.add(criteriaBuilder.equal(p, filterValue));
			}

		} else if (p.getJavaType().isEnum()) {
			Object objectEnum;
			if (filterValue.getClass().equals(String.class)) {
				objectEnum = Enum.valueOf(p.getJavaType(), (String) filterValue);
			} else {
				objectEnum = filterValue;
			}
			predicates.add(criteriaBuilder.equal(p, objectEnum));
		} else {
			if (!p.getJavaType().isAssignableFrom(Date.class)) {
				predicates.add(criteriaBuilder.equal(p, filterValue));
			}
			else
			{
				predicates.add(criteriaBuilder.equal((Expression<Date>)p,(Date)filterValue));
			}
		}
	}

	public AbstractFacade<T> addFilter(Map<String, Object> filters) {
		for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {

			String filterProperty = it.next();
			Object filterValue = filters.get(filterProperty);

			Path p = getTransitivePath(filterProperty);
			commonAddFilter(p, filterValue);
		}
		return this;
	}
	public AbstractFacade<T> addFilterObject(Map<Object, Object> filters) {
		for (Iterator<Object> it = filters.keySet().iterator(); it.hasNext();) {
			Object filterProperty = it.next();
			Object filterValue = filters.get(filterProperty);
			if (null != filterValue) {
				Path p = getGeneratedSourcePath(filterProperty);
				commonAddFilter(p, filterValue);
			}
		}
		return this;

	}
	protected Path getGeneratedSourcePath(Object pathGeneratedObject) {
		Path p = (Path) pathGeneratedObject;
		return p;
	}

	public AbstractFacade<T> addCriteria(Predicate p) {
		this.predicates.add(p);
		return this;
	}
	public Map<Object, Object> getGlobalFilters() {
		return globalFilters;
	}
	public void setGlobalFilters(Map<Object, Object> globalFilters) {
		this.globalFilters = globalFilters;
	}
	/**
	 * @return from Root instance
	 * scope is public to make it available to classes where it is used,
	 * because protected/private fields are not available there
	 */
	protected Root<T> getRoot() {
		return from;
	}

	/** Any override implementation of this method must specify Fetch join to those Colloection Attribute that needs to be Fetch(must be implemented in All the fetchwhere role based Pagination is needed. 
	 * @modified By Anil
	 */
	protected AbstractFacade<T> addJoinForLoad()
	{
		return this;
	}

	public AbstractFacade<T> distinct()
	{
		criteriaQuery.select(from).distinct(true);
		return this;
	}

	/** Returns USerRoel AssignFacade
	 * @return not null instance of UserRoleAssignmentFacade
	 */
	protected  AbstractFacade<T> imposeFilter(User currentUser)
	{
		return this;
	}
	public  void addorderBy(){}
}
