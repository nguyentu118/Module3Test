<%--
  Created by IntelliJ IDEA.
  User: ADMIN
  Date: 08/10/2025
  Time: 9:58 SA
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html>
<head>
  <title>Product Management</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <style>
    th {
      background-color: #007bff;
      color: white;
      text-align: center;
    }
    .table td {
      vertical-align: middle;
      text-align: center;
    }
  </style>
</head>
<body class="p-4 bg-light">

<div class="container bg-white p-4 rounded shadow">
  <h4 class="mb-3">Management Product</h4>

  <c:if test="${not empty sessionScope.successMessage}">
    <div class="alert alert-success">
        ${sessionScope.successMessage}
    </div>
    <c:remove var="successMessage" scope="session"/>
  </c:if>


  <form action="categories" method="get" class="row g-2 mb-4">
    <input type="hidden" name="action" value="search">

    <div class="col-md-3">
      <input type="text" name="name" class="form-control" placeholder="Enter Product Name">
    </div>

    <div class="col-md-2">
      <input type="text" name="price" class="form-control" placeholder="Enter Price">
    </div>

    <div class="col-md-3">
      <input type="text" name="categoryName" class="form-control" placeholder="Enter Category">
    </div>

    <div class="col-md-2">
      <input type="text" name="color" class="form-control" placeholder="Enter Color">
    </div>

    <div class="col-md-2 d-flex gap-2">
      <button type="submit" class="btn btn-primary w-100">Search</button>
      <a href="categories" class="btn btn-secondary">Clear</a>
    </div>
  </form>

  <div class="d-flex justify-content-end mb-3">
    <a href="categories?action=create" class="btn btn-success">Add Product</a>
  </div>

  <table class="table table-bordered table-striped align-middle">
    <thead>
    <tr>
      <th>STT</th>
      <th>Product Name</th>
      <th>Price</th>
      <th>Quantity</th>
      <th>Color</th>
      <th>Category</th>
      <th>Action</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="cat" items="${listCategory}" varStatus="loop">
      <tr>
        <td>${loop.index + 1}</td>
        <td>${cat.name}</td>
        <td>
          <fmt:formatNumber value="${cat.price}" type="number" pattern="#,##0"/>
        </td>

        <td>${cat.quantity}</td>
        <td>${cat.color}</td>
        <td>${cat.categoryName}</td>
        <td>
          <a href="categories?action=edit&id=${cat.id}" class="btn btn-warning btn-sm">Edit</a>
          <a href="categories?action=delete&id=${cat.id}"
             class="btn btn-danger btn-sm"
             onclick="return confirm('Bạn có chắc chắn muốn xóa sản phẩm này không?');">
            Delete
          </a>

        </td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
</div>
</body>
</html>

