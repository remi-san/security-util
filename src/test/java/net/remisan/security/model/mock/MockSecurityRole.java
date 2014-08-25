package net.remisan.security.model.mock;

import javax.persistence.Entity;

import net.remisan.security.model.SecurityRole;

@Entity
public class MockSecurityRole implements SecurityRole {

    private static final long serialVersionUID = -5713058614496358797L;
    
    private Long id;
    private String name;

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public boolean isNew() {
        return this.id == null;
    }

}
