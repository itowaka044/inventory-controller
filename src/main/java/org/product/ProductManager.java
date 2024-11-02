package org.product;

import java.sql.*;
import java.util.Scanner;

public class ProductManager {

    public void addProduct(String name, double price) {
        String sql = "INSERT INTO products (name, price) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();

            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setDouble(2, price);

            pstmt.executeUpdate();

            System.out.println("produto adicionado.");

        } catch (SQLException e) {
            System.out.println("erro ao adicionar produto: " + e.getMessage());
        }
    }

    public void listProducts() {
        String sql = "SELECT * FROM products";

        try (Connection conn = DatabaseConnection.getConnection();

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {

            if (!rs.isBeforeFirst()) {
                System.out.println("nenhum produto encontrado.");
            }

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + ", nome: " +
                        rs.getString("name") + ", preço: R$" + rs.getDouble("price"));
            }

        } catch (SQLException e) {
            System.out.println("erro ao listar produtos: " + e.getMessage());
        }
    }

    public void updateProduct(int id, String name, double price) {
        String sql = "UPDATE products SET name = ?, price = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setDouble(2, price);
            pstmt.setInt(3, id);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("produto atualizado.");
            } else {
                System.out.println("produto não encontrado.");
            }

        } catch (SQLException e) {
            System.out.println("erro ao atualizar produto: " + e.getMessage());
        }
    }

    public void deleteProduct(int id) {
        String sql = "DELETE FROM products WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("produto removido.");
            } else {
                System.out.println("produto não encontrado.");
            }

        } catch (SQLException e) {
            System.out.println("erro ao remover produto: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        ProductManager manager = new ProductManager();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.printf("\nescolha uma opção:\n1. adicionar produto\n2. listar produtos\n" +
                    "3. atualizar produto\n4. excluir produto\n5. sair\n");
            System.out.print("opção: ");
            int option = scanner.nextInt();

            scanner.nextLine();

            switch (option) {
                case 1:
                    System.out.print("nome do produto: ");
                    String name = scanner.nextLine();

                    System.out.print("preço do produto: ");
                    double price = scanner.nextDouble();

                    manager.addProduct(name, price);

                    break;

                case 2:
                    manager.listProducts();
                    break;

                case 3:
                    System.out.print("ID do produto para atualizar: ");
                    int updateId = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("novo nome: ");
                    String newName = scanner.nextLine();

                    System.out.print("novo preço: ");
                    double newPrice = scanner.nextDouble();

                    manager.updateProduct(updateId, newName, newPrice);

                    break;

                case 4:
                    System.out.print("ID do produto para excluir: ");
                    int deleteId = scanner.nextInt();

                    manager.deleteProduct(deleteId);
                    break;

                case 5:
                    System.out.println("encerrando o programa.");

                    scanner.close();

                    return;

                default:
                    System.out.println("opção inválida.");
            }
        }
    }
}
