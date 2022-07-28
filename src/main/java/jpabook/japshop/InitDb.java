package jpabook.japshop;

import jpabook.japshop.domain.*;
import jpabook.japshop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;
        public void dbInit1() {
            Member member = createMember("userA", "서울", "1","1111");
            em.persist(member);

            Book book1 = createBook("JPA1 BOOK", 10000, 100);
            em.persist(book1);

            Book book2 = createBook("JPA2 BOOK", 10000, 100);
            em.persist(book2);

            OrderItem orderItem1= OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 10000, 1);

            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());

            Orders orders = Orders.createOrders(member, delivery, orderItem1, orderItem2);
            em.persist(orders);
        }

        private static Member createMember(String name, String city, String street, String zipcode) {
            Member member = new Member();
            member.setName(name);
            member.setAddress(new Address(city, street, zipcode));
            return member;
        }

        public void dbInit2() {
            Member member = createMember("userB", "경기", "2","2222");
            em.persist(member);

            Book book1 = createBook("SPRING1 BOOK", 20000, 200);
            em.persist(book1);

            Book book2 = createBook("SPRING2 BOOK", 40000, 300);
            em.persist(book2);

            OrderItem orderItem1= OrderItem.createOrderItem(book1, 20000, 2);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 40000, 3);

            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());

            Orders orders = Orders.createOrders(member, delivery, orderItem1, orderItem2);
            em.persist(orders);
        }

        private static Book createBook(String name, int price, int stockQuantity) {
            Book book1 = new Book();
            book1.setName(name);
            book1.setPrice(price);
            book1.setStockQuantity(stockQuantity);
            return book1;
        }
    }

}
