<?php
	include("adb.php");
	class order extends adb
	{

		function viewOrders()
        {
	    	$str_query="SELECT onga_mwc_meals.meal_name, onga_mwc_meals.meal_price, 
	    		onga_mwc_orders.order_id,onga_mwc_orders.user_id, onga_mwc_orders.order_date, 
	    		onga_mwc_orders.discharge_time, onga_mwc_orders.order_time FROM onga_mwc_orders 
	    		INNER JOIN onga_mwc_meals ON onga_mwc_orders.meal_id=onga_mwc_meals.meal_id 
	    		WHERE onga_mwc_orders.discharge_time='00:00:00' ORDER BY onga_mwc_orders.order_time ASC";
	    	return $this->query($str_query);

	    }

	    function dischargeOrder($id)
        {
	    	$str_query="UPDATE onga_mwc_orders set discharge_time=CURTIME() where order_id=$id";	    	
	    	return $this->query($str_query);
	    }

	    function orderReady($id){
	    	$str_query="UPDATE onga_mwc_orders set order_status='ready' where order_id=$id";	    	
	    	return $this->query($str_query);
	    }

	    function viewHistory(){
	    	$str_query="SELECT onga_mwc_meals.meal_name, onga_mwc_meals.meal_price, 
	    		onga_mwc_orders.order_id,onga_mwc_orders.user_id, onga_mwc_orders.order_date, 
	    		onga_mwc_orders.discharge_time FROM onga_mwc_orders INNER JOIN onga_mwc_meals 
	    		ON onga_mwc_orders.meal_id=onga_mwc_meals.meal_id 
	    		WHERE onga_mwc_orders.discharge_time!='00:00:00' 
	    		ORDER BY onga_mwc_orders.order_date ASC, onga_mwc_orders.discharge_time ASC";
	    	return $this->query($str_query);
	    }
	}
?>