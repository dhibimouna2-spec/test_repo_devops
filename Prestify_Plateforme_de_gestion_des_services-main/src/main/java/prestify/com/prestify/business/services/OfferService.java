package prestify.com.prestify.business.services;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import prestify.com.prestify.dao.entities.Offer;

public interface OfferService {

    List<Offer> getAllOffres(); 

    Offer getOffreById(Long id); 

    List<Offer> getOffreByTitre(String titre); 
    
    List<Offer> getOffreSortedByPrix(String order); 

    
   Page<Offer> getAllOffrePagination(Pageable pageable);
    
   Page<Offer> getOffreSortedByPrixPagination(String order, Pageable pageable);

    
    Offer addOffre(Offer offre);


    void deleteOffreById(Long id);

    public void save(Offer offre);
   
    List<Offer> findAllOffers(); 

    Page<Offer> getOfferByLocationPagination(String location, Pageable pageable);    

    List<Offer> getOffreByLocation(String location); 

    List<Offer> getOffersByCategory(String categoryName);
    public Offer updateOffer(Offer offer);   
// Utilisez 'nom' et non 'name'
List<Offer> getOfferByCategoryNom(String nom); 


   
   // List<Offer> getOffresByFournisseurId(Long fournisseurId); 

   // public Page<Offer> getOffersByFournisseurId(Long fournisseurId, Pageable pageable);

   /*  Page<Offre> getOffreByEtatPagination(String etat, Pageable pageable);

    Page<Offre> getOffreSortedByDureePagination(String sortByDuree, Pageable pageable);

    List<Offre> getOffresByEtat(String etat);

    Page<Offre> getAllOffresPagines(Pageable pageable); 
*/
    
}
 

