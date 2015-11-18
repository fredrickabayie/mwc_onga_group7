<?php    
    if(!isset($_REQUEST['cmd'])){
        echo '{"result":0,message:"unknown command"}';
        exit();
    }
    $cmd=$_REQUEST['cmd'];
    switch($cmd)
    {
        case 1:     
            viewOrders();  
        break;
        
        case 2:     
            setOrderReady();  
        break;

        case 3:     
            dischargeOrder();  
        break;

        case 4:     
            viewHistory();  
        break;
        default:
            echo '{"result":0,"message":"unknown command"}';
        break;
    }
    
        function viewOrders(){
            include("order.php");
            $obj=new order();
            
            if($obj->viewOrders()) {               
                $row=$obj->fetch();
                echo '{"result":1,"orders":[';    
                while($row){
                    echo json_encode($row);         
                    $row=$obj->fetch();
                    if($row){
                        echo ",";                   
                    }
                }
                echo "]}";       
            }
            else {
                echo '{"result":0}';        
            }
        }

        function setOrderReady(){
            $id = $_REQUEST['id'];
            include("order.php");
            $obj=new order();
            
            if($obj->orderReady($id)) {               
                echo '{"result":1}';        
            }
            else {
                echo '{"result":0}';        
            }
        }

        function dischargeOrder(){
            $id = $_REQUEST['id'];
            include("order.php");
            $obj=new order();
            
            if($obj->dischargeOrder($id)) {               
                echo '{"result":1}';        
            }
            else {
                echo '{"result":0}';        
            }
        }

        function viewHistory(){
            include("order.php");
            $obj=new order();
            
            if($obj->viewHistory()) {               
                $row=$obj->fetch();
                echo '{"result":1,"history":[';    
                while($row){
                    echo json_encode($row);         
                    $row=$obj->fetch();
                    if($row){
                        echo ",";                   
                    }
                }
                echo "]}";       
            }
            else {
                echo '{"result":0}';        
            }
        }
?>