/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.Book;
import entity.LendAndReturn;
import entity.Member;
import error.EntityManagerException;
import error.FineNotPaidException;
import error.MemberNotFoundException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import session.stateless.BookSessionBeanLocal;
import session.stateless.LendAndReturnSessionBeanLocal;
import session.stateless.MemberSessionBeanLocal;

/**
 *
 * @author jarrentoh
 */
@Named(value = "bookManagedBean")
@SessionScoped
public class BookManagedBean implements Serializable {

    @EJB
    private LendAndReturnSessionBeanLocal lendAndReturnSessionBean;

    @EJB
    private MemberSessionBeanLocal memberSessionBean;

    @EJB
    private BookSessionBeanLocal bookSessionBean;

    /**
     * Creates a new instance of BookManagedBean
     */
    private String searchType = "TITLE";
    private String searchString;
    private List<Book> books;

    private int booksCount = 0;
    private List<Book> booksToLend;

    //For Lending
    private Long bookId;
    private Book selectedBook;
    private String memberId;

    private String title;
    private String isbn;
    private byte[] bookImage;
    private String author;

    private List<LendAndReturn> lendAndReturns;
    private BigDecimal fineAmount;

    private static final Logger LOGGER = Logger.getLogger(AuthenticationManagedBean.class.getName());

    public BookManagedBean() {
    }

    @PostConstruct
    public void init() {
        if (getSearchString() == null || getSearchString().equals("")) {

            setBooks(bookSessionBean.retrieveAllBooks());

        } else {
            switch (getSearchType()) {
                case "TITLE":
                    setBooks(bookSessionBean.searchBooksByTitle(getSearchString()));
                    break;
                case "ISBN": {
                    setBooks(bookSessionBean.searchBooksByISBN(getSearchString()));
                    break;
                }
                case "AUTHOR": {
                    setBooks(bookSessionBean.searchBooksByAuthor(getSearchString()));
                    break;
                }
            }
        }
    }

    public void handleSearch() {
        init();
    } //end handleSearch

    public void loadSelectedBook() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            this.setSelectedBook(bookSessionBean.retrieveBookByBookId(bookId));
            setTitle(selectedBook.getTitle());
            setIsbn(selectedBook.getIsbn());
            setAuthor(selectedBook.getAuthor());
            setBookImage(selectedBook.getBookImage());
            List<LendAndReturn> lendAndReturnsOfBook = lendAndReturnSessionBean.searchLendAndReturnByBook(bookId);
            setLendAndReturns(lendAndReturnsOfBook);

            if (lendAndReturnsOfBook.size() == 0) {

                setFineAmount(BigDecimal.ZERO);
                return;

            } else {

                if (lendAndReturnsOfBook.get(0).getReturnDate() != null) {

                    setFineAmount(BigDecimal.ZERO);
                    return;

                }

                Date pastDate = lendAndReturnsOfBook.get(0).getLendDate();
                Date currentDate = new Date();
                long differenceInMillis = currentDate.getTime() - pastDate.getTime();
                int days = (int) (differenceInMillis / (24 * 60 * 60 * 1000));

                if (days > 14) {

                    BigDecimal value = BigDecimal.valueOf((days - 14) * 0.5);
                    value = value.setScale(2, RoundingMode.HALF_UP);
                    if (lendAndReturnsOfBook.get(0).getFineAmount().compareTo(BigDecimal.ZERO) > 0) {

                        setFineAmount(BigDecimal.ZERO);
                        return;
                    }

                    setFineAmount(value);

                } else {

                    setFineAmount(BigDecimal.ZERO);

                }

            }

        } catch (Exception e) {

            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to load Book"));

        }
    } //end loadSelectedBook

    public void createLending() {

        System.out.println("managedbean.BookManagedBean.createLending()");
        FacesContext context = FacesContext.getCurrentInstance();
        LendAndReturn newLendAndReturn = new LendAndReturn();
        try {
            System.out.println(memberId);
            Member member = memberSessionBean.retrieveMemberByIdentityNo(memberId);

            newLendAndReturn.setMember(member);
            newLendAndReturn.setBook(selectedBook);
//            System.out.println(newLendAndReturn.getMember());
//            System.out.println(newLendAndReturn.getMember());

            newLendAndReturn.setMemberId(member.getMemberId());
            newLendAndReturn.setBookId(selectedBook.getBookId());

            Date currentTime = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String formattedTime = dateFormat.format(currentTime);
            newLendAndReturn.setLendDate(dateFormat.parse(formattedTime));

            newLendAndReturn.setReturnDate(null);
            newLendAndReturn.setFineAmount(BigDecimal.ZERO);

            lendAndReturnSessionBean.createLendAndReturn(newLendAndReturn);

            setSelectedBook(null);

        } catch (MemberNotFoundException | ParseException | EntityManagerException ex) {

            context.addMessage(memberId, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to Lend"));
            context.validationFailed();

        }
    }

    public boolean isAvailable(Book book) {
        
        List<LendAndReturn> lendAndReturnsOfBook = lendAndReturnSessionBean.searchLendAndReturnByBook(book.getBookId());

        if (lendAndReturnsOfBook.isEmpty()) {

            return true;
        }

        LendAndReturn latestLendAndReturn = lendAndReturnsOfBook.get(0);

        return latestLendAndReturn.getReturnDate() != null;

    }


public void returnBook() {

        LendAndReturn latestLendAndReturn = lendAndReturns.get(0);

        try {
            Date currentTime = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String formattedTime = dateFormat.format(currentTime);
            lendAndReturnSessionBean.returnBook(latestLendAndReturn.getLendId(), dateFormat.parse(formattedTime));
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Book returned successfully."));

        } catch (FineNotPaidException | ParseException ex) {

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Could Not Return!"));

        }
    }

    public void payFine() {

        LendAndReturn latestLendAndReturn = lendAndReturns.get(0);
        try {
            lendAndReturnSessionBean.payFine(fineAmount, latestLendAndReturn.getLendId());
            setFineAmount(BigDecimal.ZERO);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Fine Paid!"));
            
        } catch (FineNotPaidException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Could Not Pay Fine!"));
        }
        

        
    }

    public boolean noFineOutstanding() {

        return fineAmount.equals(BigDecimal.ZERO);
    }

    public boolean availableForReturn() {

        if (noFineOutstanding()) {

            if (!isAvailable(selectedBook)) {

                return true;
            }

            return false;

        }
        return false;
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
     * @return the books
     */
    public List<Book> getBooks() {
        return books;
    }

    /**
     * @param books the books to set
     */
    public void setBooks(List<Book> books) {
        this.books = books;
    }

    /**
     * @return the booksCount
     */
    public int getBooksCount() {
        return booksCount;
    }

    /**
     * @param booksCount the booksCount to set
     */
    public void setBooksCount(int booksCount) {
        this.booksCount = booksCount;
    }

    /**
     * @return the booksToLend
     */
    public List<Book> getBooksToLend() {
        return booksToLend;
    }

    /**
     * @param booksToLend the booksToLend to set
     */
    public void setBooksToLend(List<Book> booksToLend) {
        this.booksToLend = booksToLend;
    }

    /**
     * @return the bookId
     */
    public Long getBookId() {
        return bookId;
    }

    /**
     * @param bookId the bookId to set
     */
    public void setBookId(Long bookId) {
        this.bookId = bookId;
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
     * @return the bookImage
     */
    public StreamedContent getBookImage() {
        if (bookImage != null) {
            InputStream inputStream = new ByteArrayInputStream(bookImage);
            return new DefaultStreamedContent(inputStream, "image/jpeg");
        } else {
            InputStream inputStream = getClass().getResourceAsStream("/images/bookCover.png");
            StreamedContent streamedContent = new DefaultStreamedContent(inputStream, "image/jpeg");
            return streamedContent;
        }
    }

    /**
     * @param bookImage the bookImage to set
     */
    public void setBookImage(byte[] bookImage) {
        this.bookImage = bookImage;
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
     * @return the selectedBook
     */
    public Book getSelectedBook() {
        return selectedBook;
    }

    /**
     * @param selectedBook the selectedBook to set
     */
    public void setSelectedBook(Book selectedBook) {
        this.selectedBook = selectedBook;
    }

    /**
     * @return the memberId
     */
    public String getMemberId() {
        return memberId;
    }

    /**
     * @param memberId the memberId to set
     */
    public void setMemberId(String memberId) {
        this.memberId = memberId;
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
     * @return the fineAmount
     */
    public BigDecimal getFineAmount() {
        return fineAmount;
    }

    /**
     * @param fineAmount the fineAmount to set
     */
    public void setFineAmount(BigDecimal fineAmount) {
        this.fineAmount = fineAmount;
    }

}
