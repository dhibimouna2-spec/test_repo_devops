package prestify.com.prestify.dao.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "offers")
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Double price;

    @Column(name = "duration")
    private int duration;

    @ManyToOne
    @JoinColumn(name = "category_id") // Assurez-vous que la colonne de la base de données soit category_id
    private Categorie category;

    @Column(name = "status")
    private String status;

    @Column(name = "image")
    private String image;

    @Column(name = "includes")
    private String includes;

    @Column(name = "excludes")
    private String excludes;

    @Column(name = "location")
    private String location;

    @Column(name = "phoneNumber", length = 15, nullable = false)
    private String phoneNumber;

    public Offer(Long id, String title, String description, Double price, int duration, Categorie category,
                 String status, String image, String includes, String excludes, String location, String phoneNumber) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.category = category;
        this.status = status;
        this.image = image;
        this.includes = includes;
        this.excludes = excludes;
        this.location = location;
        this.phoneNumber = phoneNumber;
    }
}
