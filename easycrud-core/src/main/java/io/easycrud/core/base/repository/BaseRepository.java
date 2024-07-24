package io.easycrud.core.base.repository;

import io.easycrud.core.base.annotation.UpdatedBy;
import io.easycrud.core.base.entity.BaseDO;
import io.easycrud.core.base.exception.BaseException;
import io.easycrud.core.base.exception.ExceptionEnum;
import io.easycrud.core.config.header.CommonHeaderManager;
import io.easycrud.core.config.header.CommonHeaders;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Slf4j
@NoRepositoryBean
@Transactional(readOnly = true)
public abstract class BaseRepository<DO extends BaseDO> {

    protected final static String ID = "id";
    protected final static String DELETED = "deleted";
    protected final static String CREATED_BY = "createdBy";
    protected final static boolean DELETED_FLAG = true;
    protected final static boolean NOT_DELETED_FLAG = false;

    @Autowired
    protected EntityManager entityManager;

    protected abstract Class<DO> getDomainClass();

    @Transactional
    public DO create(DO item) {
        item.setId(null);
        entityManager.persist(item);
        entityManager.flush();
        return item;
    }

    public Optional<DO> find(String id) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<DO> query = cb.createQuery(getDomainClass());
        Root<DO> root = query.from(getDomainClass());

        query
                .select(root)
                .where(cb.and(cb.equal(root.get(ID), id),
                        cb.equal(root.get(DELETED), NOT_DELETED_FLAG)));

        return entityManager.createQuery(query).getResultStream().findFirst();
    }

    public Optional<DO> find(String id, String userId) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<DO> query = cb.createQuery(getDomainClass());
        Root<DO> root = query.from(getDomainClass());

        query
                .select(root)
                .where(cb.and(cb.equal(root.get(ID), id),
                        cb.equal(root.get(CREATED_BY), userId),
                        cb.equal(root.get(DELETED), NOT_DELETED_FLAG)
                ));

        return entityManager.createQuery(query).getResultStream().findFirst();
    }

    public List<DO> findAll(String userId) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<DO> query = cb.createQuery(getDomainClass());
        Root<DO> root = query.from(getDomainClass());

        query
                .select(root)
                .where(cb.and(cb.equal(root.get(CREATED_BY), userId),
                        cb.equal(root.get(DELETED), NOT_DELETED_FLAG)
                ));

        return entityManager.createQuery(query).getResultStream().collect(Collectors.toList());
    }

    public Page<DO> findAll(Pageable pageable) {
        BiFunction<CriteriaBuilder, Root<DO>, Predicate> condition = (cb, root)
                -> cb.and(cb.equal(root.get(DELETED), NOT_DELETED_FLAG));
        return innerFindAll(pageable, condition);
    }

    public Page<DO> findAll(Pageable pageable, String userId) {
        BiFunction<CriteriaBuilder, Root<DO>, Predicate> condition = (cb, root)
                -> cb.and(cb.equal(root.get(DELETED), NOT_DELETED_FLAG), cb.equal(root.get(CREATED_BY), userId));
        return innerFindAll(pageable, condition);
    }

    private Page<DO> innerFindAll(Pageable pageable, BiFunction<CriteriaBuilder, Root<DO>, Predicate> condition) {
        // SELECT LIST //
        // basic objects
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<DO> query = cb.createQuery(getDomainClass());
        Root<DO> root = query.from(getDomainClass());

        // build sql
        query.select(root).where(condition.apply(cb, root));
        if (pageable.getSort().isSorted()) {
            List<Order> orders = new ArrayList<>();
            for (Sort.Order order : pageable.getSort()) {
                Order jpaOrder = order.isAscending() ? cb.asc(root.get(order.getProperty())) : cb.desc(root.get(order.getProperty()));
                orders.add(jpaOrder);
            }
            query.orderBy(orders);
        }

        // execute sql
        List<DO> resultList = entityManager.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        // SELECT COUNT //
        // basic objects
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<DO> countRoot = countQuery.from(getDomainClass());

        // build sql
        countQuery.select(cb.count(countRoot));
        query.select(root).where(condition.apply(cb, countRoot));

        // execute sql
        Long total = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(resultList, pageable, total);
    }

    @Transactional
    public void updateSelective(DO entity) {
        update(entity, true);
    }

    @Transactional
    public void update(DO entity) {
        update(entity, false);
    }

    private void update(DO entity, boolean selective) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaUpdate<DO> update = cb.createCriteriaUpdate(getDomainClass());
        Root<DO> root = update.from(getDomainClass());

        updateAudit(update, root);
        Field[] declaredFields = getDomainClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            Object value;
            try {
                value = field.get(entity);
            } catch (IllegalAccessException e) {
                throw BaseException.builder()
                        .exceptionEnum(ExceptionEnum.UPDATE_FAILED)
                        .build();
            }
            if (selective && value == null) {
                continue;
            }
            update.set(root.get(field.getName()), value);
        }
        update
                .where(cb.and(cb.equal(root.get(ID), entity.getId()),
                        cb.equal(root.get(DELETED), NOT_DELETED_FLAG)));

        int i = entityManager.createQuery(update).executeUpdate();
        assertUpdated(i);
    }

    @Transactional
    public void delete(@NotBlank String id) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaUpdate<DO> update = cb.createCriteriaUpdate(getDomainClass());
        Root<DO> root = update.from(getDomainClass());

        updateAudit(update, root);
        update
                .set(root.get(DELETED), DELETED_FLAG)
                .where(cb.and(cb.equal(root.get(ID), id),
                        cb.equal(root.get(DELETED), NOT_DELETED_FLAG)
                ));

        int i = entityManager.createQuery(update).executeUpdate();
        assertUpdated(i);
    }

    @Transactional
    public void delete(String id, String userId) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaUpdate<DO> update = cb.createCriteriaUpdate(getDomainClass());
        Root<DO> root = update.from(getDomainClass());

        updateAudit(update, root);
        update
                .set(root.get(DELETED), DELETED_FLAG)
                .where(cb.and(cb.equal(root.get(ID), id),
                        cb.equal(root.get(CREATED_BY), userId),
                        cb.equal(root.get(DELETED), NOT_DELETED_FLAG)
                ));

        int i = entityManager.createQuery(update).executeUpdate();
        assertUpdated(i);
    }

    // TODO big set partial
    @Transactional
    public void delete(Set<String> ids, String userId) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaUpdate<DO> update = cb.createCriteriaUpdate(getDomainClass());
        Root<DO> root = update.from(getDomainClass());

        updateAudit(update, root);
        update
                .set(root.get(DELETED), DELETED_FLAG)
                .where(cb.and(root.get(ID).in(ids),
                        cb.equal(root.get(CREATED_BY), userId),
                        cb.equal(root.get(DELETED), NOT_DELETED_FLAG)
                ));

        entityManager.createQuery(update).executeUpdate();
    }

    private List<Field> getAllFields(Class<?> type) {
        List<Field> fields = new ArrayList<>();
        while (type != null) {
            Field[] declaredFields = type.getDeclaredFields();
            fields.addAll(Arrays.asList(declaredFields));
            type = type.getSuperclass();
        }
        return fields;
    }

    private void updateAudit(CriteriaUpdate<DO> update, Root<DO> root) {
        Class<? super DO> superclass = getDomainClass().getSuperclass();
        List<Field> allSuperFields = getAllFields(superclass);
        for (Field field: allSuperFields) {
            if (field.isAnnotationPresent(UpdateTimestamp.class)) {
                update.set(root.get(field.getName()), LocalDateTime.now());
            } else if(field.isAnnotationPresent(UpdatedBy.class)) {
                Optional.ofNullable(CommonHeaderManager.getCommonHeaders())
                        .map(CommonHeaders::getUserId)
                        .ifPresent(userId -> update.set(root.get(field.getName()), userId));
            }
        }
    }

    private static void assertUpdated(int i) {
        if (i == 0) {
            throw BaseException.builder()
                    .exceptionEnum(ExceptionEnum.NOT_FOUND_EXCEPTION)
                    .build();
        }
    }

}
