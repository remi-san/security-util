package net.remisan.security.util;

import net.remisan.security.model.SecuredPersistable;

import org.springframework.stereotype.Component;

@Component
public class BaseUtil implements ModelUtil {

    @Override
    public void preSave(SecuredPersistable object) {
    }

    @Override
    public void postSave(SecuredPersistable object, boolean newObject) {
    }
}
