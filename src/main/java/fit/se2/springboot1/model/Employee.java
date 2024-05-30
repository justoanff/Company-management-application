package fit.se2.springboot1.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;


@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Length(min = 3, max = 30)
    private String name;
    @Min(18)
    @Max(55)
    private int age;
    @NotEmpty(message = "Image cannot be empty")
    private String image;
    private String address;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @ManyToOne
    private Company company;

    public Employee() {

    }

    public Employee(Long id, String name, int age, String image, String address, Gender gender, Company company) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.image = image;
        this.address = address;
        this.gender = gender;
        this.company = company;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public enum Gender {
        MALE,
        FEMALE
    }
}
