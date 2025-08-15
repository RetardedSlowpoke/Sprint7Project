package Project.CourierTests;
import java.util.concurrent.ThreadLocalRandom;

public class CourierObj {

        private String login;
        private String password;

        public CourierObj(String login, String password, String firstName) {
            this.login = login;
            this.password = password;
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

    }

