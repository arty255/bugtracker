package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.dao.util.Contract;
import org.hetsold.bugtracker.model.SecurityUser;
import org.hetsold.bugtracker.model.SecurityUser_;
import org.hetsold.bugtracker.model.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class UserHibernateDAO implements UserDAO {
    private HibernateTemplate hibernateTemplate;

    @Autowired
    public UserHibernateDAO(SessionFactory sessionFactory) {
        hibernateTemplate = new HibernateTemplate(sessionFactory);
    }

    @Override
    public void save(User user) {
        hibernateTemplate.saveOrUpdate(user);
    }

    @Override
    public void delete(User user) {
        hibernateTemplate.delete(user);
    }

    @Override
    public User getUserById(String uuid) {
        return hibernateTemplate.get(User.class, uuid);
    }

    @Override
    public long getUsersCount(Contract contract) {
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
    public List<User> getUsers(Contract contract, int first, int limit) {
        return hibernateTemplate.execute(session -> {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> query = builder.createQuery(User.class);
            Root<User> root = query.from(User.class);
            query.where(ContractReader.readContract(contract, root, builder));
            query.select(root);
            return session.createQuery(query).setFirstResult(first).setMaxResults(limit).getResultList();
        });
    }

    public UserDetails getSecUserByUsername(String username) {
        return null;
    }

    @Override
    public void save(SecurityUser securityUser) {
        hibernateTemplate.saveOrUpdate(securityUser);
    }

    @Override
    public boolean isLoginTaken(String login) {
        Boolean result = hibernateTemplate.execute(session -> {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<String> query = builder.createQuery(String.class);
            Root<SecurityUser> root = query.from(SecurityUser.class);
            query.where(builder.equal(root.get(SecurityUser_.username), login));
            query.select(root.get(SecurityUser_.username));
            return session.createQuery(query).setMaxResults(1).stream().findFirst().isPresent();
        });
        return result != null ? result : false;
    }
}