<?php
$con = mysqli_connect("localhost","id11607027_adm","123456789","id11607027_bdapp1") or die ("Sin Conexion");
$sql = "select * from alabanza";
$datos = Array();
$resul = mysqli_query($con,$sql);

while($row = mysqli_fetch_object($resul)){
    $datos[] = $row;
}

echo json_encode($datos);
mysqli_close($con);
?>