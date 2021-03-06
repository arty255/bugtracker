package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.dao.util.Contract;
import org.hetsold.bugtracker.model.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.UUID;

@Repository
public class UserHibernateDAO implements UserDAO {
    private final HibernateTemplate hibernateTemplate;

    @Autowired
    public UserHibernateDAO(final SessionFactory sessionFactory) {
        hibernateTemplate = new HibernateTemplate(sessionFactory);
    }

    @Override
    public void save(final User user) {
        hibernateTemplate.saveOrUpdate(user);
    }

    @Override
    public void delete(final User user) {
        hibernateTemplate.delete(user);
    }

    @Override
    public User getUserByUUID(final UUID uuid) {
        return hibernateTemplate.get(User.class, uuid);
    }

    @Override
    public long getUsersCount(final Contract contract) {
        Long count = hibernateTemplate.execute(session -> {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Long> query = builder.createQuery(Long.class);
            Root<User> root = query.from(User.class);
            query.where(ContractReader.readContract(contract, root, builder));
            query.select(builder.count(root));
            return session.createQuery(query).getSingleResult();
        });
        return count != null ? count : 0;
    }

    @Override
    public List<User> getUsers(final Contract contract, final int first, final int limit) {
        return hibernateTemplate.execute(session -> {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> query = builder.createQuery(User.class);
            Root<User> root = query.from(User.class);
            query.where(ContractReader.readContract(contract, root, builder));
            query.orderBy(ContractReader.getOrders(contract, root, builder));
            query.select(root);
            return session.createQuery(query).setFirstResult(first).setMaxResults(limit).getResultList();
        });
    }
}