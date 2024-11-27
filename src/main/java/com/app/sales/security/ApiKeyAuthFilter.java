package com.app.sales.security;

import com.app.sales.exception.InvalidApiKeyException;
import com.app.sales.exception.MissingApiKeyException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.io.IOException;

public class ApiKeyAuthFilter extends AbstractPreAuthenticatedProcessingFilter {

    private final String principalRequestHeader;
    private final String principalRequestValue;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    public ApiKeyAuthFilter(String principalRequestHeader, String principalRequestValue,
                            AuthenticationEntryPoint authenticationEntryPoint) {
        this.principalRequestHeader = principalRequestHeader;
        this.principalRequestValue = principalRequestValue;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    public Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        return request.getHeader(principalRequestHeader);
    }

    @Override
    public Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return "N/A";
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String apiKey = httpRequest.getHeader(principalRequestHeader);

        try {
            if (apiKey == null) {
                throw new MissingApiKeyException("API key is missing.");
            }

            if (!principalRequestValue.equals(apiKey)) {
                throw new InvalidApiKeyException("Invalid API key provided.");
            }

            // Proceed with authentication
            Authentication authentication = new PreAuthenticatedAuthenticationToken(apiKey, null);
            authentication.setAuthenticated(true);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } catch (AuthenticationException authException) {
            // Clear the context and delegate to the failure handler
            SecurityContextHolder.clearContext();
            // Delegate to AuthenticationEntryPoint
            authenticationEntryPoint.commence(httpRequest, (HttpServletResponse) response, authException);
        }
    }
}