package domain.System;
import domain.TDA.SinglyLinkedList;
import java.util.ArrayList;
import java.util.List;

public class User {
     private static SinglyLinkedList singlyLinkedList = new SinglyLinkedList();
    private String identificacion;
    private String nombre;
    private String telefono;
    private String correo;
    private String direccion;
    private String rol;
    
    public User(String identificacion, String nombre, String correo, String direccion, String rol) {
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.correo = correo;
        this.direccion = direccion;
        this.rol = rol;
    }
    public User(String identificacion){
        this.identificacion = identificacion;
    }

    @Override
    public String toString() {
        return "User{" +
                "identificacion='" + identificacion + '\'' +
                ", nombre='" + nombre + '\'' +

                ", correo='" + correo + '\'' +
                ", direccion='" + direccion + '\'' +
                ", rol='" + rol + '\'' +
                '}';
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
