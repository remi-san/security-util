package net.remisan.security.util.factory;

import java.util.HashMap;
import java.util.Map;

import net.remisan.security.util.BaseUtil;
import net.remisan.security.util.ModelUtil;
import net.remisan.security.util.UserUtil;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration("file:src/test/resources/root-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class ModelUtilFactoryTest {

    private ModelUtilFactory factory;
    
    @Autowired
    private BaseUtil baseUtil;
    
    @Autowired
    private UserUtil userUtil;
    
    @Before
    public void init() {
        
        Map<String, ModelUtil> utilList = new HashMap<String, ModelUtil>();
        utilList.put("default", this.baseUtil);
        utilList.put("net.remisan.security.model.SecurityUser", this.userUtil);
        
        this.factory = new ModelUtilFactory();
        this.factory.setUtilList(utilList);
    }
    
    @Test
    public void test() {
        
        ModelUtil util = this.factory.getModelUtil("default");
        Assert.assertEquals(BaseUtil.class, util.getClass());
    }
    
}
