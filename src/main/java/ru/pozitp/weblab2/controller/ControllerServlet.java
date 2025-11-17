package ru.pozitp.weblab2.controller;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.pozitp.weblab2.model.PointResult;
import ru.pozitp.weblab2.model.ResultsBean;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "controllerServlet", urlPatterns = "/controller")
public class ControllerServlet extends HttpServlet {
    private static final String SESSION_ERRORS = "flashErrors";
    private static final String SESSION_FORM_VALUES = "formValues";
    private static final String SESSION_LATEST_RESULT = "latestResult";

    @EJB
    private transient ResultsBean resultsBean;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String rawX = req.getParameter("x");
        String rawY = req.getParameter("y");
        String rawR = req.getParameter("r");
        boolean hasCoordinates = (rawX != null && !rawX.isBlank()) || (rawY != null && !rawY.isBlank()) || (rawR != null && !rawR.isBlank());
        if (hasCoordinates) {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Submitting coordinates via GET is not allowed.");
            return;
        }

        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        extractSessionData(req, session);
        setResultsAttributes(req);
        setCacheHeaders(resp);

        if (hasCoordinates(req)) {
            forwardToAreaCheck(req, resp);
            return;
        }

        String action = req.getParameter("action");
        if ("form".equals(action) && session != null) {
            session.removeAttribute(SESSION_LATEST_RESULT);
        }

        if (shouldShowResult(action, session)) {
            forwardToResult(req, resp, session);
            return;
        }

        req.getRequestDispatcher("/WEB-INF/views/form.jsp").forward(req, resp);
    }

    private void extractSessionData(HttpServletRequest req, HttpSession session) {
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
        req.setAttribute(SESSION_FORM_VALUES, formValues);
    }

    private void setResultsAttributes(HttpServletRequest req) {
        List<PointResult> history = resultsBean.getResultsSnapshot();
        req.setAttribute("results", history);
        req.setAttribute(SESSION_LATEST_RESULT, history.isEmpty() ? null : history.get(0));
    }

    private void setCacheHeaders(HttpServletResponse resp) {
        resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        resp.setHeader("Pragma", "no-cache");
        resp.setHeader("Expires", "0");
    }

    private boolean hasCoordinates(HttpServletRequest req) {
        String rawX = req.getParameter("x");
        String rawY = req.getParameter("y");
        String rawR = req.getParameter("r");
        return (rawX != null && !rawX.isBlank()) || (rawY != null && !rawY.isBlank()) || (rawR != null && !rawR.isBlank());
    }

    private void forwardToAreaCheck(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("viaController", Boolean.TRUE);
        req.getRequestDispatcher("/area-check").forward(req, resp);
    }

    private boolean shouldShowResult(String action, HttpSession session) {
        return "result".equals(action) || (session != null && session.getAttribute(SESSION_LATEST_RESULT) != null);
    }

    private void forwardToResult(HttpServletRequest req, HttpServletResponse resp, HttpSession session) throws ServletException, IOException {
        PointResult latest = null;
        if (session != null) {
            Object sr = session.getAttribute(SESSION_LATEST_RESULT);
            if (sr instanceof PointResult latestResult) {
                latest = latestResult;
            }
        }
        req.setAttribute("result", latest);
        req.setAttribute("viaController", Boolean.TRUE);
        req.getRequestDispatcher("/result").forward(req, resp);
    }
}