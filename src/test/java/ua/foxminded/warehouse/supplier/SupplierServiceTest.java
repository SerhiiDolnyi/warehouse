package ua.foxminded.warehouse.supplier;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ua.foxminded.warehouse.models.Supplier;
import ua.foxminded.warehouse.repositories.SupplierRepository;
import ua.foxminded.warehouse.services.SupplierService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SupplierServiceTest {
    @Mock
    private SupplierRepository supplierRepository;

    @InjectMocks
    private SupplierService supplierService;

    @Test
    public void findByIdTest_ShouldReturnSupplier(){
        Supplier supplier = new Supplier("Supplier_Name", 3);
        when(supplierRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(supplier));
        assertEquals(supplier, supplierService.findById(1));
        verify(supplierRepository).findById(1);
    }

    @Test
    public void findAllTest_ShouldReturnAllSupplier(){
        Supplier supplier1 = new Supplier("Supplier_Name1", 3);
        Supplier supplier2 = new Supplier("Supplier_Name2", 3);
        List<Supplier> suppliers = new ArrayList<>();
        suppliers.add(supplier1);
        suppliers.add(supplier2);
        when(supplierRepository.findAll()).thenReturn(suppliers);
        assertEquals(suppliers, supplierService.findAll());
        verify(supplierRepository).findAll();
    }

    @Test
    public void saveSupplierTest_ShouldVerifySaveMethodOfSupplierRepository() {
        Supplier supplier = new Supplier();
        supplierService.save(supplier);
        verify(supplierRepository).save(supplier);
    }

    @Test
    public void updateSupplierTest_ShouldVerifySaveMethodOSupplierRepository() {
        Supplier supplier = new Supplier();
        supplierService.update(2, supplier);
        verify(supplierRepository).save(supplier);
    }

    @Test
    public void deleteSupplierServiceTest_ShouldVerifyDeleteMethodOfSupplierRepository() {
        supplierService.delete(1);
        verify(supplierRepository).deleteById(1);
    }
}
