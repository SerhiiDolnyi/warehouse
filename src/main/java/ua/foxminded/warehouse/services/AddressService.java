package ua.foxminded.warehouse.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.foxminded.warehouse.models.Address;
import ua.foxminded.warehouse.repositories.AddressRepository;

import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.warehouse.restcontrollers.AddressControler;
import ua.foxminded.warehouse.util.exception.address.AddressNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AddressService {
    AddressRepository addressRepository;
    private static final Logger log = LoggerFactory.getLogger(AddressControler.class);

    @Autowired
    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public List<Address> findAll(){
        return addressRepository.findAll();
    }

    public Address findById(int addressId){
        Optional<Address> foundAddress = addressRepository.findById(addressId);
        return foundAddress.orElseThrow(AddressNotFoundException::new);
    }

    @Transactional
    public void save (Address address){
        addressRepository.save(address);
    }

    @Transactional
    public void update (int addressId, Address updatedAddress){
        updatedAddress.setId(addressId);
        addressRepository.save(updatedAddress);
    }

    @Transactional
    public void delete (int addressId){
        addressRepository.deleteById(addressId);
    }
}
