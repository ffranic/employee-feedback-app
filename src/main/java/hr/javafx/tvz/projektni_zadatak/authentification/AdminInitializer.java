package hr.javafx.tvz.projektni_zadatak.authentification;

import hr.javafx.tvz.projektni_zadatak.exceptions.FileAccessException;
import org.mindrot.jbcrypt.BCrypt;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class AdminInitializer {

    public static void main(String[] args) throws FileAccessException {

        Path file = Path.of("userData/employees.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file.toFile(), true))) {
            int id = 3;
            String userName = "markoMarkic@teampulse.hr";
            String password = "MarkOMarkiC";
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            String role = "EMPLOYEE";

            String line = id + "," + userName + "," + hashedPassword + "," + role;

            writer.write(line);
            writer.newLine();

            System.out.println("Admin korisnik uspješno dodan u datoteku.");
        } catch (IOException exc) {
            throw new FileAccessException("File not found");
        }
    }
}
