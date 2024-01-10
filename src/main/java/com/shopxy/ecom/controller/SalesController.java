package com.shopxy.ecom.controller;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shopxy.ecom.model.Order;
import com.shopxy.ecom.model.OrderItem;
import com.shopxy.ecom.service.OrderService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/seller/sales")
public class SalesController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/download")
    @ResponseBody
    public void salesReport(HttpServletResponse response, @RequestParam("startDate") LocalDate dates,
            @RequestParam("endDate") LocalDate endDates) throws IOException {
        List<Order> orders = orderService.getAllOrderByDate(dates, endDates);

        try (FileWriter writer = new FileWriter("sales_report.csv")) {
            writer.append("Id,Product,Quantity,User,Payment,OrderDate,Revenue\n");
            for (Order order : orders) {
                List<OrderItem> orderItems = order.getOrderItems();
                for (OrderItem orderItem : orderItems) {
                    writer.append(order.getId() + "").append(",");
                    writer.append(orderItem.getProduct().getPname()).append(",");
                    writer.append(orderItem.getQuantity() + "").append(",");
                    writer.append(order.getUser().getEmail()).append(",");
                    writer.append(order.getPaymentMethod()).append(",");
                    writer.append(order.getOrderDateTime().toString()).append(",");
                    writer.append(order.getTotalAmount() + "").append("\n");
                }
            }
        } catch (IOException e) {
            // Handle file creation error
            e.printStackTrace();
        }
        // Configure the response to allow file download
        // Set appropriate headers
        // You may need to adjust the content type and headers based on your file type
        // (e.g., Excel)
        response.setHeader("Content-Disposition", "attachment; filename=sales_report.csv");
        response.setContentType("text/csv");

        // Copy the file content to the response output stream
        try (FileInputStream fileInputStream = new FileInputStream("sales_report.csv");
                OutputStream outputStream = response.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            // Handle file download error
            e.printStackTrace();
        }
    }

    

}