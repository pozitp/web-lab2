package ru.pozitp.weblab2.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import ru.pozitp.weblab2.model.ResultsBean;

@WebListener
public final class ApplicationStartupListener implements ServletContextListener {
    public static final String RESULTS_BEAN_KEY = "resultsBean";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        synchronized (context) {
            if (context.getAttribute(RESULTS_BEAN_KEY) == null) {
                context.setAttribute(RESULTS_BEAN_KEY, new ResultsBean());
            }
        }
    }
}