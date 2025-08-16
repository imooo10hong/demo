package org.example.orm;

import java.util.List;
import org.example.orm.dao.EmployeeDAO;
import org.example.orm.entity.Department;
import org.example.orm.entity.Employee;

public class MainAPP {

  public static void main(String[] args) {
    EmployeeDAO employeeDAO = new EmployeeDAO();

    // 创建部门
    Department devDept = new Department();
    devDept.setName("开发部");

    // 创建员工
    Employee emp1 = new Employee();
    emp1.setName("李四");
    emp1.setEmail("lisi@example.com");
    emp1.setSalary(12000.0);
    emp1.setDepartment(devDept);

    // 保存
    employeeDAO.saveEmployee(emp1);

    // 查询
    Employee foundEmp = employeeDAO.getEmployeeById(1L);
    System.out.println("查询到的员工: " + foundEmp.getName());

    // 更新
    foundEmp.setSalary(15000.0);
    employeeDAO.updateEmployee(foundEmp);

    // 查询所有
    List<Employee> allEmps = employeeDAO.getAllEmployees();
    allEmps.forEach(emp -> System.out.println(emp.getName() + " - " + emp.getSalary()));

    // 删除
    // employeeDAO.deleteEmployee(1L);
  }

}

/*
      判断Hibernate的xml文件有没有配置正确：
      try
      {
          Configuration config = new Configuration().configure();
          // 如果没报错，说明文件位置正确
          System.out.println("Hibernate配置加载成功！");
      }
      catch (HibernateException e)
      {
      System.err.println("加载Hibernate配置失败: " + e.getMessage());
      }
 */
