package org.example.orm.util;

import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateUtil {

  @Getter
  private static final SessionFactory sessionFactory = buildSessionFactory();

  private static SessionFactory buildSessionFactory() {
    try {
      // 创建服务注册
      StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
          .configure("hibernate.cfg.xml")
          .build();

      // 创建元数据源
      Metadata metadata = new MetadataSources(standardRegistry)
          .getMetadataBuilder()
          .build();

      return metadata.getSessionFactoryBuilder().build();
    } catch (Exception ex) {
      System.err.println("初始化SessionFactory失败。" + ex);
      throw new ExceptionInInitializerError(ex);
    }
  }

  public static void shutdown() {
    if (sessionFactory != null && !sessionFactory.isClosed()) {
      sessionFactory.close();
    }
  }
}
