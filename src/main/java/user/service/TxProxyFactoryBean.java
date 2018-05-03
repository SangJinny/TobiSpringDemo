package user.service;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import java.lang.reflect.Proxy;

/* 다이내믹 프록시의 생성을 Spring Container에서 처리할 수 있도록해주는 Factory Bean
*  DI 설정만 하면 다이나막 프록시를 다양한 타겟 오브젝트에 적용 가능. 462p*/
public class TxProxyFactoryBean implements FactoryBean<Object> {
    Object target;
    PlatformTransactionManager transactionManager;
    String pattern;
    Class<?> serviceInterface; // UserService를 제외한 다른 타깃에도 사용 가능.

    public void setTarget(Object target) {
        this.target = target;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public void setServiceInterface(Class<?> serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    @Override
    public Object getObject() throws Exception {
        TransactionHandler txHandler = new TransactionHandler();
        txHandler.setTarget(target);
        txHandler.setTransactionManager(transactionManager);
        txHandler.setPattern(pattern);
        /* 다이내믹 프록시는 꼭 아래와 같이 생성해야하다 보니 이러한 팩토리 빈이 필요하다!!! */
        return Proxy.newProxyInstance(
                getClass().getClassLoader(), new Class[] {serviceInterface}, txHandler
        );
    }

    @Override
    public Class<?> getObjectType() {
        return serviceInterface;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
