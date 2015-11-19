<?php
include_once 'adb.php';

/**
*Class to model the queries for Onga Systems
*/
class Queries extends adb {
    
    
    /**
    * Function to query the users table for login details
    *
    * @param $username The username of the user
    * @param $password The password of the user
    */
    function onga_mwc_users ( $username, $password ) {
        $login_query = "SELECT *
                        FROM `onga_mwc_users`
                        WHERE username='$username'
                        and password='$password'";
        return $this->query(login_query);
    }
}

?>