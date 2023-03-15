/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.Staff;
import error.InvalidLoginException;
import error.StaffNotFoundException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import session.stateless.StaffSessionBeanLocal;

/**
 *
 * @author jarrentoh
 */
@Named(value = "authenticationManagedBean")
@SessionScoped
public class AuthenticationManagedBean implements Serializable {

    @EJB
    private StaffSessionBeanLocal staffSessionBean;

    /**
     * Creates a new instance of AuthenticationManagedBean
     */
    private long staffId = -1;
    private String username = null;
    private String password = null;
    private String firstName = null;
    private String lastName = null;
    private byte[] profileImage;

    private Staff staff;
    private static final Logger LOGGER = Logger.getLogger(AuthenticationManagedBean.class.getName());

    public AuthenticationManagedBean() {
    }

    public String login() {

        try {
            staff = staffSessionBean.staffLogin(username.trim(), password.trim());
            setStaffId(staff.getStaffId());
            setFirstName(staff.getFirstName());
            setLastName(staff.getLastName());
            setProfileImage(staff.getProfileImage());
            setPassword(staff.getPassword());
            LOGGER.info("login succeed");
            return "/authenticatedUser/bookSearch.xhtml?faces-redirect=true";

        } catch (InvalidLoginException e) {

            FacesContext.getCurrentInstance().addMessage("Login Error: ", new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
            //login failed
            username = null;
            password = null;
            setFirstName(null);
            setLastName(null);
            setProfileImage(null);
            staffId = -1;
            LOGGER.info("fail to login");
            return "login.xhtml";
        }
    } //end login

    public String logout() {
        username = null;
        password = null;
        setFirstName(null);
        setLastName(null);
        setProfileImage(null);
        LOGGER.info("logout");
        staffId = -1;
        return "/login.xhtml?faces-redirect=true";
    } //end logout
    
    public String save() {
    
        try {
            Staff updateStaff = staffSessionBean.retrieveStaffByUsername(username);
            updateStaff.setFirstName(firstName);
            updateStaff.setLastName(lastName);
            updateStaff.setPassword(password);
            staffSessionBean.updateStaff(updateStaff);
            FacesContext.getCurrentInstance().addMessage("Successful Update!", new FacesMessage(FacesMessage.SEVERITY_INFO, "User info updated", null));
            
        } catch (StaffNotFoundException ex) {
            
            FacesContext.getCurrentInstance().addMessage("User Error: ", new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
        }
        
        return "/authenticatedUser/profilePage";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the profileImage
     */
    public StreamedContent getProfileImage() {
        if (profileImage != null) {
            InputStream inputStream = new ByteArrayInputStream(profileImage);
            return new DefaultStreamedContent(inputStream, "image/jpeg");
        } else {
            return null;
        }
    }

    /**
     * @param profileImage the profileImage to set
     */
    public void setProfileImage(byte[] profileImage) {
        this.profileImage = profileImage;
    }
}
