package ru.home.persist.user;

import ru.home.service.user.UserRepr;

import javax.persistence.*;

@Entity
@Table(name = "users")
@NamedQueries({
        @NamedQuery(name = "userByName", query = "from User u where u.username=:username"),
        @NamedQuery(name = "allUsers", query = "from User")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@NotEmpty // эта аннотация удалена, так как для отображения, где она используется создан отдельный DTO-класс
    @Column(length = 128, unique = true, nullable = false)
    private String username;

    //@NotEmpty
    @Column(length = 512, nullable = false)
    private String password;

    //@NotEmpty
    //@Transient // костыльный способ (аннотация для случая, когда поле не должно попадать в БД)
    private String matchingPassword;

    @Column
    private String email;

    public User() {
    }

    public User(UserRepr user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
    }

    public User(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
