package prestify.com.prestify.business.servicesImpl;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import prestify.com.prestify.business.services.OfferService;
import prestify.com.prestify.dao.entities.Offer;
import prestify.com.prestify.dao.repositories.OfferRepository;

import java.util.List;

@Service
public class OfferServiceImpl implements OfferService {

    final OfferRepository offreRepository;

    public OfferServiceImpl(OfferRepository offreRepository) {
        this.offreRepository = offreRepository;
    }

    @Override
    public List<Offer> getAllOffres() {
        return offreRepository.findAll();
    } 

   
   
    

    @Override
    public void save(Offer offre) {
    
    offreRepository.save(offre);
}

    @Override
    public Offer getOffreById(Long id) {
        if (id == null) {
            return null;
        }
        return offreRepository.findById(id).get();
    }

    @Override 
    public Offer addOffre(Offer offre) {  
        if (offre == null) {
            return null; 
        }
        return this.offreRepository.save(offre);
    }

    
  
    
    @Override 
    public void deleteOffreById(Long id){ 
         
        if(id==null){
            return ;
        }
         this.offreRepository.deleteById(id);
    }
    

    @Override 
    public List<Offer> getOffreByTitre(String title){ 

        return this.offreRepository.findByTitle(title);
    }
    

    @Override 
    public List<Offer> getOffreByLocation(String location){ 

        return this.offreRepository.findByLocation(location);
    }
    

    @Override 
    public List<Offer> getOffreSortedByPrix(String order){ 

        Sort.Direction direction = Sort.Direction.ASC; 
        if("desc".equalsIgnoreCase(order)){
            direction= Sort.Direction.DESC;
        } 

        return offreRepository.findAll(Sort.by(direction, "price"));
    }

    

    @Override 
    public Page<Offer> getAllOffrePagination(Pageable pageable){ 

        if(pageable ==null){ 
            return null;
        } 
        return this.offreRepository.findAll(pageable);
    }
    

    @Override 
    public  Page<Offer> getOffreSortedByPrixPagination(String order, Pageable pageable) { 

        if(pageable ==null){
            return null;
        }  
        Sort.Direction direction= Sort.Direction.ASC;
        if("desc".equalsIgnoreCase(order)){
            direction= Sort.Direction.DESC;
        }
        Pageable sortedPageable=PageRequest.of(
            pageable.getPageNumber(),
            pageable.getPageSize(),
            Sort.by(direction,"price")
        ); 
        return this.offreRepository.findAll(sortedPageable);

    }
    @Override 
    public List<Offer> findAllOffers() {
        return offreRepository.findAll();
    }

    @Override 
    public Page<Offer> getOfferByLocationPagination(String location, Pageable pageable) {
        return offreRepository.findByCategory(location, pageable);
    }  


    @Override 
public List<Offer> getOfferByCategoryNom(String categoryNom) {
    return offreRepository.getOfferByCategoryNom(categoryNom);
}

    //@Override 
    //public List<Offer> getOffresByFournisseurId(Long fournisseurId) {
        //return offreRepository.findByFournisseurId(fournisseurId);
   // }  

    //@Override
    //public Page<Offer> getOffersByFournisseurId(Long fournisseurId, Pageable pageable) {
      //  return offreRepository.findByFournisseurId(fournisseurId, null);
    //}
    
   

    @Override
    public Offer updateOffer(Offer offer) {
        if (offer == null || offer.getId() == null) {
            return null; 
        }
        if (!offreRepository.existsById(offer.getId())) {
            return null; 
        }
        return offreRepository.save(offer); 
    }
    
    public List<Offer> getOffersByCategory(String categoryName) {
        return offreRepository.getOfferByCategoryNom(categoryName);
    }
    
}
