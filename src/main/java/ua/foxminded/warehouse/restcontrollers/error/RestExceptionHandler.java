package ua.foxminded.warehouse.restcontrollers.error;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ua.foxminded.warehouse.util.exception.address.AddressNotCreatedException;
import ua.foxminded.warehouse.util.exception.address.AddressNotFoundException;
import ua.foxminded.warehouse.util.exception.address.AddressNotUpdatedException;
import ua.foxminded.warehouse.util.exception.customer.CustomerNotCreatedException;
import ua.foxminded.warehouse.util.exception.customer.CustomerNotFoundException;
import ua.foxminded.warehouse.util.exception.customer.CustomerNotUpdatedException;
import ua.foxminded.warehouse.util.exception.invoice.InvoiceNotCreatedException;
import ua.foxminded.warehouse.util.exception.invoice.InvoiceNotFoundException;
import ua.foxminded.warehouse.util.exception.invoice.InvoiceNotUpdatedException;
import ua.foxminded.warehouse.util.exception.item.ItemNotCreatedExcetion;
import ua.foxminded.warehouse.util.exception.item.ItemNotFoundException;
import ua.foxminded.warehouse.util.exception.item.ItemNotUpdatedException;
import ua.foxminded.warehouse.util.exception.offer.OfferNotCreatedException;
import ua.foxminded.warehouse.util.exception.offer.OfferNotFoundException;
import ua.foxminded.warehouse.util.exception.offer.OfferNotUpdatedException;
import ua.foxminded.warehouse.util.exception.supplier.SupplierNotCreatedException;
import ua.foxminded.warehouse.util.exception.supplier.SupplierNotFoundException;
import ua.foxminded.warehouse.util.exception.supplier.SupplierNotUpdatedException;
import ua.foxminded.warehouse.util.exception.user.UserNotCreatedException;
import ua.foxminded.warehouse.util.exception.user.UserNotFoundException;
import ua.foxminded.warehouse.util.exception.user.UserNotUpdatedException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AddressNotFoundException.class)
    private ResponseEntity<ErrorResponse> handleAddressNotFoundException(HttpServletRequest request, AddressNotFoundException e) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.NOT_FOUND, LocalDateTime.now()," Address not found! " + request.getRequestURI());

        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(AddressNotCreatedException.class)
    private ResponseEntity<ErrorResponse> handleAddressNotCreatedException(AddressNotCreatedException e, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST, LocalDateTime.now(), e.getMessage() + "RequestURI: " + request.getRequestURI());

        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(AddressNotUpdatedException.class)
    private ResponseEntity<ErrorResponse> handleAddressNotUpdatedException(AddressNotUpdatedException e, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST, LocalDateTime.now(), e.getMessage() + "RequestURI: " + request.getRequestURI());

        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    private ResponseEntity<ErrorResponse> handleCustomerNotFoundException(HttpServletRequest request, CustomerNotFoundException e) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.NOT_FOUND, LocalDateTime.now()," Customer not found! " + request.getRequestURI());

        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(CustomerNotCreatedException.class)
    private ResponseEntity<ErrorResponse> handleCustomerNotCreatedException(CustomerNotCreatedException e, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST, LocalDateTime.now(), e.getMessage() + "RequestURI: " + request.getRequestURI());

        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(CustomerNotUpdatedException.class)
    private ResponseEntity<ErrorResponse> handleCustomerNotUpdatedException(CustomerNotUpdatedException e, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST, LocalDateTime.now(), e.getMessage() + "RequestURI: " + request.getRequestURI());

        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(ItemNotFoundException.class)
    private ResponseEntity<ErrorResponse> handleItemNotFoundException(HttpServletRequest request, ItemNotFoundException e) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.NOT_FOUND, LocalDateTime.now()," Item not found! " + request.getRequestURI());

        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(ItemNotCreatedExcetion.class)
    private ResponseEntity<ErrorResponse> handleCustomerNotCreatedException(ItemNotCreatedExcetion e, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST, LocalDateTime.now(), e.getMessage() + "RequestURI: " + request.getRequestURI());

        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(ItemNotUpdatedException.class)
    private ResponseEntity<ErrorResponse> handleItemNotUpdatedException(ItemNotUpdatedException e, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST, LocalDateTime.now(), e.getMessage() + "RequestURI: " + request.getRequestURI());

        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(SupplierNotFoundException.class)
    private ResponseEntity<ErrorResponse> handleSupplierNotFoundException(HttpServletRequest request, SupplierNotFoundException e) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.NOT_FOUND, LocalDateTime.now()," Item not found! " + request.getRequestURI());

        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(SupplierNotCreatedException.class)
    private ResponseEntity<ErrorResponse> handleSupplierNotCreatedException(SupplierNotCreatedException e, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST, LocalDateTime.now(), e.getMessage() + "RequestURI: " + request.getRequestURI());

        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(SupplierNotUpdatedException.class)
    private ResponseEntity<ErrorResponse> handleSupplierNotUpdatedException(SupplierNotUpdatedException e, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST, LocalDateTime.now(), e.getMessage() + "RequestURI: " + request.getRequestURI());

        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(InvoiceNotFoundException.class)
    private ResponseEntity<ErrorResponse> handleInvoiceNotFoundException(HttpServletRequest request, InvoiceNotFoundException e) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.NOT_FOUND, LocalDateTime.now()," Item not found! " + request.getRequestURI());

        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(InvoiceNotCreatedException.class)
    private ResponseEntity<ErrorResponse> handleInvoiceNotCreatedException(InvoiceNotCreatedException e, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST, LocalDateTime.now(), e.getMessage() + "RequestURI: " + request.getRequestURI());

        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(InvoiceNotUpdatedException.class)
    private ResponseEntity<ErrorResponse> handleInvoiceNotUpdatedException(InvoiceNotUpdatedException e, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST, LocalDateTime.now(), e.getMessage() + "RequestURI: " + request.getRequestURI());

        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(OfferNotFoundException.class)
    private ResponseEntity<ErrorResponse> handleOfferNotFoundException(HttpServletRequest request, OfferNotFoundException e) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.NOT_FOUND, LocalDateTime.now()," Item not found! " + request.getRequestURI());

        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(OfferNotCreatedException.class)
    private ResponseEntity<ErrorResponse> handleOfferNotCreatedException(OfferNotCreatedException e, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST, LocalDateTime.now(), e.getMessage() + "RequestURI: " + request.getRequestURI());

        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(OfferNotUpdatedException.class)
    private ResponseEntity<ErrorResponse> handleOfferNotUpdatedException(OfferNotUpdatedException e, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST, LocalDateTime.now(), e.getMessage() + "RequestURI: " + request.getRequestURI());

        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(UserNotFoundException.class)
    private ResponseEntity<ErrorResponse> handleUserNotFoundException(HttpServletRequest request, UserNotFoundException e) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.NOT_FOUND, LocalDateTime.now()," Item not found! " + request.getRequestURI());

        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(UserNotCreatedException.class)
    private ResponseEntity<ErrorResponse> handleUserNotCreatedException(UserNotCreatedException e, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST, LocalDateTime.now(), e.getMessage() + "RequestURI: " + request.getRequestURI());

        return new ResponseEntity<>(response, response.getStatus());
    }

    @ExceptionHandler(UserNotUpdatedException.class)
    private ResponseEntity<ErrorResponse> handleUserNotUpdatedException(UserNotUpdatedException e, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST, LocalDateTime.now(), e.getMessage() + "RequestURI: " + request.getRequestURI());

        return new ResponseEntity<>(response, response.getStatus());
    }
}
