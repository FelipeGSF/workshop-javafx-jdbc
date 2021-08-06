/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

/**
 *
 * @author felip
 */
public class Utils {
    
    public static Stage currentStage(ActionEvent event){
        
        //para acesar o Stage onde o controller que recebeu o evento está
        //ex.: se clicar em um botao vai pegar o evento do botao
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }
}
