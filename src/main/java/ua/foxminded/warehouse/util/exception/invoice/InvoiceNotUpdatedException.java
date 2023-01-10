package ua.foxminded.warehouse.util.exception.invoice;

public class InvoiceNotUpdatedException extends RuntimeException{
    public InvoiceNotUpdatedException(String msg) {
        super(msg);
    }
}
