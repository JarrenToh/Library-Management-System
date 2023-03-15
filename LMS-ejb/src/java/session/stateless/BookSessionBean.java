/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.stateless;

import entity.Book;
import error.BookNotFoundException;
import error.EntityManagerException;
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
public class BookSessionBean implements BookSessionBeanLocal {

    @PersistenceContext
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public Book createBook(Book newBook) throws EntityManagerException {
        try {

            em.persist(newBook);
            return newBook;

        } catch (Exception e) {

            throw new EntityManagerException("Cannot persist Book into database.");

        }

    }

    @Override
    public List<Book> retrieveAllBooks() {
        Query query = em.createQuery("SELECT b FROM Book b");
        return query.getResultList();
    }

    @Override
    public Book retrieveBookByBookId(Long bookId) throws BookNotFoundException {

        Book book = em.find(Book.class, bookId);

        if (book != null) {

            return em.find(Book.class, bookId);

        } else {

            throw new BookNotFoundException("Book Not Found!");
        }

    }

    @Override
    public List<Book> searchBooksByTitle(String title) {
        Query q;

        if (title != null) {

            q = em.createQuery("SELECT b FROM Book b WHERE LOWER(b.title) LIKE :title");
            q.setParameter("title", "%" + title.toLowerCase() + "%");
            return q.getResultList();
        }

        return retrieveAllBooks();
    }

    @Override
    public List<Book> searchBooksByISBN(String ISBN) {
        Query q;

        if (ISBN != null) {

            q = em.createQuery("SELECT b FROM Book b WHERE LOWER(b.isbn) LIKE :isbn");
            q.setParameter("isbn", "%" + ISBN.toLowerCase() + "%");
            return q.getResultList();
        }

        return retrieveAllBooks();
    }

    @Override
    public List<Book> searchBooksByAuthor(String author) {
        Query q;

        if (author != null) {

            q = em.createQuery("SELECT b FROM Book b WHERE LOWER(b.author) LIKE :author");
            q.setParameter("author", "%" + author.toLowerCase() + "%");
            return q.getResultList();
        }

        return retrieveAllBooks();
    }

}
