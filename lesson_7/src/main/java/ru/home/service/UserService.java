package ru.home.service;

import ru.home.persist.user.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserRepr> findAll();

    Optional<UserRepr> findById(long id); // меняется метод, так как репозиторий наследует JpaRepository

    //void insert(UserRepr user);
    void save(UserRepr user);

    //void update(UserRepr user);

    void delete(long id);

    List<UserRepr> findWithFilter(String usernameFilter);
}
