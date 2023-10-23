package com.leoric01.hogwarts.respositories;

import com.leoric01.hogwarts.models.wizard.Wizard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WizardRepository extends JpaRepository<Wizard, Long> {}
