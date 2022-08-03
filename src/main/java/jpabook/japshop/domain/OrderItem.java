package jpabook.japshop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jpabook.japshop.domain.item.Item;
import lombok.Data;

import javax.persistence.*;
import javax.persistence.criteria.Order;

@Entity
@Data
public class OrderItem {
    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id")
    private Orders order;

    private int orderPrice;
    private int count;

    //==생성 메서드==//
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);
        return orderItem;
    }

    //==비즈니스 로직==//
    public void cancel() {
        getItem().addStock(getCount());
    }

    //==조회 로직==//

    /**
     * 주문상품 전체 가격 조회
     * @return getOrderPrice() * getCount();
     */
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }

}
