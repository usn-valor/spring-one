package ru.home.persist.product;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class ProductRepository {

    private final EntityManagerFactory emFactory;

    public ProductRepository(EntityManagerFactory emFactory) {
        this.emFactory = emFactory;
    }

    public List<Product> findAll() {
        System.out.println("All products in table");
        return emFactory.createEntityManager().createQuery("from Product", Product.class).getResultList();
    }

    public Product findById(long id) {
        return emFactory.createEntityManager().find(Product.class, id);
    }

    public void insert(Product product) {
        EntityManager em = emFactory.createEntityManager();
        em.getTransaction().begin();

        em.persist(product);

        em.getTransaction().commit();

        em.close();
    }

    public void saveOrUpdate(Product product) {
        EntityManager em = emFactory.createEntityManager();
        em.getTransaction().begin();

        Product p = em.find(Product.class, product.getId());

        em.getTransaction().begin();

        p.setDescription("Нереально крутой продукт");

        em.getTransaction().commit();
        em.close();
    }

    public void deleteById(long id) {
        EntityManager em = emFactory.createEntityManager();
        em.getTransaction().begin();

        em.createQuery("delete from Product where id=:id")
                .setParameter("id", id).executeUpdate();

        em.getTransaction().commit();
        em.close();
    }
}
