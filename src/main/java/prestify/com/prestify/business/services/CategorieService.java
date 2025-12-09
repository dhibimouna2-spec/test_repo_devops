package prestify.com.prestify.business.services;

import java.util.List;



import prestify.com.prestify.dao.entities.Categorie;



public interface CategorieService {

   Categorie addCategorie(Categorie categorie);

    Categorie getCategorieById(Long id);

    List<Categorie> getAllCategories(); 

    void deleteCategorie(Long id);

    Categorie updateCategorie(Long id, Categorie categorie);   

    List<Categorie> findAll(); 

    public boolean existsByNom(String nom);
   
}
