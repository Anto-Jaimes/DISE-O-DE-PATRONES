package pe.edu.utp.proyecto.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@org.springframework.lang.NonNull HttpServletRequest request, @org.springframework.lang.NonNull HttpServletResponse response, @org.springframework.lang.NonNull Object handler) throws Exception {
        HttpSession session = request.getSession();
        if (session.getAttribute("usuarioLogueado") != null) {
            return true;
        }
        String uri = request.getRequestURI();
        if (uri.equals("/") || uri.equals("/login") || uri.equals("/apuestas/registro/guardar") || uri.startsWith("/css/") || uri.startsWith("/js/") || uri.startsWith("/img/") || uri.startsWith("/h2-console")) {
            return true;
        }
        response.sendRedirect("/login");
        return false;
    }
}
