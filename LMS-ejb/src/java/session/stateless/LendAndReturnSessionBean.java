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
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author jarrentoh
 */
@Stateless
public class LendAndReturnSessionBean implements LendAndReturnSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void createLendAndReturn(LendAndReturn newLendAndReturn) throws EntityManagerException {

        Book book = em.find(Book.class, newLendAndReturn.getBook().getBookId());
        Member member = em.find(Member.class, newLendAndReturn.getMember().getMemberId());

        try {

            em.persist(newLendAndReturn);
            book.getLending().add(newLendAndReturn);
            member.getLending().add(newLendAndReturn);

        } catch (Exception e) {

            throw new EntityManagerException("Cannot persist Lend And Return into database.");
        }

    }

    @Override
    public LendAndReturn retrieveLendAndReturnById(Long lendId) throws LendingNotFoundException {

        LendAndReturn lendAndReturn = em.find(LendAndReturn.class, lendId);

        if (lendAndReturn != null) {

            return lendAndReturn;

        } else {

            throw new LendingNotFoundException("Lend And Return Not Found!");
        }
    }

    @Override
    public List<LendAndReturn> searchLendAndReturnByBook(Long bookId) {

        Query q = em.createQuery("SELECT lar FROM LendAndReturn lar WHERE lar.bookId = :bookId ORDER BY lar.lendId DESC");
        q.setParameter("bookId", bookId);

        return q.getResultList();

    }

    @Override
    public List<LendAndReturn> searchLendAndReturnByMember(Long memberId) {

        Query q = em.createQuery("SELECT lar FROM LendAndReturn lar WHERE lar.memberId = :memberId ORDER BY lar.lendId DESC");
        q.setParameter("memberId", memberId);

        return q.getResultList();

    }

    @Override
    public void payFine(BigDecimal payment, Long lendId) throws FineNotPaidException {

        LendAndReturn lendAndReturn = em.find(LendAndReturn.class, lendId);
        
        if (!payment.equals(BigDecimal.ZERO)) {

            lendAndReturn.setFineAmount(payment);

        } else {

            throw new FineNotPaidException("Please pay exact amount!");
        }
    }

    @Override
    public void returnBook(Long lendId, Date returnDate) throws FineNotPaidException {

        LendAndReturn lendAndReturn = em.find(LendAndReturn.class, lendId);

        lendAndReturn.setReturnDate(returnDate);

    }

}
