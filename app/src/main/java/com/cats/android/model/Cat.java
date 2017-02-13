package com.cats.android.model;

import java.util.Objects;

/**
 * Created by andrey on 13.02.17.
 */

public class Cat {

    private Integer id;

    private int age;

    private String color;

    private String breed;

    private String name;

    private int weight;

    public Cat(int age, String color, String breed, String name, int weight) {
        this.age = age;
        this.color = color;
        this.breed = breed;
        this.name = name;
        this.weight = weight;
    }

    public Cat() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, age, weight, color, breed, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cat cat = (Cat) o;

        if (age != cat.age) return false;
        if (weight != cat.weight) return false;
        if (id != null ? !id.equals(cat.id) : cat.id != null) return false;
        if (color != null ? !color.equals(cat.color) : cat.color != null) return false;
        if (breed != null ? !breed.equals(cat.breed) : cat.breed != null) return false;
        return name != null ? name.equals(cat.name) : cat.name == null;
    }

    @Override
    public String toString() {
        return "Cat{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", color='" + color + '\'' +
                ", breed='" + breed + '\'' +
                ", weight=" + weight +
                '}';
    }
}
