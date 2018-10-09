package Access;

import Model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

@Component
public class UserAccess implements IUserAccess {

    private final SessionFactory sessionFactory;

    @Autowired
    public UserAccess(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void test() {
    }

    /**
     * Fetches the user from the database with the given id.
     * If no user has the given id, null is returned
     *
     * @param id The id to use to find the user
     * @return The matching user if found, else null
     */
    @Override
    public User getUserByID(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(User.class, id);
        }
    }

    /**
     * Fetches the user from the database with the given email.
     * If no user has the given email, null is returned
     *
     * @param email The email to use to find the user
     * @return The matching user if found, else null
     */
    @Override
    public User getUserByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> cQuery = builder.createQuery(User.class);
            cQuery.where(builder.equal(cQuery.from(User.class).get("email"), email));
            List<User> users = session.createQuery(cQuery).list();
            if (users.size() == 0) return null;
            return users.get(0);
        }
    }

    /**
     * Inserts the user into the database.
     *
     * @param user The new user object
     */
    @Override
    public void saveUser(User user) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(user);
            tx.commit();
        }
    }

    /**
     * Updates the user in the database with the given id
     *
     * @param user The modified user object
     */
    @Override
    public void updateUser(User user) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.update(user);
            tx.commit();
        }
    }
}
