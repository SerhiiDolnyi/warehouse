package ua.foxminded.warehouse.util.exception.invoice;

public class InvoiceNotCreatedException extends RuntimeException {
    public InvoiceNotCreatedException(String msg) {
        super(msg);
    }
}
