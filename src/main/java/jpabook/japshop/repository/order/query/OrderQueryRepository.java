package jpabook.japshop.repository.order.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//화면 API와 핵심비즈니스 로직 API의 생명 주기가 다르므로
//관심사를 분리하면 좋다.

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;

    public List<OrderQueryDto> findOrderQueryDtos() {
        List<OrderQueryDto> result = findOrders();

        result.forEach(o -> {
           List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
           o.setOrderItems(orderItems);
        });
        return result;
    }

    public List<OrderQueryDto> findAllByDto_optimization() {
        List<OrderQueryDto> result = findOrders();

        List<Long> collect = result.stream().map(o -> o.getOrderId()).collect(Collectors.toList());

        List<OrderItemQueryDto> orderItems = em.createQuery("select new jpabook.japshop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) from OrderItem oi" +
                        " join oi.item i" +
                        " where oi.order.id in :orderIds", OrderItemQueryDto.class
                ).setParameter("orderIds", collect)
                .getResultList();

        //단순하게 이렇게 할 경우 orders 와 orderItems 의 Id값 매핑이 안된다.
        //result.forEach(o -> o.setOrderItems(orderItems));  // 단순하게 이렇게 할 경우

        //orders 와 orderItems 를 매핑시켜서 set 시키는 람다식
        Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream().collect(Collectors.groupingBy(orderItemQueryDto -> orderItemQueryDto.getOrderId()));
        result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));

        return result;
    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery("select new jpabook.japshop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) from OrderItem oi" +
                " join oi.item i" +
                " where oi.order.id = :orderId", OrderItemQueryDto.class
                ).setParameter("orderId", orderId)
                .getResultList();
    }

    private List<OrderQueryDto> findOrders() {
        return em.createQuery(
                "select new jpabook.japshop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                        " from Orders o" +
                        " join o.member m" +
                        " join o.delivery d", OrderQueryDto.class
        ).getResultList();
    }

    public List<OrderFlatDto> findAllByDto_flat() {
        return em.createQuery(
                "select new jpabook.japshop.repository.order.query.OrderFlatDto(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count)" +
                        " from Orders o" +
                        " join o.member m" +
                        " join o.delivery d " +
                        " join o.orderItems oi " +
                        " join oi.item i", OrderFlatDto.class
        ).getResultList();
    }
}
