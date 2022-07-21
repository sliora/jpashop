package jpabook.japshop.controller;

import jpabook.japshop.domain.OrderStatus;
import lombok.Data;

@Data
public class OrderSearch {

    private String memberName;
    private OrderStatus orderStatus;
}
