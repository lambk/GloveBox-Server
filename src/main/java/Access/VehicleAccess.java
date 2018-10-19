package Access;

import Model.User;
import Model.Vehicle;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.HashSet;
import java.util.Set;

@Component
public class VehicleAccess implements IVehicleAccess {

    private final SessionFactory sessionFactory;

    @Autowired
    public VehicleAccess(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Fetches a vehicle record from the database that has the given plate and owner id.
     * Returns a new Vehicle object if a record is found, else returns null
     *
     * @param plate   The plate to find records using
     * @param userID The id of the owner
     * @return The matching vehicle (if found), else null
     */
    @Override
    public Vehicle getVehicle(String plate, int userID) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Vehicle> cQuery = builder.createQuery(Vehicle.class);
            Root<Vehicle> vehicles = cQuery.from(Vehicle.class);
            cQuery.select(vehicles)
                    .where(builder.equal(vehicles.get("plate"), plate),
                            builder.equal(vehicles.get("owner"), userID));
            return session.createQuery(cQuery).uniqueResult();
        }
    }

    /**
     * Fetches a set of vehicles that are linked the user with the given id
     *
     * @param userID The id of owner
     * @return The set of vehicles
     */
    @Override
    public Set<Vehicle> getVehiclesByID(int userID) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Vehicle> cQuery = builder.createQuery(Vehicle.class);
            Root<Vehicle> vehicles = cQuery.from(Vehicle.class);
            cQuery.select(vehicles)
                    .where(builder.equal(vehicles.get("owner"), userID));
            return new HashSet<>(session.createQuery(cQuery).list());
        }
    }

    /**
     * Inserts the given vehicle into the Vehicle table
     *
     * @param vehicle The new vehicle to add
     * @param userID The owners id
     */
    @Override
    public void insertVehicle(Vehicle vehicle, int userID) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            User user = session.load(User.class, userID);
            vehicle.setOwner(user);
            session.save(vehicle);
            tx.commit();
        }
    }

    @Override
    public void updateVehicle(Vehicle vehicle) {

    }

    @Override
    public void deleteVehicle(String plate) {

    }
}
