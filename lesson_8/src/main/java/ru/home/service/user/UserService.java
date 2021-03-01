package ru.home.service.user;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserRepr> findAll();

    Optional<UserRepr> findById(long id); // меняется метод, так как репозиторий наследует JpaRepository

    //void insert(UserRepr user);
    void save(UserRepr user);

    //void update(UserRepr user);

    void delete(long id);

    //List<UserRepr> findWithFilter(String usernameFilter, Integer minAge, Integer maxAge);

    Page<UserRepr> findWithFilter(String usernameFilter, Integer minAge, Integer maxAge, Integer page, Integer size);
}
