import com.google.gson.Gson;
import com.sun.tools.javac.comp.Todo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
@Path("/api")
public class Library {
    @GET
    @Path("/books")
    @Produces(MediaType.APPLICATION_JSON)
    public Response read(){
        final String QUERY = "SELECT Autore,Titolo,ISBN FROM Libri";
        final List<Book> books = new ArrayList<Book>();
        final String[] data = Database.getData();
        try(

                Connection conn = DriverManager.getConnection(data[0], data[1], data[2]);
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
        String obj = new Gson().toJson(books);
        return Response.status(200).entity(obj).build();
    }
}
