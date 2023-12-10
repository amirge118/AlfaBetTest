import org.example.rateLimiting.RateLimitingInterceptor;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;

class RateLimitingInterceptorTest {

    @Test
    void testPreHandle_allowedRequest() throws Exception {
        RateLimitingInterceptor interceptor = new RateLimitingInterceptor(1.0); // 1 request per second
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        Object handler = new Object();

        assertTrue(interceptor.preHandle(request, response, handler));
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
    }

    @Test
    void testPreHandle_deniedRequest() throws Exception {
        RateLimitingInterceptor interceptor = new RateLimitingInterceptor(1.0); // 1 request per second
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        Object handler = new Object();

        assertTrue(interceptor.preHandle(request, response, handler));
        assertFalse(interceptor.preHandle(request, response, handler));
        assertEquals(HttpStatus.TOO_MANY_REQUESTS.value(), response.getStatus());
    }
}
