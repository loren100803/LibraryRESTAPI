import com.google.gson.Gson;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/book")
public class Library {
    private final String error = "Server error, contact administrators";

    private boolean checkParams(int idPrestito, String isbn, String autore, String titolo) {
        return (isbn == null || isbn.trim().length() == 0) || (titolo == null || titolo.trim().length() == 0)
                || (autore == null || autore.trim().length() == 0);
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response read() {
        final String QUERY = "SELECT * FROM Libri";
        final List<Book> books = new ArrayList<>();
        final String[] data = Database.getData();
        try (

                Connection conn = DriverManager.getConnection(data[0]);
                PreparedStatement pstmt = conn.prepareStatement(QUERY)) {
            ResultSet results = pstmt.executeQuery();
            while (results.next()) {
                Book book = new Book();
                book.setTitolo(results.getString("Titolo"));
                book.setAutore(results.getString("Autore"));
                book.setISBN(results.getString("ISBN"));
                books.add(book);

            }
        } catch (SQLException e) {
            e.printStackTrace();
            String obj = new Gson().toJson(error);
            return Response.serverError().entity(obj).build();
        }
        String obj = new Gson().toJson(books);
        return Response.status(200).entity(obj).build();
    }

    @GET
    @Path("/prezzo")
    @Produces(MediaType.APPLICATION_JSON)
    public Response read(@QueryParam("Autore") String Autore,
                          @QueryParam("Prezzo") int Prezzo) {
                String obj;
                System.out.println("------------------------------------Z " + Autore+" "+Prezzo);
        if (checkParams(Autore, Prezzo)) {
          obj = new Gson().toJson("Parameters must be valid");
           return Response.serverError().entity(obj).build();
        }
            final String QUERY = "SELECT * FROM Libri WHERE Autore = ? AND Prezzo < ?";
            final List<Book> books = new ArrayList<>();
            final String[] data = Database.getData();
            try (

                    Connection conn = DriverManager.getConnection(data[0]);
                    PreparedStatement pstmt = conn.prepareStatement(QUERY)) {
                        pstmt.setString(1, Autore);
                        pstmt.setInt(2, Prezzo);
                ResultSet results = pstmt.executeQuery();
                while (results.next()) {
                    Book book = new Book();
                    book.setTitolo(results.getString("Titolo"));
                    book.setAutore(results.getString("Autore"));
                    book.setISBN(results.getString("ISBN"));
                    book.setPrezzo(results.getInt("Prezzo"));
                    books.add(book);

                }
            } catch (SQLException e) {
                e.printStackTrace();
                obj = new Gson().toJson(error);
                return Response.serverError().entity(obj).build();
            }
            obj = new Gson().toJson(books);
            return Response.status(200).entity(obj).build();
    }
        

  private boolean checkParams(String autore, int prezzo) {
        return false;
    }

    @PUT
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response update(@FormParam("ISBN") String isbn,
            @FormParam("Titolo") String titolo,
            @FormParam("Autore") String autore) {
        if (checkParams(isbn, titolo, autore)) {
            String obj = new Gson().toJson("Parameters must be valid");
            return Response.serverError().entity(obj).build();
        }
        final String QUERY = "UPDATE Libri SET Titolo = ?, Autore = ? WHERE ISBN = ?";
        final String[] data = Database.getData();
        try (

                Connection conn = DriverManager.getConnection(data[0]); // , data[1], data[2]);
                PreparedStatement pstmt = conn.prepareStatement(QUERY)) {
            pstmt.setString(1, titolo);
            pstmt.setString(2, autore);
            pstmt.setString(3, isbn);
            pstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            String obj = new Gson().toJson(error);
            return Response.serverError().entity(obj).build();
        }
        String obj = new Gson().toJson("Libro con ISBN:" + isbn + " modificato con successo");
        return Response.ok(obj, MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response create(@FormParam("ISBN") String isbn,
            @FormParam("Titolo") String titolo,
            @FormParam("Autore") String autore) {
        if (checkParams(isbn, titolo, autore)) {
            String obj = new Gson().toJson("Parameters must be valid");
            return Response.serverError().entity(obj).build();
        }
        final String QUERY = "INSERT INTO Libri(ISBN,Titolo,Autore) VALUES(?,?,?)";
        final String[] data = Database.getData();
        try (

                Connection conn = DriverManager.getConnection(data[0]);
                PreparedStatement pstmt = conn.prepareStatement(QUERY)) {
            pstmt.setString(1, isbn);
            pstmt.setString(2, titolo);
            pstmt.setString(3, autore);
            pstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            String obj = new Gson().toJson(error);
            return Response.serverError().entity(obj).build();
        }
        String obj = new Gson().toJson("Libro con ISBN:" + isbn + " aggiunto con successo");
        return Response.ok(obj, MediaType.APPLICATION_JSON).build();
    }

    private boolean checkParams(String isbn, String titolo, String autore) {
        return false;
    }

    @DELETE
    @Path("/delete")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response update(@FormParam("ISBN") String isbn) {
        if (isbn == null || isbn.trim().length() == 0) {
            String obj = new Gson().toJson("ISBN must be valid");
            return Response.serverError().entity(obj).build();
        }
        final String QUERY = "DELETE FROM Libri WHERE ISBN = ?";
        final String[] data = Database.getData();
        try (

                Connection conn = DriverManager.getConnection(data[0]);
                PreparedStatement pstmt = conn.prepareStatement(QUERY)) {
            pstmt.setString(1, isbn);
            pstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            String obj = new Gson().toJson(error);
            return Response.serverError().entity(obj).build();
        }
        String obj = new Gson().toJson("Libro con ISBN:" + isbn + " eliminato con successo");
        return Response.ok(obj, MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/prestito")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response create(@FormParam("idUtente") int idUtente,
            @FormParam("ISBN") String isbn,
            @FormParam("dataInizio") String dataInizio,
            @FormParam("dataFine") String dataFine,
            @FormParam("restituito") String restituito) {
        if (checkParams(idUtente, isbn, dataInizio, dataFine, restituito)) {
            String obj = new Gson().toJson("Parameters must be valid");
            return Response.serverError().entity(obj).build();
        }
        int quantita = 0;
        final String QUERY = "INSERT INTO Prestiti(idUtente, ISBN, dataInizio, dataFine) VALUE(?,?,?,?)";
        final String querySelezionaQuantita = "SELECT Quantità FROM Libri WHERE ISBN = '" + isbn + "'";

        final String[] data = Database.getData();
        try (

                Connection conn = DriverManager.getConnection(data[0]);
                PreparedStatement pstmt = conn.prepareStatement(QUERY);
                PreparedStatement pstmt1 = conn.prepareStatement(querySelezionaQuantita);) {
            pstmt.setInt(1, idUtente);
            pstmt.setString(2, isbn);
            pstmt.setString(3, dataInizio);
            pstmt.setString(4, dataFine);
            pstmt.execute();

            ResultSet r = pstmt1.executeQuery();
            while (r.next()) {
                quantita = Integer.parseInt(r.getString("quantita"));
            }

            if (quantita > 0) {
                pstmt.execute();
                final String QUERYModificaQuantita = "UPDATE Libri SET Quantità = quantita -1";
                PreparedStatement pstmt2 = conn.prepareStatement(QUERYModificaQuantita);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            String obj = new Gson().toJson(error);
            return Response.serverError().entity(obj).build();
        }
        String obj = new Gson().toJson("Libro con ISBN:" + isbn + " aggiunto con successo");
        return Response.ok(obj, MediaType.APPLICATION_JSON).build();
    }

    private boolean checkParams(int idUtente, String isbn, String dataInizio, String dataFine, String restituito) {
        return false;
    }

    @POST
    @Path("/restituzione")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response create(@FormParam("idPrestito") int idPrestito,
            @FormParam("ISBN") String isbn,
            @FormParam("restituito") String restituito) {
        if (checkParams(idPrestito, isbn, restituito)) {
            String obj = new Gson().toJson("Parameters must be valid");
            return Response.serverError().entity(obj).build();
        }
        int quantita = 0;
        final String QUERY = "UPDATE Prestiti SET restituito='si'";
        final String QUERY1 = "UPDATE Libri SET Quantità= Quantità+1";

        final String[] data = Database.getData();
        try (

                Connection conn = DriverManager.getConnection(data[0]);
                PreparedStatement pstmt = conn.prepareStatement(QUERY);
                PreparedStatement pstmt1 = conn.prepareStatement(QUERY1);) {

            if (quantita > 0) {
                pstmt.execute();
                final String QUERYModificaQuantita = "UPDATE Libri SET Quantità = quantita -1";
                PreparedStatement pstmt2 = conn.prepareStatement(QUERYModificaQuantita);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            String obj = new Gson().toJson(error);
            return Response.serverError().entity(obj).build();
        }
        String obj = new Gson().toJson("Libro con ISBN:" + isbn + " aggiunto con successo");
        return Response.ok(obj, MediaType.APPLICATION_JSON).build();
    }

    private boolean checkParams(int idPrestito, String isbn, String restituito) {
        return false;
    }

}