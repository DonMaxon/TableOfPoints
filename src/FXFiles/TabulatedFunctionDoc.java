package FXFiles;

import functions.*;
import javafx.scene.control.Alert;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TabulatedFunctionDoc implements TabulatedFunction {

    private TabulatedFunction functionInDoc_;
    private boolean isModified_;
    private String fileName_;
    private boolean fileNameAssigned_;


    @Override
    public int getPointsCount() {
        return functionInDoc_.getPointsCount();
    }

    @Override
    public FunctionPoint getPoint(int index) {
        return functionInDoc_.getPoint(index);
    }

    @Override
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        functionInDoc_.setPoint(index, point);
        isModified_ = true;
    }

    @Override
    public double getPointX(int index) {
        return functionInDoc_.getPointX(index);
    }

    @Override
    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        functionInDoc_.setPointX(index, x);
        isModified_ = true;
    }

    @Override
    public double getPointY(int index) {
        return functionInDoc_.getPointY(index);
    }

    @Override
    public void setPointY(int index, double y) {
        functionInDoc_.setPointY(index, y);
        isModified_ = true;
    }

    @Override
    public void deletePoint(int index) {
        functionInDoc_.deletePoint(index);
        isModified_ = true;
    }

    @Override
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        functionInDoc_.addPoint(point);
        isModified_ = true;
    }


    @Override
    public double getLeftDomainBorder() {
        return functionInDoc_.getLeftDomainBorder();
    }

    @Override
    public double getRightDomainBorder() {
        return functionInDoc_.getRightDomainBorder();
    }

    @Override
    public double getFunctionValue(double x) {
        return functionInDoc_.getFunctionValue(x);
    }

    public String toString(){
        return functionInDoc_.toString();
    }

    public int hashCode(){
        return functionInDoc_.hashCode();
    }

    public TabulatedFunction getFunctionInDoc(){
        try{
            return (TabulatedFunction)functionInDoc_.clone();
        }
        catch (CloneNotSupportedException e){
            System.err.println("Error in clone");
        }
        return null;
    }

    public void newFunction(double leftX, double rightX, int pointsCount){
        isModified_=true;
        fileNameAssigned_=false;
        functionInDoc_ = new ArrayTabulatedFunction(leftX, rightX, pointsCount);
    }

    public void tabulateFunction(Function function, double leftX, double rightX, int pointsCount){
        functionInDoc_ = TabulatedFunctions.tabulate(function, leftX, rightX, pointsCount);
        isModified_=true;
    }

    public void saveFunction(){
        try(FileWriter writerInDoc = new FileWriter(fileName_)){
            TabulatedFunctions.writeTabulatedFunction(functionInDoc_, writerInDoc);
        }
        catch(IOException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Bad name of file");
            alert.show();
        }
        fileNameAssigned_=true;
        isModified_=false;
    }

    public  void saveFunctionAs(String fileName){
        try(FileWriter writerInDoc = new FileWriter(fileName)){
            TabulatedFunctions.writeTabulatedFunction(functionInDoc_, writerInDoc);
        }
        catch(IOException ie){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Bad name of file");
            alert.show();
        }
        catch (NullPointerException ne){
            Alert alert = new Alert(Alert.AlertType.ERROR, "File not found");
            alert.show();
        }
        fileName_=fileName;
        fileNameAssigned_=true;
        isModified_=false;
    }

    public void loadFunction(String fileName){
        try(FileReader readerFromDoc = new FileReader(fileName)){
            functionInDoc_ = TabulatedFunctions.readTabulatedFunction(readerFromDoc);
        }
        catch(IOException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Bad name of file");
            alert.show();
        }
        fileNameAssigned_=true;
        isModified_=false;
        fileName_=fileName;
    }

    public boolean modified(){
        return isModified_;
    }

    public boolean fileNameAssigned(){
        return fileNameAssigned_;
    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        TabulatedFunctionDoc result = new TabulatedFunctionDoc();
        result.fileName_ = fileName_;
        result.isModified_=isModified_;
        result.functionInDoc_=(TabulatedFunction)functionInDoc_.clone();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null){
            return false;
        }
        if (o instanceof TabulatedFunctionDoc) {
            return functionInDoc_.equals(o) & fileName_.equals(((TabulatedFunctionDoc) o).fileName_);
        }
        return false;
    }

}
