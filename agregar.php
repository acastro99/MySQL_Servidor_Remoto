<?php
$titulo = $_REQUEST["titulo"];
$autor = $_REQUEST["autor"];
$letra = $_REQUEST["letra"];

$con = mysqli_connect("localhost","id11607027_adm","123456789","id11607027_bdapp1") or die ("Sin Conexion");
$sql = "INSERT INTO alabanza (titulo, autor, letra ) VALUES ('$titulo', '$autor', '$letra')";

$resul = mysqli_query($con, $sql);

echo $resul;
mysqli_close($con);
?>