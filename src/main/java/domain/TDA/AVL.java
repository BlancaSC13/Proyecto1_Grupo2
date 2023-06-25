package domain.TDA;

import domain.Objects.BTreeNode;
import exceptions.TreeException;
import interfaces.Tree;
import java.util.ArrayList;
import java.util.List;

public class AVL implements Tree{
    private BTreeNode root;

    public AVL() {
        this.root = null;
    }

    public BTreeNode getRoot() {
        return root;
    }

    @Override
    public int size() throws TreeException {
        if(isEmpty()){
            throw new TreeException("AVL Binary Search Tree is empty");
        }
        return size(root);
    }

    private int size(BTreeNode node){
        if(node==null)
            return 0;
        else
            return 1+size(node.left)+size(node.right);
    }

    @Override
    public void clear() {
        this.root = null;
    }

    @Override
    public boolean isEmpty() {
        return root==null;
    }

    @Override
    public boolean contains(Object element) throws TreeException {
        if(isEmpty()){
            throw new TreeException("AVL Binary Search Tree is empty");
        }
        return binarySearch(root, element);
    }

    private boolean binarySearch(BTreeNode node, Object element){
        if(node==null)
            return false;
        else if(util.Utility.compare(node.data, element)==0)
            return true; //ya lo encontro
        else if(util.Utility.compare(element, node.data)< 0)
            return binarySearch(node.left, element);
        else return binarySearch(node.right, element);
    }

    @Override
    public void add(Object element) {
        root = add(root, element, "root");
    }

    private BTreeNode add(BTreeNode node, Object element, String sequence){
        if(node==null) //el arbol esta vacio
            node = new BTreeNode(element, "Added as "+sequence);
        else if(util.Utility.compare(element, node.data)< 0)
            node.left = add(node.left, element, sequence+"/left");
        else if(util.Utility.compare(element, node.data)> 0)//va como hijo der
            node.right = add(node.right, element, sequence+"/right");

        //se debe verificar que el arbol este balanceado
        int balance = getBalanceFactor(node);

        //Left Left Case
        if(balance>1&&util.Utility.compare(element, node.left.data)<0) {
            node.path += ". Simple right rotate";
            return rightRotate(node);
        }

        //Right Right Case
        if(balance<-1&&util.Utility.compare(element, node.right.data)>0) {
            node.path += ". Simple left rotate";
            return leftRotate(node);
        }

        //Left Right Case
        if(balance>1&&util.Utility.compare(element, node.left.data)>0) {
            node.path += ". Double left/right rotate";
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        //Right Left Case
        if(balance<-1&&util.Utility.compare(element, node.right.data)<0) {
            node.path += ". Double right/left rotate";
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    private int getBalanceFactor(BTreeNode node) {
        if(node==null)
            return 0;
        else return height(node.left) - height(node.right);
    }

    private BTreeNode leftRotate(BTreeNode node) {
        BTreeNode node1 = node.right;
        if (node1 != null){ //importante para evitar NullPointerException
            BTreeNode node2 = node1.left;
            //se realiza la rotacion (perform rotation)
            node1.left = node;
            node.right = node2;
        }
        return node1;
    }

    private BTreeNode rightRotate(BTreeNode node) {
        BTreeNode node1 = node.left;
        if (node1 != null) { //importante para evitar NullPointerException
            BTreeNode node2 = node1.right;
            //se realiza la rotacion (perform rotation)
            node1.right = node;
            node.left = node2;
        }
        return node1;
    }

    @Override
    public void remove(Object element) throws TreeException {
        if(isEmpty()){
            throw new TreeException("AVL Binary Search Tree is empty");
        }
        root = remove(root, element);
    }

    private BTreeNode remove(BTreeNode node, Object element){
        if(node!=null) {
            if (util.Utility.compare(element, node.data) < 0)
                node.left = remove(node.left, element);
            else if (util.Utility.compare(element, node.data) > 0)
                node.right = remove(node.right, element);
            else if (util.Utility.compare(node.data, element) == 0) { //ya encontramos el elemento a eliminar
                //Caso 1. Es un nodo sin hijos. Es una hoja
                if (node.left == null && node.right == null)
                    return null;
                    //Caso 2. El nodo solo tiene un hijo
                else if (node.left != null && node.right == null)
                    return node.left; //retorna el subarbol izq y sustituye el nodo actual
                else if (node.left == null && node.right != null)
                    return node.right; //retorna el subarbol derecho y sustituye el nodo actual
                    //Caso 3. El nodo tiene 2 hijos
                else if (node.left != null && node.right != null) {
                    Object value = min(node.right);
                    node.data = value;
                    node.right = remove(node.right, value);
                }
            }

            //se debe verificar que el arbol este balanceado
            int balance = getBalanceFactor(node);

            //Left Left Case
            if(balance>1&&getBalanceFactor(node.left)>=0){
                return rightRotate(node);}

            //Right Right Case
            if (balance < -1 && getBalanceFactor(node.right) <= 0){
                return leftRotate(node);}

            //Left Right Case
            if (balance > 1 && getBalanceFactor(node.left) < 0) {
                node.left = leftRotate(node.left);
                return rightRotate(node);
            }

            //Right Left Case
            if (balance < -1 && getBalanceFactor(node.right) > 0) {
                node.right = rightRotate(node.right);
                return leftRotate(node);
            }
        }//end node!=null



        return node; //retorna el nodo modificado
    }


    @Override
    public int height(Object element) throws TreeException {
        if(isEmpty()){
            throw new TreeException("AVL Binary Search Tree is empty");
        }
        return height(root, element, 0);
    }

    private int height(BTreeNode node, Object element, int counter){
        if(node==null)
            return -1;
        else if(util.Utility.compare(node.data, element)==0)
            return counter;
        else //en este caso debe buscar por la izq y por la der
            if(util.Utility.compare(element, node.data)< 0)
                return height(node.left, element, ++counter);
            else return height(node.right, element, ++counter);
        //return Math.max(height(node.left, element, ++counter), height(node.right, element, counter));
    }

    @Override
    public int height() throws TreeException {
        if(isEmpty()){
            throw new TreeException("AVL Binary Search Tree is empty");
        }
        return height(root)-1; //pq no cuente el nivel de la raiz
    }

    private int height(BTreeNode node){
        if(node==null)
            return 0;
        else
            return 1+Math.max(height(node.left), height(node.right));
    }

    @Override
    public Object min() throws TreeException {
        if(isEmpty())
            throw new TreeException("AVL Binary Search Tree is empty");
        return min(root);
    }

    private Object min(BTreeNode node){
        if(node.left!=null)
            return min(node.left);
        return node.data;
    }

    @Override
    public Object max() throws TreeException {
        if(isEmpty())
            throw new TreeException("AVL Binary Search Tree is empty");
        return max(root);
    }

    private Object max(BTreeNode node){
        if(node.right!=null)
            return max(node.right);
        return node.data;
    }

    @Override
    public List<Object> preOrder() throws TreeException {
        if(isEmpty())
            throw new TreeException("AVL Binary Search Tree is empty");
        List<Object> list = new ArrayList<>();
        preOrder(root, list);
        return list;
    }

    //metodo interno
    //preOrder: node-left-right
    private void preOrder(BTreeNode node, List<Object> list){
        if(node!=null){
            list.add(node.data);
            preOrder(node.left, list);
            preOrder(node.right, list);
        }
    }


    @Override
    public List<Object> InOrder() throws TreeException {
        if(isEmpty())
            throw new TreeException("AVL Binary Search Tree is empty");
        List<Object> list = new ArrayList<>();
        inOrder(root, list);
        return list;
    }

    //metodo interno
    //preOrder: left-node-right
    private void inOrder(BTreeNode node, List<Object> list){
        if(node!=null){
            inOrder(node.left, list);
            list.add(node.data);
            inOrder(node.right, list);
        }
    }

    @Override
    public List<Object> postOrder() throws TreeException {
        if(isEmpty())
            throw new TreeException("AVL Binary Search Tree is empty");
        List<Object> list = new ArrayList<>();
        postOrder(root, list);
        return list;
    }

    //metodo interno
    //preOrder: left-right-node
    private void postOrder(BTreeNode node, List<Object> list){
        if(node!=null){
            postOrder(node.left, list);
            postOrder(node.right, list);
            list.add(node.data);
        }
    }

    public boolean isBalanced() throws TreeException{
        if(isEmpty()){
            throw new TreeException("AVL Binary Search Tree is empty");
        }
        return isBalanced(root);
    }
    private boolean isBalanced (BTreeNode node){
        if(node==null) return true;
        else return getBalanceFactor(node)<2
                &&getBalanceFactor(node)>-2
                &&isBalanced(node.left)
                &&isBalanced(node.right);
    }

    @Override
    public boolean modify(Object element) throws TreeException {
        if (!contains(element)) return false;
        else {
            remove(element);
            add(element);
            return true;
        }
    }

    @Override
    public String toString() {
        if(isEmpty()) return "AVL Binary Search tree is empty";
        String result = "AVL Binary Search Tree Tour...\n";
        try {
            result+="PreOrder: "+preOrder()+"\n";
            result+="InOrder: "+InOrder()+"\n";
            result+="PostOrder: "+postOrder()+"\n";
        } catch (TreeException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    //Gabriel Chaves
    public String getSequence(){
        return getSequence(root);
    }

    private String getSequence(BTreeNode node) {
        String sequence = "";

        if (node != null && util.Utility.compare(node.data, root.data) == 0){
            sequence = "Se insertó " + node.data + " como " + node.path;
        }

        if (node.left != null) {
            sequence += "\nSe insertó " + node.left.data + " como " + node.left.path;
            sequence += getSequence(node.left);
        }

        if (node.right != null) {
            sequence += "\nSe insertó " + node.right.data + " como " + node.right.path;
            sequence += getSequence(node.right);
        }

        return sequence;
    }

}
