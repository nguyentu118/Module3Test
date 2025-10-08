<%--
  Created by IntelliJ IDEA.
  User: ADMIN
  Date: 08/10/2025
  Time: 9:58 SA
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<html>
<head>
    <title>Create Category</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="container mt-5">
<h2 class="mb-4">Thêm mới Category</h2>

<c:if test="${not empty errorMessage}">
    <div class="alert alert-danger">
            ${errorMessage}
    </div>
</c:if>

<form action="categories?action=insert" method="post" class="border p-4 rounded bg-light">
    <div class="mb-3">
        <label class="form-label">Name</label>
        <input type="text" name="name" value="${param.name}" class="form-control">
    </div>

    <div class="mb-3">
        <label class="form-label">Price</label>
        <input type="number" step="0.01" name="price" value="${param.price}" class="form-control">
    </div>

    <div class="mb-3">
        <label class="form-label">Quantity</label>
        <input type="number" name="quantity" value="${param.quantity}" class="form-control">
    </div>

    <div class="mb-3">
        <label class="form-label">Color</label>
        <select name="color" class="form-select">
            <c:forEach var="c" items="${colorList}">
                <option value="${c}" <c:if test="${param.color eq c}">selected</c:if>>${c}</option>
            </c:forEach>
        </select>
    </div>

    <div class="mb-3">
        <label class="form-label">Description</label>
        <textarea name="description" class="form-control">${param.description}</textarea>
    </div>

    <div class="mb-3">
        <label class="form-label">Category Name</label>
        <select name="categoryName" class="form-select">
            <c:forEach var="cat" items="${categoryList}">
                <option value="${cat.categoryName}" <c:if test="${param.categoryName eq cat.categoryName}">selected</c:if>>
                        ${cat.categoryName}
                </option>
            </c:forEach>
        </select>
    </div>

    <button type="submit" class="btn btn-success">Thêm mới</button>
    <a href="categories" class="btn btn-secondary">Quay lại</a>
</form>
</body>
</html>


