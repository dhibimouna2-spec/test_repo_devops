package prestify.com.prestify.web.controllers;
import org.springframework.data.domain.PageRequest;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import prestify.com.prestify.business.services.CategorieService;
import prestify.com.prestify.business.services.OfferService;
import prestify.com.prestify.dao.entities.Offer;
import prestify.com.prestify.web.models.OfferForm;



@Controller
public class OfferController {

    

    public static String uploadDirectory = System.getProperty("user.dir") + "/src/main/resources/static/images";

    private final OfferService offerService; 
    private final CategorieService categorieService; 

    public OfferController(OfferService offerService,CategorieService categorieService){ 

        this.offerService = offerService; 
        this.categorieService = categorieService;
    }
   
   

   


  @RequestMapping("/offers")
  @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT', 'SUPPLIER')")
   public String getOfferSorted(@RequestParam(required = false, defaultValue = "asc") String sortByPrix,
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "3") int pageSize,
          Model model) { 

            Page<Offer> offrePage = this.offerService.getOffreSortedByPrixPagination(sortByPrix, 
                                         PageRequest.of(page, pageSize)); 
        model.addAttribute("offers",offrePage.getContent());
        model.addAttribute("sortByAge", sortByPrix);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", offrePage.getTotalPages());
        return "offers/offerlist";
    }

    @RequestMapping("/offers/create")
    public String showAddOffreForm(Model model) { 
        model.addAttribute("offerForm", new OfferForm());
        model.addAttribute("categorie", this.categorieService.getAllCategories()); 

        return "offers/add-offer";
    }
 

    @RequestMapping(path = "/offers/create", method = RequestMethod.POST)
public String addOffer(@Valid @ModelAttribute OfferForm offerForm,
                       BindingResult bindingResult,
                       Model model,
                       @RequestParam MultipartFile file) {

                        if (bindingResult.hasErrors()) {
                            bindingResult.getAllErrors().forEach(error -> {
                                System.out.println("Validation error: " + error.getDefaultMessage());
                            });
                            model.addAttribute("categorie", this.categorieService.getAllCategories());
                            return "offers/add-offer";
                        }

                         // Initialise le chemin de l'image

                        if (!file.isEmpty()) {
                            String fileName = file.getOriginalFilename();
                            Path newFilePath = Paths.get(uploadDirectory, fileName);
                
                            try {
                                Files.write(newFilePath, file.getBytes());
                                uploadDirectory = "/images/" + fileName; // Chemin relatif
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
        

    // Créer l'offre avec ou sans image et l'ajouter
    this.offerService.addOffre(new Offer(
        null,  // Id généré automatiquement
        offerForm.getTitle(),
        offerForm.getDescription(),
        offerForm.getPrice(),
        offerForm.getDuration(),
        offerForm.getCategory(),
        offerForm.getStatus(),
        uploadDirectory,  // Utiliser le chemin de l'image si présent
        offerForm.getIncludes(),
        offerForm.getExcludes(),
        offerForm.getLocation(),
        offerForm.getPhoneNumber()
    ));

    // Rediriger vers la liste des offres après l'ajout
    return "redirect:/offers";  
}
   





    @RequestMapping("/offers/{id}/edit")
    public String showEditOfferForm(@PathVariable Long id, Model model) {
        
    
        Offer offreUpdate = this.offerService.getOffreById(id);
        model.addAttribute("offerForm",new OfferForm(offreUpdate.getTitle(), offreUpdate.getDescription(), offreUpdate.getPrice(), offreUpdate.getDuration(), offreUpdate.getStatus(), offreUpdate.getCategory(), offreUpdate.getIncludes(), offreUpdate.getExcludes(),offreUpdate.getLocation(), offreUpdate.getPhoneNumber()));
        model.addAttribute("categorie", this.categorieService.getAllCategories());  
        model.addAttribute("imagePath", offreUpdate.getImage()); 
                         return "offers/edit-offer";

       
    }

    @RequestMapping(path = "/offers/{id}/edit", method = RequestMethod.POST)
public String editOffre(@Valid @ModelAttribute OfferForm offerForm,
                        BindingResult bindingResult,
                        @PathVariable Long id,
                        Model model,
                        @RequestParam MultipartFile file) {
    if (bindingResult.hasErrors()) {
        return "offers/edit-offer";
    }

    Offer offreUpdate = this.offerService.getOffreById(id);
   

    offreUpdate.setTitle(offerForm.getTitle());
    offreUpdate.setDescription(offerForm.getDescription());
    offreUpdate.setPrice(offerForm.getPrice());
    offreUpdate.setDuration(offerForm.getDuration());
    offreUpdate.setStatus(offerForm.getStatus());
    offreUpdate.setCategory(offerForm.getCategory());
    offreUpdate.setIncludes(offerForm.getIncludes());
    offreUpdate.setExcludes(offerForm.getExcludes()); 
    offreUpdate.setLocation(offerForm.getLocation());

    if (!file.isEmpty()) {
        String fileName = file.getOriginalFilename();
        Path newFilePath = Paths.get(uploadDirectory, fileName);
        try {
            Files.write(newFilePath, file.getBytes());
            String imagePath = "/images/" + fileName;
            offreUpdate.setImage(imagePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    this.offerService.updateOffer(offreUpdate);
    System.out.println("Offer updated successfully with ID: " + id);

    return "redirect:/offers";
}

    @RequestMapping(path = "/offers/{id}/delete", method = RequestMethod.POST)
public String deleteOffre(@PathVariable Long id, Model model) {
    // Supprimer l'offre
    this.offerService.deleteOffreById(id);

    // Ajouter la liste mise à jour des offres au modèle
    model.addAttribute("offers", this.offerService.findAllOffers());

    // Rediriger vers la page qui affiche la liste mise à jour
    return "offers/offerlist"; // Ici, "offers/offer-list" est le nom du fichier template Thymeleaf pour afficher la liste
}


@GetMapping("/offers/filtrer")
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

    return "offers/offerlist"; // Retourner la vue  
}

    




        @GetMapping("/offers/filtrer/location")
        public String searchLogementsByLocation(
        @RequestParam("location") String location, // Type spécifié par l'utilisateur
        Model model) {
    
        // Récupérer les logements filtrés par type 
        model.addAttribute("offers", offerService.getOffreByLocation(location)); 
        model.addAttribute("categorie",this.categorieService.getAllCategories()); 
        model.addAttribute("searchCategory", location);
        
        return "offers/offerlist"; // Retourner la même vue pour afficher les résultats
}
    
    @GetMapping("/offer/{id}")
    public String showOfferDetails(@PathVariable Long id, Model model) {
        // Récupérer l'offre par son ID
        Offer offer = offerService.getOffreById(id);
        
        // Ajouter l'offre au modèle
        model.addAttribute("offer", offer);
        
        // Retourner le nom de la vue (par exemple, "offerDetails.html")
        return "offers/moredetails";
    }


    @GetMapping("/fournisseurindex")
    public String showFournisseurIndex() {
        return "fournisseurindex";  // This will map to a template like "fournisseurindex.html"
    }


    }


// Afficher les offres d'un fournisseur spécifique
  /*  @RequestMapping("/offers/fournisseur/{id}")
   public String getOffersByFournisseur(@PathVariable Long id, 
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "3") int pageSize,
                                     Model model) { 

    Page<Offer> offerPage = this.offerService.getOffersByFournisseurId(id, PageRequest.of(page, pageSize));

    model.addAttribute("offres", offerPage.getContent());
    model.addAttribute("pageSize", pageSize); 
    model.addAttribute("currentPage", page); 
    model.addAttribute("totalPages", offerPage.getTotalPages());

    return "offer-list"; 
} */

