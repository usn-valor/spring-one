package ru.home.persist.product;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import ru.home.persist.user.User;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @Query("select p from Product p where (p.productName like concat('%', :productNameFilter, '%') or concat('%', :productNameFilter,'%') is null) and " +
            "(p.description like concat('%', :descriptionFilter, '%') or concat('%', :descriptionFilter,'%') is null) and " +
            "(p.price >= :priceMinFilter or :priceMinFilter is null) and " +
            "(p.price <= :priceMaxFilter or :priceMaxFilter is null)")
    List<Product> findWithFilter(String productNameFilter, String descriptionFilter, BigDecimal priceMinFilter, BigDecimal priceMaxFilter);
}
