package jpabook.japshop.service;

import jpabook.japshop.controller.OrderSearch;
import jpabook.japshop.domain.Delivery;
import jpabook.japshop.domain.Member;
import jpabook.japshop.domain.OrderItem;
import jpabook.japshop.domain.Orders;
import jpabook.japshop.domain.item.Item;
import jpabook.japshop.repository.ItemRepository;
import jpabook.japshop.repository.MemberRepository;
import jpabook.japshop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 조회
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //주문 생성
        Orders orders = Orders.createOrders(member, delivery, orderItem);

        //주문 저장
        orderRepository.save(orders);

        return orders.getId();
    };


    /**
     * 주문 취소
     */

    @Transactional
    public void cancelOrder(Long order) {
        Orders orders = orderRepository.findOne(order);
        orders.cancel();
    }

    public List<Orders> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllByString(orderSearch);
    }

}
