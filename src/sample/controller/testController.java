package sample.controller;
import sample.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
public class testController {

    @FXML
    Label light;
    public  void  changeTetLight(){
       light.setText("a");
    }
}
