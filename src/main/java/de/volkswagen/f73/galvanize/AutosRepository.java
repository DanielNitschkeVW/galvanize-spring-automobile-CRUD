package de.volkswagen.f73.galvanize;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutosRepository extends JpaRepository<Automobile, Long> {

    List<Automobile> findByColorAndMake(String color, String make);

    List<Automobile> findByColor(String color);

    List<Automobile> findByMake(String make);

    Automobile addAuto(Automobile auto);

    Optional<Automobile> findAutoByVin(String anyString);
}
