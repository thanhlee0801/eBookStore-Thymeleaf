// src/main/resources/static/js/admin/order.js
'use strict';

// Có thể thêm các hàm JS ở đây, ví dụ:
function confirmDelete(orderId) {
  if (confirm("Are you sure you want to delete this order?")) {
//         // Gửi request xóa
       console.log("Deleting order " + orderId);
    }
 }

document.addEventListener('DOMContentLoaded', function() {
    console.log('Order management script loaded.');
    // Thêm các event listeners hoặc logic JS khác nếu cần
});
