package eg.edu.tanta.recycling.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import eg.edu.tanta.recycling.exceptions.ApiErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class CustomEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper mapper;

  public CustomEntryPoint(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
    final String body = mapper.writeValueAsString(new ApiErrorResponse(HttpStatus.FORBIDDEN.value(), authException.getMessage()));

    response.setStatus(HttpStatus.FORBIDDEN.value());
    response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    response.getWriter().write(body);
  }
}
