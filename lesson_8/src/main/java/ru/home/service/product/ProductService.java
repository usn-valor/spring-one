package ru.home.service.product;

import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<ProductRepr> findAll();

    Optional<ProductRepr> findById(long id); // меняется метод, так как репозиторий наследует JpaRepository

    void save(ProductRepr product);

    void delete(long id);

    Page<ProductRepr> findWithFilter(String productNameFilter, String descriptionFilter, BigDecimal priceMinFilter,
                                     BigDecimal priceMaxFilter, Integer page, Integer size, String sortField);
}
