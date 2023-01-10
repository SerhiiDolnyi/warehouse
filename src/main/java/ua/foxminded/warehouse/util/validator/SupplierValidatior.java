package ua.foxminded.warehouse.util.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ua.foxminded.warehouse.dto.SupplierDTO;
import ua.foxminded.warehouse.models.Supplier;
import ua.foxminded.warehouse.repositories.SupplierRepository;

@Component
public class SupplierValidatior implements Validator {
    private SupplierRepository supplierRepository;

    @Autowired
    public SupplierValidatior(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Supplier.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SupplierDTO supplierDTO = (SupplierDTO) target;
        if(supplierRepository.findSupplierByName(supplierDTO.getName()).isPresent()){
            errors.rejectValue("name", "", "This supplier name already exists");
        }
    }
}
