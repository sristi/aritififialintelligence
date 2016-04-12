/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.skpm.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.codehaus.jackson.annotate.JsonIgnore;


/**
 *
 * @author User
 * @edited by suresh
 */
@MappedSuperclass
public abstract class AbstractLongPKEntity extends AbstractEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @JsonIgnore
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonIgnore
    public boolean isNew() {
        return (null == this.getId());
    }

    

}
