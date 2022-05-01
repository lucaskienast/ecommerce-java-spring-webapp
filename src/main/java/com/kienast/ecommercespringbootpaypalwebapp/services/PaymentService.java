package com.kienast.ecommercespringbootpaypalwebapp.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kienast.ecommercespringbootpaypalwebapp.entities.OrderPayment;
import com.kienast.ecommercespringbootpaypalwebapp.entities.ProductOrder;
import com.kienast.ecommercespringbootpaypalwebapp.repositories.PaymentRepository;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Details;
import com.paypal.api.payments.Item;
import com.paypal.api.payments.ItemList;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

@Component
public class PaymentService {
	
	private static final String CLIENT_ID = "AXWk5wSWcADJIV8YcG8SabqxG6NkJ22aqvHPNpd4v3ijWl7wiOH7buTZVKjwX6PunCQ6QICHoQMoQY_M";
	private static final String CLIENT_SECRET = "ENFYSOaki1IvDlVrhKK-LQXoi7I_Mxdprae5J4iIzni2WJTtbkYgkszDOli5tgtgDJEym3VE2MBUSTVO";
	private static final String MODE = "sandbox";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentService.class);

	
	@Autowired
	PaymentRepository paymentRepository;
	
	public List<OrderPayment> getAllPayments() {
		
		List<OrderPayment> allPayments = paymentRepository.findAll();
		LOGGER.info("Found payments -> {}", allPayments);
		
		return allPayments;
	}
	
	public OrderPayment save(OrderPayment payment) {
		
		OrderPayment savedPayment = paymentRepository.save(payment);
		LOGGER.info("Saved payment -> {}", savedPayment);

		return savedPayment;
	}
	
	public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
		PaymentExecution paymentExecution = new PaymentExecution();
		paymentExecution.setPayerId(payerId);
		Payment payment = new Payment().setId(paymentId);
		APIContext apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);
		return payment.execute(apiContext, paymentExecution);
	}
	
	public Payment getPaymentDetails(String paymentId) throws PayPalRESTException {
		APIContext apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);
		return Payment.get(apiContext, paymentId);
	}
	
	public String authorizePayment(ProductOrder orderDetail) throws PayPalRESTException {
		
		LOGGER.info("authorizePayment");

		Payer payer = getPayerInformation();
		RedirectUrls redirectUrls = getRedirectUrls();
		List<Transaction> transactionList = getTransactionInformation(orderDetail);
		LOGGER.info("Transaction list -> {}", transactionList);
		
		Payment requestPayment = new Payment();
		requestPayment.setTransactions(transactionList);
		requestPayment.setRedirectUrls(redirectUrls);
		requestPayment.setPayer(payer);
		requestPayment.setIntent("authorize");
		
		APIContext apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);
		Payment approvedPayment = requestPayment.create(apiContext);
		
		System.out.println(approvedPayment);
		
		return getApprovalLink(approvedPayment);
	}
	
	private String getApprovalLink(Payment approvedPayment) {
		List<Links> links = approvedPayment.getLinks();
		String approvalLink = null;
		
		for (Links link : links) {
			if (link.getRel().equalsIgnoreCase("approval_url")) {
				approvalLink = link.getHref();
			}
		}
		
		return approvalLink;
	}
	
	private String convertNumberFromGermanToUsFormat(String germanNumber) {
		String usNumber = germanNumber.replace(",", ".");
		return usNumber;
	}
	
	private List<Transaction> getTransactionInformation(ProductOrder orderDetail) {
		Details details = new Details();
		details.setShipping(convertNumberFromGermanToUsFormat(String.format("%.2f", orderDetail.getShipping())));
		details.setSubtotal(convertNumberFromGermanToUsFormat(String.format("%.2f", orderDetail.getSubtotal())));
		details.setTax(convertNumberFromGermanToUsFormat(String.format("%.2f", orderDetail.getTax())));
		
		Amount amount = new Amount();
		amount.setCurrency("USD");
		amount.setTotal(convertNumberFromGermanToUsFormat(String.format("%.2f", orderDetail.getTotal())));
		amount.setDetails(details);
		System.out.println(amount);
		
		Item item = new Item();
		item.setCurrency("USD");
		item.setName("" + orderDetail.getProduct().getId());
		item.setPrice(convertNumberFromGermanToUsFormat(String.format("%.2f", orderDetail.getPrice())));
		item.setTax(convertNumberFromGermanToUsFormat(String.format("%.2f", orderDetail.getTax())));
		item.setQuantity("" + orderDetail.getAmount());

		List<Item> items = new ArrayList<Item>();
		items.add(item);
		
		ItemList itemList = new ItemList();
		itemList.setItems(items);
		
		Transaction transaction = new Transaction();
		transaction.setAmount(amount);
		transaction.setDescription("" + orderDetail.getAmount() + "x " + orderDetail.getProduct().getProductLine().getName() );
		transaction.setItemList(itemList);
		
		List<Transaction> transactionList = new ArrayList<Transaction>();
		transactionList.add(transaction);
		
		return transactionList;
	}

	private RedirectUrls getRedirectUrls() {
		RedirectUrls redirectUrls = new RedirectUrls();
		redirectUrls.setCancelUrl("http://localhost:8080/cancel_payment");
		redirectUrls.setReturnUrl("http://localhost:8080/review_payment");
		return redirectUrls;
	}

	private Payer getPayerInformation() {
		PayerInfo payerInfo = new PayerInfo();
		payerInfo.setFirstName("Lucas");
		payerInfo.setLastName("Kienast");
		
		Payer payer = new Payer();
		payer.setPaymentMethod("paypal");
		payer.setPayerInfo(payerInfo);
		
		return payer;
	}

}
