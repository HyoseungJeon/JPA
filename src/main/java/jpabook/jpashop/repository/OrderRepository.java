package jpabook.jpashop.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.QMember;
import jpabook.jpashop.domain.QOrder;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    private final JPAQueryFactory queryFactory;

    private final QOrder qOrder = QOrder.order;

    private final QMember qMember = QMember.member;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    public List<Order> findAll(OrderSearch orderSearch) {
        // 동적 쿼리
        // 1. ~~ if 분기로 String += 하여 where문 완성
        String jpql1 = "select o from Order o join o.member m";
        if (orderSearch.getOrderStatus() != null) {
            jpql1 += "where = ~~ ";
        }

        // 2. ~~ query 관련 객체에 담고, 짜집고 해서 마지막에 createQuery에 삽입

        List<Order> orderList = queryFactory
                .select(qOrder)
                .from(qOrder)
                .join(qOrder.member, qMember)
                .fetchJoin()
                .where(memberNameEquals(orderSearch.getMemberName()),
                        orderStatusEquals(orderSearch.getOrderStatus()))
                .orderBy(qOrder.orderDate.desc())
                .fetch();

        // 정적 쿼리
        return orderList;
    }

    private BooleanExpression memberNameEquals(String memberName) {
        return memberName != null ? qOrder.member.name.contains(memberName) : null;
    }

    private BooleanExpression orderStatusEquals(OrderStatus orderStatus) {
        return orderStatus != null ? qOrder.status.eq(orderStatus) : null;
    }
}


