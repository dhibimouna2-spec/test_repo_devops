package prestify.com.prestify.web.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.validation.Valid;
import prestify.com.prestify.business.services.CategorieService;
import prestify.com.prestify.dao.entities.Categorie;
import prestify.com.prestify.web.models.CategorieForm;

@Controller 
public class CategorieController {
      
    @Autowired
    private CategorieService categorieService;  


    @GetMapping("/categories")
    public String listCategories(Model model) {
        List<Categorie> categories = categorieService.getAllCategories();
        model.addAttribute("categories", categories);
        return "Categories/categoriesList"; // Nom du fichier HTML/Thymeleaf pour afficher la liste des groupes d'âge
    }   

}