package org.danil.lab20.fetcher;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CachedFilter implements Filter {
    private final Logger logger = LoggerFactory.getLogger(CachedFilter.class);

    protected record CachedKey(String url) {
    }
    protected record CachedValue(int statusCode, Map<String, List<String>> headers, byte[] body) {
    }
    private final Map<CachedKey, CachedValue> map = new WeakHashMap<>();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(response instanceof HttpServletResponse httpResponse && request instanceof HttpServletRequest httpRequest)) {
            chain.doFilter(request, response);
            return;
        }

        final var key = new CachedKey(httpRequest.getRequestURL().toString());

        if(map.containsKey(key)) {
            final var value = map.get(key);
            httpResponse.setStatus(value.statusCode());
            value.headers().forEach((k, v) -> v.forEach(h -> httpResponse.setHeader(k, h)));
            httpResponse.getOutputStream().write(value.body());
            logger.info("used cached value for {}", httpRequest.getRequestURL().toString());
            return;
        }

        final var responseWrapper = new ContentCachingResponseWrapper(httpResponse);
        chain.doFilter(request, responseWrapper);


        if (responseWrapper.getStatus() / 100 != 2) {
            responseWrapper.copyBodyToResponse();
            return;
        }


        Map<String, List<String>> headersMap = responseWrapper.getHeaderNames().stream()
                .collect(Collectors.toMap(Function.identity(), h -> new ArrayList<>(responseWrapper.getHeaders(h))));

        headersMap.put("X-Cached-At", List.of(new Date().toString()));
//        logger.info("body: {}", new String(bodyBytes, StandardCharsets.UTF_8));
        final var bodyBytes = responseWrapper.getContentAsByteArray();
        final var value = new CachedValue(responseWrapper.getStatus(), headersMap, bodyBytes);
        map.put(key, value);
        logger.info("request to {} is cached at {}", httpRequest.getRequestURL().toString(), new Date());

        responseWrapper.copyBodyToResponse();
    }
}
