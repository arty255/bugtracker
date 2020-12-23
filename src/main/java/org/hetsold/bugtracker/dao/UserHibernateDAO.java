package org.hetsold.bugtracker.dao;

import org.hetsold.bugtracker.model.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
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
        hibernateTemplate.save(user);
    }

    @Override
    public List<User> listAll() {
        return hibernateTemplate.loadAll(User.class);
    }

    @Override
    public void delete(User user) {
        hibernateTemplate.delete(user);
    }

    @Override
    public User getUserById(String uuid) {
        return hibernateTemplate.load(User.class, uuid);
    }

    @Override
    public long getUsersCount() {
        Long count = hibernateTemplate.execute(session -> {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Long> query = builder.createQuery(Long.class);
            Root<User> root = query.from(User.class);
            query.select(builder.count(root));
            return session.createQuery(query).getSingleResult();
        });
        if (count != null) {
            return count;
        }
        return 0;
    }
}
