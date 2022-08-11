package com.example.thymeleaf.entity;

import com.example.thymeleaf.entity.enums.App_User_Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "user_details", uniqueConstraints = @UniqueConstraint(name = "unique_email", columnNames = "Email_Address"))
@NoArgsConstructor
public class App_Users {

    @Id
    @SequenceGenerator(name = "user_data_sequence", sequenceName = "user_data_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "user_data_sequence")
    private Long id;
    @Column(name = "First_Name")
    private String firstName;
    @Column(name = "Last_Name")
    private String lastName;
    @Column(name = "Email_Address")
    private String email;
    private String password;
    @Enumerated
    private App_User_Role app_user_role;

    private boolean isEnabled = false;

    public App_Users(String firstName, String lastName,
                     String email, String password,
                     App_User_Role app_user_role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.app_user_role = app_user_role;
    }

}
