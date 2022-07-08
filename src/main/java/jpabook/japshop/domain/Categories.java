package jpabook.japshop.domain;

import jpabook.japshop.domain.item.Item;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Categories {
    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;
    private String name;

    private Item Items;
}
