package org.example.orm.dao;

import java.util.List;
import org.example.orm.entity.Employee;
import org.example.orm.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class EmployeeDAO {

  private SessionFactory sessionFactory;

  public EmployeeDAO() {
    this.sessionFactory = HibernateUtil.getSessionFactory();
  }

  public void saveEmployee(Employee employee) {
    Transaction transaction = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();
      session.persist(employee);  // 替代旧的save()
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
      e.printStackTrace();
    }
  }

  public Employee getEmployeeById(Long id) {
    try (Session session = sessionFactory.openSession()) {
      return session.get(Employee.class, id);
    }
  }

  public List<Employee> getAllEmployees() {
    try (Session session = sessionFactory.openSession()) {
      return session.createQuery("FROM Employee", Employee.class).list();
    }
  }

  public void updateEmployee(Employee employee) {
    Transaction transaction = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();
      session.merge(employee);  // 替代旧的update()
      transaction.commit();
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
      e.printStackTrace();
    }
  }

  public void deleteEmployee(Long id) {
    Transaction transaction = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();

      Employee employee = session.get(Employee.class, id);
      if (employee != null) {
        session.remove(employee);  // 使用 remove() 替代 delete()
        System.out.println("员工删除成功，ID: " + id);
      }

      transaction.commit();
    } catch (Exception e) {
      if (transaction != null) {
        transaction.rollback();
      }
      e.printStackTrace();
    }
  }
}
