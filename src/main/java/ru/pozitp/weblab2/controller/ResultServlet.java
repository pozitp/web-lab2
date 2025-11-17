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

@WebServlet(name = "resultServlet", urlPatterns = "/result")
public class ResultServlet extends HttpServlet {
    private static final String SESSION_LATEST_RESULT = "latestResult";

    @EJB
    private transient ResultsBean resultsBean;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object via = req.getAttribute("viaController");
        boolean allowed = via instanceof Boolean b && b;
        if (!allowed) {
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Direct access to result is not allowed. Use the front controller.");
            return;
        }

        HttpSession session = req.getSession(false);
        PointResult latestResult = null;

        if (session != null) {
            Object storedResult = session.getAttribute(SESSION_LATEST_RESULT);
            if (storedResult instanceof PointResult latest) {
                latestResult = latest;
            }
        }

        List<PointResult> history = resultsBean.getResultsSnapshot();

        req.setAttribute("result", latestResult);
        req.setAttribute("results", history);

        resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        resp.setHeader("Pragma", "no-cache");
        resp.setHeader("Expires", "0");

        req.getRequestDispatcher("/WEB-INF/views/result.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "POST is not allowed for result page.");
    }
}