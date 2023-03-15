/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.Member;
import error.EntityManagerException;
import error.MemberNotFoundException;
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
public class MemberSessionBean implements MemberSessionBeanLocal {
    
    @PersistenceContext
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Member createMember(Member newMember) throws EntityManagerException {
        try {
            
            em.persist(newMember);
            em.flush();
            return newMember;
            
        } catch (Exception e) {
            
            throw new EntityManagerException("Cannot persist Member into database.");
        }
        
    }
    
    @Override
    public List<Member> retrieveAllMembers() {
        Query query = em.createQuery("SELECT m FROM Member m");
        return query.getResultList();
    }
    
    @Override
    public Member retrieveMemberByMemberId(Long id) throws MemberNotFoundException {
        try {
            
            return em.find(Member.class, id);
            
        } catch (NoResultException | NonUniqueResultException ex) {
            
            throw new MemberNotFoundException("Member Identity Number does Not exist!");
        }
    }
    
    @Override
    public Member retrieveMemberByIdentityNo(String idNo) throws MemberNotFoundException {
        
        Query query = em.createQuery("SELECT m FROM Member m WHERE LOWER(m.identityNo) =:idNo");
        query.setParameter("idNo", idNo.toLowerCase());
        
        try {
            
            return (Member) query.getSingleResult();
            
        } catch (NoResultException | NonUniqueResultException ex) {
            
            throw new MemberNotFoundException("Member Identity Number does Not exist!");
        }
        
    }
    
    @Override
    public List<Member> searchMembersByIdentityNo(String idNo) {
        
        Query q;
        
        if (idNo != null) {
            
            q = em.createQuery("SELECT m FROM Member m WHERE LOWER(m.identityNo) LIKE :idNo");
            q.setParameter("idNo", "%" + idNo.toLowerCase() + "%");
            return q.getResultList();
            
        }
        
        return retrieveAllMembers();
    }
    
    @Override
    public List<Member> searchMembersByFirstName(String firstName) {
        
        Query q;
        
        if (firstName != null) {
            
            q = em.createQuery("SELECT m FROM Member m WHERE LOWER(m.firstName) LIKE :firstName");
            q.setParameter("firstName", "%" + firstName.toLowerCase() + "%");
            return q.getResultList();
            
        }
        
        return retrieveAllMembers();
        
    }
    
    @Override
    public List<Member> searchMembersByPhone(String phoneNo) {
        
        Query q;
        
        if (phoneNo != null) {
            q = em.createQuery("SELECT m FROM Member m WHERE LOWER(m.phone) LIKE :phoneNo");
            q.setParameter("phoneNo", "%" + phoneNo.toLowerCase() + "%");
            return q.getResultList();
        }
        
        return retrieveAllMembers();
    }
    
    @Override
    public void updateMember(Member updatedMember) throws MemberNotFoundException {
        
        try {
            
            Member memberToBeUpdated = retrieveMemberByIdentityNo(updatedMember.getIdentityNo());
            memberToBeUpdated.setFirstName(updatedMember.getFirstName());
            memberToBeUpdated.setLastName(updatedMember.getLastName());
            memberToBeUpdated.setGender(updatedMember.getGender());
            memberToBeUpdated.setPhone(updatedMember.getPhone());
            memberToBeUpdated.setAddress(updatedMember.getAddress());
            memberToBeUpdated.setAge(updatedMember.getAge());
            
            em.flush();
            
        } catch (MemberNotFoundException e) {
            
            throw e;
        }
        
    }
    
}
