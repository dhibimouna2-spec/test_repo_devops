
package prestify.com.prestify.web.models;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CategorieForm {

    @NotBlank(message = "Le nom de la catégorie est obligatoire !")
    @Size(max = 100, message = "Le nom de la catégorie ne doit pas dépasser 100 caractères !")
    private String nom;

    public CategorieForm(String nom) {
        this.nom = nom;
    }
}
