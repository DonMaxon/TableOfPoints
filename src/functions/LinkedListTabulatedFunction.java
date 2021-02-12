package functions;

import java.io.Serializable;
import java.io.Externalizable;
import java.io.ObjectOutput;
import java.io.ObjectInput;
import java.io.IOException;
import java.util.Objects;


public class LinkedListTabulatedFunction implements TabulatedFunction, Serializable, Externalizable, Cloneable {
    private int length_;
    private transient FunctionNode current_;
    private FunctionNode head_;
    private transient int currentIndex_;

    public LinkedListTabulatedFunction(){
        length_=0;
        current_ = null;
        currentIndex_=-1;
        head_ =null ;
    }

    private class FunctionNode implements Externalizable{
        FunctionPoint value;
        FunctionNode prev;
        FunctionNode next;
        private FunctionNode(FunctionNode prev, FunctionPoint value, FunctionNode next){
            this.prev = prev;
            this.next = next;
            this.value = value;
        }

        private FunctionNode(FunctionNode prev, FunctionNode next){
            this.prev = prev;
            this.next = next;
        }

        private FunctionNode(){
            value = null;
            next = null;
            prev = null;
        }

        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
        }

        @Override
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        }
    }

    private FunctionNode getNodeByIndex(int index){
        FunctionNode returning;
        if (index < currentIndex_){
            if (currentIndex_-index<index){
                returning = current_;
                for (int i = 0; i<currentIndex_-index; ++i){
                    returning = returning.prev;
                }
            }
            else{
                returning = head_.next;
                for (int i = 0; i<index; ++i){
                    returning = returning.next;
                }
            }
        }
        else{
            if(index-currentIndex_<length_-index+1){
                returning = current_;
                for (int i = 0; i<index-currentIndex_; ++i){
                    returning = returning.next;
                }
            }
            else{
                returning = head_.next;
                for (int i = 0; i<length_-index; ++i){
                    returning = returning.prev;
                }
            }
        }
        currentIndex_= index;
        current_=returning;
        return returning;
    }

    private FunctionNode addFirst(){
        FunctionNode first = new FunctionNode();
        head_.next = first;
        first.next=first;
        first.prev=first;
        currentIndex_=0;
        current_=first;
        return  first;
    }

    private FunctionNode addNodeToTail(){
        if (length_==0){
            ++length_;
            return addFirst();
        }
        ++length_;
        FunctionNode oldTail = head_.next.prev;
        FunctionNode firstNode = head_.next;
        FunctionNode newNode = new FunctionNode(oldTail, firstNode);
        firstNode.prev = newNode;
        oldTail.next = newNode;
        currentIndex_=length_-1;
        current_=newNode;
        return newNode;
    }

    private FunctionNode addNodeByIndex(int index){
        if (length_==0){
            ++length_;
            return addFirst();
        }
        ++length_;
        FunctionNode nextForNew = getNodeByIndex(index);
        FunctionNode prevForNew = nextForNew.prev;
        FunctionNode newNode = new FunctionNode(prevForNew, nextForNew);
        nextForNew.prev=newNode;
        prevForNew.next=newNode;
        if (index == 0){
            head_.next=newNode;
        }
        return newNode;
    }

    private FunctionNode deleteNodeByIndex(int index){
        FunctionNode deleting = getNodeByIndex(index);
        FunctionNode nextForDeleting = deleting.next;
        FunctionNode prevForDeleting = deleting.prev;
        nextForDeleting.prev=prevForDeleting;
        prevForDeleting.next=nextForDeleting;
        currentIndex_=index;
        current_=nextForDeleting;
        return deleting;
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount){
        head_ = new FunctionNode();
        head_.next = head_;
        head_.prev = head_;
        current_ = head_;
        currentIndex_ = -1;
        if (leftX>=rightX){
            throw new IllegalArgumentException ();
        }
        double interval = (rightX-leftX)/(pointsCount-1);
        for (int i = 0; i < pointsCount; ++i) {
            FunctionNode cur = addNodeToTail();
            FunctionPoint curPoint = new FunctionPoint(leftX + interval * i, 0);
            cur.value=curPoint;
        }
        current_ = head_.next;
        currentIndex_ = 0;
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values){
        head_ = new FunctionNode();
        head_.next = head_;
        head_.prev = head_;
        if (leftX>=rightX) {
            throw new IllegalArgumentException();
        }
        double interval = (rightX-leftX)/(values.length-1);
        for (int i = 0; i < values.length; ++i) {
            FunctionNode cur = addNodeToTail();
            FunctionPoint curPoint = new FunctionPoint(leftX + interval * i, values[i]);
            cur.value=curPoint;
        }
        current_ = head_.next;
        currentIndex_ = 0;
    }

    public LinkedListTabulatedFunction( FunctionPoint[] values){
        head_ = new FunctionNode();
        head_.next = head_;
        head_.prev = head_;
        if (values[0].x_>=values[values.length-1].x_){
            throw new IllegalArgumentException ();
        }
        if (values.length<2){
            throw new IllegalArgumentException();
        }
        FunctionNode cur = addNodeToTail();
        FunctionPoint curPoint = new FunctionPoint(values[0]);
        for (int i = 0; i < values.length; ++i) {
            if (i==0) {
                cur = addFirst();
            }
            else{
                cur=addNodeToTail();
            }
            curPoint = new FunctionPoint(values[i]);
            cur.value=curPoint;
            if (i>0 && curPoint.x_<=cur.prev.value.x_){
                throw new IllegalArgumentException();
            }
        }
        current_ = head_.next;
        currentIndex_ = 0;
    }

    @Override
    public double getLeftDomainBorder(){
        return head_.next.value.x_;
    }

    @Override
    public double getRightDomainBorder(){
        return head_.next.prev.value.x_;
    }

    @Override
    public double getFunctionValue(double x){
        if (!(getRightDomainBorder()>=x && getLeftDomainBorder()<=x)){
            return Double.NaN;
        }
        current_ = head_.next;
        currentIndex_=0;
        while (x>current_.next.value.x_){
            current_ = current_.next;
            ++currentIndex_;
        }
        if (x==current_.value.x_) {
            return current_.value.y_;
        }
        else{
            return current_.value.y_+(x-current_.value.x_)*
                    (current_.next.value.y_-current_.value.y_)/
                    (current_.next.value.x_-current_.value.x_);
        }
    }

    @Override
    public int getPointsCount(){
        return length_;
    }

    @Override
    public FunctionPoint getPoint(int index){
        if (index < 0 | index > length_ - 1){
            throw new FunctionPointIndexOutOfBoundsException ();
        }
        current_=getNodeByIndex(index);
        currentIndex_ = index;
        return new FunctionPoint(current_.value);
    }

    @Override
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        if (index < 0 | index > length_- 1){
            throw new FunctionPointIndexOutOfBoundsException ();
        }
        if (index == length_-1 ) {
            if (head_.next.prev.prev.value.x_<point.x_) {
                current_=head_.next.prev;
                current_.value = point;
                currentIndex_=length_-1;
                return;
            }
            throw new InappropriateFunctionPointException();
        }
        if (index == 0){
            if (head_.next.next.value.x_>point.x_){
                current_=head_.next;
                head_.next.value = point;
                currentIndex_=0;
                return;
            }
            throw new InappropriateFunctionPointException();
        }
        current_ = getNodeByIndex(index);
        if (current_.prev.value.x_<point.x_ && point.x_<current_.next.value.x_){
            currentIndex_=index;
            current_.value= point;
            return;
        }
        throw new InappropriateFunctionPointException();
    }

    @Override
    public double getPointX(int index){
        if (index < 0 | index > length_ - 1){
            throw new FunctionPointIndexOutOfBoundsException ();
        }
        current_ = getNodeByIndex(index);
        currentIndex_=index;
        return current_.value.x_;
    }

    @Override
    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        if (index < 0 | index > length_ - 1){
            throw new FunctionPointIndexOutOfBoundsException ();
        }
        current_ = getNodeByIndex(index);
        currentIndex_= index;
        if (index == length_-1 ) {
            if (current_.prev.value.x_<x) {
                current_.value.x_ = x;
                return;
            }
            throw new InappropriateFunctionPointException();
        }
        if (index == 0){
            if (current_.next.value.x_>x){
                current_.value.x_=x;
                return;
            }

            throw new InappropriateFunctionPointException();
        }
        if (current_.prev.value.x_<x && x<current_.next.value.x_){
            current_.value.x_=x;
            return;
        }
        throw new InappropriateFunctionPointException("");
    }

    @Override
    public double getPointY(int index){
        if (index < 0 | index > length_ - 1){
            throw new FunctionPointIndexOutOfBoundsException ();
        }
        current_ = getNodeByIndex(index);
        currentIndex_= index;
        return current_.value.y_;
    }

    @Override
    public void setPointY(int index, double y){
        if (index < 0 | index > length_ - 1){
            throw new FunctionPointIndexOutOfBoundsException ();
        }
        current_ = getNodeByIndex(index);
        currentIndex_= index;
        current_.value.y_= y;
    }

    @Override
    public void deletePoint(int index){
        if (length_<3) {
            throw new IllegalStateException();
        }
        if (index < 0 | index > length_ - 1){
            throw new FunctionPointIndexOutOfBoundsException ();
        }
        --length_;
        deleteNodeByIndex(index);
    }

    @Override
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        current_ = head_.next;
        currentIndex_=0;
        while (point.x_>current_.value.x_ && currentIndex_!=length_){
            current_ = current_.next;
            ++currentIndex_;
        }
        if (point.x_==current_.value.x_){
            throw new InappropriateFunctionPointException();
        }
        ++length_;
        FunctionNode newPrev = current_.prev;
        FunctionNode newNext = current_;
        FunctionNode newNode = new FunctionNode(newPrev, point, newNext);
        newPrev.next = newNode;
        newNext.prev = newNode;
        if (currentIndex_==0){
            head_.next=newNode;
        }
        current_=current_.prev;

    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(length_);
        FunctionPoint[] points = new FunctionPoint[length_];
        FunctionNode cur = head_.next;
        for (int i = 0; i<length_; ++i){
            points[i]=cur.value;
            cur = cur.next;
        }
        out.writeObject(points);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        int size = in.readInt();
        FunctionPoint[] values = new FunctionPoint[length_];
        values = (FunctionPoint[])in.readObject();
        head_ = new FunctionNode();
        currentIndex_=-1;
        current_=head_;
        addFirst().value = values[0];
        for (int i = 1; i<size; ++i){
            addNodeToTail().value=values[i];
        }
    }

    @Override
    public String toString() {
        String result = "{ ";
        FunctionNode cur = head_.next;
        for (int i = 0; i < length_-1;++i){
            result+=cur.value.toString()+", ";
            cur = cur.next;
        }
        result+=cur.value.toString();
        result +="}";
        return result;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        FunctionPoint[] points = new FunctionPoint[length_];
        for(int i = 0; i < length_; ++i){
            points[i]=getPoint(i).clone();
        }
        LinkedListTabulatedFunction returning = new LinkedListTabulatedFunction(points);
        return returning;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null
                || !(o instanceof TabulatedFunction)
                || (length_ != ((TabulatedFunction) o).getPointsCount())) return false;
        if ( o instanceof LinkedListTabulatedFunction){
            LinkedListTabulatedFunction that = (LinkedListTabulatedFunction)o;
            FunctionNode curOfThat = that.head_.next;
            FunctionNode curOfThis = head_.next;
            for (int i = 0; i<length_; ++i){
                if (curOfThat.value.equals(curOfThis.value)){
                    return false;
                }
            }
            return true;
        }
        TabulatedFunction that = ((TabulatedFunction) o);
        for (int i = 0; i < length_; ++i){
            if (!that.getPoint(i).equals(getPoint(i))){
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(length_);
        FunctionNode cur = head_.next;
        for (int i = 0; i < length_; ++i){
            result = result*(int)Math.pow(31, i) + cur.value.hashCode();
            cur = cur.next;
        }
        return result;
    }
}

