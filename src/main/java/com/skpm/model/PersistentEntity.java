package com.skpm.model;

import java.io.Serializable;

/**
 *
 * @author Dr. Spock (spock at dev.java.net)
 */
public interface PersistentEntity<PK extends Serializable> extends Serializable {

    PK getId();

    boolean isNew();

    public void setId(PK id);
    
    public  boolean equals(Object object);
    
    public  int hashCode();
    
    public  String toString();
}
