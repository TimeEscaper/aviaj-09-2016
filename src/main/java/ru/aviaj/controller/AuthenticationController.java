package ru.aviaj.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.aviaj.database.exception.DbException;
import ru.aviaj.model.ErrorList;
import ru.aviaj.model.ErrorType;
import ru.aviaj.model.UserProfile;
import ru.aviaj.service.AccountService;
import ru.aviaj.service.SessionService;

import javax.servlet.http.HttpSession;

@SuppressWarnings("unused")
@RestController
public class AuthenticationController {

    @Autowired
    private  AccountService accountService;

    @Autowired
    private  SessionService sessionService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);

    @RequestMapping(path = "/api/auth/login", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity login(@RequestBody UserRequest body, HttpSession httpSession) {

        try {
            final long loginedUserId = sessionService.getUserIdBySession(httpSession.getId());
            if (loginedUserId != 0)
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                        new ErrorList(ErrorType.ALREADYLOGIN)
                );
        } catch (DbException e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorList(ErrorType.DBERROR)
            );
        }

        final UserProfile requestUser;
        try {
            requestUser = accountService.getUserByLogin(body.getLogin());
            if (requestUser == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ErrorList(ErrorType.NOLOGIN)
                );
            }
        } catch (DbException e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorList(ErrorType.DBERROR)
            );
        }

        final String truePasswordHash = requestUser.getPassword();
        final String bodyPassword = body.getPassword();
        final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (!encoder.matches(bodyPassword, truePasswordHash)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ErrorList(ErrorType.WRONGPASSWORD)
            );
        }

        try {
            final boolean success = sessionService.addSession(httpSession.getId(), requestUser.getId());
            if (!success) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                        new ErrorList(ErrorType.UNEXPECTEDERROR)
                );
            }
        } catch (DbException e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorList(ErrorType.DBERROR)
            );
        }

        return ResponseEntity.ok(new UserResponse(requestUser));

    }

    @RequestMapping(path = "/api/auth/authenticate", method = RequestMethod.GET)
    public ResponseEntity authenticate(HttpSession httpSession) {

        final long loginedUserId;
        try {
            loginedUserId = sessionService.getUserIdBySession(httpSession.getId());
            if (loginedUserId == 0) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        new ErrorList(ErrorType.NOTLOGINED)
                );
            }
        } catch (DbException e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorList(ErrorType.DBERROR)
            );
        }

        try {
            final UserProfile loginedUser = accountService.getUserById(loginedUserId);
            if (loginedUser == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                        new ErrorList(ErrorType.UNEXPECTEDERROR)
                );
            }

            return ResponseEntity.ok(new UserResponse(loginedUser));

        } catch (DbException e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorList(ErrorType.DBERROR)
            );
        }
    }

    @RequestMapping(path = "/api/auth/logout", method = RequestMethod.POST)
    public ResponseEntity logout(HttpSession httpSession) {


        final String sessionId = httpSession.getId();

        try {
            final long loginedUserId = sessionService.getUserIdBySession(sessionId);
            if (loginedUserId == 0) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        new ErrorList(ErrorType.NOTLOGINED)
                );
            }
        } catch (DbException e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorList(ErrorType.DBERROR)
            );
        }

        try {
            final boolean success = sessionService.removeSession(sessionId);
            if (!success) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                        new ErrorList(ErrorType.UNEXPECTEDERROR)
                );
            }
        } catch (DbException e) {
            LOGGER.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorList(ErrorType.DBERROR)
            );
        }

        return ResponseEntity.ok("{\"success\": \"true\"}");

    }

    @SuppressWarnings("unused")
    public static final class UserRequest {
        private String login;
        private String password;

        private UserRequest() { }

        private UserRequest(String login, String email, String password) {
            this.login = login;
            this.password = password;
        }

        public String getLogin() {
            return login;
        }

        public String getPassword() {
            return password;
        }
    }

    @SuppressWarnings("unused")
    public static final class UserResponse {
        private long id;
        private String login;
        private String email;
        private long rating;

        private UserResponse() { }

        private UserResponse(UserProfile user) {
            this.id = user.getId();
            this.login = user.getLogin();
            this.email = user.getEmail();
            this.rating = user.getRating();
        }

        public long getId() {
            return id;
        }

        public String getLogin() {
            return login;
        }

        public String getEmail() { return email; }

        public long getRating() {
            return rating;
        }
    }

}



