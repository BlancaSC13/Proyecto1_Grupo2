package domain.Objects;
public class Node {
    public Object data;
    //Node porque es del tipo de la clase, funcionando como apuntador
    public Node prev; //apuntador al nodo anterior
    public Node next; //apuntador al sgte nodo
    public Integer priority; //1=low, 2=medium, 3=high


    //Constructor
    //Constructor
    public Node(Object data){
        this.data = data;
        this.next = null; //apunta a nulo
    }

    //Constructor sobrecargado
    public Node(Object data, Integer priority){
        this.data = data;
        this.priority = priority;
        this.next = null; //apunta a nulo
    }

    //Constructor sobrecargado
    public Node(){
        this.next = null; //apunta a nulo
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
}
