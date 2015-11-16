<?php

if (isset($_GET['cmd'])) {
    $cmd = $_REQUEST['cmd'];
    
    switch ($cmd) {
        case 'onga_mwc_users':
            onga_mwc_users ();
            break;
            
        default:
            echo 'Enter right command';
            break;
    }
}

function onga_mwc_users () {
    if (isset($_REQUEST['username']) && isset($_REQUEST['password'])) {
        include_once 'queries.php';
        
        $username = $_REQUEST['username'];
        $password = $_REQUEST['password'];
        
        $user_login = new Queries ();
        $user_login->onga_mwc_users ($username, $password);
        
        if (!$row = $user_login->fetch()) {
            echo 'Failed to login';
        } else {
            echo ''.$row['username'].' '.$row['user_image'];
        }
        
    } else {
        echo 'username or password not set';
    }
}

?>