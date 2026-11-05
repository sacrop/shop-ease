package com.shop.ease.Controller;

import java.time.LocalDate;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.shop.ease.service.SalesService;

@Controller
@RequestMapping("/admin/sales")
public class SalesController {

    private final SalesService salesService;

    public SalesController(SalesService salesService) {
        this.salesService = salesService;
    }

    @GetMapping("/report")
    public ResponseEntity<byte[]> salesReportHandler(
        @RequestParam("startDate") LocalDate startDate,
        @RequestParam("endDate") LocalDate endDate
    ) {
        byte[] data = salesService.getSalesReportData(startDate, endDate);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename("orders.csv").build().toString())
                .contentType(MediaType.parseMediaType("text/csv"))
                .contentLength(data.length)
                .body(data);
    }


    // @GetMapping("/download")
    // @ResponseBody
    // public void salesReport(HttpServletResponse response, @RequestParam("startDate") LocalDate startDate,
    //         @RequestParam("endDate") LocalDate endDate) throws IOException {
    //     List<Order> orders = orderService.getAllOrderByDate(startDate, endDate);

    //     try (FileWriter writer = new FileWriter("sales_report.csv")) {
    //         writer.append("Product,Quantity,Revenue\n");
    //         for (Order order : orders) {
    //             List<OrderItem> orderItems = order.getOrderItems();
    //             for (OrderItem orderItem : orderItems) {
    //                 writer.append(orderItem.getProduct().getProductName()).append(",");
    //                 writer.append(orderItem.getOrderQuantity() + "").append(",");
    //                 writer.append(order.getTotalAmount() + "").append("\n");
    //             }
    //         }
    //     } catch (IOException e) {
    //         // Handle file creation error
    //         e.printStackTrace();
    //     }
    //     // Configure the response to allow file download
    //     // Set appropriate headers
    //     // You may need to adjust the content type and headers based on your file type
    //     // (e.g., Excel)
    //     response.setHeader("Content-Disposition", "attachment; filename=sales_report.csv");
    //     response.setContentType("text/csv");

    //     // Copy the file content to the response output stream
    //     try (FileInputStream fileInputStream = new FileInputStream("sales_report.csv");
    //             OutputStream outputStream = response.getOutputStream()) {
    //         byte[] buffer = new byte[1024];
    //         int bytesRead;
    //         while ((bytesRead = fileInputStream.read(buffer)) != -1) {
    //             outputStream.write(buffer, 0, bytesRead);
    //         }
    //     } catch (IOException e) {
    //         // Handle file download error
    //         e.printStackTrace();
    //     }
    // }

}
