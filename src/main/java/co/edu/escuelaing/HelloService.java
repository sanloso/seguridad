package co.edu.escuelaing;

import spark.Request;
import spark.Response;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import static spark.Spark.*;

public class HelloService {

    static String password = "helloworld";

    public static void main( String[] args )
    {
        //API: secure(keystoreFilePath, keystorePassword, truststoreFilePath,truststorePassword);
        secure("keystores/ecikeystore.p12", "123456", null, null);
        port(getPort());
        get("/hello", (req, res) -> "Hello World");
        get("/login", (req, res) -> validationPassw(req, res));
    }

    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567;
    }

    private static String obtenerHASHMD5(String textoEntrada) {
        if (textoEntrada.equals("")) {
            return "";
        } else {
            try {
                MessageDigest HashMD5 = MessageDigest.getInstance("MD5");
                byte[] mensajeMatriz = HashMD5.digest(textoEntrada.getBytes());
                BigInteger numero = new BigInteger(1, mensajeMatriz);
                StringBuilder hashMD5Salida = new StringBuilder(numero.toString(16));

                while (hashMD5Salida.length() < 32) {
                    hashMD5Salida.insert(0, "0");
                }
                return hashMD5Salida.toString();
            } catch (NoSuchAlgorithmException e) {
                System.out.println("Error al obtener el hash: " + e.getMessage());
                return "";
            }
        }
    }

    private static String validationPassw(Request req, Response res){
        String contraseñaURL = obtenerHASHMD5(req.queryParams("password"));
        String passwordHash = obtenerHASHMD5(password);
        if (contraseñaURL.equals(passwordHash)){
            Date fecha = new Date();
           return "Ha ingresado de manera correcta a las: " + fecha.toString();
        }else{
            return "Verifique su contraseña";
        }
    }
}
