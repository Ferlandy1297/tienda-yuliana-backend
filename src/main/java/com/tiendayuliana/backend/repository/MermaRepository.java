// repository/MermaRepository.java
package com.tiendayuliana.backend.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tiendayuliana.backend.model.Merma;
public interface MermaRepository extends JpaRepository<Merma, Integer> {
    
}
