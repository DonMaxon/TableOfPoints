package functions;

public class FunctionPointIndexOutOfBoundsException extends IndexOutOfBoundsException {
    FunctionPointIndexOutOfBoundsException(){
        System.out.println("Error, FunctionPointIndexOutOfBoundsException");
    }
    FunctionPointIndexOutOfBoundsException(String s){
        System.out.println(s);
    }
}
