/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.LendAndReturn;
import entity.Member;
import error.EntityManagerException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.application.ResourceHandler;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.shaded.commons.io.IOUtils;
import session.stateless.LendAndReturnSessionBeanLocal;
import session.stateless.MemberSessionBeanLocal;

/**
 *
 * @author jarrentoh
 */
@Named(value = "memberManagedBean")
@SessionScoped
public class MemberManagedBean implements Serializable {

    @EJB
    private LendAndReturnSessionBeanLocal lendAndReturnSessionBean;

    @EJB
    private MemberSessionBeanLocal memberSessionBean;

    private static final Logger LOGGER = Logger.getLogger(MemberManagedBean.class.getName());

    private String searchType = "IDENTITYNO";
    private String searchString;
    private List<Member> members;

    private UploadedFile file;

    private String firstName;
    private String lastName;
    private Character gender;
    private Integer age;
    private String identityNo;
    private String phone;
    private String address;
    private byte[] profileImage;

    //View Member Page
    private Long memberId;
    private Member selectedMember;
    private List<LendAndReturn> lendAndReturns;

    /**
     * Creates a new instance of MemberManagedBean
     */
    public MemberManagedBean() {
    }

    @PostConstruct
    public void init() {
        if (getSearchString() == null || getSearchString().equals("")) {

            setMembers(memberSessionBean.retrieveAllMembers());

        } else {
            switch (getSearchType()) {
                case "IDENTITYNO":
                    setMembers(memberSessionBean.searchMembersByIdentityNo(getSearchString()));
                    break;
                case "PHONE": {
                    setMembers(memberSessionBean.searchMembersByPhone(getSearchString()));
                    break;
                }
                case "FIRSTNAME": {
                    setMembers(memberSessionBean.searchMembersByFirstName(getSearchString()));
                    break;
                }

            }
        }
    }

    public void handleSearch() {
        init();
    } //end handleSearch

    public void loadSelectedMember() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            setSelectedMember(memberSessionBean.retrieveMemberByMemberId(memberId));
            setFirstName(selectedMember.getFirstName());
            setLastName(selectedMember.getLastName());
            setGender(selectedMember.getGender());
            setAge(selectedMember.getAge());
            setIdentityNo(selectedMember.getIdentityNo());
            setPhone(selectedMember.getPhone());
            setAddress(selectedMember.getAddress());
            setProfileImage(selectedMember.getProfileImage());

            List<LendAndReturn> lendAndReturnsOfBook = lendAndReturnSessionBean.searchLendAndReturnByMember(memberId);
            setLendAndReturns(lendAndReturnsOfBook);

        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to load Member"));
        }
    }

    public void upload() {
        if (file != null) {
            FacesMessage message = new FacesMessage("Successful", file.getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public String registerMember() {

//        System.out.println("manHELLO");
        FacesContext context = FacesContext.getCurrentInstance();
        Member newMember = new Member();
        newMember.setFirstName(firstName);
        newMember.setLastName(lastName);
        newMember.setGender(gender);
        newMember.setAge(age);
        newMember.setIdentityNo(identityNo);
        newMember.setPhone(phone);
        newMember.setAddress(address);

        if (file != null) {
            InputStream input;
            try {
                input = file.getInputStream();
                byte[] image = IOUtils.toByteArray(input);
                newMember.setProfileImage(image);
            } catch (IOException ex) {
                throw new FacesException("Error in writing captured image.", ex);
            }
        }

        try {
            memberSessionBean.createMember(newMember);
            setFirstName(null);
            setLastName(null);
            setGender(null);
            setAge(null);
            setIdentityNo(null);
            setPhone(null);
            setFile(null);
            setAddress(null);
            init();
            return "/authenticatedUser/memberSearch.xhtml?faces-redirect=true";

        } catch (EntityManagerException e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to register Member"));
            return "/authenticatedUser/memberRegistration.xhtml";
        }

    }

    public String redirectMemberRegistration() {

        setFirstName(null);
        setLastName(null);
        setGender(null);
        setAge(null);
        setIdentityNo(null);
        setPhone(null);
        setFile(null);
        setAddress(null);

        return "/authenticatedUser/memberRegistration.xhtml?faces-redirect=true";
    }

    public void handleFileUpload(FileUploadEvent event) {
        UploadedFile uploadedFile = event.getFile();
        setFile(uploadedFile);
    }

    /**
     * @return the searchType
     */
    public String getSearchType() {
        return searchType;
    }

    /**
     * @param searchType the searchType to set
     */
    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    /**
     * @return the searchString
     */
    public String getSearchString() {
        return searchString;
    }

    /**
     * @param searchString the searchString to set
     */
    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    /**
     * @return the members
     */
    public List<Member> getMembers() {
        return members;
    }

    /**
     * @param members the members to set
     */
    public void setMembers(List<Member> members) {
        this.members = members;
    }

//    /**
//     * @return the profileImage
//     */
//    /**
//     * @return the profileImage
//     */
//    public StreamedContent getProfileImage() {
//        System.out.println("getProfileImage() called");
//        if (profileImage != null) {
//            InputStream inputStream = new ByteArrayInputStream(profileImage);
//            return new DefaultStreamedContent(inputStream, "image/jpeg");
//        } else {
//            return null;
//        }
//    }
//
//    /**
//     * @param profileImage the profileImage to set
//     */
//    public void setProfileImage(byte[] profileImage) {
//        this.profileImage = profileImage;
//    }
    /**
     * @return the file
     */
    public UploadedFile getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(UploadedFile file) {
        this.file = file;
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
     * @return the gender
     */
    public Character getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(Character gender) {
        this.gender = gender;
    }

    /**
     * @return the age
     */
    public Integer getAge() {
        return age;
    }

    /**
     * @param age the age to set
     */
    public void setAge(Integer age) {
        this.age = age;
    }

    /**
     * @return the identityNo
     */
    public String getIdentityNo() {
        return identityNo;
    }

    /**
     * @param identityNo the identityNo to set
     */
    public void setIdentityNo(String identityNo) {
        this.identityNo = identityNo;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the memberId
     */
    public Long getMemberId() {
        return memberId;
    }

    /**
     * @param memberId the memberId to set
     */
    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    /**
     * @return the selectedMember
     */
    public Member getSelectedMember() {
        return selectedMember;
    }

    /**
     * @param selectedMember the selectedMember to set
     */
    public void setSelectedMember(Member selectedMember) {
        this.selectedMember = selectedMember;
    }

    /**
     * @return the lendAndReturns
     */
    public List<LendAndReturn> getLendAndReturns() {
        return lendAndReturns;
    }

    /**
     * @param lendAndReturns the lendAndReturns to set
     */
    public void setLendAndReturns(List<LendAndReturn> lendAndReturns) {
        this.lendAndReturns = lendAndReturns;
    }

    /**
     * @return the profileImage
     */
    public StreamedContent getProfileImage() throws FileNotFoundException {
        if (profileImage != null) {
            InputStream inputStream = new ByteArrayInputStream(profileImage);
            return new DefaultStreamedContent(inputStream, "image/jpeg");

        } else {

            String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/images/student.png");
            File file = new File(path);
            InputStream stream = new FileInputStream(file);
            String contentType = "image/png";
            StreamedContent streamedContent = new DefaultStreamedContent(stream, contentType);
            return streamedContent;

        }
    }

    /**
     * @param profileImage the profileImage to set
     */
    public void setProfileImage(byte[] profileImage) {
        this.profileImage = profileImage;
    }
}
