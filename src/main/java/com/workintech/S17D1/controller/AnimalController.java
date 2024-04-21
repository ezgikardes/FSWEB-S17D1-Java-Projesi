package com.workintech.S17D1.controller;

import com.workintech.S17D1.entity.Animal;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/workintech/animal")
public class AnimalController {
    private Map<Integer, Animal> animals;

    @Value("${project.owner}") //config'de belirlediğimiz value'yu alıyoruz ve bir değişkene atıyoruz:
    private String projectOwner;

    @Value("${project.name}")
    private String projectName;

    @PostConstruct
    public void loadAll(){
        System.out.println("project owner değeri = " + projectOwner);
        System.out.println("project name değeri = " + projectName);
        this.animals = new HashMap<>();
        // bu noktada, new HashMap<>(); diyip bırakabilirdik.default değer atama bizim tercihimiz:
        this.animals.put(1, new Animal(1, "maymun"));
    } //@PostConstruct, uygulama ayağa kalkarken çalışır.
    // Uygulama ayakta olduğu anda animals map'ine default bir entry ekledim.
    // Bunu field kısmında yapamazdım çünkü put metodunu ancak bir metod içinde kullanabilirsin.
    // Ya main metodu ya da sınıf içindeki metodlarda.

    @GetMapping //burada endpoint'i belirtebiliriz ama uzatmamak için yukarıda @RequestMapping'i kullandık
    public List<Animal> getAnimals(){
        System.out.println("---animls list get trigerred---");
        return new ArrayList<>(this.animals.values()); //map'in value'larını array liste çevirdik.
    }

    @GetMapping("/{id}")
    public Animal getAnimalById(@PathVariable("id") Integer id){
        //içeride map metodu yazacağımız için parametre olarak referans tipi kullandık.
        //Çünkü animals map'i <Integer, Animal> entry'lerinden oluşuyor.
        return this.animals.get(id);
    }
    // bu metoddaki url aslında şu: /workintech/animal/{id}
    // client/kullanıcı bu URL'e bir istek gönderdiğinde Spring Boot ilgili controller'ı bulur
    // id path'i olan @GetMapping'i (getAnimalById) yakalar.
    // @PathVariable'daki id'yi alır ve bu parametreyi kullanarak metodu çağırır.
    // metodun içine id'yi verir.


    @PostMapping
    public void addAnimal(@RequestBody Animal animal){ //buraya id'si ve name'i olan bir Animal nesnesi (animal) parametre gelecek
        this.animals.put(animal.getId(),animal); //gelen animal nesnesini animals map'ine ekle.
        // İlk parametre, pu metodunun key'ine, animal'ın id'sini koymak için.
    }

    @PutMapping("/{id}")
    public Animal updateAnimal(@PathVariable("id") Integer id, @RequestBody Animal newAnimal){
        this.animals.replace(id, newAnimal);
        //eski kaydın id'siyle git o eski kaydı bul, onun yerine bana gelen newAnimal kaydını ekle.
        return this.animals.get(id);
    }

    @DeleteMapping("/{id}")
    public void deleteAnimal(@PathVariable("id") Integer id) {
        this.animals.remove(id);
    }
}
