package com.github.springclips.repositories;

import com.github.springclips.domains.TestDomain;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TestRepository extends CrudRepository<TestDomain, String> {
    @Query("SELECT t FROM TestDomain t WHERE t.test = ?1")
    List<TestDomain> findByTest(TestDomain.TestEnum test);
}
