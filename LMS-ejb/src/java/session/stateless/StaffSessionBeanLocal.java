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
import javax.ejb.Local;

/**
 *
 * @author jarrentoh
 */
@Local
public interface StaffSessionBeanLocal {

    List<Staff> retrieveAllStaffs();

    Staff staffLogin(String userName, String password) throws InvalidLoginException;

    Staff retrieveStaffByUsername(String userName) throws StaffNotFoundException;

    void updateStaff(Staff updatedStaff) throws StaffNotFoundException;

    void createStaff(Staff newStaff) throws EntityManagerException;
    
}
