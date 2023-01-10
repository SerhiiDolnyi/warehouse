package ua.foxminded.warehouse.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.foxminded.warehouse.models.Address;
import ua.foxminded.warehouse.models.Offer;
import ua.foxminded.warehouse.repositories.OfferRepository;
import ua.foxminded.warehouse.util.exception.offer.OfferNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class OfferService {
    OfferRepository offerRepository;

    @Autowired
    public OfferService(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    public List<Offer> findAll(){
        return offerRepository.findAll();
    }

    public Offer findById(int offerId){
        Optional<Offer> foundOffer = offerRepository.findById(offerId);
        return foundOffer.orElseThrow(OfferNotFoundException::new);
    }

    @Transactional
    public void save (Offer offer){
        offerRepository.save(offer);
    }

    @Transactional
    public void update (int offerId, Offer updatedOffer){
        updatedOffer.setId(offerId);
        offerRepository.save(updatedOffer);
    }

    @Transactional
    public void delete (int offerId){
        offerRepository.deleteById(offerId);
    }
}
