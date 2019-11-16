<?php
$id = $_REQUEST["id_a"];
$con = mysqli_connect("localhost","id11607027_adm","123456789","id11607027_bdapp1") or die ("Sin Conexion");
$sql = "delete from alabanza where id_a=$id";
if(mysqli_query($con,$sql)){
    echo "Alabanza eliminada exitosamente";
}else{
    echo "Error: " . mysqli_error($con);
}
mysqli_close($con);
?>