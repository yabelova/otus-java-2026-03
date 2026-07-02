package ru.otus.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.Properties;
import java.util.function.Consumer;

public final class HibernateUtils {

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    private HibernateUtils() {
    }

    public static SessionFactory buildSessionFactory(Class<?>... annotatedClasses) {
        return buildSessionFactory(new Properties(), annotatedClasses);
    }

    public static SessionFactory buildSessionFactory(Properties overrides, Class<?>... annotatedClasses) {
        Configuration configuration = new Configuration().configure(HIBERNATE_CFG_FILE);
        overrides.forEach((k, v) -> configuration.setProperty((String) k, (String) v));

        for (var cls : annotatedClasses) {
            configuration.addAnnotatedClass(cls);
        }

        return configuration.buildSessionFactory();
    }

    public static void doInSessionWithTransaction(SessionFactory sessionFactory, Consumer<Session> action) {
        try (Session s = sessionFactory.openSession()) {
            Transaction t = s.getTransaction();
            t.begin();
            try {
                action.accept(s);
                t.commit();
            } catch (Exception e) {
                t.rollback();
                throw e;
            }
        }
    }
}