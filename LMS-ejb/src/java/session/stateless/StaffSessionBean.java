/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.Staff;
import error.EntityManagerException;
import error.InvalidLoginException;
import error.StaffNotFoundException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author jarrentoh
 */
@Stateless
public class StaffSessionBean implements StaffSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public List<Staff> retrieveAllStaffs() {
        Query query = em.createQuery("SELECT s FROM Staff s");

        return query.getResultList();
    }

    @Override
    public Staff retrieveStaffByUsername(String userName) throws StaffNotFoundException {
        Query query = em.createQuery("SELECT s FROM Staff s WHERE LOWER(s.userName) = :inUsername");
        query.setParameter("inUsername", userName.toLowerCase());

        try {

            return (Staff) query.getSingleResult();

        } catch (NoResultException | NonUniqueResultException ex) {

            throw new StaffNotFoundException("Staff Username " + userName + " does not exist!");

        }

    }

    @Override
    public Staff staffLogin(String userName, String password) throws InvalidLoginException {
        try {

            Staff staff = retrieveStaffByUsername(userName);

            if (staff.getPassword().equals(password)) {

                return staff;

            } else {

                throw new InvalidLoginException("Username does not exist or invalid password!");

            }

        } catch (StaffNotFoundException ex) {

            throw new InvalidLoginException("Username does not exist or invalid password!");
        }
    }

    @Override
    public void updateStaff(Staff updatedStaff) throws StaffNotFoundException {

        try {

            Staff staffToBeUpdated = retrieveStaffByUsername(updatedStaff.getUserName());
            staffToBeUpdated.setFirstName(updatedStaff.getFirstName());
            staffToBeUpdated.setLastName(updatedStaff.getLastName());
            staffToBeUpdated.setPassword(updatedStaff.getPassword());

        } catch (StaffNotFoundException e) {

            throw e;
        }
    }

    @Override
    public void createStaff(Staff newStaff) throws EntityManagerException{

        try {

            em.persist(newStaff);
            em.flush();

        } catch (Exception e) {

            throw new EntityManagerException("Cannot persist Book into database.");

        }
    }

}
