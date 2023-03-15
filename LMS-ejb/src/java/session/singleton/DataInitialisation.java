/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session.singleton;

import entity.Book;
import entity.Member;
import entity.Staff;
import error.StaffNotFoundException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import session.stateless.BookSessionBeanLocal;
import session.stateless.MemberSessionBeanLocal;
import session.stateless.StaffSessionBeanLocal;

/**
 *
 * @author jarrentoh
 */
@Singleton
@LocalBean
@Startup
public class DataInitialisation {

    @EJB
    private MemberSessionBeanLocal memberSessionBean;

    @EJB
    private BookSessionBeanLocal bookSessionBean;

    @EJB
    private StaffSessionBeanLocal staffSessionBean;

    private static final Logger LOGGER = Logger.getLogger(DataInitialisation.class.getName());

    public DataInitialisation() {
    }

    @PostConstruct
    public void postConstruct() {

        LOGGER.info("PostConstruct");

        try {
            staffSessionBean.retrieveStaffByUsername("eric");

        } catch (StaffNotFoundException ex) {
            initializeData();

        }
    }

    private void initializeData() {

        try {
            LOGGER.info("Initializing data");
            String filePath = DataInitialisation.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            String[] path = filePath.split(File.separator);
            String[] imagePathArr = Arrays.copyOfRange(path, 0, path.length - 4);
            
            //PLEASE ADJUST THIS PATH ACCORDINGLY "YOUR PATH"/LMS/images/ ESPECIALLY FOR WINDOWS USER!!!
            String imagePath = String.join(File.separator, imagePathArr) + File.separator + "images" + File.separator;
            LOGGER.info(imagePath);
            // Iterate over the image files
            List<String> imageFiles = Arrays.asList("Eric.jpg", "Sarah.jpg", "Tony.jpg", "Dewi.jpg", "Anna.jpeg", "Madame.jpeg", "Hamlet.jpeg", "Hobbit.jpeg", "Great.jpeg", "Pride.jpeg", "Wuthering.jpeg");
            List<byte[]> images = new ArrayList<>();

            for (String fileName : imageFiles) {
                // Load the image from the project folder
                File file = new File(imagePath + fileName);
                FileInputStream inputStream = new FileInputStream(file);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                byte[] imageData = outputStream.toByteArray();

//                LOGGER.info(imageData.toString());
                // Set the BLOB parameter and execute the statement
                images.add(imageData);
            }

            //Staff
            staffSessionBean.createStaff(new Staff("Eric", "Some", "eric", "password", images.get(0)));
            staffSessionBean.createStaff(new Staff("Sarah", "Brightman", "sarah", "password", images.get(1)));

            //Member
            memberSessionBean.createMember(new Member("Tony", "Shade", 'M', 31, "S8900678A", "83722773", "13 Jurong East Ave 3", images.get(2)));
            memberSessionBean.createMember(new Member("Dewi", "Tan", 'F', 35, "S8581028X", "94602711", "15 Computing Drive", images.get(3)));
            
            //Books
            bookSessionBean.createBook(new Book("Anna Karenina", "0451528611", "Leo Tolstoy", images.get(4)));
            bookSessionBean.createBook(new Book("Madame Bovary", "979-8649042031", "Gustave Flaubert", images.get(5)));
            bookSessionBean.createBook(new Book("Hamlet", "1980625026", "William Shakespeare", images.get(6)));
            bookSessionBean.createBook(new Book("The Hobbit", "9780007458424", "J R R Tolkien", images.get(7)));
            bookSessionBean.createBook(new Book("Great Expectation", "1521853592", "Charles Dickens", images.get(8)));
            bookSessionBean.createBook(new Book("Pride and Prejudice", "979-8653642272", "Jane Austen", images.get(9)));
            bookSessionBean.createBook(new Book("Wuthering Heights", "3961300224", "Emily Bronte", images.get(10)));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
