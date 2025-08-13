package Project.CourierTests;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

public class CourierObj {

        private String login;
        private String password;
        private String firstName;

        public CourierObj(String login, String password, String firstName) {
            this.login = login;
            this.password = password;
            this.firstName = firstName;
        }

        public static CourierObj random() {
            return new CourierObj("login_" + ThreadLocalRandom.current().nextInt(10000, 99999), "pass1234", "Ivanov");
        }

        public String getLogin() {
            return login;
        }

        public String getPassword() {
            return password;
        }

        public String getFirstName() {
            return firstName;
        }
    }

