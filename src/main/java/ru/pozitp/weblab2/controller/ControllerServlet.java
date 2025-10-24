package ru.pozitp.weblab2.controller;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.pozitp.weblab2.listener.ApplicationStartupListener;
import ru.pozitp.weblab2.model.PointResult;
import ru.pozitp.weblab2.model.ResultsBean;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "controllerServlet", urlPatterns = "/controller")
public class ControllerServlet extends HttpServlet {
    private static final String SESSION_ERRORS = "flashErrors";
    private static final String SESSION_FORM_VALUES = "formValues";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "GET is not allowed for controller.");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        List<String> errors = List.of();
        Map<String, String> formValues = Map.of();
        if (session != null) {
            Object storedErrors = session.getAttribute(SESSION_ERRORS);
            if (storedErrors instanceof List<?>) {
                @SuppressWarnings("unchecked") List<String> casted = (List<String>) storedErrors;
                errors = casted;
            }
            session.removeAttribute(SESSION_ERRORS);

            Object storedValues = session.getAttribute(SESSION_FORM_VALUES);
            if (storedValues instanceof Map<?, ?> map) {
                @SuppressWarnings("unchecked") Map<String, String> casted = (Map<String, String>) map;
                formValues = casted;
            }
        }
        req.setAttribute("errors", errors);
        req.setAttribute("formValues", formValues);

        ServletContext context = getServletContext();
        ResultsBean resultsBean = (ResultsBean) context.getAttribute(ApplicationStartupListener.RESULTS_BEAN_KEY);
        List<PointResult> history = resultsBean != null ? resultsBean.getResultsSnapshot() : List.of();
        req.setAttribute("results", history);
        req.setAttribute("latestResult", history.isEmpty() ? null : history.get(0));

        resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        resp.setHeader("Pragma", "no-cache");
        resp.setHeader("Expires", "0");

        req.getRequestDispatcher("/WEB-INF/views/form.jsp").forward(req, resp);
    }
}