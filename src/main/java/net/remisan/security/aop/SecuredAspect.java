package net.remisan.security.aop;

import net.remisan.security.model.SecuredPersistable;
import net.remisan.security.util.ModelUtil;
import net.remisan.security.util.factory.ModelUtilFactory;

import org.aspectj.lang.ProceedingJoinPoint;

//@Aspect
public class SecuredAspect {

    // @Autowired
    private ModelUtilFactory modelUtilFactory;

    // @Around("execution(* **.service.*.save(..))")
    public Object interceptSaveUser(ProceedingJoinPoint joinPoint) throws Throwable {
        Object obj = joinPoint.getArgs()[0];
        boolean newObject = false;

        System.out.println("Save");

        ModelUtil modelUtil = this.modelUtilFactory.getModelUtil(obj.getClass().getCanonicalName());

        if (obj instanceof SecuredPersistable) {
            if (((SecuredPersistable) obj).isNew()) {
                newObject = true;
            }
            modelUtil.preSave((SecuredPersistable) obj);
        }

        obj = joinPoint.proceed();

        if (obj instanceof SecuredPersistable) {
            modelUtil.postSave((SecuredPersistable) obj, newObject);
        }

        return obj;
    }
}
