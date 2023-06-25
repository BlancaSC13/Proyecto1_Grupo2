package util;

import domain.System.*;
import domain.TDA.CircularLinkedList;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Utility {
    private static Random random;    // pseudo-random number generator
    private static long seed;        // pseudo-random number generator seed
    private static CircularLinkedList employeeList;

    // static initializer
    static {
        // this is how the seed was set in Java 1.4
        seed = System.currentTimeMillis();
        random = new Random(seed);
    }



    public static CircularLinkedList getEmployeeList() {
        return employeeList;
    }

    public static int random() {
        return 1 + (int) Math.floor(Math.random() * 99);
    }

    public static int random(int bound) {
        //return 1+random.nextInt(bound);
        return 1 + (int) Math.floor(Math.random() * bound);
    }

    public static String format(double value) {
        return new DecimalFormat("###,###,###,###.##")
                .format(value);
    }

    public static String perFormat(double value) {
        //#,##0.00 '%'
        return new DecimalFormat("#,##0.00'%'")
                .format(value);
    }

    public static String dateFormat(Date value) {
        return new SimpleDateFormat("dd/MM/yyyy")
                .format(value);
    }

    /**
     * a < b return -1
     * a > b return 1
     * a == b return 0
     *
     * @param a
     * @param b
     * @return
     **/
    public static int compare(Object a, Object b) {
        switch (instanceOf(a, b)) {
            case "Integer":
                Integer int1 = (Integer) a;
                Integer int2 = (Integer) b;
                return int1 < int2 ? -1 :
                        int1 > int2 ? 1 : 0; //0==equal
            case "String":
                String str1 = (String) a;
                String str2 = (String) b;
                return str1.compareToIgnoreCase(str2) < 0 ? -1 :
                        str1.compareToIgnoreCase(str2) > 0 ? 1 : 0;
            case "Character":
                Character ch1 = (Character) a;
                Character ch2 = (Character) b;
                return ch1.compareTo(ch2) < 0 ? -1 :
                        ch1.compareTo(ch2) > 0 ? 1 : 0;
            case "Product":
                Product pr1 = (Product) a;
                Product pr2 = (Product) b;
                return pr1.getId() < pr2.getId() ? -1 :
                        pr1.getId() > pr2.getId() ? 1 : 0;
            case "OrderDetail":
                OrderDetail or1 = (OrderDetail) a;
                OrderDetail or2 = (OrderDetail) b;
                return or1.getId() < or2.getId() ? -1 :
                        or1.getId() > or2.getId() ? 1 : 0;
            case "Security":
                Security s1 = (Security) a; Security s2 = (Security) b;
                return s1.toString().compareTo(s2.toString())<0? -1:
                        s1.toString().compareTo(s2.toString())>0?  1: 0;
            case "User":
                User us1 = (User) a; User us2 = (User) b;
                return us1.getIdentificacion().compareTo(us2.getIdentificacion())<0? -1:
                        us1.getIdentificacion().compareTo(us2.getIdentificacion())>0?  1: 0;
            case "Supplier":
                Supplier su1 = (Supplier) a; Supplier su2 = (Supplier) b;
                return su1.getId().compareTo(su2.getId())<0? -1:
                        su1.getId().compareTo(su2.getId())>0?  1: 0;
        }
        return 2; //Unknown
    }

    private static String instanceOf(Object a) {
        if (a instanceof Integer) return "Integer";
        if (a instanceof String) return "String";
        if (a instanceof Character) return "Character";
        return "Unknown"; //desconocido
    }

    public static String instanceOf(Object a, Object b) {
        if (a instanceof Integer && b instanceof Integer) return "Integer";
        if (a instanceof String && b instanceof String) return "String";
        if (a instanceof Character && b instanceof Character) return "Character";
        if (a instanceof Product && b instanceof Product) return "Product";
        if (a instanceof OrderDetail && b instanceof OrderDetail) return "OrderDetail";
        if (a instanceof Security && b instanceof Security) return "Security";
        if (a instanceof User && b instanceof User) return "User";
        if (a instanceof Supplier && b instanceof Supplier) return "Supplier";
        return "Unknown"; //desconocido
    }

    public static String getCountryName() {
        String list[] = {"Argentina", "Australia", "Austria", "Alemania",
                "Belgica", "Bolivia", "Brasil", "Belice",
                "Costa Rica", "Colombia", "Canada", "Chile",
                "Dinamarca", "Ecuador", "Estonia", "El Salvador",
                "Francia", "Finlandia", "Grecia", "Guatemala",
                "Honduras", "Hungria", "India", "Italia",
                "Jamaica", "Japon", "Mexico", "Marruecos",
                "USA", "Nigeria", "Panama", "Portugal",
        };
        //return list[(int) (Math.random() * 31 - 1)];
        return list[random(31)];
    }

    public static char getAlphabet() {
        char alfabeto[] = new char[26];
        int cont = 0;
        for (char i = 'a'; i <= 'z'; i++)
            alfabeto[cont++] = i;
        return alfabeto[(int) (Math.random() * 25 - 1)];
    }

    public static int getAge(Date date) {
        Date actual = new Date();
        int mesActual = actual.getMonth();
        int diaActual = actual.getDay();
        int year = actual.getYear();
        int diferencia = year - (date.getYear());
        // Si la diferencia entre los años es distinta de 0
        if (diferencia != 0) {
            //Si el mes actual es menor que el mes recibido le resta 1
            //Si el mes es igual y el dia que recibe es mayor al actual, se resta 1
            if (mesActual <= (date.getMonth())) {
                if (mesActual == (date.getMonth())) {
                    if (date.getDate() > diaActual) {
                        diferencia--;
                    }
                } else {
                    diferencia--;
                }
            }
        }

        return diferencia;
    }

    public static int random(int lowBound, int highBound) {
        int value = 0;
        do {
            value = lowBound + (int) Math.floor((Math.random() * highBound));
        } while (!isBetween(value, lowBound, highBound));
        return value;
    }

    private static boolean isBetween(int value, int lowBound, int highBound) {
        return lowBound <= value && value <= highBound;
    }
    public static boolean validatePassword(String password) {
        boolean validate = false;
        int numeros = 0, mayusculas = 0, minusculas = 0;
        if (password.length() >= 8) {
            for (int i = 0; i < password.length(); i++) {
                char letra = password.charAt(i); //Revisar uno a uno cada caracter para saber si es un número

                if (letra >= 49 && letra <= 57) //Valida el número con las posiciones en el ASCII
                    numeros++;

                if (letra >= 65 && letra <= 90)//Valida las mayúsculas con las posiciones en el ASCII
                    mayusculas++;

                if (letra >= 97 && letra <= 122)
                    minusculas++;
            }//End for

            if (numeros > 0 && mayusculas > 0 && minusculas > 0)
                validate = true;
        }//End if
        return validate;
    }
    public static String fecha() {
        Date fechaHoraActual = new Date();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
        String fechaActual = formatoFecha.format(fechaHoraActual);
        String horaActual = formatoHora.format(fechaHoraActual);
        // Construir el string final
        String result = fechaActual + ", a las " + horaActual;
        return result;
    }

}
