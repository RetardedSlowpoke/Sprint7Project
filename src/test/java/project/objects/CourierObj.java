package project.objects;
import com.github.javafaker.Faker;

public class CourierObj {

        private String login;
        private String password;

        public CourierObj(String login, String password, String firstName) {
            this.login = login;
            this.password = password;
        }

        public static CourierObj random() {
            Faker faker = new Faker();
            return new CourierObj(
                    faker.name().username(),
                    faker.internet().password(),
                    faker.name().firstName());
        }

        public String getLogin() {
            return login;
        }

        public String getPassword() {
            return password;
        }

    }

