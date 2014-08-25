package net.remisan.security.util;

import net.remisan.security.model.SecuredPersistable;

public interface ModelUtil {

    void preSave(SecuredPersistable object);

    void postSave(SecuredPersistable object, boolean newObject);

}
