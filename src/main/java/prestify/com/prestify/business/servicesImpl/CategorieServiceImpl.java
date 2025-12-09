package prestify.com.prestify.business.servicesImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import prestify.com.prestify.business.services.CategorieService;
import prestify.com.prestify.dao.entities.Categorie;
import prestify.com.prestify.dao.repositories.CategorieRepository;

@Service
public class CategorieServiceImpl implements CategorieService{
      
    @Autowired 
    
    private CategorieRepository categorieRepository; 
    
    @Override 
    public Categorie addCategorie(Categorie categorie) {
        if(categorie==null){
            return null;
        }
       return categorieRepository.save(categorie);
    }   

    
    @Override
    public Categorie getCategorieById(Long id) {
        if(id==null){
            return null;
        }
        return this.categorieRepository
        .findById(id) 
        .orElseThrow(()->new EntityNotFoundException("Categorie with id:"+id+"not found"));
    }

    @Override
    public List<Categorie> getAllCategories() { 

        return categorieRepository.findAll();
        
    }

    @Override
    public void deleteCategorie(Long id) {
        if(id==null){
            return ;
        } else if(this.categorieRepository.existsById(id)){
            this.categorieRepository.deleteById(id);
        }else{
            throw new EntityNotFoundException("Age Group with id: "+id+" not found");
        }   
    }

    @Override
    public Categorie updateCategorie(Long id, Categorie categorie) { 
            
        
        Categorie existingCategorie = this.getCategorieById(id);
            

           existingCategorie.setNom(categorie.getNom());

           return categorieRepository.save(existingCategorie);  

        
    }
    @Override 
    public List<Categorie> findAll() {
     return categorieRepository.findAll();
 }
     
 @Override 
 public boolean existsByNom(String nom) {
    return categorieRepository.existsByNom(nom);
}

}











