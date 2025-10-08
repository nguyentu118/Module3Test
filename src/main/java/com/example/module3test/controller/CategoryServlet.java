package com.example.module3test.controller;

import com.example.module3test.DAO.CategoryDAO;
import com.example.module3test.model.Category;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "CategoryServlet", urlPatterns = "/categories")
public class CategoryServlet extends HttpServlet {
    private CategoryDAO categoryDAO;

    @Override
    public void init() {
        categoryDAO = new CategoryDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) action = "list";

        try {
            switch (action) {
                case "create":
                    showNewForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteCategory(request, response);
                    break;
                case "search":
                    searchCategory(request, response);
                    break;
                default:
                    listCategory(request, response);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        try {
            switch (action) {
                case "insert":
                    insertCategory(request, response);
                    break;
                case "update":
                    updateCategory(request, response);
                    break;
                default:
                    response.sendRedirect("categories");
                    break;
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void listCategory(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        List<Category> list = categoryDAO.selectAllCategories();
        request.setAttribute("listCategory", list);
        RequestDispatcher dispatcher = request.getRequestDispatcher("view/list.jsp");
        dispatcher.forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<String> colorList = List.of("Đỏ", "Xanh", "Vàng", "Trắng", "Đen");

        CategoryDAO categoryDAO = new CategoryDAO();
        List<Category> categoryList = categoryDAO.selectAllCategories();

        request.setAttribute("colorList", colorList);
        request.setAttribute("categoryList", categoryList);


        RequestDispatcher dispatcher = request.getRequestDispatcher("view/create.jsp");
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        Category existing = categoryDAO.selectCategory(id);
        request.setAttribute("category", existing);


        List<String> colorList = Arrays.asList("Đỏ", "Xanh", "Vàng", "Trắng", "Đen");
        request.setAttribute("colorList", colorList);


        List<Category> categoryList = categoryDAO.selectAllCategories();
        request.setAttribute("categoryList", categoryList);


        RequestDispatcher dispatcher = request.getRequestDispatcher("view/edit.jsp");
        dispatcher.forward(request, response);
    }


    private void insertCategory(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {

        String name = request.getParameter("name");
        String priceStr = request.getParameter("price");
        String quantityStr = request.getParameter("quantity");
        String color = request.getParameter("color");
        String description = request.getParameter("description");
        String categoryName = request.getParameter("categoryName");

        String error = validate(name, priceStr, quantityStr);
        if (error != null) {
            request.setAttribute("errorMessage", error);
            RequestDispatcher dispatcher = request.getRequestDispatcher("view/create.jsp");
            dispatcher.forward(request, response);
            return;
        }

        double price = Double.parseDouble(priceStr);
        int quantity = Integer.parseInt(quantityStr);

        Category category = new Category(name, price, quantity, color, description, categoryName);
        categoryDAO.insertCategory(category);

        HttpSession session = request.getSession();
        session.setAttribute("successMessage", "Thêm sản phẩm thành công!");
        response.sendRedirect("categories");
    }


    private void updateCategory(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String priceStr = request.getParameter("price");
        String quantityStr = request.getParameter("quantity");
        String color = request.getParameter("color");
        String description = request.getParameter("description");
        String categoryName = request.getParameter("categoryName");

        String error = validate(name, priceStr, quantityStr);
        if (error != null) {
            request.setAttribute("errorMessage", error);
            request.setAttribute("category", categoryDAO.selectCategory(id));
            RequestDispatcher dispatcher = request.getRequestDispatcher("view/edit.jsp");
            dispatcher.forward(request, response);
            return;
        }

        double price = Double.parseDouble(priceStr);
        int quantity = Integer.parseInt(quantityStr);

        Category category = new Category(id, name, price, quantity, color, description, categoryName);
        categoryDAO.updateCategory(category);
        response.sendRedirect("categories");
    }

    private void deleteCategory(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        categoryDAO.deleteCategory(id);
        response.sendRedirect("categories");
    }

    private void searchCategory(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        String name = request.getParameter("name");
        String price = request.getParameter("price");
        String color = request.getParameter("color");
        String categoryName = request.getParameter("categoryName");

        List<Category> list = categoryDAO.search(name, price, color, categoryName);
        request.setAttribute("listCategory", list);
        RequestDispatcher dispatcher = request.getRequestDispatcher("view/list.jsp");
        dispatcher.forward(request, response);
    }


    private String validate(String name, String priceStr, String quantityStr) {
        if (name == null || name.trim().isEmpty()) {
            return "Tên sản phẩm không được để trống!";
        }
        if (priceStr == null || priceStr.trim().isEmpty()) {
            return "Giá không được để trống!";
        }
        if (quantityStr == null || quantityStr.trim().isEmpty()) {
            return "Số lượng không được để trống!";
        }

        try {
            double price = Double.parseDouble(priceStr);
            if (price < 10000000) {
                return "Giá phải lớn hơn 10.000.000!";
            }
        } catch (NumberFormatException e) {
            return "Giá phải là số!";
        }

        try {
            int quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) {
                return "Số lượng phải là số nguyên dương!";
            }
        } catch (NumberFormatException e) {
            return "Số lượng phải là số nguyên!";
        }

        return null;
    }
}
