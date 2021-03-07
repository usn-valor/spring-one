package ru.home.service.cart;

import java.util.List;

public interface CartService {

    void addProductForUserQty(long productId, long userId, int qty);

    void removeProductForUser(long productId, long userId, int qty);

    void removeAllForUser(long userId);

    List<LineItem> findAllItemsForUser(long userId);
}
