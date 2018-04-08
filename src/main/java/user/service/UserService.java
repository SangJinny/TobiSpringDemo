package user.service;


import user.dao.UserDao;
import user.domain.Level;
import user.domain.User;

import java.util.List;

public class UserService {
    UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void upgradeLevels() {
        List<User> userList = userDao.getAll();
        for(User user : userList) {
            if(canUpdradeLevel(user)) {
                upgradeLevel(user);
            }
        }
    }

    public void add(User user) {
        if(user.getLevel() == null) {
            user.setLevel(Level.BASIC);
        }
        userDao.add(user);
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
    private void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
    }
}
