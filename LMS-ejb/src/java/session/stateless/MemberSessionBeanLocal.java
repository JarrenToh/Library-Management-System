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
import javax.ejb.Local;

/**
 *
 * @author jarrentoh
 */
@Local
public interface MemberSessionBeanLocal {

    Member createMember(Member newMember) throws EntityManagerException;

    List<Member> retrieveAllMembers();

    Member retrieveMemberByIdentityNo(String idNo) throws MemberNotFoundException;

    List<Member> searchMembersByIdentityNo(String idNo);

    List<Member> searchMembersByFirstName(String firstName);

    void updateMember(Member updatedMember) throws MemberNotFoundException;

    List<Member> searchMembersByPhone(String phoneNo);

    Member retrieveMemberByMemberId(Long id) throws MemberNotFoundException;
    
}
