import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Library {

    public List<Book> read(Connection conn){
        final String QUERY = "SELECT Autore,Titolo,ISBN FROM Libri";
        final List<Book> books = new ArrayList<Book>();
        try(
                PreparedStatement pstmt = conn.prepareStatement( QUERY )
        ) {
            ResultSet results =  pstmt.executeQuery();
            while (results.next()){
                Book book = new Book();
                book.setTitolo(results.getString("Titolo"));
                book.setAutore(results.getString("Autore"));
                book.setISBN(results.getString("ISBN"));
                books.add(book);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return books;
    }
}
