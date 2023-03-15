/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.Book;
import entity.LendAndReturn;
import entity.Member;
import error.EntityManagerException;
import error.FineNotPaidException;
import error.LendingNotFoundException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author jarrentoh
 */
@Local
public interface LendAndReturnSessionBeanLocal {

    void createLendAndReturn(LendAndReturn newLendAndReturn) throws EntityManagerException;

    List<LendAndReturn> searchLendAndReturnByBook(Long bookId);

    List<LendAndReturn> searchLendAndReturnByMember(Long memberId);

    LendAndReturn retrieveLendAndReturnById(Long lendId) throws LendingNotFoundException;

    void payFine(BigDecimal payment, Long lendId) throws FineNotPaidException;

    void returnBook(Long lendId, Date returnDate) throws FineNotPaidException;
    
}
