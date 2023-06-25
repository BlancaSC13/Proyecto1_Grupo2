package domain.TDA;

import domain.Objects.BTreeNode;
import exceptions.TreeException;
import interfaces.Tree;

import java.util.ArrayList;
import java.util.List;

public class BTree implements Tree{
    private BTreeNode root;

    public BTreeNode getRoot() {
        return root;
    }

    public BTree() {
        this.root = null;
    }

    @Override
    public int size() throws TreeException {
        if(isEmpty()){
            throw new TreeException("Binary Tree is empty");
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
            throw new TreeException("Binary Tree is empty");
        }
        return binarySearch(root, element);
    }

    private boolean binarySearch(BTreeNode node, Object element){
        if(node==null)
            return false;
        else if(util.Utility.compare(node.data, element)==0)
            return true; //ya lo encontro
        else
            return binarySearch(node.left, element) || binarySearch(node.right, element);
    }

    @Override
    public void add(Object element) {
        //root = add(root, element);
        root = add(root, element, "root");
    }

    private BTreeNode add(BTreeNode node, Object element){
        if(node==null){ //el arbol esta vacio
            node = new BTreeNode(element);
        }else{
            int value = util.Utility.random(10);
            if(value%2==0){ //si son pares va como hijo izq
                node.left = add(node.left, element);
            }else{ //va como hijo der
                node.right = add(node.right, element);
            }
        }
        return node;
    }

    private BTreeNode add(BTreeNode node, Object element, String path){
        if(node==null){ //el arbol esta vacio
            node = new BTreeNode(element, path);
        }else{
            int value = util.Utility.random(10);
            if(value%2==0){ //si son pares va como hijo izq
                node.left = add(node.left, element, path+"/left");
            }else{ //va como hijo der
                node.right = add(node.right, element, path+"/right");
            }
        }
        return node;
    }

    @Override
    public void remove(Object element) throws TreeException {
        if(isEmpty()){
            throw new TreeException("Binary Tree is empty");
        }
        root = remove(root, element);

    }

    private BTreeNode remove(BTreeNode node, Object element){
        if(node!=null){
            if(util.Utility.compare(node.data, element)==0){ //ya encontramos el elemento a eliminar
                //Caso 1. Es un nodo sin hijos. Es una hoja
                if(node.left==null && node.right==null)
                    return null;
                    //Caso 2. El nodo solo tiene un hijo
                else if(node.left!=null && node.right==null){
                    node.left = newPathLabel(node.left, node.path);
                    return node.left; //retorna el subarbol izq y sustituye el nodo actual
                } else if(node.left==null && node.right!=null){
                    node.right = newPathLabel(node.right, node.path);
                    return node.right; //retorna el subarbol derecho y sustituye el nodo actual
                    //Caso 3. El nodo tiene 2 hijos
                }else if(node.left!=null && node.right!=null){
                    Object value = getLeaf(node.right);
                    node.data = value;
                    node.right = removeLeaf(node.right, value);
                }

            }
            //si no hemos encontrado el elemento, debemos buscar por la izq y por la der
            node.left = remove(node.left, element);
            node.right = remove(node.right, element);
        }
        return node; //retorna el nodo modificado
    }

    private BTreeNode newPathLabel(BTreeNode node, String path) {
        if(node!=null){
            node.path = path;
            node.left = newPathLabel(node.left, path+"/left");
            node.right = newPathLabel(node.right, path+"/right");
        }
        return node;
    }

    private BTreeNode removeLeaf(BTreeNode node, Object element) {
        if(node==null)
            return null;
            //Si es una hoja
        else if(node.left==null && node.right==null && util.Utility.compare(node.data, element)==0)
            return null; //es la hoja que andamos buscando y se debe eliminar
        else{
            node.left = removeLeaf(node.left, element);
            node.right = removeLeaf(node.right, element);
        }
        return node;
    }

    private Object getLeaf(BTreeNode node) {
        Object aux;
        if(node==null)
            return null;
            //Si es una hoja
        else if(node.left==null && node.right==null)
            return node.data; //es una hoja
        else{
            aux=getLeaf(node.left);
            if(aux==null)
                return getLeaf(node.right);
        }
        return aux;
    }

    @Override
    public int height(Object element) throws TreeException {
        if(isEmpty()){
            throw new TreeException("Binary Tree is empty");
        }
        return height(root, element, 0);
    }

    private int height(BTreeNode node, Object element, int counter){
        if(node==null)
            return 0;
        else if(util.Utility.compare(node.data, element)==0)
            return counter;
        else //en este caso debe buscar por la izq y por la der
            return Math.max(height(node.left, element, ++counter), height(node.right, element, counter));
    }

    @Override
    public int height() throws TreeException {
        if(isEmpty()){
            throw new TreeException("Binary Tree is empty");
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
        return null;
    }

    //private Object min(BTreeNode node)

    @Override
    public Object max() throws TreeException {
        return null;
    }

    //private Object max(BTreeNode node)

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
        if(isEmpty()) return "Binary tree is empty";
        String result = "Binary Tree Tour...\n";
        try {
            result+="PreOrder: "+preOrder()+"\n";
            result+="InOrder: "+InOrder()+"\n";
            result+="PostOrder: "+postOrder()+"\n";
        } catch (TreeException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
