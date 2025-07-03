package com.vn.ebookstore.controller.admin;

import com.vn.ebookstore.model.OrderDetail;
import com.vn.ebookstore.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/orders")
// @PreAuthorize("hasRole('ADMIN')") // Thêm nếu có Spring Security
public class OrderController {

    @Autowired
    private OrderDetailService orderDetailService;

    @GetMapping("")
    public String listOrders(Model model) {
        List<OrderDetail> orders = orderDetailService.getAllOrderDetails();
        model.addAttribute("orders", orders);
        return "page/admin/order/list"; // Trỏ đến file Thymeleaf
    }

    // Có thể thêm endpoint API để lấy chi tiết cho modal hoặc trang riêng
    @GetMapping("/{id}")
    public String orderDetail(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<OrderDetail> optionalOrder = orderDetailService.getOrderDetailById(id);
        if (optionalOrder.isPresent()) {
            model.addAttribute("order", optionalOrder.get());
            return "page/admin/order/detail"; // Hoặc trả về một fragment/modal
        } else {
            redirectAttributes.addFlashAttribute("error", "Order not found.");
            return "redirect:/admin/orders";
        }
    }

    @PostMapping("/{id}/status")
    public String updateOrderStatus(@PathVariable Integer id,
                                    @RequestParam("status") String newStatus,
                                    RedirectAttributes redirectAttributes) {
        OrderDetail updatedOrder = orderDetailService.updateOrderStatus(id, newStatus);
        if (updatedOrder != null) {
            redirectAttributes.addFlashAttribute("success", "Order status updated successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to update order status.");
        }
        return "redirect:/admin/orders/" + id; // Chuyển hướng về trang chi tiết sau khi cập nhật
    }
}
