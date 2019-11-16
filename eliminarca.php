<?php
$id = $_REQUEST["id_ca"];
$con = mysqli_connect("localhost","id11607027_adm","123456789","id11607027_bdapp1") or die ("Sin Conexion");
$sql = "delete from corosAdo where id_ca=$id";
if(mysqli_query($con,$sql)){
    echo "Coro eliminado exitosamente";
}else{
    echo "Error: " . mysqli_error($con);
}
mysqli_close($con);
?>