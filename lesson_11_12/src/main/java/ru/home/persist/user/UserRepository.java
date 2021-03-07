package ru.home.persist.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> { // все закомменчено и класс заменен на интерфейс

    /*@PersistenceContext // аннотация, гарантирующая, что у нас всегда правильный экземпляр энтити-менеджера
    private EntityManager em;

    public List<User> findAll() {
        return em.createQuery("from User", User.class).getResultList();
    }

    public User findById(long id) {
        return em.find(User.class, id);
    }

    public void insert(User user) {
        em.persist(user);
    }

    public void update(User user) {
        em.merge(user);
    }

    public void delete(long id) {
        *//*User user = findById(id);
        if (user != null)
            em.remove(user);*//*
    em.createQuery("delete from User where id = :id").setParameter("id", id).executeUpdate();
    }

    @Query("select u from User u where u.username like concat('%',:username,'%')")
    List<User> findUserByUsernameLike(@Param("username") String username); // по имени метода определяется какого рода запрос нужен в БД */

    @Query("select u from User u where (u.username like concat('%', :username, '%') or concat('%', :username,'%') is null) and " +
            "(u.age >= :minAge or :minAge is null) and " +
            "(u.age <= :maxAge or :maxAge is null)")
    List<User> findWithFilter(@Param("username") String usernameFilter, @Param("minAge") Integer minAge, @Param("maxAge") Integer maxAge);

    Optional<User> findUserByUsername(String username);
}
