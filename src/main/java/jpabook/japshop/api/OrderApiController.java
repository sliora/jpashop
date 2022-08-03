package jpabook.japshop.api;

import jpabook.japshop.controller.OrderSearch;
import jpabook.japshop.domain.Address;
import jpabook.japshop.domain.OrderItem;
import jpabook.japshop.domain.OrderStatus;
import jpabook.japshop.domain.Orders;
import jpabook.japshop.repository.OrderRepository;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.Order;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;

    //모듈이 없기 떄문에 안됨..
    @GetMapping("/api/v1/orders")
    public List<Orders> ordersV1() {
        List<Orders> all = orderRepository.findAllByString(new OrderSearch());
        for (Orders orders : all) {
            orders.getMember().getName();           //모듈을 사용할 경우 LAZY가 기본이라 강제 호출
            orders.getDelivery().getAddress();

            List<OrderItem> orderItems = orders.getOrderItems();
            orderItems.stream().forEach(o -> o.getItem().getName());
        }
        return all;
    }

    @GetMapping("/api/v2/orders")
    public List<OrdersDto> ordersV2() {
        List<Orders> getOrders = orderRepository.findAllByString(new OrderSearch());
        List<OrdersDto> collect = getOrders.stream().map(o -> new OrdersDto(o)).collect(Collectors.toList());
        return collect;
    }

    @Data
    static class OrdersDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItem> orderItems;

        public OrdersDto(Orders orders) {
            this.orderId = orders.getId();
            this.name = orders.getMember().getName();
            this.orderDate = orders.getOrderDate();
            this.orderStatus = orders.getStatus();
            this.address = orders.getMember().getAddress();
            //orders.getOrderItems().stream().map(o -> new OrderItemDto(o)).collect(Collectors.toList());
            this.orderItems = orders.getOrderItems();
        }

        /*@Getter
        private class OrderItemDto {
            private

            public OrderItemDto(OrderItem item) {
            }
        }*/
    }
}
