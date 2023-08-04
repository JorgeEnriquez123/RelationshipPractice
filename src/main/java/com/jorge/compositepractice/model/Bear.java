package com.jorge.compositepractice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.util.Lazy;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
/*@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")*/
public class Bear {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String species;

    //PERSIST will persist the Zoo, and MERGE will update it with the attributes set when creating it
    //We avoid DELETE so when we delete a Bear, it won't delete the Zoo
    @ManyToOne(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
            })
    @JoinColumn(name = "zoo_id")
    @JsonBackReference
    private Zoo zoo;

}
