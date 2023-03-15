# NetBeans Library Management System:
This is a library management system built using Java and the NetBeans IDE. The system provides functionalities for managing books, staffs and members in a library.

## Table of Contents:

### - System Requirement
### - Web Application Framework
### - Installation
### - Usage
### - Contributing

### System Requirement:

    1. Java Platform, Standard Edition, Development Kit (JDK) 8
    2. GlassFish v5.1.0 (should work with older versions of GlassFish)
    3. NetBeans IDE v12.0
    4. 2 GB RAM

### Web Application Framework:

    1. JavaServer Faces 2.3
    2. PrimeFaces v8.0

### Installation:

To install the library management system, follow these steps:

    1. Clone this repository to your local machine.
    2. Open the project in the NetBeans IDE.
    3. Add in frameworks.
    3. Build and run the project.

### Usage:

Once the project is running, you can use the system to manage books, staffs and members in a library. 

Use Cases:

    1. Login: Staff must be currently logged into the system to perform all other use cases.
        Accessible on login.xhtml

    2. Register Member: If a member is not registered, the admin staff registers a new member record in the system
        Accessible on memberRegistraion.xhtml which is accessed via memberSearch.xhtml

    3. Lend Book: Admin staff can lend books available in the library, to the members.
        Accessible on the bookSearch.xhtml 

    4. View fine amount: If the return date is later than two weeks, calculate the fine and show the fine amount for the lending.
        Accessible on the bookPage.xhtml bottom right of the layout panel at the top of the page which is accessed via bookSearch.xhtml

    5. Return Book: When a member returns a book, store the book return date and the fine amount.
        Accessible on the bookPage.xhtml bottom right of the layout panel at the top of the page which is accessed via bookSearch.xhtml

    6. Logout: Logout if the staff is currently logged in the system.
        Accessible on left navigation bar (default.xhtml)

Additional Use Cases/ Functionality:

    1. Dynamic Images: The system can retrieve and display images from the database for books, members, and staff.
    2. Staff Management: Staff members can update their first name, last name, and password.
    3. Book Search: Books can be searched by title, ISBN, and author.
    4. Book Details: Users can click on a book row to view more information about the book.
    5. Lending Details: Lending and return details can be viewed on both the book and member pages.
    6. Member Images: Members can upload images when registering.
    7. Member Search: Members can be searched by first name, identity number, or phone number.
    8. Resizable Pages: All pages in the system are resizable for better usability.


To get started, log in to the system using the default username and password:

    Username: sarah
    Password: password

### Contributing:
If you would like to contribute to the library management system, please follow these guidelines:

    1. Fork the repository.
    2. Create a new branch.
    3. Make your changes.
    4. Test your changes.
    5. Submit a pull request.

NOTE: The MySQL database has been updated to allow storage of images, which can now be accessed and utilized for the entities "Book", "Member", and "Staff" within the system. This modification enables the system to store images as a part of the data associated with each of these entities, providing greater flexibility and functionality for managing image data within the database.

To test the fine amount feature in the library management system using MySQL Workbench, follow these steps:

    1. Open MySQL Workbench and connect to the database where the library management system is stored.

    2. In the query editor, enter the following code:
        UPDATE drrsdbs.LENDANDRETURN SET lenddate = DATE_SUB(lenddate, INTERVAL 1 MONTH) WHERE lendid = 1;
    Replace "drrsdbs" with the name of the database where the system is stored, and replace "1" with the lend ID that you want to test on.

    3. Execute the query to update the lend date for the specified lend ID, effectively simulating a late return of the book.

    4. Verify that the fine amount for the associated member has been updated accordingly in the system.
