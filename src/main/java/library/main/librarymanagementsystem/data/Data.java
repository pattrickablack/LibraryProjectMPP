package library.main.librarymanagementsystem.data;

import library.main.librarymanagementsystem.models.Book;
import library.main.librarymanagementsystem.models.LibraryMember;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/*
* This is a mock data that
* try to emulate a database started DATA
* */
public class Data {
    public static List<Book> listBooks = new ArrayList<>();
    public static List<LibraryMember> listMembers = new ArrayList<>();

    static{

        /*INITIALIZING BOOKS*/
        Book book1= new Book("Book1","123", "categoryX","autorBook1");
        Book book2= new Book("Book2","124", "categoryX","autorBook2");
        Book book3= new Book("Book3","125", "categoryX","autorBook3");

        listBooks.add(book1);
        listBooks.add(book2);
        listBooks.add(book3);

        /*INITIALIZING MEMBERS*/
        LibraryMember member1 = new LibraryMember("Pancho");
        LibraryMember member2 = new LibraryMember("Pincho");
        LibraryMember member3 = new LibraryMember("Poncho");
        LibraryMember member4 = new LibraryMember("Flor");
        LibraryMember member5 = new LibraryMember("Girasol");
        listMembers.add(member1);
        listMembers.add(member2);
        listMembers.add(member3);
        listMembers.add(member4);
        listMembers.add(member5);
    }
    public Data() {

    }


    public static   List<Book> requestBooks(){
        return listBooks;
    }

    public static boolean createBook(String nameBook, String isbn ,String category,String nameAuthor){
        Book book= new Book(nameBook,isbn, category,nameAuthor);
        listBooks.add(book); // mock database
        return true;
    };

    public static void removeBook(String oldIsbn){
        System.out.println("oldISBN to delete : " + oldIsbn);

       listBooks= listBooks.stream()
                            .filter(book->!book.getIsbn().equals(oldIsbn))
                            .collect(Collectors.toList());

        System.out.println("remaining books " + listBooks);
    }

    public static void updateBook(Book updatedBook){

        listBooks= listBooks.stream()
                .map(currentBook->{
                    if(currentBook.getIsbn().equals(updatedBook.getIsbn()))
                        return updatedBook;
                    return currentBook;
                })
                .collect(Collectors.toList());

        System.out.println("list books UPDATED..." + listBooks);
    }



    // DATA MOCK REQUEST TO THE DATABASE

    public static  List<LibraryMember> requestMembers(){
        return listMembers;
    }



    public static void main(String[] args) {
//        removeBook("123");
//        removeBook("124");
    }
}
