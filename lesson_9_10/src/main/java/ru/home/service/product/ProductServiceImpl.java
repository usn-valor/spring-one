package ru.home.service.product;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.home.persist.product.Product;
import ru.home.persist.product.ProductRepository;
import ru.home.persist.product.ProductSpecification;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductRepr> findAll() {
        return productRepository.findAll().stream().map(ProductRepr::new).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Optional<ProductRepr> findById(long id) {
        return productRepository.findById(id).map(ProductRepr::new);
    }

    @Transactional
    @Override
    public void save(ProductRepr product) {
        productRepository.save(new Product(product));
    }

    @Transactional
    @Override
    public void delete(long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Page<ProductRepr> findWithFilter(String productNameFilter, String descriptionFilter, BigDecimal priceMinFilter,
                                        BigDecimal priceMaxFilter, Integer page, Integer size, String sortField) {
        Specification<Product> spec = Specification.where(null);
        if (productNameFilter != null && !productNameFilter.isBlank())
            spec = spec.and(ProductSpecification.productNameLike(productNameFilter));
        if (descriptionFilter != null && !descriptionFilter.isBlank())
            spec = spec.and(ProductSpecification.descriptionLike(descriptionFilter));
        if (priceMinFilter != null)
            spec = spec.and(ProductSpecification.priceMinFilter(priceMinFilter));
        if (priceMaxFilter != null)
            spec = spec.and(ProductSpecification.priceMaxFilter(priceMaxFilter));
        if (sortField != null && !sortField.isBlank())
            return productRepository.findAll(spec, PageRequest.of(page, size, Sort.by(sortField))).map(ProductRepr::new);
        return productRepository.findAll(spec, PageRequest.of(page, size)).map(ProductRepr::new);
    }
}
