package library.main.librarymanagementsystem.application;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import library.main.librarymanagementsystem.App;
import library.main.librarymanagementsystem.application.dialog.DialogError;
import library.main.librarymanagementsystem.data.Data;
import library.main.librarymanagementsystem.models.Book;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class LibraryController implements Initializable {

    @FXML
    private ListView<String> booksListView;

    @FXML
    private TextField author;

    @FXML
    private TextField book;

    @FXML
    private TextField isbn;

    @FXML
    private TextField category;

    @FXML
    private TextField search;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadBooks();
        } catch (IOException e) {
            e.printStackTrace();
        }

        booksListView.setFixedCellSize(50.0);
    }

    @FXML
    protected void searchBook() throws IOException {

        String search_text = search.getText().strip().toLowerCase();
        loadBooks();
        if (search_text.length() >= 3) {
            ArrayList<String> results = new ArrayList<>();

            for(String book: booksListView.getItems()) {
                if (book.toLowerCase().contains(search_text)) results.add(book);
            }

            booksListView.getItems().clear();
            if (results.size() > 0) {
                for(String found_book: results) booksListView.getItems().add(found_book);
                booksListView.refresh();
                search.setText("");
            }
        }
    }

    @FXML
    protected void editBook() throws IOException {

        // Recovering the item selected
        ObservableList<Integer> selectedIndices = booksListView.getSelectionModel().getSelectedIndices();

        // Veryfing if one item was seleted
        if (selectedIndices.size() == 1) {

            String bookToEdit = booksListView.getItems().get(selectedIndices.get(0));
            String[] oldData = bookToEdit.split(";");

            System.out.println("oldData Array "+ oldData.toString());
//            String oldIsbn = bookToEdit.split(";")[2];


            EditBookWindow dialogEditBook = new EditBookWindow(oldData[0],oldData[1],oldData[2], oldData[3] );

            String str = dialogEditBook.getResult();

            Book bookEdited = dialogEditBook.getResultBookEdited();
            System.out.printf("Data bookEdited in controller, %s %s %s %s  \n",bookEdited.getTitle() ,bookEdited.getIsbn(),bookEdited.getCategory(), bookEdited.getListAuthors() );

            if (str != null) {

                Data.updateBook(bookEdited);

                loadBooks();
                search.setText("");

            }
        }
    }

    @FXML
    protected void deleteBook() throws IOException {

        // Recovering Item Seleted
        ObservableList<Integer> selectedIndices = booksListView.getSelectionModel().getSelectedIndices();

        if (selectedIndices.size() == 1) {
            String bookToEdit = booksListView.getItems().get(selectedIndices.get(0));

            String oldIsbn = bookToEdit.split(";")[1];

            Data.removeBook(oldIsbn);


            loadBooks();
            search.setText("");
        }
    }

    @FXML
    protected void addItem() throws IOException {

        /*String book_name_text = book.getText();
        String isbn_text = isbn.getText();
        String category_text = category.getText();
        String author_text = author.getText();
*/
        // validate data
        if(!isValidData())
        {
           // System.out.println(isValidData());
            Data.createBook(book.getText(), isbn.getText(),
                    category.getText(), author.getText());
            //System.out.println("Hello from data");

            // reset fields
            clearFields();
        }else {
            new DialogError("Validation Error","Check you fields");
        }

        // reaload the books list
        this.loadBooks();

    }

    public static ArrayList<String> listFilesForFolder(final File folder) throws IOException {
        ArrayList<String> al = new ArrayList<>();
        for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            String read = Files.readAllLines(Paths.get(fileEntry.getPath())).get(0);
            al.add(read.strip());
        }
        return al;
    }



    public void loadBooks() throws IOException {

        booksListView.getItems().clear();

        for(Book book: Data.requestBooks()) {
            booksListView.getItems().add(book.toString());
        }

        booksListView.refresh();
    }


    public static void changeScene(String resourceName) throws IOException {

//        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("library.fxml"));
//        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("manage-books.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(resourceName));
        Scene scene = new Scene(fxmlLoader.load()); // scene

        Stage stage = App.getPrimaryStage();
        stage.hide();
        stage.setTitle("Library Management System");
        stage.setScene(scene);
        stage.show();
    }


    private boolean isValidData() {
        return (book.getText().equals("") || isbn.getText().equals("") || category.getText().equals("") ||
                author.getText().equals(""));
    }

    private void clearFields() {
        author.setText("");
        book.setText("");
        isbn.setText("");
        category.setText("");
    }

}
