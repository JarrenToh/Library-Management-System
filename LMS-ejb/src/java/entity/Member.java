/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author jarrentoh
 */
@Entity
public class Member implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false)
    @NotNull
    private String firstName;

    @Column(nullable = false)
    @NotNull
    private String lastName;

    @Column(nullable = false)
    @NotNull
    private Character gender;

    @Column(nullable = false)
    @NotNull
    @Min(1)
    @Max(130)
    private Integer age;

    @Column(nullable = false, unique = true)
    @NotNull
    private String identityNo;

    @Column
    @Size(min = 1)
    private String phone;

    @Column
    private String address;

    @Lob
    private byte[] profileImage;

    @OneToMany(mappedBy = "member", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private ArrayList<LendAndReturn> lending;

    public Member(String firstName, String lastName, Character gender, Integer age, String idenntityNo, String phone, String address, byte[] profileImage) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.age = age;
        this.identityNo = idenntityNo;
        this.phone = phone;
        this.address = address;
        this.profileImage = profileImage;
        this.lending = new ArrayList<>();
    }

    public Member() {
        this.lending = new ArrayList<>();
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (memberId != null ? memberId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the memberId fields are not set
        if (!(object instanceof Member)) {
            return false;
        }
        Member other = (Member) object;
        if ((this.memberId == null && other.memberId != null) || (this.memberId != null && !this.memberId.equals(other.memberId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Member[ id=" + memberId + " ]";
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
     * @return the lending
     */
    public ArrayList<LendAndReturn> getLending() {
        return lending;
    }

    /**
     * @param lending the lending to set
     */
    public void setLending(ArrayList<LendAndReturn> lending) {
        this.lending = lending;
    }

    /**
     * @return the profileImage
     */
    public byte[] getProfileImage() {
        return profileImage;
    }

    /**
     * @param profileImage the profileImage to set
     */
    public void setProfileImage(byte[] profileImage) {
        this.profileImage = profileImage;
    }


}
