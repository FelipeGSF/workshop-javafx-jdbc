package gui;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable, DataChangeListener{
    
    /** INVERSAO DE CONTROLE
    *   Declarando dependencia sem colocar a implementação da classe DepartmentService ( = new DepartmentService(); )
    */
    private DepartmentService service;
    
    @FXML
    private TableView<Department> tableViewDepartment;//cria referencia para a tableview do tipo department
    
    @FXML//table column pede dois tipos. Um é o tipo da entidade (Department) e o outro é o tipo da coluna id (Integer)
    private TableColumn<Department, Integer> tableColumnId;//cria referencia para a coluna id
    
    @FXML
    private TableColumn<Department, String> tableColumnName;//cria referencia para a coluna name
    
    @FXML
    private TableColumn<Department, Department> tableColumnEDIT;
    
    @FXML
    private TableColumn<Department, Department> tableColumnREMOVE;
    
    @FXML
    private Button btNew;//cria referencia do Button New
    
    
    @FXML // este ObservableList será associado ao TableView para mostrar os departamentos na tela
    private ObservableList<Department> obsList;
    
    @FXML
    public void onBtNewAction(ActionEvent event){
        Stage parentStage = Utils.currentStage(event);
        Department obj = new Department();
        createDialogForm(obj, "/gui/DepartmentForm.fxml", parentStage);
//        System.out.println("onBtNewAction");
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
        initializeNodes();//metodo auxiliar para iniciar componentes da tela
    }
    
    //comando para iniar apropriadamente o comportamento das colunas da tabela
    private void initializeNodes() {
        //padrao do JavaFX para iniciar o comportamento das colunas
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        //para a tableview acompanhar o tamanho da janela
        Stage stage = (Stage)Main.getMainScene().getWindow();//a função getWindows pega a referencia para a janela
        //Window é uma superclasse de Stage portanto o downcasting é necessario
        
        //para a table view acompanhar a altura da janela
        tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
    }
    
    /**
     * Método responsável por acessar o serviço
     * carregar os departamentos
     * e jogar os departamentos na <!--ObservableList<Department>-->
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
        
        //acrescenta um novo botão em cada linha da tabela. quando clicado ele abre a tela de edição do createDialogForm
        initEditButtons();
        initRemoveButtons();
    }
    
    private void createDialogForm(Department obj, String absoluteName, Stage parentStage){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load();
            
            DepartmentFormController controller = loader.getController();
            controller.setDepartment(obj);
            controller.setDepartmentService(new DepartmentService());
            controller.subscribeDataChangeListener(this);
            controller.updateFormData();
            
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Enter Department Data");
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(parentStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();
            
        }catch(IOException ioe){
            Alerts.showAlert("IO Exception", "Error loading view", ioe.getMessage(), AlertType.ERROR);
        }
    }

    @Override
    public void onDataChanged() {
        updateTableView();
    }
    
    private void initEditButtons(){
        tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnEDIT.setCellFactory(param -> new TableCell<Department, Department>(){
            private final Button button = new Button("edit");
            
            @Override
            protected void updateItem(Department obj, boolean empty){
                super.updateItem(obj, empty);
                
                if(obj == null){
                    setGraphic(null);
                    return;
                }
                
                setGraphic(button);
                button.setOnAction(event -> createDialogForm(obj, "/gui/DepartmentForm.fxml", Utils.currentStage(event)));
            }
        });
    }
    
    private void initRemoveButtons(){
        tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnREMOVE.setCellFactory(param -> new TableCell<Department, Department>(){
            private final Button button = new Button("remove");
            
            @Override
            protected void updateItem(Department obj, boolean empty){
                super.updateItem(obj, empty);
                
                if(obj == null){
                    setGraphic(null);
                    return;
                }
                
                setGraphic(button);
                button.setOnAction(event -> removeEntity(obj));
            }
        });
    }
    
    private void removeEntity(Department obj){
        Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");
        
        if(result.get() == ButtonType.OK){
            if(service == null){
                throw new IllegalStateException("Service was null");
            }
            try{
                service.remove(obj);
                updateTableView();
            }catch(DbIntegrityException dbie){
                Alerts.showAlert("Error removing object", null, dbie.getMessage(), AlertType.ERROR);
            }
            
        }
    }
    
}
