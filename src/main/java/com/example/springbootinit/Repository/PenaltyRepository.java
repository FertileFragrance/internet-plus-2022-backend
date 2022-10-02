package com.example.springbootinit.Repository;

import com.example.springbootinit.Entity.Penalty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;

public interface PenaltyRepository extends JpaRepository<Penalty, Integer> ,JpaSpecificationExecutor<Penalty> {

    /*@Query(value =
            "Select type, frequency, round(frequency / total * 100, 2) as ratio, amount " +
            "From " +
            "(Select punishmentType as type, count(*) as frequency, sum(fine) as amount " +
            "From penalty " +
            "Where type = ?1 and year(date) = ?2 and month(date) = ?3 " +
            "Group by punishmentType) t1 " +
            "Inner join " +
            "(Select count(*) as total " +
            "From penalty " +
            "Where type = ?1 and year(date) = ?2 and month(date) = ?3) t2 ",
            nativeQuery = true)
    List<Object[]> getAnalysis(Integer type, String year, String month);*/

     /*@Query(value =
            "Select * " +
                    "From Penalty " +
                    "Where year(date) = :year and month(date) = :month "
            , nativeQuery = true)
    List<Penalty> findAllByDate(@Param("year") String year,
                                @Param("month") String month);*/

  /*  @Query(value =
            "Select * " +
                    "From Penalty " +
                    "Where type = :type and year(date) = :year and month(date) = :month "
            , nativeQuery = true)
    List<Penalty> findAllByTypeAndDate(@Param("type") Integer type,
                                       @Param("year") String year,
                                       @Param("month") String month);*/

    /*@Query(value =
            "Select * " +
                    "From Penalty " +
                    "Where year(date) = :year and month(date) = :month " +
                    "Order by fine Desc " +
                    "Limit 10 "
            , nativeQuery = true)
    List<Penalty> findAllOrderByFine(@Param("year") String year,
                                     @Param("month") String month);*/

    List<Penalty> findAllByDateBetween(LocalDate startDate, LocalDate endDate);

    List<Penalty> findAllByTypeAndDateBetween(Integer type, LocalDate startDate, LocalDate endDate);

}
