package sn.esmt.mpisi2.controller;


import org.springframework.test.web.servlet.request.RequestPostProcessor;
        import org.springframework.mock.web.MockHttpServletRequest;
        import org.springframework.security.web.csrf.CsrfToken;
        import org.springframework.security.web.csrf.DefaultCsrfToken;

public class CsrfRequestPostProcessor implements RequestPostProcessor {

    private CsrfToken csrfToken;

    public CsrfRequestPostProcessor(CsrfToken csrfToken) {
        this.csrfToken = csrfToken;
    }

    @Override
    public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
        request.setAttribute(CsrfToken.class.getName(), this.csrfToken);
        request.setAttribute(this.csrfToken.getParameterName(), this.csrfToken.getToken());
        return request;
    }
}
