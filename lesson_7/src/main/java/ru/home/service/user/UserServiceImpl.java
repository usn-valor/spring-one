package ru.home.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.home.persist.user.User;
import ru.home.persist.user.UserRepository;
import ru.home.service.user.UserRepr;
import ru.home.service.user.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserRepr> findAll() {
        return userRepository.findAll().stream().map(UserRepr::new).collect(Collectors.toList());
    }

    @Transactional // аннотация, чтобы автоматически открывалась и завершалась транзакция
    @Override
    /*public UserRepr findById(long id) {
        User user = userRepository.findById(id);
        if (user != null)
            return new UserRepr(user);
        return null;*/
    public Optional<UserRepr> findById(long id) {
        return userRepository.findById(id).map(UserRepr::new);
    }

    @Transactional
    @Override
    /*public void insert(UserRepr user) {
        userRepository.insert(new User(user));
    }*/
    public void save(UserRepr user) {
        userRepository.save(new User(user));
    }

    /*@Transactional
    @Override
    public void update(UserRepr user) {
        userRepository.update(new User(user));
    }*/

    @Transactional
    @Override
    /*public void delete(long id) {
        userRepository.delete(id);
    }*/
    public void delete(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<UserRepr> findWithFilter(String usernameFilter) {
        return userRepository.findUserByUsernameLike(usernameFilter).stream().map(UserRepr::new).collect(Collectors.toList());
    }
}
