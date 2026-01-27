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

    public AuthAuditAspect(LogService logService, AccessCredentialRepository credentialRepository) {
        this.logService = logService;
        this.credentialRepository = credentialRepository;
    }

    /*Registro Exitoso*/
    @AfterReturning(
            pointcut = "execution(* com.login21.service.UserRegistrationService.register(..))",
            returning = "result"
    )
    public void logRegisterSuccess(User result) {

        logService.log(
                result.getId(),
                "REGISTER",
                result.getAccessCredential().getUser(),
                result.getId(),
                "Registro de nuevo usuario",
                getClientIp()
        );
    }

    /*Login Exitoso*/
    @AfterReturning(
            pointcut = "execution(* com.login21.service.AuthService.login(..))",
            returning = "result"
    )
    public void logLoginSuccess(AccessCredential result) {


        logService.log(
                result.getId(), // opcional si no tienes idUser directo
                "LOGIN_SUCCESS",
                result.getUser(),
                result.getId(), // opcional si no tienes idUser directo,
                "Inicio de sesión exitoso: " + result.getUser(),
                getClientIp()
        );
    }

    /*Login Fallido*/
    @AfterThrowing(
            pointcut = "execution(* com.login21.service.AuthService.login(..))",
            throwing = "ex"
    )
    public void logLoginFailure(JoinPoint joinPoint, Exception ex) {

        String username = (String) joinPoint.getArgs()[0];

        if (ex instanceof UserNotFoundException) {

            // Usuario NO existe
            logService.log(
                    null,
                    "LOGIN_FAILED",
                    "ACCESS_CREDENTIAL",
                    null,
                    "Login fallido - usuario no existe: " + username,
                    getClientIp()
            );

        } else if (ex instanceof InvalidPasswordException) {

            // Usuario SÍ existe → buscamos su ID
            credentialRepository.findByUser(username)
                    .ifPresent(cred -> logService.log(
                            cred.getId(),
                            "LOGIN_FAILED",
                            "ACCESS_CREDENTIAL",
                            cred.getId(),
                            "Login fallido - contraseña incorrecta",
                            getClientIp()
                    ));
        }
        else if (ex instanceof InvalidPasswordException) {

            credentialRepository.findByUser(username).ifPresent(cred -> {
                if (cred.getFailedAttempts() >= 3) {
                    logService.log(
                            cred.getId(),
                            "USER_BLOCKED",
                            "ACCESS_CREDENTIAL",
                            cred.getId(),
                            "Usuario bloqueado por intentos fallidos",
                            getClientIp()
                    );
                }
            });
        }

    }


    /*IP CLIENTE*/
    private String getClientIp() {
        ServletRequestAttributes attrs =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attrs == null) return "SYSTEM";

        HttpServletRequest request = attrs.getRequest();
        return request.getRemoteAddr();
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
}
