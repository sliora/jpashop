package jpabook.japshop.domain;

import jpabook.japshop.domain.item.Item;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class OrderItem {
    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "orders_id")
    private Orders order;

    private int orderPrice;
    private int count;

}
