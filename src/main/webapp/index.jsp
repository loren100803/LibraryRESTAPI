<!DOCTYPE html>
<html>

<head>
  <title>ClientGestioneBiblioteca</title>

  <style>
    table {
      border-collapse: collapse;
      width: 100%;
    }

    th,
    td {
      text-align: center;
      padding: 8px;
    }

    tr:nth-child(even) {
      text-align: center;
      background-color: #f2f2f2
    }

    th {
      background-color: #04AA6D;
      color: white;
    }
  </style>

  <script>
    function loadDoc() {
      const xhttp = new XMLHttpRequest();
      //gestione risposta
      xhttp.onload = function () {
        document.getElementById("libriDisponibili").innerHTML = this.responseText;
        //effettuo il parsing della risposta
        let dati = JSON.parse(this.responseText);
        //inserisco i dati nell'interfaccia
        let text = "<table border='1'> <th>Autore</th><th>Titolo</th><th>ISBN</th>";
        for (let x in dati) {
          text += "<tr><td>" + dati[x].Autore + "</td>";
          text += "<td>" + dati[x].Titolo + "</td>";

          text += "<td>" + dati[x].ISBN + "</td></tr>";

          text += "<tr><td> <button type='button' onclick='showUpdate(" + dati[x].ISBN + ")'> Aggiorna </button> <button type='button' onclick='loadDelete(" + dati[x].ISBN + ")'>Cancella</button></td></tr>";
        }
        text += "</table>";
        document.getElementById("libriDisponibili").innerHTML = text;

      }
      //preparo l'URL
      xhttp.open("GET", "api/book/all");
      //popolo l'intestazione
      xhttp.setRequestHeader("accept", "application/json");
      //richiamo l'URL
      xhttp.send();
    }


    function Aggiorna() {
      location.reload();

    }






    function loadDelete(x) {
      var formBody = new URLSearchParams({ 'ISBN': x });


      let res = fetch("/api/book/delete", {
        method: "DELETE",
        body: formBody,
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
        }
      });

      Aggiorna();
    }

    function loadInsert(ISBN, Autore, Titolo) {
      var formBody = new URLSearchParams({ 'ISBN': ISBN, 'Titolo': Titolo, 'Autore': Autore });


      let res = fetch("/api/book/add", {
        method: "POST",
        body: formBody,
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
        }
      });

      Aggiorna();

    }

    function loadUpdate(ISBN, Autore, Titolo) {
      var formBody = new URLSearchParams({ 'ISBN': ISBN, 'Titolo': Titolo, 'Autore': Autore });


      let res = fetch("/api/book/update", {
        method: "PUT",
        body: formBody,
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded;charset=UTF-8'
        }
      });

      Aggiorna();

    }





    function showUpdate(x) {
      document.getElementById("zonaUpdate").innerHTML = this.responseText;
      let text1 = "<form>";
      let funzione = 'loadUpdate(' + x + ', document.getElementById("autore1").value, document.getElementById("titolo1").value)';
      text1 += "<input type='text' id='isbn1' value='" + x + "' readonly></input>";
      text1 += "<input type='text' id='autore1' placeholder='Autore'>";
      text1 += "<input type='text'  id='titolo1' placeholder='Titolo'>";
      text1 += "<button type='button' onclick='" + funzione + "'>Aggiorna Libro</button>";
      text1 += "</form>";
      document.getElementById("zonaUpdate").innerHTML = text1;
    }



    function showAdd() {
      document.getElementById("zonaInsert").innerHTML = this.responseText;
      let text1 = "<form>";
      let funzione = 'loadInsert(document.getElementById("isbn").value, document.getElementById("autore").value, document.getElementById("titolo").value)';
      text1 += "<input type='text' id='isbn' placeholder='ISBN'>";
      text1 += "<input type='text' id='autore' placeholder='Autore'>";
      text1 += "<input type='text'  id='titolo' placeholder='Titolo'>";
      text1 += "<button type='button' onclick='" + funzione + "'>Aggiungi Libro</button>";
      text1 += "</form>";
      document.getElementById("zonaInsert").innerHTML = text1;


    }

  </script>
</head>

<body>

  <h1 align="center">GESTIONE PAGINA BIBLIOTECA</h1>
  <h4 align="center">Benvenuto</h4>

  <button type="button" onclick="loadDoc()">Mostra Libri Disponibili</button>
  <button type="button" onclick="showAdd()">Aggiungi Libri</button>


  <p id="libriDisponibili"></p>
  <p id="zonaUpdate"></p>
  <p id="zonaInsert"></p>
</body>

</html>