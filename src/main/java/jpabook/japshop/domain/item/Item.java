package jpabook.japshop.domain.item;

import jpabook.japshop.domain.Categories;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;

@Entity
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;
}
