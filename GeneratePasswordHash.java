import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeneratePasswordHash {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String rawPassword = "admin123"; // change if you want
        String hash = encoder.encode(rawPassword);

        System.out.println("RAW PASSWORD  : " + rawPassword);
        System.out.println("BCrypt HASH   : " + hash);
    }
}
