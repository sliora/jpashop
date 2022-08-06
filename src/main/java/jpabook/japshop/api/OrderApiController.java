package jpabook.japshop.api;

import jpabook.japshop.controller.OrderSearch;
import jpabook.japshop.domain.Address;
import jpabook.japshop.domain.OrderItem;
import jpabook.japshop.domain.OrderStatus;
import jpabook.japshop.domain.Orders;
import jpabook.japshop.repository.OrderRepository;
import jpabook.japshop.repository.order.query.OrderQueryDto;
import jpabook.japshop.repository.order.query.OrderQueryRepository;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.Order;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

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

    @GetMapping("/api/v3/orders")
    public List<OrdersDto> ordersV3() {
        List<Orders> getOrders = orderRepository.findAllWithItem();
        List<OrdersDto> collect = getOrders.stream().map(o -> new OrdersDto(o)).collect(Collectors.toList());
        return collect;
    }

    @GetMapping("/api/v3.1/orders")
    public List<OrdersDto> ordersV3_page(
            @RequestParam(value = "offset") int offset,
            @RequestParam(value = "limit") int limit) {
        List<Orders> getOrders = orderRepository.findAllWithMemberDelivery(offset, limit);
        List<OrdersDto> collect = getOrders.stream().map(o -> new OrdersDto(o)).collect(Collectors.toList());
        return collect;
    }

    @GetMapping("api/v4/orders")
    public List<OrderQueryDto> ordersV4() {
        return orderQueryRepository.findOrderQueryDtos();
    }

    @Data
    static class OrdersDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;

        public OrdersDto(Orders orders) {
            this.orderId = orders.getId();
            this.name = orders.getMember().getName();
            this.orderDate = orders.getOrderDate();
            this.orderStatus = orders.getStatus();
            this.address = orders.getMember().getAddress();
            this.orderItems = orders.getOrderItems().stream().map(o -> new OrderItemDto(o)).collect(Collectors.toList());
            //this.orderItems = orders.getOrderItems();
        }

        @Getter
        private class OrderItemDto {

            private String itemName;
            private int orderPrice;
            private int count;

            public OrderItemDto(OrderItem orderItem) {
                this.itemName = orderItem.getItem().getName();
                this.orderPrice = orderItem.getOrderPrice();
                this.count = orderItem.getCount();

            }
        }
    }
}
