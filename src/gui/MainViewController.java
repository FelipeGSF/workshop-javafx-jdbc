package gui;

import application.Main;
import gui.util.Alerts;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartmentService;

public class MainViewController implements Initializable{

    private static Scene mainScene;
    
    @FXML
    private MenuItem menuItemSeller;
    @FXML
    private MenuItem menuItemDepartment;
    @FXML
    private MenuItem menuItemAbout;
    
    @FXML
    public void onMenuItemSellerAction(){
        System.out.println("onMenuItemSellerAction");
    }
    
    @FXML
    public void onMenuItemDepartmentAction(){
        // o segundo parametro é uma expressao LAMBDA que vai receber um parametro DepartmentListController
        loadView("/gui/DepartmentList.fxml", (DepartmentListController controller) -> {
            controller.setDepartmentService(new DepartmentService());
            controller.updateTableView();
        });
    }
    
    @FXML
    public void onMenuItemAboutAction(){
        loadView("/gui/About.fxml", x -> {});
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
    
    // função parametrizada com o um tipo <T>, ou seja tipo Generic
    // foi feita para nao ter que criar varios loadView()
    // para istou foi utilizado o Consumer<T> initializingAction
    private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            VBox newVBox = loader.load();
            
            Scene mainScene = Main.getMainScene();
            VBox mainVBox = (VBox)((ScrollPane)mainScene.getRoot()).getContent();//pega o primeiro elemento da view o ScrollPane
            
            Node mainMenu = mainVBox.getChildren().get(0);
            mainVBox.getChildren().clear();
            mainVBox.getChildren().add(mainMenu);
            mainVBox.getChildren().addAll(newVBox.getChildren());
            
            // o getController() vai retornar o controlador do tipo que 
            // for chamado na função onMenuItemDepartmentAction()
            // neste caso um DepartmentListController
            T controller = loader.getController();//essas duas linhas vão
            initializingAction.accept(controller);//executar as funções que forem passadas como parametro
            //na expressao LAMBDA de onMenuItemDepartmentAction()
            
        } catch (IOException ex) {
            Alerts.showAlert("IO Exception", "Error loading view", ex.getMessage(), AlertType.ERROR);
        }
    }
}
