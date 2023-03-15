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
import javax.ejb.Local;

/**
 *
 * @author jarrentoh
 */
@Local
public interface BookSessionBeanLocal {

    Book createBook(Book newBook) throws EntityManagerException;

    List<Book> retrieveAllBooks();

    Book retrieveBookByBookId(Long bookId) throws BookNotFoundException;

    List<Book> searchBooksByTitle(String title);

    List<Book> searchBooksByISBN(String ISBN);

    List<Book> searchBooksByAuthor(String author);
    
}
