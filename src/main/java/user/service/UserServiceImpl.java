package user.service;


import org.springframework.transaction.PlatformTransactionManager;
import user.dao.UserDao;
import user.domain.Level;
import user.domain.User;

import javax.sql.DataSource;
import java.util.List;
import java.util.Properties;

public class UserServiceImpl implements UserService {
    private UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void upgradeLevels() {
        try {
            upgradeLevelsInternal();
        } catch (Exception e) {
            throw e;
        }
    }

    private void upgradeLevelsInternal() {
        List<User> users = userDao.getAll();
        for (User user :
                users) {
            if(canUpdradeLevel(user)) {
                upgradeLevel(user);
            }
        }
    }

    // 업그레이드 가능한지 확인
    private boolean canUpdradeLevel(User user) {
        Level currentLevel = user.getLevel();
        switch (currentLevel) {
            case BASIC: return (user.getLogin() >= 50);
            case SILVER: return (user.getRecommend() >= 30);
            case GOLD: return false;
            default: throw new IllegalArgumentException("UNKNOWN LEVEL: "+currentLevel);
        }
    }

    // 레벨에 따른 업그레이드.
    protected void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
        sendUpgradeEmail(user);
    }

    public void sendUpgradeEmail(User user) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "mail.ksug.org");

    }
    public void add(User user) {
        if(user.getLevel() == null) {
            user.setLevel(Level.BASIC);
        }
        userDao.add(user);
    }

    public static class TestUserServiceException extends RuntimeException {
    }

}
