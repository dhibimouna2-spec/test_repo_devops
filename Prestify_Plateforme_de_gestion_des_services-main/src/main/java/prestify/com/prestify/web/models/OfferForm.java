package prestify.com.prestify.web.models;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import prestify.com.prestify.dao.entities.Categorie;

@NoArgsConstructor
@Getter
@Setter
public class OfferForm {

    @NotBlank(message = "Le titre est obligatoire !")
    @Size(max = 100, message = "Le titre ne doit pas dépasser 20 caractères !")
    private String title;

    @NotBlank(message = "La description est obligatoire !")
    private String description;

    @NotNull(message = "Le prix est obligatoire")
    private Double price;

    @NotNull(message = "Veuillez indiquer la durée !")
    @Positive(message = "La durée doit être un nombre positif")
    private int duration;

    @NotBlank(message = "L'état de l'offre est obligatoire !")
    private String status;

    private MultipartFile image;  


    private Categorie category;

    @NotBlank(message = "Les éléments inclus sont obligatoires")
    @Size(max = 1000, message = "Les éléments inclus ne doivent pas dépasser 1000 caractères")
    private String includes;

    @NotBlank(message = "Les éléments exclus sont obligatoires")
    @Size(max = 1000, message = "Les éléments exclus ne doivent pas dépasser 1000 caractères")
    private String excludes;

    @NotBlank(message = "La localisation est obligatoire !")
    @Size(max = 255, message = "La localisation ne doit pas dépasser 255 caractères")
    private String location;

    @NotBlank(message = "Le numéro de téléphone est obligatoire !")
    private String phoneNumber;

    public OfferForm(String title, String description, Double price, int duration, String status, Categorie category, String includes, String excludes, String location, String phoneNumber) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.status = status;
        this.category = category;
        this.includes = includes;
        this.excludes = excludes;
        this.location = location;
        this.phoneNumber = phoneNumber;
    }
}
