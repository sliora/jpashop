package jpabook.japshop.api;

import jpabook.japshop.controller.OrderSearch;
import jpabook.japshop.domain.Address;
import jpabook.japshop.domain.OrderStatus;
import jpabook.japshop.domain.Orders;
import jpabook.japshop.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderService orderService;

    @GetMapping("/api/v1/simple-orders")
    public List<Orders> ordersV1() {
        List<Orders> orders = orderService.findOrders(new OrderSearch());
        for (Orders order : orders) {
            order.getMember().getName();   //LAZY 강제 초기화
        }
        return orders;
    }

    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() {
        //ORDER 2개
        //N + 1 -> 1 + 회원 N + 배송 N
        List<Orders> orders = orderService.findOrders(new OrderSearch());
        List<SimpleOrderDto> collect = orders.stream().map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());

        return collect;
    }

    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3() {
        List<Orders> orders = orderService.findAllWithMemberDelivery();
        List<SimpleOrderDto> collect = orders.stream().map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());

        return collect;
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Orders orders) {
            this.orderId = orders.getId();
            this.name = orders.getMember().getName();;
            this.orderDate = orders.getOrderDate();
            this.orderStatus = orders.getStatus();
            this.address = orders.getMember().getAddress();
        }
    }


}
