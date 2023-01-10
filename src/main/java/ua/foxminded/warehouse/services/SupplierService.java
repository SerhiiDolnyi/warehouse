package ua.foxminded.warehouse.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.warehouse.models.Address;
import ua.foxminded.warehouse.models.Supplier;
import ua.foxminded.warehouse.repositories.SupplierRepository;
import ua.foxminded.warehouse.util.exception.supplier.SupplierNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SupplierService {
    SupplierRepository supplierRepository;

    @Autowired
    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public List<Supplier> findAll(){
        return supplierRepository.findAll();
    }

    public Supplier findByName(String name) {
        Optional<Supplier> foundSupllier = supplierRepository.findSupplierByName(name);
        return foundSupllier.orElseThrow(SupplierNotFoundException::new);
    }

    public Supplier findById(int supplierId){
        Optional<Supplier> foundSupplier = supplierRepository.findById(supplierId);
        return foundSupplier.orElseThrow(SupplierNotFoundException::new);
    }

    @Transactional
    public void save (Supplier supplier){
        supplierRepository.save(supplier);
    }

    @Transactional
    public void update (int supplierId, Supplier updatedSupplier){
        updatedSupplier.setId(supplierId);
        supplierRepository.save(updatedSupplier);
    }

    @Transactional
    public void delete (int supplierId){
        supplierRepository.deleteById(supplierId);
    }
}
