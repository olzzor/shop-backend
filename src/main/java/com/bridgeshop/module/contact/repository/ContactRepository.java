package com.bridgeshop.module.contact.repository;

import com.bridgeshop.module.contact.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long>, ContactRepositoryCustom {
    List<Contact> findAllByRefOrderByStep(Long ref);

    List<Contact> findAllByUser_IdAndStepOrderByIdDesc(Long userId, int step);

    Page<Contact> findAllByStepOrderByRefDesc(int step, Pageable pageable);

    @Query("select c.ref from Contact c where c.id = :id")
    Long findRefById(@Param("id") Long id);

    @Query("select max(c.ref) from Contact c")
    Long findMaxRef();

    @Query("select max(c.step) from Contact c where c.ref = :ref")
    int findMaxStepByRef(@Param("ref") Long ref);

    int countByRef(Long ref);
}
