package model.course;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.Teacher;
import model.course.cons.DifficultyLevel;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cid;
    private String courseCode;
    private String courseName;
    private String description;
    private DifficultyLevel difficultyLevel;
    private List<Long> prerequisiteCourseIds;
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", nullable = false)
    private Teacher teacher;

    @JsonIgnore
    @OneToMany(mappedBy = "mid",cascade = CascadeType.ALL)
    private List<Module> modules;
}

