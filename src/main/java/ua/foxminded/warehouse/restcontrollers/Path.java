package ua.foxminded.warehouse.restcontrollers;

public final class Path {
    public Path() {
    }

    public static final String ADDRESS = "/address";
    public static final String GET_ALL_ADDRESS = "/getAllAddress";
    public static final String CREATE_ADDRESS = "/creatAddress";
    public static final String UPDATE_ADDRESS = "/{id}/editAddress";
    public static final String GET_ADDRESS_BY_ID = "/getAddressByID/{id}";
    public static final String DELETE_ADDRESS_BY_ID = "/deleteAddressByID/{id}";

    public static final String CUSTOMER = "/customer";
    public static final String GET_ALL_CUSTOMER = "/getAllCustomer";
    public static final String CREATE_CUSTOMER = "/createCustomer";
    public static final String UPDATE_CUSTOMER = "/{id}/editCustomer";
    public static final String GET_CUSTOMER_BY_ID = "/getCustomerById/{id}";
    public static final String DELETE_CUSTOMER_BY_ID = "/deleteCustomerById/{id}";

    public static final String INVOICE = "/invoice";
    public static final String GET_ALL_INVOICE = "/getAllInvoice";
    public static final String CREATE_INVOICE = "/createInvoice";
    public static final String UPDATE_INVOICE = "/{id}/editInvoice";
    public static final String GET_INVOICE_BY_ID = "/getInvoiceById/{id}";
    public static final String DELETE_INVOICE_BY_ID = "/deleteInvoiceById/{id}";

    public static final String ITEM = "/item";
    public static final String GET_ALL_ITEM = "/getAllItem";
    public static final String CREATE_ITEM = "/createItem";
    public static final String UPDATE_ITEM = "/{id}/editItem";
    public static final String GET_ITEM_BY_ID = "/getItemById/{id}";
    public static final String DELETE_ITEM_BY_ID = "/deleteItemById/{id}";

    public static final String OFFER = "/offer";
    public static final String GET_ALL_OFFER = "/getAllOffer";
    public static final String CREATE_OFFER = "/createOffer";
    public static final String UPDATE_OFFER = "/{id}/editOffer";
    public static final String GET_OFFER_BY_ID = "/getOfferById/{id}";
    public static final String DELETE_OFFER_BY_ID = "/deleteOfferById/{id}";

    public static final String SUPPLIER = "/supplier";
    public static final String GET_ALL_SUPPLIER = "/getAllSupplier";
    public static final String CREATE_SUPPLIER = "/createSupplier";
    public static final String UPDATE_SUPPLIER = "/{id}/editSupplier";
    public static final String GET_SUPPLIER_BY_ID = "/getSupplierById/{id}";
    public static final String DELETE_SUPPLIER_BY_ID = "/deleteSupplierById/{id}";

    public static final String ADMIN = "/admin";
    public static final String GET_ALL_USER = "/getAllUser";
    public static final String CREATE_USER = "/createUser";
    public static final String UPDATE_USER = "/{id}/editUser";
    public static final String GET_USER_BY_ID = "/getUserById/{id}";
    public static final String DELETE_USER_BY_ID = "/deleteUserById/{id}";

    public static final String MANAGER = "/manager";
    public static final String ITEM_BY_AMOUNT_LESS_THAN = "/itemByAmountLessThan";
    public static final String INVOICES_BY_CUSTOMER_ID = "/invoicesByCustomerId";
    public static final String OFFER_BY_SUPPLIER_ID = "/offerBySupplierId";
    public static final String FIRST_3_CUSTOMERS_BY_MOST_EXPENSIVE_INVOICE = "/first3CustomerByTheMostExpensiveInvoice";
    public static final String INVOICES_BY_CUSTOMERRATE_AND_PRICE = "/invoicesByCustomerRateAndPrice";
    public static final String OFFERS_BY_PRICE_AND_SUPPLIERCITY = "/offersByPriceAndSupplierCity";

    public static final String AUTH = "/auth";
    public static final String PROCESS_LOGIN = "/process_login";
    public static final String REGISTRATION = "/registration";
}
