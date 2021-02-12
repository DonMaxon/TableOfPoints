package functions;

public class InappropriateFunctionPointException extends Exception{
    InappropriateFunctionPointException(){
        System.out.println("Error, InappropriateFunctionPointException");
    }
    InappropriateFunctionPointException(String s){
        System.out.println(s);
    }

}
