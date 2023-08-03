package com.jorge.compositepractice.model.DTO;

import com.jorge.compositepractice.model.Zoo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BearDTO {
    private long id;
    private String name;
    private String zooname;
}
