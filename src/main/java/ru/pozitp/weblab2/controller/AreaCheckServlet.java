package ru.pozitp.weblab2.controller;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.pozitp.weblab2.listener.ApplicationStartupListener;
import ru.pozitp.weblab2.model.PointRequest;
import ru.pozitp.weblab2.model.PointResult;
import ru.pozitp.weblab2.model.ResultsBean;
import ru.pozitp.weblab2.service.GeometryCalculator;
import ru.pozitp.weblab2.service.InputValidator;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.OffsetDateTime;

@WebServlet(name = "areaCheckServlet", urlPatterns = "/area-check")
public class AreaCheckServlet extends HttpServlet {
    private static final String SESSION_ERRORS = "flashErrors";
    private static final String SESSION_FORM_VALUES = "formValues";

    private static final MathContext TIME_CONTEXT = new MathContext(10);

    private transient InputValidator validator;
    private transient GeometryCalculator calculator;
    private transient ResultsBean resultsBean;

    private static BigDecimal toMillis(long nanos) {
        BigDecimal elapsed = new BigDecimal(nanos, TIME_CONTEXT);
        return elapsed.divide(BigDecimal.valueOf(1_000_000L), TIME_CONTEXT);
    }

    @Override
    public void init() throws ServletException {
        super.init();
        this.validator = new InputValidator();
        this.calculator = new GeometryCalculator();
        ServletContext context = getServletContext();
        Object attribute = context.getAttribute(ApplicationStartupListener.RESULTS_BEAN_KEY);
        if (attribute instanceof ResultsBean bean) {
            this.resultsBean = bean;
        } else {
            this.resultsBean = new ResultsBean();
            context.setAttribute(ApplicationStartupListener.RESULTS_BEAN_KEY, this.resultsBean);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "GET is not allowed for area verification.");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(true);
        long started = System.nanoTime();

        InputValidator.ValidationResult validation = validator.validate(req.getParameter("x"), req.getParameter("y"), req.getParameter("r"));

        session.setAttribute(SESSION_FORM_VALUES, validation.getNormalizedValues());

        if (validation.hasErrors()) {
            session.setAttribute(SESSION_ERRORS, validation.getErrors());
            resp.sendRedirect(req.getContextPath() + "/controller");
            return;
        }

        PointRequest payload = validation.getPayload();
        boolean hit = calculator.isHit(payload);
        BigDecimal processingTime = toMillis(System.nanoTime() - started);
        PointResult result = new PointResult(payload.x(), payload.y(), payload.r(), hit, OffsetDateTime.now(), processingTime);

        resultsBean.addResult(result);

        session.setAttribute("latestResult", result);

        resp.sendRedirect(req.getContextPath() + "/result");
    }
}