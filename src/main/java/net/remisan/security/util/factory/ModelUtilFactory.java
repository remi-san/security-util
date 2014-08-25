package net.remisan.security.util.factory;

import java.util.Map;

import net.remisan.security.util.ModelUtil;

public class ModelUtilFactory {

    private Map<String, ModelUtil> utilList;

    public ModelUtil getModelUtil(String className) {

        String key = "default";
        if (this.utilList.containsKey(className)) {
            key = className;
        }

        return this.utilList.get(key);
    }

    public void setUtilList(Map<String, ModelUtil> utilList) {
        this.utilList = utilList;
    }
}
