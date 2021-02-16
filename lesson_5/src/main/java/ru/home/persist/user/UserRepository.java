package ru.home.persist.user;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class UserRepository {

    private final EntityManagerFactory emFactory;

    public UserRepository(EntityManagerFactory emFactory) {
        this.emFactory = emFactory;
    }

    public List<User> findAll() {
        System.out.println("All users in table");
        return emFactory.createEntityManager().createQuery("from User", User.class).getResultList();
    }

    public User findById(long id) {
        return emFactory.createEntityManager().find(User.class, id);
    }

    public void insert(User user) {
        EntityManager em = emFactory.createEntityManager();
        em.getTransaction().begin();

        em.persist(user);

        em.getTransaction().commit();

        em.close();
    }

    public void update(User user) {
        EntityManager em = emFactory.createEntityManager();
        em.getTransaction().begin();

        User u = em.find(User.class, user.getId());

        em.getTransaction().begin();

        u.setPassword("1234");

        em.getTransaction().commit();
        em.close();
    }

    public void delete(long id) {
        EntityManager em = emFactory.createEntityManager();
        em.getTransaction().begin();

        em.createQuery("delete from User where id=:id")
                .setParameter("id", id).executeUpdate();

        em.getTransaction().commit();
        em.close();
    }

}
