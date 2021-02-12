package FXFiles;


import java.io.File;
import java.io.IOException;
import java.util.Optional;

import functions.basic.*;
import functions.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;


public class MainWindowController {

    private TabulatedFunctionDoc doc_;

    private Stage mainStage_;

    private FileChooser fileC_;

    ObservableList<FunctionPoint> listOfElems = FXCollections.observableArrayList();


    @FXML
    private MenuItem saveAsItem;


    @FXML
    private TextField xText;

    @FXML
    private TextField yText;

    @FXML
    private TableView<FunctionPoint> tableOfPoints;

    @FXML
    private TableColumn<FunctionPoint, Double> xFromPoint =new TableColumn<FunctionPoint, Double>("x");

    @FXML
    private TableColumn<FunctionPoint, Double> yFromPoint=new TableColumn<FunctionPoint, Double>("y");

    @FXML
    private LineChart<Number, Number> chart;

    private void refill(TabulatedFunction newFunc, String seriesName){
        listOfElems.remove(0, listOfElems.size());
        chart.getData().clear();
        XYChart.Series<Number, Number> series= new XYChart.Series<Number, Number>();
        for (int i = 0; i < doc_.getPointsCount(); ++i){
            listOfElems.add(newFunc.getPoint(i));
            if (!Double.isInfinite(newFunc.getPointY(i))&&!Double.isNaN(newFunc.getPointY(i))) {
                series.getData().add(new XYChart.Data<Number, Number>(newFunc.getPointX(i), newFunc.getPointY(i)));
            }
        }
        chart.getData().add(series);
        series.setName(seriesName);
        if (mainStage_!=null) {
            mainStage_.setOnCloseRequest(event -> {
                event.consume();
                quitAction();
            });
        }
    }

    @FXML
    private void addPoint(){
        try {
            FunctionPoint point = new FunctionPoint(Double.parseDouble(xText.getText()), Double.parseDouble(yText.getText()));
            doc_.addPoint(point);
            refill(doc_.getFunctionInDoc(), "Not standart function");
        }
        catch(NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Bad type of point for adding");
            alert.show();
        }
        catch (InappropriateFunctionPointException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Bad point for adding");
            alert.show();
        }

    }

    @FXML
    private void deletePoint(){
        ObservableList<FunctionPoint> allPoints;
        FunctionPoint selected;
        allPoints = tableOfPoints.getItems();
        selected = tableOfPoints.getSelectionModel().getSelectedItem();
        for (int i =0; i<doc_.getPointsCount(); ++i){
            if (doc_.getPoint(i).equals(selected)){
                try {
                    doc_.deletePoint(i);
                    allPoints.remove(i);
                }
                catch (IllegalStateException e){
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Function must have more than 2 points");
                    alert.show();
                }
            }
        }
    }

    @FXML
    public void showCos(){
        Cos cos;
        Pair<AdditionalWindows, Stage> p = windowSetting("CoeffsInput.fxml");
        if (p.getKey().showDialog(p.getValue(), "a*cos(bx)")) {
            CoeffsWindow controller = (CoeffsWindow) p.getKey();
            double a = controller.getA();
            double b = controller.getB();
            cos = new Cos(a, b);
            auxillaryLoad(cos, trigonometricNameSet("cos(", cos));
        }


    }

    @FXML
    public void showSin(){
        Sin sin;
        Pair<AdditionalWindows, Stage> p = windowSetting("CoeffsInput.fxml");
        if (p.getKey().showDialog(p.getValue(), "a*sin(bx)")) {
            CoeffsWindow controller = (CoeffsWindow) p.getKey();
            double a = controller.getA();
            double b = controller.getB();
            sin = new Sin(a, b);
            auxillaryLoad(sin, trigonometricNameSet("sin(", sin));
        }

    }

    @FXML
    public void showTg(){
        Tan tan;
        Pair<AdditionalWindows, Stage> p = windowSetting("CoeffsInput.fxml");
        if (p.getKey().showDialog(p.getValue(), "a*tan(bx)")) {
            CoeffsWindow controller = (CoeffsWindow) p.getKey();
            double a = controller.getA();
            double b = controller.getB();
            tan = new Tan(a, b);
            auxillaryLoad(tan, trigonometricNameSet("tan(", tan));
        }
    }

    private String trigonometricNameSet(String funcName, TrigonometricFunction func){
        return func.getA()+"*"+funcName+func.getB()+"x)";
    }

    @FXML
    public void showExp(){
        Exp exp;
        Pair<AdditionalWindows, Stage> p = windowSetting("CoeffsInput.fxml");
        if (p.getKey().showDialog(p.getValue(), "a*b^x")) {
            CoeffsWindow controller = (CoeffsWindow) p.getKey();
            double a = controller.getA();
            double b = controller.getB();
            exp = new Exp(a, b);
            auxillaryLoad(exp, expNameSetting(a, b));
        }

    }

    private String expNameSetting(Double a, Double b){
        return a+"*"+b+"^x";
    }

    @FXML
    public void showLog(){
        Log log;
        Pair<AdditionalWindows, Stage> p = windowSetting("CoeffsInput.fxml");
        if (p.getKey().showDialog(p.getValue(), "a*log(b, x)")) {
            CoeffsWindow controller = (CoeffsWindow) p.getKey();
            double a = controller.getA();
            double b = controller.getB();
            if (logCheck(b)) {
                log = new Log(a, b);
                auxillaryLoad(log, logNameSetting(a, b));
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR, "Bad value of the base of logarithm");
                alert.show();//shows in case of some problems during input
            }
        }
    }

    private boolean logCheck(double b){
        if (b!=1&&b>0){
            return true;
        }
        return false;
    }

    private String logNameSetting(Double a, Double b){
        return "y = "+ a+ "*log("+b+", x)";
    }

    @FXML
    public void showPoly(){
        Function poly;
        try{
            Pair<AdditionalWindows, Stage> p = windowSetting("PolyInput.fxml");
            if (p.getKey().showDialog(p.getValue(), "")) {
                PolynomWindow controller = (PolynomWindow) p.getKey();
                double coeffs[] = controller.getCoeffs();
                poly = new Polynom(coeffs);
                auxillaryLoad(poly, polyNameSetting(coeffs));
            }
        }
        catch (NumberFormatException NFexc){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Bad type of coeffcients or separators");
            alert.show();//shows in case of some problems during input
        }
    }
    private String polyNameSetting(double[] coeffs){
        String res = "y = ";
        for (int i = 0; i< coeffs.length-1; ++i){
            if (coeffs[i]!=0){
                res+="x^"+(Integer)(coeffs.length-i-1)+" +";
            }
        }
        res+=(Double) (coeffs[coeffs.length-1]);
        if (res == "y = "){
            return "0";
        }
        else return res;
    }

    private Pair<AdditionalWindows, Stage> windowSetting(String nameOfWindow){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(nameOfWindow));
            Parent root = loader.load();
            AdditionalWindows controller = loader.getController();
            Scene scene = new Scene(root, 300, 300);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setOnCloseRequest(windowEvent -> {
                stage.hide();
            });
            return new Pair<>(controller, stage);
        }
        catch (IOException IOExc){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Page can not be opened");
            alert.show();//shows in case of some problems during uploading the fxml page
        }
        return new Pair<>(null, null);
    }

    private void auxillaryLoad(Function function, String seriesName){
        Pair<AdditionalWindows, Stage> p = windowSetting("AuxillaryWindow.fxml");
        AuxillaryWindow controller = (AuxillaryWindow)p.getKey();
        Function func = function;
        if (p.getKey().showDialog(p.getValue(), "")){
            doc_.tabulateFunction(func, controller.getLeftDomainBorder(),
                    controller.getRightDomainBorder(), controller.getPointsCount());
            refill(doc_.getFunctionInDoc(), seriesName);
        }
    }

    @FXML
    public void saveAction() {
        try {
            if (doc_.fileNameAssigned()) {
                doc_.saveFunction();
            } else {
                saveAsAction();
            }
        }
        catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "File not found");
            alert.show();
        }
    }

    @FXML
    private void openAction(){
        File file = fileC_.showOpenDialog(new Stage());
        try {
            doc_.loadFunction(file.getAbsolutePath());
            refill(doc_.getFunctionInDoc(), "Not standart function");
        }
        catch (NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "File not found");
            alert.show();
        }
        catch(RuntimeException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Wrong type of file was chosen");
            alert.show();
        }
    }


    @FXML
    public void quitAction(){
        if (doc_.modified()){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you really want to exit?");
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == ButtonType.OK) {
                mainStage_.close();
            }
            else{
                return;
            }
        }
        mainStage_.close();
    }

    @FXML
    public void saveAsAction(){
        File file = fileC_.showSaveDialog(new Stage());
        try {
            doc_.saveFunctionAs(file.getAbsolutePath());
        }
        catch(NullPointerException e){

        }
    }

    @FXML
    private void newAction(){
        Pair<AdditionalWindows, Stage> p = windowSetting("AuxillaryWindow.fxml");
        AuxillaryWindow controller = (AuxillaryWindow)p.getKey();
        if (p.getKey().showDialog(p.getValue(), "")){
            doc_.newFunction(controller.getLeftDomainBorder(), controller.getRightDomainBorder(), controller.getPointsCount());
            refill(doc_.getFunctionInDoc(), "y = 0");
        }
    }

    public void setStage(Stage stage){
        mainStage_ = stage;
        mainStage_.show();
    }

    @FXML
    public void initialize() {
        tableOfPoints.setItems(listOfElems);
        xFromPoint.setCellValueFactory(cellData -> cellData.getValue().getPropertyX().asObject());
        yFromPoint.setCellValueFactory(cellData -> cellData.getValue().getPropertyY().asObject());
        fileC_ = new FileChooser();
        doc_ = new TabulatedFunctionDoc();
        doc_.newFunction(0, 10, 5);
        refill(doc_.getFunctionInDoc(), "y = 0");
    }

}
