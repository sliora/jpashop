package jpabook.japshop.service;

import jpabook.japshop.domain.Address;
import jpabook.japshop.domain.Member;
import jpabook.japshop.domain.OrderStatus;
import jpabook.japshop.domain.Orders;
import jpabook.japshop.domain.item.Book;
import jpabook.japshop.repository.OrderRepository;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;


    @Test
    public void 상품주문() throws Exception {
        //given
        Member member = new Member();
        member.setName("테스트");
        member.setAddress(new Address("서울시", "용산구", "12345"));
        em.persist(member);

        Book book = new Book();
        book.setName("라틴어수업");
        book.setPrice(10000);
        book.setStockQuantity(10);
        em.persist(book);


        //when
        Long order = orderService.order(member.getId(), book.getId(), 2);

        //then
        Orders getOrder = orderRepository.findOne(order);
        assertEquals(OrderStatus.ORDER, getOrder.getStatus(), "상품 주문시 상태는 ORDER");
        assertEquals(1, getOrder.getOrderItems().size(), "주문한 상품 종류 수가 정확해야 한다.");
        assertEquals(book.getPrice() * 2, getOrder.getTotalPrice(), "주문 가격은 가격 * 수량이다.");
        assertEquals(8, book.getStockQuantity(), "주문 수량만큼 재고가 줄어야 한다.");


    }

}