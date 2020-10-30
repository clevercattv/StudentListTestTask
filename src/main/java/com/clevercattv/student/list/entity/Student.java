package com.clevercattv.student.list.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.relational.core.mapping.Table;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.clevercattv.student.list.util.StudentValidationConstant.*;

@Data
@Table(value = "STUDENT")
@Entity(name = "STUDENT")
@EqualsAndHashCode(of = {"firstName", "lastName", "university", "specialty", "semester", "age"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Student implements Serializable {

    private static final long serialVersionUID = 413385207841147485L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    @NotNull
    @Size(min = MIN_FIRST_NAME_LENGTH, max = MAX_FIRST_NAME_LENGTH)
    @Pattern(regexp = TEXT_PATTERN)
    String firstName;

    @NotNull
    @Size(min = MIN_LAST_NAME_LENGTH, max = MAX_LAST_NAME_LENGTH)
    @Pattern(regexp = TEXT_PATTERN)
    String lastName;

    @NotNull
    @Size(min = MIN_UNIVERSITY_LENGTH, max = MAX_UNIVERSITY_LENGTH)
    @Pattern(regexp = TEXT_PATTERN)
    String university;

    @NotNull
    @Size(min = MIN_SPECIALTY_LENGTH, max = MAX_SPECIALTY_LENGTH)
    @Pattern(regexp = TEXT_PATTERN)
    String specialty;

    @NotNull
    @Min(MIN_SEMESTER)
    @Max(MAX_SEMESTER)
    Integer semester;

    @NotNull
    @Min(MIN_AGE)
    @Max(MAX_AGE)
    Integer age;

    @OneToMany(mappedBy = "student", cascade = CascadeType.PERSIST)
    @JsonIgnoreProperties("student")
    List<Book> books = new ArrayList<>();

    @Builder.Default
    LocalDateTime creationTime = LocalDateTime.now();

}
