package FXFiles;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;



public class AuxillaryWindow implements AdditionalWindows {

    boolean status = false;

    private Stage auxillaryStage_;

    public boolean showDialog(Stage stage, String s){
        auxillaryStage_= stage;
        auxillaryStage_.showAndWait();
        return status;
    }



    @FXML
    private DialogPane AuxillaryDialogPane;

    @FXML
    private Spinner<Integer> SpinnerOfPointsCount;

    @FXML
    private Label LabelforLeftBorder;

    @FXML
    private Label LabelforRightBorder;

    @FXML
    private Label LabelForSpinner;

    @FXML
    private TextField leftBorderTextField;

    @FXML
    private  TextField rightBorderTextField;

    @FXML
    private Button okButton;

    @FXML
    private Button cancelButton;

    @FXML
    private void cancelAction(){
        auxillaryStage_.hide();
        status=CANCEL;
    }

    @FXML
    private void okAction(){
        try {
            if (getLeftDomainBorder() < getRightDomainBorder()) {
                auxillaryStage_.hide();
                status = OK;
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR, "Right border is less or equal than left");
                alert.show();
            }
        }
        catch(NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Bad type of borders");
            alert.show();
        }

    }

    public  double getLeftDomainBorder(){
        double result = Double.parseDouble(leftBorderTextField.getText());
        return result;
    }

   public double getRightDomainBorder(){
        double result = Double.parseDouble(rightBorderTextField.getText());
        return result;
    }

    public int getPointsCount(){
        return SpinnerOfPointsCount.getValue();
    }


    @FXML
    void initialize() {
        SpinnerValueFactory<Integer> valueFactory = //
                new SpinnerValueFactory.IntegerSpinnerValueFactory(2, Integer.MAX_VALUE, 2);
        SpinnerOfPointsCount.setValueFactory(valueFactory);
    }
}
