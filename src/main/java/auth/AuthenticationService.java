package auth;

import java.util.ArrayList;
import java.util.List;

public class AuthenticationService {
    List<User> userList;
    final String USERNAME = "admin";
    final String PASSWORD = "solo";

    public AuthenticationService() {
        userList = new ArrayList<>();
        userList.add(new User(USERNAME, Role.ADMIN));
        userList.add(new User("Charlie", Role.USER));
        userList.add(new User("Eliza", Role.USER));
        userList.add(new User("Eliza", Role.USER));
    }

    public User authenticate(String username, String password) {
        if (USERNAME.equals(username)) {
            if (PASSWORD.equals(password)) {
                return new User(username, Role.ADMIN);
            }
            return null;
        }

        return findUser(username);
    }

    public User findUser(String username) {
    return userList.stream()
            .filter(user -> user.getUsername().equals(username))
            .findFirst()
        .orElse(null);
    }
}
