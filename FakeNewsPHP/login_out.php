<?php
session_destroy();
unset($_SESSION['login']);
header("Location: index.php");exit;
?>