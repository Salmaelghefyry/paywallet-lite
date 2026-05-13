package com.paylogic.paywalletlite;

import com.paylogic.paywalletlite.config.root.RootConfig;
import com.paylogic.paywalletlite.config.web.WebConfig;
import jakarta.servlet.ServletException;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.File;

public class PayWalletLiteApplication {

    public static void main(String[] args) throws LifecycleException, ServletException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        tomcat.getConnector();

        // Contexte déployé à "/pwl"
        String contextPath = "/pwl";
        String baseDir = new File("src/main/webapp").getAbsolutePath();
        Context ctx = tomcat.addContext(contextPath, baseDir);

        // Contexte Spring MVC
        AnnotationConfigWebApplicationContext springCtx =
                new AnnotationConfigWebApplicationContext();
        springCtx.register(RootConfig.class, WebConfig.class);
        springCtx.setServletContext(ctx.getServletContext());
        springCtx.refresh();                 // ← INDISPENSABLE

        // DispatcherServlet
        DispatcherServlet dispatcher = new DispatcherServlet(springCtx);
        Tomcat.addServlet(ctx, "dispatcher", dispatcher);
        ctx.addServletMappingDecoded("/", "dispatcher");

        tomcat.start();
        System.out.println("Tomcat démarré sur http://localhost:8080/pwl/");
        tomcat.getServer().await();
    }
}