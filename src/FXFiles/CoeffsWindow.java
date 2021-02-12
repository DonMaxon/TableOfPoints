package FXFiles;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

public class CoeffsWindow implements AdditionalWindows {

    boolean status = false;

    private Stage logStage_;

    @FXML
    private AnchorPane PaneForAll;

    @FXML
    private TextField aField;

    @FXML
    private Button okButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Label aLabel;

    @FXML
    private Label bLabel;

    @FXML
    private Label nameOfFunc;

    @FXML
    private TextField bField;

    @FXML
    private void cancelAction() {
        logStage_.hide();
        status=CANCEL;
    }

    public boolean showDialog(Stage stage, String generalFuncForm){
        logStage_= stage;
        nameOfFunc.setText(generalFuncForm);
        logStage_.showAndWait();
        return status;
    }

    @FXML
    private void okAction() {
        try {
            double a = getA();
            a = getB();
            logStage_.hide();
            status = OK;
        }
        catch(NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Bad type of a or b");
            alert.show();
        }
    }

    public double getA(){
        double result = Double.parseDouble(aField.getText());
        return result;
    }

    public double getB(){
        double result = Double.parseDouble(bField.getText());
        return result;
    }

}