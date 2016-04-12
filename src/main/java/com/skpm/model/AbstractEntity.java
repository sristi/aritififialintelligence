/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.skpm.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 *
 * @author dev
 */
@JsonIgnoreProperties(ignoreUnknown=true)  
public abstract class AbstractEntity<T extends Number>  implements PersistentEntity<T>  {

     
    @Override
    public abstract T getId();
    
    @Override
    public boolean isNew() {
        return (this.getId() == null);
    }
    
    @Override
    public abstract boolean equals(Object object);
    
    @Override
    public abstract int hashCode();
    
    @Override
    public abstract String toString();
    
}
