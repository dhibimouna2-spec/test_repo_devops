package prestify.com.prestify.dao.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import prestify.com.prestify.dao.entities.Categorie;

public interface CategorieRepository extends JpaRepository<Categorie,Long> {
    boolean existsByNom(String nom);

}