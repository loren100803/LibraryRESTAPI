<!DOCTYPE html>
<html>
    <head>
        <script>
function loadDoc() {
  const xhttp = new XMLHttpRequest();
  //gestione risposta
  xhttp.onload = function() {
    document.getElementById("demo").innerHTML = this.responseText;
    //effettuo il parsing della risposta
    let dati=JSON.parse(this.responseText); 
    //inserisco i dati nell'interfaccia
    let text ="<table border='1'> <th>Autore</th><th>Titolo</th><th>ISBN</th>";
    for (let x in dati){
        text +="<tr><td>"+dati[x].Autore+"</td>";
        text +="<td>"+dati[x].Titolo+"</td>";
        
        text +="<td>"+dati[x].ISBN+"</td></tr>";
        text +="<td> <button type='button' onclick='delete("+dati[x].ISBN+")'>Delete</button></td>";
    }
    text +="</table>";
    document.getElementById("demo").innerHTML=text;
    //document.getElementById("num").value=dati[0].id;
    //document.getElementById("nom").value=dati[0].info;
  }
  //preparo l'URL
  xhttp.open("GET", "api/book/all");
  //popolo l'intestazione
  xhttp.setRequestHeader("accept","application/json");
  //richiamo l'URL
  xhttp.send();
}

function del(x)
{
    xhhtp.open("GET", "api/book/delete?ISBN="+x);
    //popolo l'intestazione
  xhttp.setRequestHeader("accept","application/json");
  //richiamo l'URL
  xhttp.send();
}


</script>
    </head>
<body>

<h2>The XMLHttpRequest Object</h2>
<button type="button" onclick="loadDoc()">Request data</button>

<p id="demo"></p>

<form>
    
    id<input type="text" name="num" id="num">
    info<input type="text" name="nom" id="nom">    
</form>
</body>
</html>