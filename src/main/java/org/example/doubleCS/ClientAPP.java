package org.example.doubleCS;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Scanner;


public class ClientAPP {
    public static void main(String[] args) {
        System.out.println("欢迎来到个人通讯录系统！");
        String request;
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("----------------------");
            System.out.println("请输入你需要处理的业务：QUERY(1),ADD(2),DELETE(3),UPDATE(4):");
            request = sc.nextLine();
            //out.println(messageToServer);
            //进行服务要求判断
            switch (request) {
                case "ADD":
                case "2":
                    //ADD逻辑
                    ADD();
                    break;
                case "DELETE":
                case "3":
                    //DELETE逻辑
                    DELETE();
                    break;
                case "QUERY":
                case "1":
                    //QUERY逻辑
                    QUERY();
                    break;
                case "UPDATE":
                case "4":
                    //UPDATE逻辑
                    UPDATE();
                    break;
                default:
                    System.out.println("输入错误！重新输入！");
            }
            System.out.println("正在退出当前业务...");
        }
    }

    public static Connection connectToDatabase() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/contact?useSSL=false&serverTimezone=UTC"
                    , "root"
                    , "123456");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }


    public static void ADD() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入新联系人的信息：");
        System.out.print("姓名: ");
        String name = scanner.nextLine();
        System.out.print("地址: ");
        String address = scanner.nextLine();
        System.out.print("电话号码: ");
        double tel = Double.parseDouble(scanner.nextLine());
        // 在这里执行添加逻辑，将 newContact 添加到数据库
        Connection connection = connectToDatabase();
        if (connection != null) {
            try {
                // 编写 SQL 插入语句
                String sql = "INSERT INTO contacts (name, address, tel) VALUES (?, ?, ?)";

                // 创建 PreparedStatement 对象
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, address);
                preparedStatement.setDouble(3, tel);

                // 执行插入操作
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("联系人已成功添加到数据库。");
                } else {
                    System.out.println("无法添加联系人到数据库。");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("无法连接到数据库。");
        }
    }

    public static void DELETE() {
        Scanner scanner = new Scanner(System.in);
        // 这里可以要求客户端提供联系人的信息并从数据库中删除
        // 例如：要求客户端输入要删除的联系人的姓名
        // 然后根据姓名查找并删除联系人
        System.out.print("请输入要删除的联系人的姓名: ");
        String nameToDelete = scanner.nextLine();
        // 在这里执行删除逻辑，从数据库中删除符合条件的联系人
        Connection connection = connectToDatabase();
        if (connection != null) {
            try {
                // 编写 SQL 删除语句
                String sql = "DELETE FROM contacts WHERE name = ?";

                // 创建 PreparedStatement 对象
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, nameToDelete);

                // 执行删除操作
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("联系人已成功删除。");
                } else {
                    System.out.println("未找到匹配的联系人，无法删除。");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("无法连接到数据库。");
        }
        System.out.println("联系人已删除。");
    }

    public static void QUERY() {
        Scanner scanner = new Scanner(System.in);
        // 这里可以要求客户端提供查询条件，并从数据库中查询联系人信息
        // 例如：要求客户端输入查询条件，如姓名关键字
        System.out.print("请输入查询条件（例如姓名关键字）: ");
        String queryCondition = scanner.nextLine();

        Connection connection = connectToDatabase();
        if (connection != null) {
            try {
                // 编写 SQL 查询语句，使用 LIKE 实现模糊匹配
                String sql = "SELECT * FROM contacts WHERE name LIKE ?";

                // 创建 PreparedStatement 对象
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, "%" + queryCondition + "%");

                // 执行查询操作
                ResultSet resultSet = preparedStatement.executeQuery();

                // 输出查询结果给客户端
                System.out.println("查询结果：");
                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String address = resultSet.getString("address");
                    double tel = resultSet.getDouble("tel");
                    BigDecimal teldb = new BigDecimal(tel);
                    System.out.println("姓名: " + name + ", 地址: " + address + ", 电话号码: " + teldb.toPlainString());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("无法连接到数据库。");
        }
    }


    public static void UPDATE() {
        Scanner scanner = new Scanner(System.in);
        // 这里可以要求客户端提供要更新的联系人信息
        // 例如：要求客户端输入联系人的姓名，然后提供新的信息（如地址和电话号码）
        System.out.print("请输入要更新的联系人的姓名: ");
        String nameToUpdate = scanner.nextLine();

        Connection connection = connectToDatabase();
        if (connection != null) {
            try {
                // 先查询是否存在匹配的联系人
                String selectSql = "SELECT name FROM contacts WHERE name = ?";
                PreparedStatement selectStatement = connection.prepareStatement(selectSql);
                selectStatement.setString(1, nameToUpdate);
                ResultSet resultSet = selectStatement.executeQuery();

                if (resultSet.next()) {
                    //展示存在匹配的联系人
                    String sql = "SELECT * FROM contacts WHERE name LIKE ?";

                    // 创建 PreparedStatement 对象
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, "%" + nameToUpdate + "%");

                    // 执行查询操作
                    ResultSet resultSet1 = preparedStatement.executeQuery();

                    // 输出查询结果给客户端
                    System.out.println("原个人信息：");
                    while (resultSet1.next()) {
                        String name = resultSet1.getString("name");
                        String address = resultSet1.getString("address");
                        double tel = resultSet1.getDouble("tel");
                        BigDecimal teldb = new BigDecimal(tel);
                        System.out.println("姓名: " + name + ", 地址: " + address + ", 电话号码: " + teldb.toPlainString());
                    }
                    //客户输入更改信息
                    System.out.print("新地址: ");
                    String newAddress = scanner.nextLine();
                    System.out.print("新电话号码: ");
                    double newTel = Double.parseDouble(scanner.nextLine());
                    // 编写 SQL 更新语句
                    String updateSql = "UPDATE contacts SET address = ?, tel = ? WHERE name = ?";
                    PreparedStatement updateStatement = connection.prepareStatement(updateSql);
                    updateStatement.setString(1, newAddress);
                    updateStatement.setDouble(2, newTel);
                    updateStatement.setString(3, nameToUpdate);

                    // 执行更新操作
                    int rowsAffected = updateStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("联系人信息已成功更新。");
                    } else {
                        System.out.println("未能更新联系人信息。");
                    }
                } else {
                    System.out.println("未找到匹配的联系人，无法更新信息。");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("无法连接到数据库。");
        }
    }


}


