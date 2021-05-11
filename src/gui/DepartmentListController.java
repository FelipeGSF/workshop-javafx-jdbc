package gui;

import application.Main;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable{
    
    /** INVERSAO DE CONTROLE
    *   Declarando dependencia sem colocar a implementação da classe DepartmentService ( = new DepartmentService(); )
    */
    private DepartmentService service;
    
    @FXML
    private TableView<Department> tableViewDepartment;
    
    @FXML
    private TableColumn<Department, Integer> tableColumnId;
    
    @FXML
    private TableColumn<Department, String> tableColumnName;
    
    @FXML
    private Button btNew;
    
    
    @FXML // este ObservableList será associado ao TableView para mostrar os departamentos na tela
    private ObservableList<Department> obsList;
    
    @FXML
    public void onBtNewAction(){
        System.out.println("onBtNewAction");
    }
    
    /** INVERSÃO DE CONTROLE. não dando o "= new DepartmentService();" direto na classe 
    *   e sim disponibilizar uma forma de injetar a dependencia
    *   princípio SOLID
    */
    public void setDepartmentService(DepartmentService service){
        this.service = service;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeNodes();
    }

    private void initializeNodes() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        Stage stage = (Stage)Main.getMainScene().getWindow();
        tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
    }
    
    /**
     * Método responsável por acessar o serviço
     * carregar os departamentos
     * e jogar os departamentos na ObservableList<Department>
     */
    public void updateTableView(){
        if(service == null){
            throw  new IllegalStateException("Serviço estava nulo.");
        }
        
        // esta list recupera os Departamentos do DepartmentService
        List<Department> list = service.findAll();
        // carrega a list no ObservableList
        obsList = FXCollections.observableArrayList(list);
        // chama o TableView e carrega os itens da obsList na TableView
        tableViewDepartment.setItems(obsList);
    }
    
}
