package ua.foxminded.warehouse.supplier;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import ua.foxminded.warehouse.models.Supplier;
import ua.foxminded.warehouse.repositories.SupplierRepository;
import ua.foxminded.warehouse.services.SupplierService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource("/application-test.properties")
@SpringBootTest
public class SupplierDBIntegrationTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private SupplierRepository supplierRepository;

    @Value("${sql.script.create.address}")
    private String sqlAddAddress;

    @Value("${sql.script.create.address3}")
    private String sqlAddAddress3;

    @Value("${sql.script.create.supplier2}")
    private String sqlAddSupplier2;

    @Value("${sql.script.create.supplier3}")
    private String sqlAddSupplier3;

    @Value("${sql.script.delete.address}")
    private String sqlDeleteAddresses;

    @Value("${sql.script.delete.supplier}")
    private String sqlDeleteSupplier;

    @BeforeEach
    public void setupDatabase() {
        jdbcTemplate.execute(sqlAddAddress);
        jdbcTemplate.execute(sqlAddSupplier2);
    }

    @AfterEach
    public void setupAfterTransaction(){
        jdbcTemplate.execute(sqlDeleteSupplier);
        jdbcTemplate.execute(sqlDeleteAddresses);
    }

    @Test
    public void findAllSupplier_ShouldReturnSizeOfList() {
        jdbcTemplate.execute(sqlAddAddress3);
        jdbcTemplate.execute(sqlAddSupplier3);
        List<Supplier> suppliers = supplierService.findAll();
        assertEquals(2, suppliers.size());
    }

    @ParameterizedTest
    @CsvSource({"0, 7", "1, 5"})
    public void findAllSupplier_ShouldReturnReturnRateOfSupplier(int index, int returnRate) {
        jdbcTemplate.execute(sqlAddAddress3);
        jdbcTemplate.execute(sqlAddSupplier3);
        List <Supplier> suppliers = supplierService.findAll();
        Supplier supplier = suppliers.get(index);
        assertEquals(returnRate, supplier.getGoodsReturnRate());
    }

    @Test
    public void findSupplierByIdTest_ShouldReturnNameOfFoundSupplier() {
        Supplier foundSupplier = supplierService.findById(2);
        assertEquals("test_SupplierName2", foundSupplier.getName());
    }

    @Test
    public void saveSupplierTest_ShouldReturnNameOfSavedCustomer() {
        Supplier supplier = new Supplier("Saved_Supplier_Name", 3);
        supplierService.save(supplier);
        Supplier savedSupplier = supplierRepository.findSupplierByName("Saved_Supplier_Name").get();
        assertEquals("Saved_Supplier_Name", savedSupplier.getName());
    }

    @Test
    public void deleteSupplierTest_ShouldReturnFalse(){
        Optional<Supplier> deletedSupplier = supplierRepository.findById(2);
        assertTrue(deletedSupplier.isPresent());
        supplierService.delete(2);
        deletedSupplier = supplierRepository.findById(2);
        assertFalse(deletedSupplier.isPresent());
    }

    @Test
    public void updateSupplierTest_ShouldReturnUpdatedName(){
        Optional<Supplier> supplier = supplierRepository.findById(2);
        assertTrue(supplier.isPresent());
        Supplier supplierToUpdate = new Supplier("Updated_Supplier_Name", 3);
        supplierService.update(2, supplierToUpdate);
        Supplier updatedSupplier = supplierRepository.findById(2).get();
        assertEquals("Updated_Supplier_Name", updatedSupplier.getName());
    }
}
