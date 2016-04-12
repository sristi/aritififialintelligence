package com.skpm.util;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.primefaces.model.SortOrder;

import com.skpm.facade.AbstractFacade;
import com.skpm.model.AbstractLongPKEntity;


public class LazyLodingDataModel<T extends AbstractLongPKEntity> extends org.primefaces.model.LazyDataModel<T>  {
	protected AbstractFacade ejbFacade;
	protected List<T> list;

    public LazyLodingDataModel(AbstractFacade<T> ejbFacade) {
        this.ejbFacade = ejbFacade;

    }

    @Override
    public T getRowData(String rowKey) {
        for (T ob : list) {
            if (String.valueOf(ob.getId()).equals(rowKey)) {
                return ob;
            }
        }
        return null;
    }

    @Override
    public Object getRowKey(T eq) {
        return eq.getId();
    }

    @Override
    public void setRowIndex(int rowIndex) {
        /*
         * The following is in ancestor (LazyDataModel):
         * this.rowIndex = rowIndex == -1 ? rowIndex : (rowIndex % pageSize);
         */
        if (rowIndex == -1 || getPageSize() == 0) {
            super.setRowIndex(-1);
        } else {
            super.setRowIndex(rowIndex % getPageSize());
        }
    }
	@Override
    public List<T> load(int first, int pageSize, final String sortField, final SortOrder sortOrder, Map<String, Object> filters) {
    	list = null;
        try {
        	list = ejbFacade.load(first, pageSize, sortField, sortOrder, filters,null);
        } catch (NoSuchFieldException ex) {
            Logger.getLogger(LazyLodingDataModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            setRowCount(ejbFacade.getRowSize(filters,null));
        } catch (NoSuchFieldException ex) {
            Logger.getLogger(LazyLodingDataModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
}