package ru.garant.catalog.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity // Превязка таблицы к классу в Java
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
public class Category {

    @Id // Первичный ключ
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Автоматическое присваивание id (AUTO_INCREMENT)
    private Long id;

    @Column(nullable = false, length = 255) // NOT_NULL, VARCHAR(255)
    private String name;

    @Column(nullable = false, unique = true, length = 255) // NOT_NULL, UNIQUE, VARCHAR(255)
    private String slug;

    @Column(columnDefinition = "TEXT") // Тип данных "TEXT"
    private String description;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "sort_order")
    private Integer sortOrder = 0;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at", updatable = false) // updatable - при UPDATE значение этого поля не меняется
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Ссылка на родительскую категорию
    @ManyToOne(fetch = FetchType.LAZY) // Родитель загружается только когда к нему обратятся
    @JoinColumn(name = "parent_id") // колонка parent_id - это внешний ключ на эту же таблицу
    private Category parent; // Ссылка на родителя

    // Дочерние категории
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    // mappedBy - за связь отвечает поле parent в классе Category. CascadeType.ALL - если удалишь родителя, то удалятся и дети тоже.
    private List<Category> children = new ArrayList<>();

    // Хуки - методы, которые вызываются автоматически
    @PrePersist // перед первым сохранением в БД (заполняем createdAt и updatedAt)
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate // перед обновлением (обновляем updatedAt)
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
