package com.bridgeshop.module.address.repository;

import com.bridgeshop.module.address.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findAllByUser_IdOrderByIsDefaultDescIdDesc(Long userId);

    Optional<Address> findByUser_IdAndIsDefaultTrue(Long userId);
}
