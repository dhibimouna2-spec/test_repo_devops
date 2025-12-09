package prestify.com.prestify.dao.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;  // Utiliser la bonne classe Pageable
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import prestify.com.prestify.dao.entities.Offer;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {

    List<Offer> getOffreSortedByTitle(String title); 

    List<Offer> findByTitle(String title);    

    List<Offer> findByLocation(String location);    

    Page<Offer> findByCategory(String category, Pageable pageable);   


// Si vous voulez filtrer par le nom de la catégorie
// Utilisez 'nom' et non 'name'
List<Offer> getOfferByCategoryNom(String nom);



    //List<Offer> findByFournisseurId(Long fournisseurId); 
    
   
    //Page<Offer> findByFournisseurId(Long fournisseurId, Pageable pageable);

}
