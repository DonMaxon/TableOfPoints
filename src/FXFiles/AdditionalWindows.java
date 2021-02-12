package FXFiles;

import javafx.stage.Stage;

interface AdditionalWindows {
    boolean CANCEL = false;
    boolean OK = true;
    boolean showDialog(Stage stage, String generalFuncForm);
}
