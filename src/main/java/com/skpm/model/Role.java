package com.skpm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The persistent class for the role database table.
 * 
 */
@Entity
@Table(name="role")
@XmlRootElement(name="Role")
public class Role extends AbstractLongPKEntity {

	
	@Column(name="role_name", nullable=false, length=30, unique=true)
	private String roleName;
	@Column(name="role_rank",unique=true)
    private long roleRank;
	//bi-directional many-to-many association to Permission
	/*@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(
		name="role_permission"
		, joinColumns={
			@JoinColumn(name="role_id", nullable=false)
			}
		, inverseJoinColumns={
			@JoinColumn(name="permission_id", nullable=false)
			}
		)
	private List<Permission> permissions;*/

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	public long getRoleRank() {
		return roleRank;
	}

	public void setRoleRank(long roleRank) {
		this.roleRank = roleRank;
	}

	@Override
	public String toString() {
		return getId().toString();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((roleName == null) ? 0 : roleName.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Role)) {
			return false;
		}
		Role other = (Role) obj;
		if (roleName == null) {
			if (other.roleName != null) {
				return false;
			}
		} else if (!roleName.equals(other.roleName)) {
			return false;
		}
		return true;
	}
	
	
}