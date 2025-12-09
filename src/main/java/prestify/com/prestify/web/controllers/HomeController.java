package prestify.com.prestify.web.controllers;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestParam;

import prestify.com.prestify.business.services.CategorieService;
import prestify.com.prestify.business.services.OfferService;
import prestify.com.prestify.dao.entities.Categorie;
import prestify.com.prestify.dao.entities.Offer;

@Controller
public class HomeController {

    private final OfferService offerService; 
    private final CategorieService categorieService;


    // Le nom du constructeur doit correspondre à celui du contrôleur
    public HomeController(OfferService offerService,CategorieService categorieService) { 
        this.offerService = offerService; 
        this.categorieService = categorieService;
      
    }

  
    @GetMapping("/client/offers")  
    public String getOffers(Model model) { 
        List<Offer> offers = offerService.findAllOffers(); // Récupérer toutes les offres
        List<Categorie> categories = categorieService.findAll(); // Récupérer toutes les catégories
        model.addAttribute("offers", offers); // Ajouter les offres au modèle
        model.addAttribute("categories", categories); // Ajouter les catégories au modèle
        return "clients/Service"; // Retourner la vue qui affiche les offres et les catégories
    } 
   


    @GetMapping("/services")
    public String getServicesByCategory(@RequestParam(required = false) String category, Model model) {
        // Log pour vérifier la catégorie reçue
        if (category != null && !category.isEmpty()) {
            System.out.println("Filtering offers by category: " + category); // Utilisation correcte
        } else {
            System.out.println("No category selected, retrieving all offers.");
        }
    
        // Récupérer toutes les catégories en une seule requête
        List<Categorie> categories = categorieService.findAll();
        
        // Initialiser la liste des offres
        List<Offer> offers;
        if (category != null && !category.isEmpty()) {
            // Récupérer les offres filtrées par catégorie
            offers = offerService.getOfferByCategoryNom(category);
            if (offers.isEmpty()) {
                System.out.println("No offers found for category: " + category);
            }
        } else {
            // Récupérer toutes les offres si aucune catégorie n'est spécifiée
            offers = offerService.findAllOffers();
        }
    
        // Ajouter les données nécessaires au modèle
        model.addAttribute("categories", categories);
        model.addAttribute("category", category);  // Ajouter la catégorie sélectionnée pour le contexte Thymeleaf
        model.addAttribute("offers", offers);
    
        return "clients/Service"; // Nom de la vue
    }
    



    @GetMapping("/client/offer/{id}")
    public String showOfferDetails(@PathVariable Long id, Model model) {
        Offer offer = offerService.getOffreById(id);
        model.addAttribute("offer", offer);
        return "client/offer-details";
    }

@GetMapping("/client/offer/filtrer")
public String offersSorted(
        @RequestParam(required = false, defaultValue = "asc") String sortByPrix, // Default sorting by price in ascending order
        @RequestParam(defaultValue = "0") int page, // Default page is 0 (first page)
        @RequestParam(defaultValue = "3") int pageSize, // Default page size is 3
        Model model) {

    // Récupérer les offres triées par prix avec pagination 
    Page<Offer> offerPage = this.offerService.getOffreSortedByPrixPagination(sortByPrix, PageRequest.of(page, pageSize));
   
    // Ajouter les données au modèle 
    model.addAttribute("offers", offerPage.getContent()); // Liste des offres
    model.addAttribute("sortByPrix", sortByPrix); // Direction de tri (asc/desc)
    model.addAttribute("pageSize", pageSize); // Taille de la page pour la pagination
    model.addAttribute("currentPage", page); // Numéro de la page actuelle
    model.addAttribute("totalPages", offerPage.getTotalPages()); // Nombre total de pages

    return "clients/Service"; // Retourner la vue  
}
        @GetMapping("/client/offers/filtrer/location")
        public String searchLogementsByLocation(
        @RequestParam("location") String location, // Type spécifié par l'utilisateur
        Model model) {
    
        // Récupérer les logements filtrés par type 
        model.addAttribute("offers", offerService.getOffreByLocation(location)); 
        model.addAttribute("categorie",this.categorieService.getAllCategories()); 
        model.addAttribute("searchCategory", location);
        
        return "clients/Service"; // Retourner la même vue pour afficher les résultats
}
    

}
