package user.service;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import user.domain.User;
/* 미사용. 팩토리 빈 및 다이내믹 프록시를 활용하여, 동적으로 이러한 프록시를 생성한다. */
public class UserServiceTx implements UserService {
    UserService userService;
    PlatformTransactionManager transactionManager;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    /* DI받은 오브젝트에 모든 것을 위임하는 모습. */
    @Override
    public void add(User user) {
        userService.add(user);
    }
    /* 특정 동작에 대해서는 이렇게 재구현을 한다. 조금 귀찮을 듯... */
    @Override
    public void upgradeLevels() {
        TransactionStatus status
                = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
        try{
            userService.upgradeLevels();
            this.transactionManager.commit(status);
        } catch (RuntimeException e){
            this.transactionManager.rollback(status);
            throw e;
        }
    }
}
