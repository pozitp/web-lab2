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

@WebServlet(name = "resultServlet", urlPatterns = "/result")
public class ResultServlet extends HttpServlet {
    private static final String SESSION_LATEST_RESULT = "latestResult";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        PointResult latestResult = null;

        if (session != null) {
            Object storedResult = session.getAttribute(SESSION_LATEST_RESULT);
            if (storedResult instanceof PointResult) {
                latestResult = (PointResult) storedResult;
                session.removeAttribute(SESSION_LATEST_RESULT);
            }
        }

        ServletContext context = getServletContext();
        ResultsBean resultsBean = (ResultsBean) context.getAttribute(ApplicationStartupListener.RESULTS_BEAN_KEY);
        List<PointResult> history = resultsBean != null ? resultsBean.getResultsSnapshot() : List.of();

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