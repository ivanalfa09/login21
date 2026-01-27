package com.login21.aop;

import com.login21.entity.AccessCredential;
import com.login21.entity.User;
import com.login21.exception.InvalidPasswordException;
import com.login21.exception.UserNotFoundException;
import com.login21.repository.AccessCredentialRepository;
import com.login21.service.LogService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class AuthAuditAspect {

    private final LogService logService;
    private final AccessCredentialRepository credentialRepository;

    public AuthAuditAspect(
            LogService logService,
            AccessCredentialRepository credentialRepository
    ) {
        this.logService = logService;
        this.credentialRepository = credentialRepository;
    }


    // REGISTER EXITOSO

    @AfterReturning(
            pointcut = "execution(* com.login21.service.UserRegistrationService.register(..))",
            returning = "user"
    )
    public void logRegisterSuccess(User user) {

        logService.log(
                user.getId(),
                "REGISTER",
                "USER",
                user.getId(),
                "Registro de nuevo usuario",
                getClientIp()
        );
    }


    // LOGIN EXITOSO

    @AfterReturning(
            pointcut = "execution(* com.login21.service.AuthService.login(..))",
            returning = "cred"
    )
    public void logLoginSuccess(AccessCredential cred) {

        User user = cred.getUserEntity(); // 👈 relación correcta

        logService.log(
                user.getId(),
                "LOGIN_SUCCESS",
                "USER",
                user.getId(),
                "Inicio de sesión exitoso: " + cred.getUser(),
                getClientIp()
        );
    }


    // LOGIN FALLIDO

    @AfterThrowing(
            pointcut = "execution(* com.login21.service.AuthService.login(..))",
            throwing = "ex"
    )
    public void logLoginFailure(JoinPoint joinPoint, Exception ex) {

        String username = (String) joinPoint.getArgs()[0];

        //  Usuario no existe
        if (ex instanceof UserNotFoundException) {

            logService.log(
                    null,
                    "LOGIN_FAILED",
                    "USER",
                    null,
                    "Login fallido - usuario no existe: " + username,
                    getClientIp()
            );
            return;
        }

        //  Password incorrecta (usuario sí existe)
        if (ex instanceof InvalidPasswordException) {

            credentialRepository.findByUser(username).ifPresent(cred -> {

                User user = cred.getUserEntity();

                // intento fallido
                logService.log(
                        user.getId(),
                        "LOGIN_FAILED",
                        "USER",
                        user.getId(),
                        "Login fallido - contraseña incorrecta",
                        getClientIp()
                );

                //  usuario bloqueado
                if (cred.getFailedAttempts() >= 3) {
                    logService.log(
                            user.getId(),
                            "USER_BLOCKED",
                            "USER",
                            user.getId(),
                            "Usuario bloqueado por intentos fallidos",
                            getClientIp()
                    );
                }
            });
        }
    }


    // IP CLIENTE

    private String getClientIp() {
        ServletRequestAttributes attrs =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attrs == null) return "SYSTEM";

        HttpServletRequest request = attrs.getRequest();
        return request.getRemoteAddr();
    }
}

    /*Para produccion*/
    /*
    private String getClientIp() {
    ServletRequestAttributes attrs =
            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

    if (attrs == null) return "SYSTEM";

    HttpServletRequest request = attrs.getRequest();

    String xff = request.getHeader("X-Forwarded-For");
    if (xff != null && !xff.isEmpty()) {
        return xff.split(",")[0].trim();
    }

    return request.getRemoteAddr();
}
*/

