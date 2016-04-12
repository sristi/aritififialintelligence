package com.skpm.controller;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.inject.Inject;

import com.skpm.facade.AbstractFacade;
import com.skpm.model.ValueEnum;
import com.skpm.util.MessageUtil;

/**
 *
 * @author uttam
 * @edited by suresh
 */
public abstract class AbstractController<T, C extends AbstractFacade<T>> implements Serializable {

	@Inject
	protected MessageUtil messageUtil;
	private T current;
	private AbstractFacade<T> ejbFacade;

	public void setSelected(T current) {
		this.current = current;
	}

	public T getSelected() {
		return current;
	}

	public abstract AbstractFacade<T> getFacade();
	
	public SelectItem[] getItemsAvailableSelectMany() {
		return getSelectItems(getFacade().findAll(), false);
	}
	
	public SelectItem[] getItemsAvailableSelectOne() {
		return getSelectItems(getFacade().findAll(), true);
	}

	public SelectItem[] getSelectItems(Collection<?> entities, boolean selectOne) {
		int size = selectOne ? entities.size() + 1 : entities.size();
		SelectItem[] items = new SelectItem[size];
		int i = 0;
		if (selectOne) {
			items[0] = new SelectItem("", "Select One - - -");
			i++;
		}
		for (Object x : entities) {
			items[i++] = new SelectItem(x, x.toString());
		}
		return items;
	}

	public SelectItem[] getEnumOptions(Class<?> e) {
		if (e.isEnum()) {
			Object[] a = e.getEnumConstants();
			SelectItem[] items = new SelectItem[a.length+1];
			int i = 0;
			items[0] = new SelectItem("", "Select One - - -");

			for (Object o : a) {
				if (o instanceof ValueEnum) {
					items[++i] = new SelectItem(o, ((ValueEnum) o).getValue());
				} else {
					items[++i] = new SelectItem(o, o.toString());
				}
			}
			return items;
		} else {
			throw new IllegalArgumentException(e.toString() + "can't be used !");
		}


	}

	public List<T> findAll() {
		return this.getFacade().findAll();
	}

	public List<T> getAll() {
		return this.findAll();
	}

	public List<T> findAllFiltered() {
		return this.getFacade().findAll();
	}

}