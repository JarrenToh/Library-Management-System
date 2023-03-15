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
import javax.validation.constraints.NotNull;

/**
 *
 * @author jarrentoh
 */
@Entity
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    @Column(nullable = false)
    @NotNull
    private String title;
    
    @Column(nullable = false, unique = true)
    @NotNull
    private String isbn;
    
    @Lob
    private byte[] bookImage;
    
    @Column(nullable = false)
    @NotNull
    private String author;

    @OneToMany(mappedBy = "book", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private ArrayList<LendAndReturn> lending;

    public Book(String title, String isbn, String author, byte[] bookImage) {
        this.title = title;
        this.isbn = isbn;
        this.bookImage = bookImage;
        this.author = author;
        this.lending = new ArrayList<>();
    }

    public Book() {
        this.lending = new ArrayList<>();
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bookId != null ? bookId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the bookId fields are not set
        if (!(object instanceof Book)) {
            return false;
        }
        Book other = (Book) object;
        if ((this.bookId == null && other.bookId != null) || (this.bookId != null && !this.bookId.equals(other.bookId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Book[ id=" + bookId + " ]";
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the isbn
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * @param isbn the isbn to set
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author the author to set
     */
    public void setAuthor(String author) {
        this.author = author;
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
     * @return the bookImage
     */
    public byte[] getBookImage() {
        return bookImage;
    }

    /**
     * @param bookImage the bookImage to set
     */
    public void setBookImage(byte[] bookImage) {
        this.bookImage = bookImage;
    }

}
