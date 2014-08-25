package net.remisan.security.registry;

import java.util.Map;

import net.remisan.base.model.PersistableEntity;
import net.remisan.base.registry.BaseRegistry;
import net.remisan.base.service.Service;

public class SecurityServiceRegistry extends BaseRegistry<Service<PersistableEntity>> {

    public SecurityServiceRegistry(Map<String, Service<PersistableEntity>> objects) {
        super(objects);
    }
    
}
