// Online Java Compiler
// Use this editor to write, compile and run your Java code online

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Stream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

class Streams {
    public static void main(String[] args) {
        List<Employee> employeeList = new ArrayList<Employee>();

        employeeList.add(new Employee(111, "Jiya Brein", 32, "Female", "HR", 2011, 25000.0));
        employeeList.add(new Employee(122, "Paul Niksui", 25, "Male", "Sales And Marketing", 2015, 13500.0));
        employeeList.add(new Employee(133, "Martin Theron", 29, "Male", "Infrastructure", 2012, 18000.0));
        employeeList.add(new Employee(144, "Murali Gowda", 28, "Male", "Product Development", 2014, 32500.0));
        employeeList.add(new Employee(155, "Nima Roy", 27, "Female", "HR", 2013, 22700.0));
        employeeList.add(new Employee(166, "Iqbal Hussain", 43, "Male", "Security And Transport", 2016, 10500.0));
        employeeList.add(new Employee(177, "Manu Sharma", 35, "Male", "Account And Finance", 2010, 27000.0));
        employeeList.add(new Employee(188, "Wang Liu", 31, "Male", "Product Development", 2015, 34500.0));
        employeeList.add(new Employee(199, "Amelia Zoe", 24, "Female", "Sales And Marketing", 2016, 11500.0));
        employeeList.add(new Employee(200, "Jaden Dough", 38, "Male", "Security And Transport", 2015, 11000.5));
        employeeList.add(new Employee(211, "Jasna Kaur", 27, "Female", "Infrastructure", 2014, 15700.0));
        employeeList.add(new Employee(222, "Nitin Joshi", 25, "Male", "Product Development", 2016, 28200.0));
        employeeList.add(new Employee(233, "Jyothi Reddy", 27, "Female", "Account And Finance", 2013, 21300.0));
        employeeList.add(new Employee(244, "Nicolus Den", 24, "Male", "Sales And Marketing", 2017, 10700.5));
        employeeList.add(new Employee(255, "Ali Baig", 23, "Male", "Infrastructure", 2018, 12700.0));
        employeeList.add(new Employee(266, "Sanvi Pandey", 26, "Female", "Product Development", 2015, 28900.0));
        employeeList.add(new Employee(277, "Anuj Chettiar", 31, "Male", "Product Development", 2010, 35700.0));
        
        // System.out.println(employeeList.stream().collect(Collectors.groupingBy(Employee::getGender,
        // Collectors.counting())));
        // System.out.println(employeeList.stream().map(Employee::getDepartment).collect(Collectors.toSet()));
        // employeeList.stream().map(Employee::getGender).distinct().forEach(System.out::println);
        // System.out.print(employeeList.stream().collect(Collectors.groupingBy(Employee::getGender,
        // Collectors.averagingInt(Employee::getAge))));
        // System.out.println(
        // employeeList.stream().collect(Collectors.minBy(Comparator.comparingInt(Employee::getAge))).get());
        // System.out.println(
        // employeeList.stream().collect(Collectors.maxBy(Comparator.comparingDouble(Employee::getSalary))).get());
        // employeeList.stream().filter(e -> e.getAge() > 20 && e.getAge() <
        // 25).map(Employee::getName).sorted(Comparator.reverseOrder())
        // .forEach(System.out::println);
        // employeeList.stream().filter(e -> e.getYearOfJoining() >
        // 2015).map(Employee::getName).forEach(System.out::println);
        // employeeList.stream().collect(Collectors.groupingBy(Employee::getDepartment,
        // Collectors.counting()))
        // .forEach((e,l) -> System.out.println(e +" = "+ l));
        // employeeList.stream().collect(Collectors.groupingBy(Employee::getDepartment,
        // Collectors.averagingDouble(Employee::getSalary))).
        // forEach((e,l) -> System.out.println(e+" "+l));
        // System.out.println(employeeList.stream()
        // .filter(e -> e.getGender().equals("Male") &&
        // e.getDepartment().equals("Product Development"))
        // .min(Comparator.comparingInt(Employee::getAge)).get());
        // System.out.println(employeeList.stream().min(Comparator.comparingInt(Employee::getYearOfJoining)).get());
        // System.out.println(
        // employeeList.stream().sorted(Comparator.comparingInt(Employee::getYearOfJoining)).findFirst().get());
        // System.out.println(employeeList.stream().filter(e ->
        // e.getDepartment().equals("Sales And Marketing"))
        // .collect(Collectors.groupingBy(Employee::getGender, Collectors.counting())));
        // System.out.println(employeeList.stream().collect(
        // Collectors.groupingBy(Employee::getGender,
        // Collectors.averagingDouble(Employee::getSalary))));
        // System.out.println(employeeList.stream().collect(Collectors.groupingBy(Employee::getDepartment)));

        // DoubleSummaryStatistics d =
        // employeeList.stream().collect(Collectors.summarizingDouble(Employee::getSalary));
        // System.out.println(d);
        // System.out.println(d.getAverage() + "||" + d.getSum());
        // Map<Boolean, List<Employee>> a =
        // employeeList.stream().collect(Collectors.partitioningBy(e -> e.getAge() <=
        // 25));
        // System.out.println("aaaaa: "+ a.get(true) + " \n" + "bbbbb" + a.get(false));
        // System.out.println(employeeList.stream().max(Comparator.comparingInt(Employee::getAge)).get());

    }
}

// class Employee {
//     int id;

//     String name;

//     int age;

//     String gender;

//     String department;

//     int yearOfJoining;

//     double salary;

//     public Employee(int id, String name, int age, String gender, String department, int yearOfJoining, double salary) {
//         this.id = id;
//         this.name = name;
//         this.age = age;
//         this.gender = gender;
//         this.department = department;
//         this.yearOfJoining = yearOfJoining;
//         this.salary = salary;
//     }

//     public int getId() {
//         return id;
//     }

//     public String getName() {
//         return name;
//     }

//     public int getAge() {
//         return age;
//     }

//     public String getGender() {
//         return gender;
//     }

//     public String getDepartment() {
//         return department;
//     }

//     public int getYearOfJoining() {
//         return yearOfJoining;
//     }

//     public double getSalary() {
//         return salary;
//     }

//     @Override
//     public String toString() {
//         return "\n  Id : " + id
//                 + ",\n  Name : " + name
//                 + ",\n  age : " + age
//                 + ",\n  Gender : " + gender
//                 + ",\n  Department : " + department
//                 + ",\n  Year Of Joining : " + yearOfJoining
//                 + ",\n  Salary : " + salary
//                 + "\n";
//     }
// }
