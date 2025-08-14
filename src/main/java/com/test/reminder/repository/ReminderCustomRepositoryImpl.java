package com.test.reminder.repository;

import com.test.reminder.domain.ReminderEntity;
import com.test.reminder.domain.UserEntity;
import com.test.reminder.dto.RemindersFilter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class ReminderCustomRepositoryImpl implements ReminderCustomRepository {
    private final EntityManager entityManager;

    @Override
    public Page<ReminderEntity> findByFilter(UserEntity user, Pageable pageable, RemindersFilter filter) {
        List<ReminderEntity> resultList = getReminderEntities(user, pageable, filter);
        Long total = getRemindersCount(user, filter);

        return new PageImpl<>(resultList, pageable, total);
    }

    private Long getRemindersCount(UserEntity user, RemindersFilter filter) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<ReminderEntity> root = criteriaQuery.from(ReminderEntity.class);
        Predicate[] countPredicates = getPredicates(user, filter, criteriaBuilder, root);
        criteriaQuery.select(criteriaBuilder.count(root));
        criteriaQuery.where(countPredicates);
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    private List<ReminderEntity> getReminderEntities(UserEntity user, Pageable pageable, RemindersFilter filter) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ReminderEntity> criteriaQuery = criteriaBuilder.createQuery(ReminderEntity.class);
        Root<ReminderEntity> root = criteriaQuery.from(ReminderEntity.class);

        Predicate[] predicates = getPredicates(user, filter, criteriaBuilder, root);

        criteriaQuery.where(predicates);

        if (pageable.getSort().isSorted()) {
            List<Order> orders = getOrders(pageable, root, criteriaBuilder);
            criteriaQuery.orderBy(orders);
        }

        TypedQuery<ReminderEntity> query = entityManager.createQuery(criteriaQuery);
        if (pageable.isPaged()) {
            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());
        }
        return query.getResultList();
    }

    private static Predicate[] getPredicates(UserEntity user, RemindersFilter filter, CriteriaBuilder cb, Root<ReminderEntity> root) {
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(root.get("user"), user));

        if (filter != null && filter.getFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("remind"), filter.getFrom()));
        }

        if (filter != null && filter.getTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("remind"), filter.getTo()));
        }

        if (filter != null && filter.getTitle() != null && !filter.getTitle().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("title")), "%" + filter.getTitle().toLowerCase() + "%"));
        }

        if (filter != null && filter.getDescription() != null && !filter.getDescription().isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("description")), "%" + filter.getDescription().toLowerCase() + "%"));
        }
        return predicates.toArray(new Predicate[0]);
    }

    private static List<Order> getOrders(Pageable pageable, Root<ReminderEntity> root, CriteriaBuilder cb) {
        List<Order> orders = new ArrayList<>();
        for (Sort.Order order : pageable.getSort()) {
            Path<Object> path = root.get(order.getProperty());
            orders.add(order.isAscending() ? cb.asc(path) : cb.desc(path));
        }
        return orders;
    }
}
