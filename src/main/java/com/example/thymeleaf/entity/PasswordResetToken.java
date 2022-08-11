package com.example.thymeleaf.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "verifyPassword_token")
public class PasswordResetToken {

        //expiration time = 10 minutes
        private static final int EXPIRATION_TIME = 10;

        @Id
        @SequenceGenerator(name = "password_reset_token_sequence", sequenceName = "password_reset_token_sequence", allocationSize = 1)
        @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "password_reset_token_sequence")
        private Long Id;
        private String token;
        private Date expiresAt;
        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(
                name = "FK_APP_USER_PASSWORD_RESET_TOKEN"
        ))
        private App_Users app_users;

        public PasswordResetToken(String token) {
            this.token = token;
            this.expiresAt = expirationTime(EXPIRATION_TIME);
        }

        public PasswordResetToken(String token, App_Users app_users) {
            this.token = token;
            this.expiresAt = expirationTime(EXPIRATION_TIME);
            this.app_users = app_users;
        }

        private Date expirationTime(int expirationTime) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(new Date().getTime());
            calendar.add(calendar.MINUTE, expirationTime);

            return new Date(calendar.getTime().getTime());
        }

}
