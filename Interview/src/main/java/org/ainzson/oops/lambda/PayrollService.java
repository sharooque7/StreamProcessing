package org.ainzson.oops.lambda;

import java.util.Map;
import java.util.List;
import java.util.function.*;

public class PayrollService {

    public static Predicate<Employee> highSalaryPredicate(double threshold) {
        return emp -> emp.getSalary() > threshold;
    }

    public static Function<Employee, Double> calculateBonus = emp -> emp.getSalary() * 0.10;

    public static Consumer<Employee> printEmployee = employee -> System.out.println("Payroll Processed "+ employee);

    public static Supplier<Integer> generateEmployeeId = () -> (int) (Math.random() * 1000);

    public static BiFunction<Employee, Employee, Double> mergeSalaries  = ((employee, employee2) -> employee.getSalary() + employee2.getSalary());

    public void doProcess() {
        List<Employee> employees = List.of(
                new Employee(1, "Alice", "IT", 70000),
                new Employee(2, "Bob", "HR", 50000),
                new Employee(3, "Charlie", "Finance", 90000)
        );

        employees.stream()
                .filter(PayrollService.highSalaryPredicate(60000))
                .forEach(PayrollService.printEmployee);

        employees.forEach(employee -> System.out.println(employee.getName() + " Bonus: " + PayrollService.calculateBonus.apply(employee)));

        System.out.println("Generated Employee ID: " + PayrollService.generateEmployeeId.get());

        double totalSalary =  PayrollService.mergeSalaries.apply(employees.get(0),employees.get(1));

        System.out.println("Merged Salary: " + totalSalary);

    }
}
