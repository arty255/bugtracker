package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.model.AbstractEntity_;
import org.hetsold.bugtracker.model.SecurityUser;
import org.hetsold.bugtracker.model.SecurityUser_;
import org.hetsold.bugtracker.model.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.UUID;

@Repository
public class SecurityUserHibernateDAO implements SecurityUserDAO {
    private final HibernateTemplate hibernateTemplate;

    @Autowired
    public SecurityUserHibernateDAO(final SessionFactory sessionFactory) {
        hibernateTemplate = new HibernateTemplate(sessionFactory);
    }

    @Override
    public SecurityUser getSecUserByUsername(final String username) {
        return hibernateTemplate.execute(session -> {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<SecurityUser> query = builder.createQuery(SecurityUser.class);
            Root<SecurityUser> root = query.from(SecurityUser.class);
            query.where(builder.equal(root.get(SecurityUser_.username), username));
            query.select(root);
            return session.createQuery(query).setMaxResults(1).stream().findFirst().orElse(null);
        });
    }

    @Override
    public void save(final SecurityUser securityUser) {
        hibernateTemplate.saveOrUpdate(securityUser);
    }

    @Override
    public boolean isLoginTaken(final String login) {
        Boolean result = hibernateTemplate.execute(session -> {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<String> query = builder.createQuery(String.class);
            Root<SecurityUser> root = query.from(SecurityUser.class);
            query.where(builder.equal(root.get(SecurityUser_.username), login));
            query.select(root.get(SecurityUser_.username));
            return session.createQuery(query).setMaxResults(1).stream().findFirst().isPresent();
        });
        return result != null && result;
    }

    @Override
    public User getUserBySecurityUserLogin(final String login) {
        return hibernateTemplate.execute(session -> {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> query = builder.createQuery(User.class);
            Root<SecurityUser> root = query.from(SecurityUser.class);
            query.where(builder.equal(root.get(SecurityUser_.username), login));
            query.select(root.get(SecurityUser_.user));
            return session.createQuery(query).getSingleResult();
        });
    }

    @Override
    public SecurityUser getSecurityUserByUuid(final UUID uuid) {
        return hibernateTemplate.get(SecurityUser.class, uuid);
    }

    @Override
    public SecurityUser getSecurityUserByUserUuid(final UUID uuid) {
        return hibernateTemplate.execute(session -> {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<SecurityUser> query = builder.createQuery(SecurityUser.class);
            Root<SecurityUser> root = query.from(SecurityUser.class);
            Join<SecurityUser, User> join = root.join(SecurityUser_.user);
            query.where(builder.equal(join.get(AbstractEntity_.uuid), uuid));
            query.select(root);
            return session.createQuery(query).getSingleResult();
        });
    }

    @Override
    public void delete(final SecurityUser securityUser) {
        hibernateTemplate.delete(securityUser);
    }
}