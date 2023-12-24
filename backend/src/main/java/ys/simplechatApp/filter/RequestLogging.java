package ys.simplechatApp.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;

@Slf4j
// 웹서버로 들어온 HTTP Request 로깅 필터
public class RequestLogging implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletResponse  = (HttpServletRequest) servletRequest;
        String requestData = extractRequestData(httpServletResponse);

        log.info("Request Info: {}", requestData);


        filterChain.doFilter(servletRequest, servletResponse);
    }

    private String extractRequestData(HttpServletRequest request) throws IOException {
        HttpServletRequestWrapper wrappedRequest = new HttpServletRequestWrapper(request);
        StringBuilder requestData = new StringBuilder();

        // 요청 라인 추가
        requestData.append(request.getMethod())
                .append(" ")
                .append(request.getRequestURI())
                .append("\n");

        // 헤더 추가
        Enumeration<String> headerNames = request.getHeaderNames();
        while(headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            requestData.append(headerName).append(": ").append(request.getHeader(headerName)).append("\n");
        }

        // 바디 추가
        StringBuilder requestBody = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(wrappedRequest.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            requestBody.append(line).append("\n");
        }
        requestData.append("Body: ").append(requestBody.toString());

        return requestData.toString();
    }
}