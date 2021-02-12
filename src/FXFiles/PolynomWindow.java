package FXFiles;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


import java.util.Arrays;

public class PolynomWindow implements AdditionalWindows{

    boolean status = false;

    private Stage polyStage_;

    @FXML
    private AnchorPane PaneForAll;

    @FXML
    private Label Coefficients;

    @FXML
    private TextField CoeffsTextField;

    @FXML
    private Button okButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Label Info;

    @FXML
    private void cancelAction() {
        polyStage_.hide();
        status=CANCEL;
    }

    public boolean showDialog(Stage stage, String notNeededForPoly){
        polyStage_= stage;
        polyStage_.showAndWait();
        return status;
    }

    @FXML
    private void okAction() {
        polyStage_.hide();
        status = OK;
    }

    public double[] getCoeffs(){
        try {
            double[] coeffs = Arrays.stream(CoeffsTextField.getText().split(" ")).mapToDouble(Double::parseDouble).toArray();
            return coeffs;
        }
        catch (NumberFormatException NFexc){
            throw NFexc;
        }

    }

}