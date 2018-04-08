package user.dao;

import user.domain.User;

import java.util.List;

/*
    기술에 독립적인 Dao를 만들기 위해 Interface로 빼봤다.
 */
public interface UserDao {
    void add(User user);
    User get(String id);
    List<User> getAll();
    void deleteAll();
    int getCount();
    void update(User user);
}
