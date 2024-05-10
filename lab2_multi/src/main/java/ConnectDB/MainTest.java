package ConnectDB;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import Entity.*;

public class MainTest {
    private static SessionFactory sessionFactory;

    public static void main(String[] args) {
        sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(InfoPlayer.class)
                .buildSessionFactory();

        String info = "5";
        saveInfoPlayer(new InfoPlayer("Trolozor", "123123", info));

        InfoPlayer infoPlayer = getInfoPlayerById(1);
        System.out.println("InfoPlayer: " + infoPlayer);
    }

    public static void saveInfoPlayer(InfoPlayer infoPlayer) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(infoPlayer);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static InfoPlayer getInfoPlayerById(int id) {
        Session session = sessionFactory.openSession();
        try {
            return session.get(InfoPlayer.class, id);
        } finally {
            session.close();
        }
    }
}
