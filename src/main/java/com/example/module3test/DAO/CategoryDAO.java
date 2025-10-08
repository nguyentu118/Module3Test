package com.example.module3test.DAO;

import com.example.module3test.model.Category;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    private static final String INSERT_CATEGORY = "insert into category (name, price, quantity, color, description, categoryName) values (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_CATEGORY = "select * from category";
    private static final String SELECT_CATEGORY_BY_ID = "select * from category where id = ?";
    private static final String DELETE_CATEGORY = "delete from category where id = ?";
    private static final String UPDATE_CATEGORY = "update category set name = ?, price = ?, quantity = ?, color = ?, description = ?, categoryName = ? where id = ?";

    public void insertCategory(Category category) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_CATEGORY)) {
            ps.setString(1, category.getName());
            ps.setDouble(2, category.getPrice());
            ps.setInt(3, category.getQuantity());
            ps.setString(4, category.getColor());
            ps.setString(5, category.getDescription());
            ps.setString(6, category.getCategoryName());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Category> selectAllCategories() {
        List<Category> categories = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_ALL_CATEGORY);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                categories.add(mapCategory(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public Category selectCategory(int id) {
        Category category = null;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_CATEGORY_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    category = mapCategory(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return category;
    }

    public boolean updateCategory(Category category) {
        boolean updated = false;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_CATEGORY)) {
            ps.setString(1, category.getName());
            ps.setDouble(2, category.getPrice());
            ps.setInt(3, category.getQuantity());
            ps.setString(4, category.getColor());
            ps.setString(5, category.getDescription());
            ps.setString(6, category.getCategoryName());
            ps.setInt(7, category.getId());
            updated = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updated;
    }

    public boolean deleteCategory(int id) {
        boolean deleted = false;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_CATEGORY)) {
            ps.setInt(1, id);
            deleted = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deleted;
    }

    public List<Category> search(String name, String price, String color, String categoryName) throws SQLException {
        List<Category> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM category WHERE 1=1 ");

        if (name != null && !name.isEmpty()) sql.append("AND name LIKE ? ");
        if (price != null && !price.isEmpty()) sql.append("AND price = ? ");
        if (color != null && !color.isEmpty()) sql.append("AND color LIKE ? ");
        if (categoryName != null && !categoryName.isEmpty()) sql.append("AND categoryName LIKE ? ");

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql.toString())) {

            int index = 1;
            if (name != null && !name.isEmpty()) ps.setString(index++, "%" + name + "%");
            if (price != null && !price.isEmpty()) ps.setDouble(index++, Double.parseDouble(price));
            if (color != null && !color.isEmpty()) ps.setString(index++, "%" + color + "%");
            if (categoryName != null && !categoryName.isEmpty()) ps.setString(index++, "%" + categoryName + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapCategory(rs));
                }
            }
        }
        return list;
    }

    private Category mapCategory(ResultSet rs) throws SQLException {
        return new Category(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getDouble("price"),
                rs.getInt("quantity"),
                rs.getString("color"),
                rs.getString("description"),
                rs.getString("categoryName")
        );
    }
}
