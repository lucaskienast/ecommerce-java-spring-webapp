package com.kienast.ecommercespringbootpaypalwebapp.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.kienast.ecommercespringbootpaypalwebapp.entities.OrderPayment;
import com.kienast.ecommercespringbootpaypalwebapp.entities.Product;
import com.kienast.ecommercespringbootpaypalwebapp.entities.ProductOrder;
import com.kienast.ecommercespringbootpaypalwebapp.services.PaymentService;
import com.kienast.ecommercespringbootpaypalwebapp.services.ProductOrderService;
import com.kienast.ecommercespringbootpaypalwebapp.services.ProductService;
import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.ShippingAddress;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.PayPalRESTException;

@Controller
public class PaymentController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentController.class);

	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductOrderService productOrderService;
	
	@PostMapping(path="/authorize_payment", consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public String authorizePayment(@RequestParam MultiValueMap<String, String> userInfo, HttpServletResponse httpResponse, Model model) {
		
		Map<String, String> registeredOrderDetails = userInfo.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get(0)));
		
		Product product = productService.getShoeById(Long.parseLong(registeredOrderDetails.get("productId")));
		LOGGER.info("Found product -> {}", product);

		ProductOrder orderDetails = new ProductOrder(
				Integer.parseInt(registeredOrderDetails.get("amount")), 
				Float.parseFloat(registeredOrderDetails.get("price")), 
				Float.parseFloat(registeredOrderDetails.get("subtotal")), 
				Float.parseFloat(registeredOrderDetails.get("shipping")), 
				Float.parseFloat(registeredOrderDetails.get("tax")), 
				Float.parseFloat(registeredOrderDetails.get("total")),
				product
				);
		
		LOGGER.info("Created order details -> {}", orderDetails);
		
		try {
			String approvalLink = paymentService.authorizePayment(orderDetails);
			LOGGER.info("Payment approval link -> {}", approvalLink);
	        httpResponse.sendRedirect(approvalLink);
		    return null;
		} catch (PayPalRESTException | IOException e1) {
			LOGGER.error("Exception -> {}", e1.toString());
			model.addAttribute("message", "Could not execute payment");
		    return "shopError";
		}
    }
	
	@GetMapping(path="/review_payment")
	public String reviewPayment(@RequestParam(name = "paymentId") String paymentId,
								@RequestParam(name = "PayerID") String payerId,
								Model model) {
		
		try {
			Payment payment = paymentService.getPaymentDetails(paymentId);
			
			PayerInfo payerInfo = payment.getPayer().getPayerInfo();
			Transaction transaction = payment.getTransactions().get(0);
			ShippingAddress shippingAddress = transaction.getItemList().getShippingAddress();
			
			LOGGER.info("Payer info -> {}", payerInfo);
			LOGGER.info("Transaction -> {}", transaction);
			LOGGER.info("Shipping address -> {}", shippingAddress);
			
			int amount = Integer.parseInt(transaction.getItemList().getItems().get(0).getQuantity());
			float price = Float.parseFloat(transaction.getItemList().getItems().get(0).getPrice());
			float subtotal = Float.parseFloat(transaction.getAmount().getDetails().getSubtotal());
			float shipping = Float.parseFloat(transaction.getAmount().getDetails().getShipping());
			float tax = Float.parseFloat(transaction.getAmount().getDetails().getTax());
			float total = Float.parseFloat(transaction.getAmount().getTotal());
			
			Long productId = Long.parseLong(transaction.getItemList().getItems().get(0).getName());
			Product product = productService.getShoeById(productId);
			
			ProductOrder givenProductOrder = new ProductOrder(
					amount, price, subtotal, shipping, tax, total, product);
			ProductOrder savedProductOrder = productOrderService.save(givenProductOrder);
			LOGGER.info("Saved order -> {}", savedProductOrder);

			if (null != savedProductOrder) {
				model.addAttribute("order", savedProductOrder);
				model.addAttribute("payer", payerInfo);
				model.addAttribute("transaction", transaction);
				model.addAttribute("shippingAddress", shippingAddress);
				
	            return "review";
			}

			model.addAttribute("message", "Could not get payment details");
		    return "shopError";
			
			
		} catch(PayPalRESTException ex) {
			LOGGER.error("Exception -> {}", ex.toString());
			model.addAttribute("message", "Could not get payment details");
		    return "shopError";
		}		
	}
	
	@RequestMapping(path="/execute_payment", method = RequestMethod.POST)
	public String printHello(@RequestParam(name = "paymentId") String paymentId,
							@RequestParam(name = "PayerID") String payerId,
							@RequestParam(name = "productOrderId") String productOrderId,
							Model model) {
		
		try {
			Payment payment = paymentService.executePayment(paymentId, payerId);
			
			PayerInfo payerInfo = payment.getPayer().getPayerInfo();
			Transaction transaction = payment.getTransactions().get(0);
			
			LOGGER.info("Payer info -> {}", payerInfo);
			LOGGER.info("Transaction -> {}", transaction);			

			ProductOrder productOrder = productOrderService.findById(Long.parseLong(productOrderId));
			float total = Float.parseFloat(transaction.getAmount().getTotal());

			OrderPayment givenOrderPayment = new OrderPayment(
					productOrder, total);
			OrderPayment savedOrderPayment = paymentService.save(givenOrderPayment);

			if (null != savedOrderPayment) {
				model.addAttribute("payer", payerInfo);
				model.addAttribute("transaction", transaction);
				
	            return "receipt";
			}

			model.addAttribute("message", "Could not get payment details");
		    return "shopError";
			
		} catch(PayPalRESTException ex) {
			LOGGER.error("Exception -> {}", ex.toString());
			model.addAttribute("message", "Could not execute payment");
		    return "shopError";
		}		
	}
	
	@PostMapping(path="/filter_payments_date", consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String filterPaymentsByDate(@RequestParam MultiValueMap<String, String> filterCriteria, Model model) {
		
		if (null == filterCriteria.get("date").get(0)) {
			model.addAttribute("message", "Payment filter failed");
	        return "adminError";
		}
		
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate givenDate = LocalDate.parse(filterCriteria.get("date").get(0), formatter);
			LOGGER.info("Given date -> {}", givenDate.atStartOfDay());

			List<OrderPayment> payments = paymentService.getAllPayments();
			LOGGER.info("Found payments -> {}", payments);

			List<OrderPayment> filteredPayments = new ArrayList<>(); 
			
			for (OrderPayment payment : payments) {
				
				LOGGER.info("Payment date -> {}", payment.getCreatedAt().toLocalDate().atStartOfDay());
				LocalDateTime foundDate = payment.getCreatedAt();

				LOGGER.info("Given date -> {}", givenDate.atStartOfDay());
				LOGGER.info("Given date == Payment date -> {}", givenDate.atStartOfDay().toString().equals(foundDate.toLocalDate().atStartOfDay().toString()));
				
				if (givenDate.atStartOfDay().toString().equals(foundDate.toLocalDate().atStartOfDay().toString())) {
					filteredPayments.add(payment);
				}
			}
			
			LOGGER.info("Filtered payments -> {}", filteredPayments);
			
			if (filteredPayments.size() >= 1) {
				model.addAttribute("payments", filteredPayments);
				return "paymentsList";
			}

			model.addAttribute("message", "Payment filter failed");
	        return "adminError";
		} catch (DateTimeParseException ex) {
			model.addAttribute("message", "Payment filter failed");
	        return "adminError";
		}
	}
	
	@GetMapping(path="/all_payments")
	public String showAllPayments(Model model) {
				
		List<OrderPayment> allPayments = paymentService.getAllPayments();
		LOGGER.info("Found payments -> {}", allPayments);
		
		if (null != allPayments) {
			model.addAttribute("payments", allPayments);
			return "paymentsList";
		}

		model.addAttribute("message", "Loading payments failed");
        return "adminError";
	}

}
